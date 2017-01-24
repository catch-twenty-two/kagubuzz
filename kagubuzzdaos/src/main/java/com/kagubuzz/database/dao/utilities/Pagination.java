package com.kagubuzz.database.dao.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.ObjectUtils;

import com.kagubuzz.datamodels.Pair;

@Transactional(readOnly = true)
public class Pagination<E extends Comparable<E>> {

    int itemsPerPage = 40;
    
    List<Pair> queryOptions = new ArrayList<Pair>();
    
    List<E> list;
    long rowCount;
    long currentPage;
    
    @SuppressWarnings("unchecked")
    public Pagination(Criteria criteria, Long currentPage, Order orderBy) {
        this.currentPage = currentPage;
        
        criteria = criteria.setProjection(Projections.rowCount());
        
        rowCount = (Long) criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        criteria.setFirstResult(currentPage.intValue() * itemsPerPage).setMaxResults(itemsPerPage);
        criteria.addOrder(orderBy);
        
        list = (List<E>) criteria.list();
        
        Collections.sort(list);
    }
    
    @SuppressWarnings("unchecked")
    public Pagination(Query hqlQuery, Query hqlCountQuery, Long currentPage) {
        this.currentPage = currentPage;
        
        rowCount = (Long) hqlCountQuery.uniqueResult();

        hqlQuery.setFirstResult(currentPage.intValue() * itemsPerPage).setMaxResults(itemsPerPage);
        
        list = (List<E>) hqlQuery.list();
        
        Collections.sort(list);
    }

    public void setModel(ModelMap modelMap) {

        int noOfPages = (int) Math.ceil(rowCount * 1.0 / itemsPerPage);
        modelMap.addAttribute("no_of_pages", noOfPages);
        modelMap.addAttribute("page", currentPage);

        if (!queryOptions.isEmpty()) {
            
            String queryString = "";
            
            for (Pair queryOption : queryOptions) {          
                queryString +=  "&amp;" + queryOption.name + "=" + ObjectUtils.toString(queryOption.value);
                modelMap.addAttribute(queryOption.name, queryOption.value);
            }
            
            modelMap.addAttribute("query_options", queryString);
        }
    }
    
    public void addQueryOption(String queryName, Object value) {
        if(value != null) {
            queryOptions.add(new Pair(queryName, value));
        }
    }
    
    public List<E> getList() { 
        return list; 
    }

    public long getRowCount() {
        return rowCount;
    }
    
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
}