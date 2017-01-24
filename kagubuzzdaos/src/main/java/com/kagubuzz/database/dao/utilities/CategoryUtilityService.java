package com.kagubuzz.database.dao.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.datamodels.hibernate.ARBase;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.utilities.KaguTextFormatter;

@Service
public class CategoryUtilityService {

    @Autowired
    CategoryDAO categoryDao;

    @Autowired
    CRUDDAO crudDAO;

    public <HibernateEntity> void createCategory(String newCategoryName, LSTCategoryBase category) {
        // Create the new Category

        category.setName(newCategoryName);
        crudDAO.create(category);

        ARBase baseAdjanceyRelationNode = category.getAdjanceyRelationEntity();

        baseAdjanceyRelationNode.setAncestorId(category.getId());
        baseAdjanceyRelationNode.setDescendantId(category.getId());

        crudDAO.create(baseAdjanceyRelationNode);
    }

    public LSTCategoryBase createCategory(String parentCategoryName, String newCategoryName, LSTCategoryBase category) {

        // create the new category

        createCategory(newCategoryName, category);

        // link it to its ancestors

        LSTCategoryBase parentCategory = categoryDao.getCategoryByName(parentCategoryName, category.getClass());

        for (LSTCategoryBase ancestorCategory : categoryDao.getAllAncestorsForCategory(parentCategory, false)) {

            ARBase adjanceyRelationNode = category.getAdjanceyRelationEntity();

            adjanceyRelationNode.setAncestorId(ancestorCategory.getId());
            adjanceyRelationNode.setDescendantId(category.getId());

            crudDAO.create(adjanceyRelationNode);
        }

        return category;
    }

    public void createInitialCategoriesEvents() {
        createCategory("Events", new LSTEventCategory());
        createCategory("Events", "Arts & Galleries", new LSTEventCategory());
        createCategory("Events", "Celebration", new LSTEventCategory());
        createCategory("Events", "Concerts & Shows", new LSTEventCategory());
        createCategory("Events", "Conferences & Meetings", new LSTEventCategory());
        createCategory("Events", "Contests & Promotions", new LSTEventCategory());
        createCategory("Events", "Competitions", new LSTEventCategory());
        createCategory("Events", "Cultural Celebration", new LSTEventCategory());
        createCategory("Events", "Educational Events", new LSTEventCategory());
        createCategory("Events", "Family Events", new LSTEventCategory());
        createCategory("Events", "Festival", new LSTEventCategory());
        createCategory("Events", "Film Events", new LSTEventCategory());
        createCategory("Events", "Food Events", new LSTEventCategory());
        createCategory("Events", "Fundraisers & Benefits", new LSTEventCategory());
        createCategory("Events", "Sporting Events", new LSTEventCategory());
        createCategory("Events", "Health & Fitness", new LSTEventCategory());
        createCategory("Events", "Holiday Celebrations", new LSTEventCategory());
        createCategory("Events", "Kids Events", new LSTEventCategory());
        createCategory("Events", "Literary", new LSTEventCategory());
        createCategory("Events", "Movies", new LSTEventCategory());
        createCategory("Events", "Museums", new LSTEventCategory());
        createCategory("Events", "Neighborhood", new LSTEventCategory());
        createCategory("Events", "Networking Events", new LSTEventCategory());
        createCategory("Events", "Nightlife", new LSTEventCategory());
        createCategory("Events", "On Campus", new LSTEventCategory());
        createCategory("Events", "Opening Ceremonies", new LSTEventCategory());
        createCategory("Events", "Organizations", new LSTEventCategory());
        createCategory("Events", "Outdoors", new LSTEventCategory());
        createCategory("Events", "Parties", new LSTEventCategory());
        createCategory("Events", "Performances", new LSTEventCategory());
        createCategory("Events", "Pets", new LSTEventCategory());
        createCategory("Events", "Political", new LSTEventCategory());
        createCategory("Events", "Press Conferences", new LSTEventCategory());
        createCategory("Events", "Radio", new LSTEventCategory());
        createCategory("Events", "Religious", new LSTEventCategory());
        createCategory("Events", "Sales", new LSTEventCategory());
        createCategory("Events", "Science", new LSTEventCategory());
        createCategory("Events", "Show Times", new LSTEventCategory());
        createCategory("Events", "Social", new LSTEventCategory());
        createCategory("Events", "Spirituality", new LSTEventCategory());
        createCategory("Events", "Sports", new LSTEventCategory());
        createCategory("Events", "Technology", new LSTEventCategory());
        createCategory("Events", "Television", new LSTEventCategory());
        createCategory("Events", "Theater", new LSTEventCategory());
        createCategory("Events", "Themed Parties", new LSTEventCategory());
        createCategory("Events", "VIP Events", new LSTEventCategory());
        createCategory("Events", "Weddings & Anniversaries", new LSTEventCategory());
        createCategory("Events", "Weekend Events", new LSTEventCategory());
        createCategory("Events", "Memorials", new LSTEventCategory());
    }

