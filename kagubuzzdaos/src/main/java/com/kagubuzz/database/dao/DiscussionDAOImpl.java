package com.kagubuzz.database.dao;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;

@Repository("discussionDAO")
@Transactional(readOnly=true)
public class DiscussionDAOImpl implements DiscussionDAO {
	
	@Autowired
	SessionFactory sessionFactory;
	
    @Autowired
    CategoryDAO categoryDAO;
    
	@Override
	public Pagination<TBLDiscussionBase> getLatestPublicDiscussions(LSTDiscussionCategory category, Long discussionToStartAt)  {
	    
	    Criteria criteria = sessionFactory.getCurrentSession()
                            .createCriteria(TBLDiscussionPublic.class)
                            .add(Restrictions.eq("active", true));
	    
        categoryDAO.addCriteriaLimitedToCategoryAndDescendants(criteria, category);     
               
		return new Pagination<TBLDiscussionBase>(criteria, discussionToStartAt, Order.desc("createdDate"));
	}
	
	 @SuppressWarnings("unchecked")
	    @Override
	    public List<LSTDiscussionCategory> getPublicDiscussionCategories() {

	        List<LSTDiscussionCategory> discussionCategoriesList = sessionFactory.getCurrentSession()
	                                                          .createCriteria(LSTDiscussionCategory.class)
	                                                          .addOrder(Order.asc("name"))
	                                                          .list();

	        return discussionCategoriesList;
	    }
	
	@SuppressWarnings("unchecked")
    @Override
    public List<TBLDiscussionPublic> getLatestPublicDiscussionTrends(int maxResults)  {
	    
        Criteria criteria = sessionFactory.getCurrentSession()
                            .createCriteria(TBLDiscussionPublic.class)
                            .addOrder(Order.desc("createdDate"))
                            .add(Restrictions.eq("active", true))
                            .setMaxResults(maxResults);
        
        return criteria.list();
    }
	
   @SuppressWarnings("unchecked")
    @Override
    public void removeUserFromAllDiscussions(TBLUser user)  {
       
    }
   
   // TODO: Optimize SQL
   
   @SuppressWarnings("unchecked")
   @Override
   public List<TBLDiscussionBase> getAllDiscussionsForUser(TBLUser user)  {
    return null;   
   }
}
