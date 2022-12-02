package com.tamsbeauty.config.Fileupload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;


public final class FileUtil {

    private FileUtil(){

    }
    public static final String folderPath="/home/ndiritu/IdeaProjects/Spring Projects/campus-project/tamsbeautyfront/src/assets/";
    public static final String folderPathtwo="/home/ndiritu/IdeaProjects/Spring Projects/campus-project/tamsbeautyfront/src/assets/nailsoftheweek/";

     public static final Path filePath=Paths.get(folderPath);


}
