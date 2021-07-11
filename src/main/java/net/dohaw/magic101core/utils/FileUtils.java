package net.dohaw.magic101core.utils;

import java.io.File;

public class FileUtils {
    public static boolean deleteDirectoryFully(File dir){
        if(dir.exists()){
            for(File file: dir.listFiles()){
                if (file.isDirectory()) {
                    deleteDirectoryFully(file);
                }
                else{
                    file.delete();
                }
            }
        }
        return dir.delete();
    }
}
