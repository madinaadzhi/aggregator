package org.madi.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
    public static String getFileContent(String fileLocation) throws IOException {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        File file = new File(classLoader.getResource(fileLocation).getFile());
        String fileContent = IOUtils.toString(new FileReader(file));
        return fileContent;
    }
}
