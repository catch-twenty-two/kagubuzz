package com.kagubuzz.services.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kagubuzz.services.cartography.Cartography;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dao-beans.xml",
                                    "classpath:service-beans.xml", 
                                    "classpath:hibernate-test-config.xml", 
                                    "classpath:datasource-test-config.xml" })

public class SearchServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    Cartography cartography;
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception   {

    }
    
    @Test
    public void testClosestZipSearch() {
        
        // should return SF zip code
        
        // should return Berkeley Zip Code
    }
}
