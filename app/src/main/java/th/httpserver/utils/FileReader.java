package th.httpserver.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {

    private static final String ROOT_DIR = System.getProperty("user.dir") + "/src/main/resources/public";
    
    
    static {
        System.out.println("Static files root directory: " + ROOT_DIR);
    }

    public static byte[] readFile(String path) throws IOException {
        File file = new File(ROOT_DIR + path);
        System.out.println("Reading file: " + file.getAbsolutePath());
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        fis.close();
        return baos.toByteArray();
    }
}
