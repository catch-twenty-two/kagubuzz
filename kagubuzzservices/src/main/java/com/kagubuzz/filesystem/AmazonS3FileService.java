package com.kagubuzz.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kagubuzz.datamodels.hibernate.TBLUser;

@Service
public class AmazonS3FileService implements FileService {
    
    @Value("${amazons3.awsaccesskey}")
    private String awsAccessKey;
    @Value("${amazons3.awssecretkey}")
    private String awsSecretKey;
    @Value("${amazons3.kagubucket}")
    private String kaguBuzzBucket;
    
    @Value("${amazons3.remoterootaddress}")
    private String awsRootDirectory;
    
    @Value("${path.userfiles}")
    private String userFileDirectory;
    
    @Value("${path.tempfiles}")
    private String tempFilePathDirectory;
    
    Logger logger = Logger.getLogger(this.getClass());
    
    S3Service s3Service = null;
    S3Bucket s3Bucket = null;
    AccessControlList bucketAcl = null;
    
    @Override
    @PostConstruct
    public void init() {
        
       AWSCredentials awsCredentials =  new AWSCredentials(awsAccessKey, awsSecretKey);       
        
       try {  
            s3Service = new RestS3Service(awsCredentials);
            s3Bucket = s3Service.getBucket(kaguBuzzBucket); 
            bucketAcl = s3Service.getBucketAcl(kaguBuzzBucket);
            
            bucketAcl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
        }
        catch (S3ServiceException e) {
            logger.error("While Initalizing S3service", e);
        }
        catch (ServiceException e) {
            logger.error("While Initalizing S3service", e);
        }  
    }
    
    @Override
    public void write(ByteArrayOutputStream stream, String fileName, String workingDirectory)    {
        
        MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();
        S3Object fileObject = null;
        
        try {           
            fileObject = new S3Object(fileName, stream.toByteArray());
            fileObject.setContentType(mimeMap.getContentType(fileName));
            fileObject.setKey(workingDirectory + fileName);            
            fileObject.setAcl(bucketAcl);
            fileObject.setContentLength(stream.toByteArray().length);
            
            s3Service.putObject(s3Bucket, fileObject);            
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("While saving to S3", e);
        }
        catch (IOException e) {
            logger.error("While saving to S3", e);
        }
        catch (S3ServiceException e) {
            logger.error("While saving to S3", e);
        }
        
        System.out.println("S3Object with data: " + fileObject);
    }
	
	@Override
    public InputStream read(String fileName , String workingDirectory)  {
	    
	    S3Object fileObject = null;
	    InputStream inputStream = null;
	    
	    try {
	        fileObject = s3Service.getObject(kaguBuzzBucket, workingDirectory + fileName);
	        
	        inputStream = fileObject.getDataInputStream();
        }
        catch (S3ServiceException e) {
            logger.error("While reading from S3", e);
        }
        catch (ServiceException e) {
            logger.error("While reading from S3", e);
        }
	    
	    return inputStream;
	}

	@Override
    public void duplicate(String sourcefileName, String destinationFileName, String workingDirectory)  {
        
        try {
            InputStream sourceFileStream = read(sourcefileName , workingDirectory); 
            
            ByteArrayOutputStream boutputStream = new ByteArrayOutputStream();      
            boutputStream.write(IOUtils.toByteArray(sourceFileStream));
            
            write(boutputStream, destinationFileName, workingDirectory);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
	

    @Override
    public void delete(String fileName, String workingDirectory)  {

        
        try {
            s3Service.deleteObject(kaguBuzzBucket, workingDirectory + fileName);
            
            //fileObject.
        }
        catch (S3ServiceException e) {
            logger.error("While deleting from S3", e);
        }
        catch (ServiceException e) {
            logger.error("While delting from S3", e);
        }
        
    }

    @Override
    public void mkdir(String fileName, String workingDirectory)  {
        // TODO Auto-generated method stub
        
    }
    
    // use for reading from jsp web pages
    
    @Override
    public String getUserPublicDirectoryURL(TBLUser user, UserSubfolder subFolder) {
        return String.format("%s/%s/%s/%s/%s/", getRootURL() , getRootDirectoryPath(), userFileDirectory, user.getUserFileStoreDirectory(), subFolder.getFolderName());
    }

    @Override
    public String getTempDirectoryURL() {
        return String.format("%s/%s/%s/", getRootURL(), getRootDirectoryPath(), getTempFileDirectoryPath());
    }
    
    @Override
    public String getRootURL() {
        return awsRootDirectory;
    }

    // use for writing to filesystem
    
    @Override
    public String getUserPublicDirectoryPath(TBLUser user, UserSubfolder subFolder) {
        return String.format("%s/%s/%s/", userFileDirectory, user.getUserFileStoreDirectory(), subFolder.getFolderName());
    }
    
    @Override
    public String getTempFilePath() {
        return String.format("%s/",  getTempFileDirectoryPath());
    }

    // used in both situations
    
    @Override
    public String getTempFileDirectoryPath() {
        return tempFilePathDirectory;
    }

    @Override
    public String getRootDirectoryPath() {
        return kaguBuzzBucket;
    }
}
