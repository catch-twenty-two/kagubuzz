package com.kagubuzz.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.DiscussionDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.PostsDAO;
import com.kagubuzz.database.dao.TagDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionPublicTag;
import com.kagubuzz.datamodels.hibernate.LSTTag;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.utilities.KaguTextFormatter;


@Controller
@RequestMapping("/discussions")
public class DiscussionsBrowseController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    DiscussionDAO discussionDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    CRUDDAO crudDAO;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    SecurityService securityService;
    @Autowired
    MessageDispatcher messageDispatcher;
    @Autowired
    TagDAO tagDAO;
    @Autowired
    SearchService searchService;
    @Autowired
    UserSessionService sessionUserService;
    
    private final int MAX_ITEMS_SHOWN = 10;
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "browse/type_ahead_tags", method = RequestMethod.GET)
    public @ResponseBody List<String> discussionTypeAheadTags(@RequestParam String query, 
                                                              ModelMap modelMap, 
                                                              HttpSession httpSession) {

        List<LSTTag> tagList = tagDAO.getTagsByPartialMatch(query, LSTDiscussionPublicTag.class);

        return LSTTag.getTagListAsStringList(tagList);
    }

    @RequestMapping(value = "browse/categories", method = RequestMethod.GET)
    public String discussionCategories(HttpSession httpSession, ModelMap modelMap) {
        
        KaguTextFormatter formatter = new KaguTextFormatter();

        List<LSTCategoryBase> categoryList = categoryDAO.getAllDescendantsForCategory(categoryDAO.getCategoryByName("Discussions", LSTDiscussionCategory.class), true);

        modelMap.addAttribute("discussion_categories", categoryList);

        List<LSTTag> tagList = tagDAO.getTags(LSTDiscussionPublicTag.class, 60, 0);

        modelMap.addAttribute("discussion_tags", tagList);

        modelMap.addAttribute("discussion_trends", discussionDAO.getLatestPublicDiscussionTrends(5));
        modelMap.addAttribute("max_tags_message", formatter.toJSON(new Notification("Max Tag Filters Reached", "Sorry you can&#39;t add anymore tag filters.", NotificationTypes.error)));
        
        return "/jsp/discussion_categories";
    }

    @RequestMapping(value = "browse/next_tag_group", method = RequestMethod.POST)
    public String discussionNextTagGroupAjax(@RequestParam(required = true, defaultValue = "0") Integer offset, 
                                             ModelMap modelMap) {

        List<LSTTag> tagList = tagDAO.getTags(LSTDiscussionPublicTag.class, 60, offset * 60);

        if (tagList.size() == 0) {
            return "/ajax/no_data";
        }

        modelMap.addAttribute("discussion_tags", tagList);

        return "/ajax/tags_browse_update";
    }

    @RequestMapping(value ="public/search", method = RequestMethod.GET)
    public String discussionSearch(ModelMap modelMap,
                                   @RequestParam(required = false, value="discussion_category_id") Long discussionCategoryId,
                                   @RequestParam(required = false, value = "querystring") String queryString,
                                   @RequestParam(required = false, value = "tagList") String tags) {
        
        KaguTextFormatter formatter = new KaguTextFormatter();
        
        TBLUser user = sessionUserService.getUser();

        LSTDiscussionCategory category = crudDAO.getById(LSTDiscussionCategory.class, discussionCategoryId);
        
        String [] tagsArray = null;
        
        if(tags != null) { tagsArray =  tags.split(","); }
        
        List<TBLDiscussionPublic> discussions = searchService.searchDiscussions(queryString,
                                                                               tagsArray, 
                                                                                user.getTblKaguLocation(),
                                                                                null, 
                                                                                null, 
                                                                                category, 
                                                                                false, 
                                                                                0, 
                                                                                MAX_ITEMS_SHOWN);
        Collections.sort(discussions);
        
        List<LSTDiscussionCategory> categoryList = discussionDAO.getPublicDiscussionCategories();

        modelMap.addAttribute("discussion_categories", categoryList);
        modelMap.addAttribute("category_dao", categoryDAO);
        modelMap.addAttribute("discussion_trends", discussionDAO.getLatestPublicDiscussionTrends(5));
        modelMap.addAttribute("discussions_list", discussions);
        modelMap.addAttribute("max_tags_message", formatter.toJSON(new Notification(null, "Sorry you can&#39;t add anymore tag filters.", NotificationTypes.error)));

        // add in previous search terms
        
        modelMap.addAttribute("querystring", queryString);
        modelMap.addAttribute("category_selection",  discussionCategoryId);

        modelMap.addAttribute("ask_zip", (sessionUserService.getUser().getZipCode() == null));
        
        return "/jsp/discussions_browse";
    }
    
    @RequestMapping(value = "browse/category/{categoryId}/{serachFriendlyURL}", method = RequestMethod.GET)
    public String discussionBrowseCategory(ModelMap modelMap,
                                   @PathVariable Long categoryId,
                                   @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage) {             

        LSTDiscussionCategory category = crudDAO.getById(LSTDiscussionCategory.class, categoryId);

        injectMainContent(modelMap, currentPage, category);
        
        return "/jsp/discussions_browse";
    }
    
    @RequestMapping(value = "browse", method = RequestMethod.GET)
    public String discussionBrowse(ModelMap modelMap,
                                   @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage) {                   

        injectMainContent(modelMap, currentPage, null);
        
        return "/jsp/discussions_browse";
    }

    public void injectMainContent(ModelMap modelMap, Long currentPage, LSTDiscussionCategory category) {
        
        KaguTextFormatter formatter = new KaguTextFormatter();
        
        Pagination<TBLDiscussionBase> discussions = discussionDAO.getLatestPublicDiscussions(category, currentPage);

        List<LSTCategoryBase> categoryList = categoryDAO.getAllDescendantsForCategory(categoryDAO.getCategoryByName("Discussions", LSTDiscussionCategory.class), true);    

        modelMap.addAttribute("discussion_categories", categoryList);

        modelMap.addAttribute("category_dao", categoryDAO);
        modelMap.addAttribute("discussion_trends", discussionDAO.getLatestPublicDiscussionTrends(5));        
        modelMap.addAttribute("discussions_list", discussions.getList());
        modelMap.addAttribute("max_tags_message", formatter.toJSON(new Notification(null, "Sorry you can&#39;t add anymore tag filters.", NotificationTypes.error)));
        
        modelMap.addAttribute("ask_zip", (sessionUserService.getUser().getZipCode() == null));
        
        discussions.setModel(modelMap);
    }
    
    @Autowired
    EventDAO eventDAO;
    @Autowired
    PostsDAO postsDAO;
    
    @RequestMapping(value = "browsegroup", method = RequestMethod.GET)
    public String groupBrowse(ModelMap modelMap,
                                   @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage) {   
        Pagination<TBLDiscussionBase> discussions = discussionDAO.getLatestPublicDiscussions(null, currentPage);
        List<Post> activitiesList = new ArrayList<Post>();
        TBLUser user = sessionUserService.getUser();
        Pagination<TBLEvent> userEventsPagination = postsDAO.getPostsForUser(0L, user, TBLEvent.class);
        Pagination<TBLAd> userAdsPagination = postsDAO.getPostsForUser(0L, user, TBLAd.class);
        activitiesList.addAll(discussionDAO.getLatestPublicDiscussionTrends(5));
        activitiesList.addAll(userEventsPagination.getList());
        activitiesList.addAll(userAdsPagination.getList());
        
        modelMap.addAttribute("discussions_list", discussions.getList());
        
        Collections.shuffle(activitiesList);
        modelMap.addAttribute("activitiesList", activitiesList);
        
        injectMainContent(modelMap, currentPage, null);
        
        return "/jsp/group_browse";
    }
}