    public void createInitialCategoriesAds() {
        createCategory("For Sale", new LSTAdCategory());
        createCategory("For Sale", "Appliances", new LSTAdCategory());
        createCategory("For Sale", "Antiques", new LSTAdCategory());
        createCategory("For Sale", "Bikes", new LSTAdCategory());
        createCategory("For Sale", "Boats", new LSTAdCategory());
        createCategory("For Sale", "Books", new LSTAdCategory());
        createCategory("For Sale", "Business", new LSTAdCategory());
        createCategory("For Sale", "Computer", new LSTAdCategory());
        createCategory("For Sale", "Free", new LSTAdCategory());
        createCategory("For Sale", "Furniture", new LSTAdCategory());
        createCategory("For Sale", "General", new LSTAdCategory());
        createCategory("For Sale", "Jewelry", new LSTAdCategory());
        createCategory("For Sale", "Materials", new LSTAdCategory());
        createCategory("For Sale", "Recreactional Vehicles", new LSTAdCategory());
        createCategory("For Sale", "Sporting Goods", new LSTAdCategory());
        createCategory("For Sale", "Tickets", new LSTAdCategory());
        createCategory("For Sale", "Tools", new LSTAdCategory());
        createCategory("For Sale", "Wanted", new LSTAdCategory());
        createCategory("For Sale", "Arts and Crafts", new LSTAdCategory());
        createCategory("For Sale", "Parts", new LSTAdCategory());
        createCategory("For Sale", "Babies and Kids", new LSTAdCategory());
        createCategory("For Sale", "Health and Beauty", new LSTAdCategory());
        createCategory("For Sale", "CDs", new LSTAdCategory());
        createCategory("For Sale", "Cell Phones", new LSTAdCategory());
        createCategory("For Sale", "Clothes and Accessories", new LSTAdCategory());
        createCategory("For Sale", "Collectibles", new LSTAdCategory());
        createCategory("For Sale", "Electronics", new LSTAdCategory());
        createCategory("For Sale", "Farm and Garden", new LSTAdCategory());
        createCategory("For Sale", "Garage Sales", new LSTAdCategory());
        createCategory("For Sale", "Household", new LSTAdCategory());
        createCategory("For Sale", "Motorcycles", new LSTAdCategory());
        createCategory("For Sale", "Musical Instruments", new LSTAdCategory());
        createCategory("For Sale", "Photo and Video", new LSTAdCategory());
        createCategory("For Sale", "Toys and Games", new LSTAdCategory());
        createCategory("For Sale", "Video Gaming", new LSTAdCategory());
        createCategory("For Sale", "Automobiles", new LSTAdCategory());
        createCategory("Automobiles", "Cars", new LSTAdCategory());
        createCategory("Automobiles", "Trucks", new LSTAdCategory());
        createCategory("Tools", "Power Tools", new LSTAdCategory());
        createCategory("Photo and Video", "VHS", new LSTAdCategory());
        createCategory("Photo and Video", "DVDs", new LSTAdCategory());
        createCategory("Housing", new LSTAdCategory());
        createCategory("Housing", "Apartments For Rent", new LSTAdCategory());
        createCategory("Housing", "Houses For Rent", new LSTAdCategory());
        createCategory("Housing", "Shared Rooms", new LSTAdCategory());
        createCategory("Housing", "Sublets and Temporary", new LSTAdCategory());
        createCategory("Housing", "Housing Wanted", new LSTAdCategory());
        createCategory("Housing", "Housing Swap", new LSTAdCategory());
        createCategory("Housing", "Vacation Rentals", new LSTAdCategory());
        createCategory("Housing", "Parking", new LSTAdCategory());
        createCategory("Housing", "Storage", new LSTAdCategory());
        createCategory("Housing", "Office and Commercial", new LSTAdCategory());
        createCategory("Housing", "Real Estate For Sale", new LSTAdCategory());
        createCategory("Jobs", new LSTAdCategory());
        createCategory("Jobs", "Accounting and Finance", new LSTAdCategory());
        createCategory("Jobs", "Art, Media and Design", new LSTAdCategory());
        createCategory("Jobs", "Administrative and Office", new LSTAdCategory());
        createCategory("Jobs", "Architecture and Engineering", new LSTAdCategory());
        createCategory("Jobs", "Biotech, R and D, and Science", new LSTAdCategory());
        createCategory("Jobs", "Business and Management", new LSTAdCategory());
        createCategory("Jobs", "Construction and Trades", new LSTAdCategory());
        createCategory("Jobs", "Customer Services", new LSTAdCategory());
        createCategory("Jobs", "Education and Training", new LSTAdCategory());
        createCategory("Jobs", "Food/Beverage and Hospitality", new LSTAdCategory());
        createCategory("Jobs", "General Labor", new LSTAdCategory());
        createCategory("Jobs", "Government", new LSTAdCategory());
        createCategory("Jobs", "Graphic and Web Design", new LSTAdCategory());
        createCategory("Jobs", "Human Resources", new LSTAdCategory());
        createCategory("Jobs", "IT", new LSTAdCategory());
        createCategory("Jobs", "Legal", new LSTAdCategory());
        createCategory("Jobs", "Marketing and PR", new LSTAdCategory());
        createCategory("Jobs", "Medical and Healthcare", new LSTAdCategory());
        createCategory("Jobs", "Nonprofit Sector", new LSTAdCategory());
        createCategory("Jobs", "Real Estate", new LSTAdCategory());
        createCategory("Jobs", "Retail and Wholesale", new LSTAdCategory());
        createCategory("Jobs", "Sales and Business Development", new LSTAdCategory());
        createCategory("Jobs", "Salon, Spa, and Fitness", new LSTAdCategory());
        createCategory("Jobs", "Skilled Trade and Crafts", new LSTAdCategory());
        createCategory("Jobs", "Software Development", new LSTAdCategory());
        createCategory("Jobs", "Transportation", new LSTAdCategory());
        createCategory("Jobs", "TV and Film", new LSTAdCategory());
        createCategory("Jobs", "Writing and Editing", new LSTAdCategory());
        createCategory("Services", new LSTAdCategory());
        createCategory("Services", "Art and Design", new LSTAdCategory());
        createCategory("Services", "Automotive", new LSTAdCategory());
        createCategory("Services", "Beauty and Health", new LSTAdCategory());
        createCategory("Services", "Creative", new LSTAdCategory());
        createCategory("Services", "Cleaning and Maintenance", new LSTAdCategory());
        createCategory("Services", "Computer", new LSTAdCategory());
        createCategory("Services", "Elderly Care", new LSTAdCategory());
        createCategory("Services", "Events and Occasions", new LSTAdCategory());
        createCategory("Services", "Farm and Garden", new LSTAdCategory());
        createCategory("Services", "Financial and Mortgages", new LSTAdCategory());
        createCategory("Services", "Household", new LSTAdCategory());
        createCategory("Services", "Legal and Lawyer", new LSTAdCategory());
        createCategory("Services", "Lessons and Tutoring", new LSTAdCategory());
        createCategory("Services", "Marine", new LSTAdCategory());
        createCategory("Services", "Moving and Storage", new LSTAdCategory());
        createCategory("Services", "Music", new LSTAdCategory());
        createCategory("Services", "Pets", new LSTAdCategory());
        createCategory("Services", "Real Estate", new LSTAdCategory());
        createCategory("Services", "Repair and Remodel", new LSTAdCategory());
        createCategory("Services", "Skilled Trades", new LSTAdCategory());
        createCategory("Services", "Small Business", new LSTAdCategory());
        createCategory("Services", "Travel and Vacation", new LSTAdCategory());
        createCategory("Services", "Writing and Editing", new LSTAdCategory());
    }

