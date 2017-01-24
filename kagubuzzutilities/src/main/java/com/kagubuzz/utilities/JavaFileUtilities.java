package com.kagubuzz.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class JavaFileUtilities {

    static String tmpFilePath;

    public ByteArrayOutputStream fileToByteArrayOutputStream(File file) {
        
        byte readBuf[] = new byte[512];
        FileInputStream fin;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        try {
            fin = new FileInputStream(file);
            
            int readCnt = fin.read(readBuf);
            
            while (0 < readCnt) {
                bout.write(readBuf, 0, readCnt);
                readCnt = fin.read(readBuf);
            }

            fin.close();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return bout;
    }

    public InputStream getResourceAsStream(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resource);
    }

    public File getResourceAsFile(String resource) {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            return new File(classLoader.getResource(resource)
                    .toURI());
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void currentWorkingDirectory() {

        File directory = new File(".");

        try {
            System.out.println("Current directory's canonical path: " + directory.getCanonicalPath());
            System.out.println("Current directory's absolute  path: " + directory.getAbsolutePath());
        }
        catch (Exception e) {
            System.out.println("Exception is =" + e.getMessage());
        }
    }

    public File getTempFile(String fileName) {
        return new File(getTempFilePath() + fileName);
    }

    public static boolean deleteFile(String fileName) {

        File fileToDelete = new File(fileName);

        return fileToDelete.delete();
    }

    public static String getTempFilePath() {

        File temp;

        if (tmpFilePath == null) {
            try {

                temp = File.createTempFile("temp-file-name", ".tmp");

                String absolutePath = temp.getAbsolutePath();
                String tempFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
                return tempFilePath + File.separatorChar;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "/tmp/";
    }
}
