/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.katarsis.lyra.service.UserSessionService;

@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    public static final String ROOT = "D:\\lyra\\upload_dir";

    private final ResourceLoader resourceLoader;

    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/decision/tree/uploadData")
    public String provideUploadInfo(Model model) throws IOException {
        model.addAttribute("file", Files.walk(Paths.get(ROOT))
                .filter(path -> !path.equals(Paths.get(ROOT)))
                .map(path -> Paths.get(ROOT).relativize(path))
                .map(path -> linkTo(methodOn(FileUploadController.class).getFile(path.toString())).withRel(path.toString()))
                .collect(Collectors.toList()));
        
        return "/dashboard/uploadForm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/decision/tree/files/list")
    @ResponseBody
    public ResponseEntity<?> getFileList(HttpServletResponse response) {
        try {
            String folderPath = userSessionService.getCurrentUserName();
            List<String> uploadedFiles = new ArrayList<String>();
            Path uploadedFilesPath = Paths.get(ROOT,folderPath);
            uploadedFiles = Files.walk(uploadedFilesPath)
                    .filter(path -> !path.equals(Paths.get(ROOT,folderPath)))
                    .map(path -> Paths.get(ROOT).relativize(path))
                    .map(path -> path.toFile().getName())
                    .collect(Collectors.toList());
            response.setHeader("Content-Disposition","inline");
            return ResponseEntity.ok(uploadedFiles);
        } catch (Exception e) {
            logger.error("Error while trying get list of files:" , e);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/dashboard/decision/tree/uploadData")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        File userFolder = new File(ROOT+File.separator+userSessionService.getCurrentUserName());
        logger.info("Trying upload file to folder: "+userFolder.getAbsolutePath());
        if(!userFolder.exists()){
            userFolder.mkdirs();
        }
        if (!file.isEmpty()) {
            try {
                Files.copy(file.getInputStream(), Paths.get(userFolder.getCanonicalPath(), file.getOriginalFilename()));
                redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");
            } catch (IOException|RuntimeException e) {
                redirectAttributes.addFlashAttribute("message", "Failued to upload " + file.getOriginalFilename() + " => " + e.getMessage());
                logger.error("Error while trying upload file: ", e);
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

        return "redirect:/dashboard/decision/tree/";
    }
 
}