package com.kagubuzz.services.sms;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SMSServiceTest {

	Logger logger = Logger.getLogger(SMSServiceTest.class);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		SMSService smsService = new SMSService();
		
		//logger.info(smsService.getPhoneDashedFormat("15032359028"));
		//smsService.sendMessage("test", "15032359028");
	}

}
