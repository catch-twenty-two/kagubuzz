/*
 *  Copyright 2010 Blue Lotus Software, LLC.
 *  Copyright 2010 John Yeary <jyeary@bluelotussoftware.com>.
 *  Copyright 2010 Allan O'Driscoll
 *
 * Dual Licensed MIT and GPL v.2 
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * The GNU General Public License (GPL) Version 2, June 1991
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; version 2 of the License.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.kagubuzz.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.utilities.JavaFileUtilities;
import com.kagubuzz.utilities.KaguImage;

public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 6748857432950840322L;
    FileService fileService;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             if a servlet-specific error occurs
     * @throws IOException
     *             if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());

        fileService = (FileService) applicationContext.getBean("fileService");
        JavaFileUtilities fileUtils = new JavaFileUtilities();
        InputStream is = null;
        FileOutputStream os = null;
        Map<String, Object> uploadResult = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();

        //TODO:  Mske all these files write to a tmp direcotry for th euploader to be erased when the review button is pushed
        try {
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            
            // Make sure image file is less than 4 megs
            
            if(bytesRead > 1024*1024*4) throw new FileUploadException();
            
            is = request.getInputStream();
            
            File tmpFile = File.createTempFile("image-", ".jpg");
            
            os = new FileOutputStream(tmpFile);
            
            System.out.println("Saving to " + JavaFileUtilities.getTempFilePath());
            
            while ((bytesRead = is.read(buffer)) != -1) os.write(buffer, 0, bytesRead);
            
            KaguImage thumbnailImage = new KaguImage(tmpFile);
            
            thumbnailImage.setWorkingDirectory(JavaFileUtilities.getTempFilePath());
            thumbnailImage.resize(140, 180);
            
            File thumbnail = thumbnailImage.saveAsJPG("thumbnail-" +  FilenameUtils.removeExtension(tmpFile.getName()));

            fileService.write(fileUtils.fileToByteArrayOutputStream(tmpFile), tmpFile.getName(), fileService.getTempFilePath());
            fileService.write(fileUtils.fileToByteArrayOutputStream(thumbnail), thumbnail.getName(), fileService.getTempFilePath());
            
            response.setStatus(HttpServletResponse.SC_OK);
            
            uploadResult.put("success", "true");
            uploadResult.put("indexedfilename", fileService.getTempDirectoryURL() + tmpFile.getName());
            uploadResult.put("thumbnailfilename",  fileService.getTempDirectoryURL() + thumbnail.getName());
        }
        catch (FileNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uploadResult.put("success", "false");
            log(UploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
        }
        catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            uploadResult.put("success", "false");
            log(UploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
        }
        catch (FileUploadException e) {
            response.setStatus(HttpServletResponse.SC_OK);
            uploadResult.put("success", "false");
            uploadResult.put("indexedfilename", "filetobig");
        }
        finally {
            try {
                mapper.writeValue(response.getWriter(), uploadResult);
                os.close();
                is.close();
            }

            catch (IOException ignored) {
            }
        }
    }
}
