package com.kagubuzz.servlet.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.kagubuzz.datamodels.hibernate.TBLUser;

public class ServletUtilities {

    static Logger logger = Logger.getLogger(ServletUtilities.class);

    public void sendIcsFileInResponse(HttpServletResponse response, byte[] bytesToSend, String icsFileName) {
        
        response.setContentType("text/calendar");
        response.addHeader("Content-Disposition", "attachment; filename=" + icsFileName + ".ics");
        
        sendBytes(response, bytesToSend);
    }

    public void sendImageInResponse(HttpServletResponse response, byte[] bytesToSend, String imageMimeType) {
        
        response.setContentType("image/" + imageMimeType);
        
        sendBytes(response, bytesToSend);
    }

    public void sendBytes(HttpServletResponse response, byte[] bytesTosend) {
        BufferedInputStream imageStream = new BufferedInputStream(new ByteArrayInputStream(bytesTosend));
        int readBytes = 0;

        response.setContentLength(bytesTosend.length);

        try {
            while ((readBytes = imageStream.read()) != -1) {
                response.getOutputStream().write(readBytes);
            }
        }
        catch (IOException e) {
            logger.error("Error while trying to send image stream", e);
        }
    }
    
    public static String deployedPath(HttpSession httpSession, String filePath) {
        ServletContext context = httpSession.getServletContext();
        return context.getRealPath("/WEB-INF/" + filePath);
    }
    
    public static void showRequestHeader(HttpServletRequest request) {
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            logger.info("Header Name: " + headerName + " [Meta Data: " + request.getHeader(headerName) + "]");
        }
    }
}
