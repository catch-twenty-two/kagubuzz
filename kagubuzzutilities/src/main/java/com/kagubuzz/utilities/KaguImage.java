package com.kagubuzz.utilities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class KaguImage {
	
	BufferedImage image = null;
	File originalFile = null;
	String workingDirectory = "/";
	
	public void setWorkingDirectory(String workingDirectory)
	{
		this.workingDirectory = workingDirectory;
	}
	
	public KaguImage(InputStream stream) {
		try 
		{
			 image = ImageIO.read(stream);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public KaguImage(URL url) {
		try 
		{
			 image = ImageIO.read(url);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public KaguImage(File imageFile) {
		
		originalFile = imageFile;
		
		try 
		{
			image = ImageIO.read(imageFile);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ByteArrayOutputStream getOutputStream() {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try 
        {
            ImageIO.write(image, "jpg", baos );
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return baos;
    }

	public byte[] getBytes() {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try 
		{
			ImageIO.write(image, "jpg", baos );
			baos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return baos.toByteArray();
	}
	
	public void lighten(float amount)
	{
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		
		BufferedImage newBuffImg = new BufferedImage(imageWidth, 
													 imageHeight,
				 									 BufferedImage.TYPE_INT_RGB);
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, amount);
		
		Graphics2D g2dRenderer = newBuffImg.createGraphics();
		 
		g2dRenderer.setColor(Color.white);
		g2dRenderer.fillRect(0, 0, imageWidth, imageHeight);
		g2dRenderer.setColor(Color.white);
		g2dRenderer.fill(new Rectangle2D.Double(0, 0, imageWidth, imageHeight));
		g2dRenderer.setComposite(ac);
		g2dRenderer.drawImage(image, 0, 0, null);
		g2dRenderer.dispose();
		
		image = newBuffImg;
	}
	
	public void resize(int newHeight, int newWidth)
	{
		
		// keep from pixelating an image
		
		if((newHeight > image.getHeight() || newWidth > image.getWidth())) return;
		
		if(newHeight < 1)
		{
			newHeight = (int)((float)image.getHeight()*(float)newWidth/(float)image.getWidth());
		}
		
		if(newWidth < 1)
		{
			newWidth = (int)((float)image.getWidth()*(float)newHeight/(float)image.getHeight());
		}
		
		BufferedImage newBuffImg = new BufferedImage(newWidth, 
													 newHeight,
				 									 BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2dRenderer = newBuffImg.createGraphics();
		g2dRenderer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2dRenderer.setBackground(Color.WHITE);
		g2dRenderer.clearRect(0, 0, newWidth, newHeight);
		g2dRenderer.drawImage(image, 0, 0, newWidth, newHeight, null);
		g2dRenderer.dispose();
		
		image = newBuffImg;
	}
	
	public File save(String name)
    {   
        return this.saveImage(name,name.substring(name.length() - 3));
    }
	
	public File saveAsPNG(String name)
	{	
		return this.saveImage(name,"PNG");
	}
	
	public File saveAsJPG(String name)
	{
		return this.saveImage(name,"JPG");
	}
	
	private File saveImage(String name, String fileType)
	{
		File newFile = new File(workingDirectory + name + "." + fileType);
		System.out.println("Saving as " + workingDirectory + name + "." + fileType);
		
		try	{
			ImageIO.write(image, "JPG", newFile);
			return newFile;
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
