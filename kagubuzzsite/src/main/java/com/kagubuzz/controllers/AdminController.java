package com.kagubuzz.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.DiscussionDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.CategoryUtilityService;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;

import edu.emory.mathcs.backport.java.util.Collections;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    MessageDispatcher messageDispatcher;

    @Autowired
    CRUDDAO crudDAO;

    @Autowired
    EventDAO eventDao;

    @Autowired
    SearchService userSearchService;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    SecurityService securityService;

    @Autowired
    CategoryUtilityService categoryUtilityService;

    @Autowired
    DiscussionDAO discussionDAO;

    @Autowired
    UserSessionService sessionUserService;

    @Autowired
    EventService eventService;

    @Autowired
    EventDAO eventDAO;

    @Autowired
    AdDAO adDAO;
    
    @Autowired
    KaguLocationDAO kaguLocationDAO;

    static Logger logger = Logger.getLogger(AdminController.class);

    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "setdefaultcategories", method = RequestMethod.POST)
    public @ResponseBody
    Notification createDefaultCategories(HttpSession httpSession, ModelMap modelMap) {

        categoryUtilityService.createInitialCategoriesAds();
        // categoryUtilityService.createInitialCategoriesDiscussions();
        // categoryUtilityService.createInitialCategoriesEvents();

        return new Notification("Default categories successfully created.", "Success");
    }

    @RequestMapping(value = "addnewcategory", method = RequestMethod.POST)
    public @ResponseBody
    Notification addNewCategoryAjax(HttpSession httpSession, ModelMap modelMap, String parentCategory, String newCateogry) {

        LSTCategoryBase cat = categoryDAO.getCategoryByName(newCateogry, LSTDiscussionCategory.class);

        if (cat != null) {
            return new Notification("Category " + getCategoryAncestorsAsString(cat) + " already exists.", "Error");
        }

        if (parentCategory == null) {
            return new Notification("Parent category required.", "Error");
        }

        LSTDiscussionCategory newCat = (LSTDiscussionCategory) categoryUtilityService.createCategory(parentCategory, newCateogry, new LSTDiscussionCategory());

        return new Notification("New cateogry " + getCategoryAncestorsAsString(newCat) + " successfully created.", "Success");
    }

    public String getCategoryAncestorsAsString(LSTCategoryBase cat) {
        String catPath = "";

        List<LSTCategoryBase> discussionCats = categoryDAO.getAllAncestorsForCategory(cat, true);

        Collections.reverse(discussionCats);

        for (String catName : LSTDiscussionCategory.getCategoryListAsStringList(discussionCats)) {
            catPath += catName + "/";
        }

        return catPath.substring(0, catPath.length() - 1);
    }

    @RequestMapping(value = "/browse", method = RequestMethod.GET)
    public String adminMainPage(HttpSession httpSession, ModelMap modelMap) {
        return "/jsp/admin";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "update_post_locations", method = RequestMethod.GET)
    public @ResponseBody
    Notification updateLocations(HttpSession httpSession, ModelMap modelMap) {
        
        List<TBLEvent> eventsList = (List<TBLEvent>) crudDAO.getSessionFactory().getCurrentSession().createCriteria(TBLEvent.class).list();
        
        for(TBLEvent event: eventsList) {
            try {
            logger.info("Updating event: " + event.getId() + "address = " + event.getAddress() + "("+event.getLongitude()+","+event.getLongitude() + ")");
            postsService.setGeographicalCoordinates(event.getLongitude(), event.getLatitude(), event);
            }
            catch (Exception e) {
               logger.error("Error - continuing", e);
            }
        }
        
        List<TBLAd> adsList = (List<TBLAd>) crudDAO.getSessionFactory().getCurrentSession().createCriteria(TBLAd.class).list();
        
        for(TBLAd ad: adsList) {
            try {
            logger.info("Updating ad: " + ad.getId() + "address = " + ad.getAddress() + "("+ad.getLongitude()+","+ad.getLongitude() + ")");
            postsService.setGeographicalCoordinates(ad.getLongitude(), ad.getLatitude(), ad);
            }
            catch (Exception e) {
               logger.error("Error - continuing", e);
            }
        }

        
        return new Notification("Success", "test complete.");
    }
    
    @RequestMapping(value = "testadd", method = RequestMethod.GET)
    public @ResponseBody
    Notification testAdd(HttpSession httpSession, ModelMap modelMap) {
        
        TBLKaguLocation kloc = kaguLocationDAO.getClosestZip(37.750867, -122.148185 );
        
        System.out.println(kloc.getCity());
        
        return new Notification("Success", "test complete.");
    }

    @Autowired
    AdService adService;
    boolean togglebuyer = false;
    int requests = 0;
    
    @Autowired
    PostsService postsService;
    
    public void testMakeOffer() {
        requests++;
        while(requests <= 1)
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        TBLAd ad = crudDAO.getById(TBLAd.class, 52L);
        
        togglebuyer =!togglebuyer;
        
        TBLUser buyer = null;
        
        if(togglebuyer){ buyer = userDAO.getUserByEmail("jimmy.johnson@kagubuzz.com");}
        else{ buyer = userDAO.getUserByEmail("eclectic.sounds@gmail.com");}
        
        TBLDiscussionAd offer = adService.createOffer(buyer, ad);
        adService.makeOffer(25, false, DeliveryMethod.Email, offer);

        adService.acceptOffer(offer);
        
        requests = 0;
        
        //postsService.deletePost(offer.getId(),TBLDiscussionAd.class, buyer);

    }

    @RequestMapping(value = "/deleteuser", method = RequestMethod.POST)
    public @ResponseBody
    Notification adminDeleteUser(HttpSession httpSession, ModelMap modelMap, Long id) {
        TBLUser user = sessionUserService.getUser();

        TBLUser userToDelete = userDAO.getUserById(id);
        String userName = userToDelete.getFirstName();
        crudDAO.delete(userToDelete);

        return new Notification("Success", "User " + userName + " was deleted.");
    }

    @RequestMapping(value = "listusers", method = RequestMethod.GET)
    public String listUsers(HttpSession httpSession, ModelMap modelMap, @RequestParam(required = false, defaultValue = "0") long page) {

        Pagination<TBLUser> listUser = userDAO.getListOfUsers(page);
        long userCount = userDAO.getUserCount();
        long eventCount = eventDAO.getEventCount();
        long adCount = adDAO.getAdCount();
        modelMap.addAttribute("listusers", listUser.getList());
        modelMap.addAttribute("usercount", userCount);
        modelMap.addAttribute("eventcount", eventCount);
        modelMap.addAttribute("adcount", adCount);
        listUser.setModel(modelMap);

        return "/jsp/admin_list_users";
    }
}
