package com.kagubuzz.services.test;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.utilities.JavaFileUtilities;
import com.kagubuzz.utilities.KaguImage;

@ContextConfiguration(locations = { "classpath:dao-beans.xml", 
                                    "classpath:service-beans.xml", 
                                    "classpath:hibernate-test-config.xml",
                                    "classpath:datasource-test-config.xml" })

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class FileServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    FileService fileService;
    
    @Autowired
    UserAccountService userService;
    
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        
    }

    @Test
    public void readFile() {

    }

    @Test
    public void writeFile() {
        JavaFileUtilities jfu = new JavaFileUtilities();
        File file = jfu.getResourceAsFile("blank_avatar.jpg");
        
        fileService.write(jfu.fileToByteArrayOutputStream(file), 
                          file.getName(),
                          fileService.getUserPublicDirectoryURL(userService.createGenericUser(),UserSubfolder.Images));
    }

}
