/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserFilesService {
    
    @Value("${upload.root}")
    private String ROOT;
    
    private static final Logger logger = LoggerFactory.getLogger(UserFilesService.class);
    
    
    public String getDataFromFileSource(File sourcefile){
        String result = null;
        try{
            result = new String(Files.readAllBytes(Paths.get(sourcefile.getCanonicalPath())), "UTF-8");
        }catch (Exception err){
            logger.error("Error while read source file", err);
        }
        return result;
    }
    
    public List<String> getUserUploadedFiles(String userName) throws IOException{
        String folderPath = userName;
        List<String> uploadedFiles = new ArrayList<String>();
        Path uploadedFilesPath = Paths.get(ROOT,folderPath);
        uploadedFiles = Files.walk(uploadedFilesPath)
                .filter(path -> !path.equals(Paths.get(ROOT,folderPath)))
                .map(path -> Paths.get(ROOT).relativize(path))
                .map(path -> path.toFile().getName())
                .collect(Collectors.toList());
        return uploadedFiles;
    }
    
    public boolean storeUploadedFile(String userName,MultipartFile file){
        File userFolder = new File(ROOT+File.separator+userName);
        boolean result = false;
        logger.info("Trying upload file to folder: "+userFolder.getAbsolutePath());
        if(!userFolder.exists()){
            userFolder.mkdirs();
        }
        if (!file.isEmpty()) {
            try {
                Files.copy(file.getInputStream(), Paths.get(userFolder.getCanonicalPath(), file.getOriginalFilename()));
                result = true;
            } catch (IOException|RuntimeException e) {
                logger.error("Error while trying upload file: ", e);
            }
        }
        return result;
    }
    
}
