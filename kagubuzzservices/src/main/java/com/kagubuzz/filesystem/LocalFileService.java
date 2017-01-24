package com.kagubuzz.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.utilities.JavaFileUtilities;

@Service
public class LocalFileService implements FileService {

    @Value("${path.userfiles}")
    private String userFileDirectory;
    @Value("${path.tempfiles}")
    private String tempFileDirectory;
    @Value("${path.rootdir}")
    private String rootFileDirectory;
    
    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public InputStream read(String fileName, String workingDirectory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String fileName, String workingDirectory) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mkdir(String fileName, String workingDirectory) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(ByteArrayOutputStream stream, String fileName, String workingDirectory) {
        File files = new File(workingDirectory);
        if (!files.exists()) {
            files.mkdirs();
        }
        File file = new File(workingDirectory + fileName);
        FileOutputStream fop;

        try {
            fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }

            stream.writeTo(fop);
            fop.flush();
            fop.close();

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    // use for reading from jsp web pages
    
    @Override
    public String getUserPublicDirectoryURL(TBLUser user, UserSubfolder subFolder) {
        return String.format("%s/%s/%s/%s/", getRootURL() , getRootDirectoryPath(), userFileDirectory, user.getUserFileStoreDirectory());
    }

    @Override
    public String getTempDirectoryURL() {
        return String.format("%s/%s/%s/", getRootURL(), getRootDirectoryPath(), getTempFileDirectoryPath());
    }
    
    @Override
    public String getRootURL() {
        return "http://localhost:8080";
    }
    
    // use for writing to filesystem
    
    @Override
    public String getUserPublicDirectoryPath(TBLUser user, UserSubfolder subFolder) {
        return String.format("/%s/%s/%s/", getRootDirectoryPath() , userFileDirectory, user.getUserFileStoreDirectory());
    }
    
    @Override
    public String getTempFilePath() {
        return String.format("/%s/%s/",  getRootDirectoryPath(), getTempFileDirectoryPath());
    }

    // used in both situations
    
    @Override
    public String getTempFileDirectoryPath() {
        return tempFileDirectory;
    }

    @Override
    public String getRootDirectoryPath() {
        return rootFileDirectory;
    }

    @Override
    public void duplicate(String sourcefileName, String destinationFileName, String workingDirectory) {
        // TODO Auto-generated method stub
        
    }

}
