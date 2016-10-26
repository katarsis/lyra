/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.katarsis.lyra.service.UserFilesService;
import ru.katarsis.lyra.service.UserSessionService;

@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${upload.root}")
    private String ROOT;

    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    UserFilesService userFilesService;
    

    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/decision/tree/uploadData")
    public String provideUploadInfo(Model model) throws IOException {
        return "/dashboard/uploadForm";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/dashboard/decision/tree/files/list")
    @ResponseBody
    public ResponseEntity<?> getFileList(HttpServletResponse response) {
        try {
            List<String> uploadedFiles = userFilesService.getUserUploadedFiles(userSessionService.getCurrentUserName());
            response.setHeader("Content-Disposition","inline");
            return ResponseEntity.ok(uploadedFiles);
        } catch (Exception e) {
            logger.error("Error while trying get list of files:" , e);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/dashboard/decision/tree/uploadData")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        userFilesService.storeUploadedFile(userSessionService.getCurrentUserName(), file);
        return "redirect:/dashboard/decision/tree/";
    }
 
}