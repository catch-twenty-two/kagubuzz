package com.kagubuzz.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.PostConstruct;

import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface FileService {

    @PostConstruct
    public abstract void init();

    public abstract InputStream read(String fileName, String workingDirectory);
    
    public abstract void delete(String fileName, String workingDirectory);
    
    public abstract void mkdir(String fileName, String workingDirectory);

    public abstract void write(ByteArrayOutputStream stream, String fileName, String workingDirectory);

    public abstract String getUserPublicDirectoryURL(TBLUser user, UserSubfolder subFolder);

    public abstract String getUserPublicDirectoryPath(TBLUser user, UserSubfolder subFolder);

    public abstract String getTempFilePath();

    String getRootURL();

    String getTempDirectoryURL();

    String getTempFileDirectoryPath();

    String getRootDirectoryPath();

    void duplicate(String sourcefileName, String destinationFileName, String workingDirectory);

}