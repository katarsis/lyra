/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserFilesService {

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
    
}
