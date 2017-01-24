package com.kagubuzz.database.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

import com.kagubuzz.database.dao.utilities.StripHibernateProxy;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;

@Repository("categoryDAO")
@Transactional(readOnly = true)
public class CategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public <HibernateEntityCategory> List<LSTCategoryBase> getAllCategories(Class<HibernateEntityCategory> category) {
        List<LSTCategoryBase> list = (List<LSTCategoryBase>) sessionFactory.getCurrentSession()
                .createCriteria(category)
                .list();

        return list;
    }

    @SuppressWarnings("unchecked")
    public <HibernateEntityCategory> List<LSTCategoryBase> getCategoriesByPartialMatch(String partialMatch, Class<HibernateEntityCategory> category) {
        List<LSTCategoryBase> partialMatchList = (List<LSTCategoryBase>) sessionFactory.getCurrentSession()
                .createCriteria(category)
                .add(Restrictions.ilike("name", "%" + partialMatch + "%"))
                .setMaxResults(8)
                .addOrder(Order.desc("name"))
                .list();

        return partialMatchList;
    }

    public <HibernateEntityCategory> LSTCategoryBase getCategoryByName(String name, Class<HibernateEntityCategory> category) {

        return (LSTCategoryBase) sessionFactory.getCurrentSession()
                .createCriteria(category)
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public <HibernateEntityCategory> List<LSTCategoryBase> getAllDescendantsForCategory(LSTCategoryBase category, boolean removeBaseCategory) {

        if (category == null)
            return null;

        category = StripHibernateProxy.initializeAndUnproxy(category);

        Table catTable = category.getClass()
                .getAnnotation(Table.class);
        Table arTable = category.getAdjanceyRelationEntity()
                .getClass()
                .getAnnotation(Table.class);

        List<LSTCategoryBase> descendantsList = sessionFactory.getCurrentSession()
                .createSQLQuery(
                        "SELECT c.* FROM " + catTable.name() + " c JOIN " + arTable.name() + " a ON (c.id = a.descendant_id) WHERE a.ancestor_id = :categoryid")
                .addEntity(category.getClass())
                .setParameter("categoryid", category.getId())
                .list();

        if(removeBaseCategory) {
            descendantsList.remove(category);
        }
        
        Collections.sort(descendantsList);

        return descendantsList;
    }

    @SuppressWarnings("unchecked")
    public <HibernateEntityCategory> List<LSTCategoryBase> getAllAncestorsForCategory(LSTCategoryBase category, boolean removeBaseCategory) {

        if (category == null)
            return null;

        category = StripHibernateProxy.initializeAndUnproxy(category);

        Table catTable = category.getClass()
                .getAnnotation(Table.class);
        Table arTable = category.getAdjanceyRelationEntity()
                .getClass()
                .getAnnotation(Table.class);

        List<LSTCategoryBase> ancestorList = sessionFactory.getCurrentSession()
                .createSQLQuery(
                        "SELECT c.* FROM " + catTable.name() + " c JOIN " + arTable.name() + " a ON (c.id = a.ancestor_id) WHERE a.descendant_id = :categoryid")
                .addEntity(category.getClass())
                .setParameter("categoryid", category.getId())
                .list();

        if(removeBaseCategory) ancestorList.remove(category);
        
        return ancestorList;
    }

    public Criteria addCriteriaLimitedToCategoryAndDescendants(Criteria criteria, LSTCategoryBase category) {

        if (category != null) {

            Conjunction conjunction = (Conjunction) Restrictions.conjunction();
            Disjunction disjunction = (Disjunction) Restrictions.disjunction();

            for (LSTCategoryBase descendant : getAllDescendantsForCategory(category, false)) {
                disjunction = (Disjunction) disjunction.add(Restrictions.eq("category", descendant));
            }

            criteria = criteria.add(conjunction.add(disjunction));
        }

        return criteria;

    }

    public Criteria addCriteriaLimitedToSingleCategory(Criteria criteria, LSTCategoryBase category) {

        if (category != null) {
            criteria = criteria.add(Restrictions.eq("category", category));
        }

        return criteria;

    }
}
