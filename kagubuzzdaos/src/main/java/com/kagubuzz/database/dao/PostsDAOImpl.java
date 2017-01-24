package com.kagubuzz.database.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.hibernate.TBLUser;

@Repository("postsDAO")
@Transactional(readOnly = true)
public class PostsDAOImpl implements PostsDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    CategoryDAO categoryDAO;
    
    @Autowired
    CRUDDAO crudDAO;
    
    static Logger log = Logger.getLogger(PostsDAOImpl.class);  
    
    @Override
    public <T extends Comparable<T>> Pagination<T> getPostsForUser(Long page, TBLUser user, Class<T> postType) {

        Criteria criteria = sessionFactory.getCurrentSession()             
                                          .createCriteria(postType)
                                          .add(Restrictions.eq("owner", user));

        return new Pagination<T>(criteria, page, Order.desc("createdDate"));
    }

}
