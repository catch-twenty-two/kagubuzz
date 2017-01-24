package com.kagubuzz.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.servlet.utilities.ServletUtilities;

@WebServlet("/fileServerServlet")
public class FileServerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(FileServerServlet.class);

    CRUDDAO crudDAO;

    public enum RequestTypes {
        
        GetAvatarByUserId, 
        GetAvatar, 
        GetEventIcs,
    }

    public FileServerServlet() {
        
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        this.parseRequestType(request, response);
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        this.parseRequestType(request, response);
        
    }

    public void parseRequestType(HttpServletRequest request, HttpServletResponse response) {
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());

        crudDAO = (CRUDDAO) applicationContext.getBean("crudDAO");
        
        String requestType = request.getParameter("type");
        ServletUtilities serverletUtils = new ServletUtilities();

        if (requestType == null) return;

        switch (RequestTypes.valueOf(requestType)) {
        
        case GetAvatar: {
            TBLUser user = crudDAO.getById(TBLUser.class, (Long) request.getSession().getAttribute("userid"));
            if (user == null) return;
            serverletUtils.sendImageInResponse(response, user.getAvatarImage(), "png");
            break;
        }
        
        case GetAvatarByUserId: {
            String id = request.getParameter("id");
            if(NumberUtils.isNumber(id) == false) return;
            
            TBLUser user = crudDAO.getById(TBLUser.class, Long.valueOf(id));
            serverletUtils.sendImageInResponse(response, user.getAvatarImage(), "png");
            break;
        }
        
        case GetEventIcs: {
            TBLEvent event = crudDAO.getById(TBLEvent.class, Long.valueOf(request.getParameter("id")));
            serverletUtils.sendIcsFileInResponse(response, event.getEventICalender(), event.getTitle().replace(" ", ""));
            break;
        }
        
        default: {
            logger.info("Command " + requestType + " not found.");
        }
        
        }
    }
}
