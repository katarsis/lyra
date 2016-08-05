/**
 * @author Petr Klimov
 * 
 */
package ru.katarsis.lyra.dto;

import java.util.ArrayList;
import java.util.List;

public class ListFiles {

    List<String> files = new ArrayList<String>();
    
    public ListFiles(){
        
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
    
}