    public void createInitialCategoriesDiscussions() {

        createCategory("Discussions", new LSTDiscussionCategory());
        createCategory("Discussions", "Art", new LSTDiscussionCategory());
        createCategory("Discussions", "Books", new LSTDiscussionCategory());
        createCategory("Discussions", "Entertainment", new LSTDiscussionCategory());
        createCategory("Discussions", "Film & Movies", new LSTDiscussionCategory());
        createCategory("Discussions", "Automobiles", new LSTDiscussionCategory());
        createCategory("Discussions", "Bicycles", new LSTDiscussionCategory());
        createCategory("Discussions", "Walking", new LSTDiscussionCategory());
        createCategory("Discussions", "Cooking", new LSTDiscussionCategory());
        createCategory("Discussions", "Computers", new LSTDiscussionCategory());
        createCategory("Discussions", "Crafts", new LSTDiscussionCategory());
        createCategory("Discussions", "Deals", new LSTDiscussionCategory());
        createCategory("Discussions", "Donations & Recycling", new LSTDiscussionCategory());
        createCategory("Discussions", "Home Improvement", new LSTDiscussionCategory());
        createCategory("Discussions", "Education", new LSTDiscussionCategory());
        createCategory("Discussions", "Fitness", new LSTDiscussionCategory());
        createCategory("Discussions", "Food & Drink", new LSTDiscussionCategory());
        createCategory("Discussions", "Travel", new LSTDiscussionCategory());
        createCategory("Discussions", "Outdoors", new LSTDiscussionCategory());
        createCategory("Discussions", "Gaming", new LSTDiscussionCategory());
        createCategory("Discussions", "Gardening", new LSTDiscussionCategory());
        createCategory("Discussions", "Veggie Exchange", new LSTDiscussionCategory());
        createCategory("Discussions", "Local Government", new LSTDiscussionCategory());
        createCategory("Discussions", "Music", new LSTDiscussionCategory());
        createCategory("Discussions", "Lifestyle", new LSTDiscussionCategory());
        createCategory("Discussions", "Health", new LSTDiscussionCategory());
        createCategory("Discussions", "Housing", new LSTDiscussionCategory());
        createCategory("Discussions", "Legal", new LSTDiscussionCategory());
        createCategory("Discussions", "Finance", new LSTDiscussionCategory());
        createCategory("Discussions", "Outdoors", new LSTDiscussionCategory());
        createCategory("Discussions", "Over 50", new LSTDiscussionCategory());
        createCategory("Discussions", "Parents", new LSTDiscussionCategory());
        createCategory("Discussions", "Pets", new LSTDiscussionCategory());
        createCategory("Discussions", "Sports", new LSTDiscussionCategory());
        createCategory("Discussions", "Shopping", new LSTDiscussionCategory());
        createCategory("Discussions", "Kagu Buzz General", new LSTDiscussionCategory());
        createCategory("Discussions", "Kagu Buzz Events", new LSTDiscussionCategory());
    }

    public String javascriptCategoryDecendentArray(List<? extends LSTCategoryBase> categories) {

        List<String> catList = new ArrayList<String>();

        for (LSTCategoryBase cat : categories) {
            catList.add(cat.getName());
        }

        Collections.sort(catList);

        return KaguTextFormatter.formatForJavaScriptArray(catList);
    }
    

}
