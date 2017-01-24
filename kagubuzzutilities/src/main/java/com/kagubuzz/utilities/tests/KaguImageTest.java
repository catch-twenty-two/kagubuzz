package com.kagubuzz.utilities.tests;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kagubuzz.utilities.KaguImage;

public class KaguImageTest {

	@Before
	public	void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test
	public void	testLighten() {
		File file = new File("/Developer/KaguBuzzUserImages/IMG_1235.JPG");
		KaguImage kimage = new KaguImage(file);
		//kimage.lighten(.5F);
		kimage.resize(-1, 635);
		kimage.saveAsJPG("trans");
	}

}
