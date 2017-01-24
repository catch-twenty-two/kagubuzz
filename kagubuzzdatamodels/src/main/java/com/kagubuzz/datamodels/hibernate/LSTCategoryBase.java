package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.kagubuzz.utilities.SEOUtilities;

@MappedSuperclass
public abstract class LSTCategoryBase implements Comparable<LSTCategoryBase>{

    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    Long id;
	    String name;
	    Integer popularity = 0;
		
		public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public String getName() { return name; }
	    public void setName(String category) { this.name = category; }

	    public String getFriendlyURL() { 
	        return SEOUtilities.URLFriendly(getName()); 
	    }
	    
		abstract public ARBase getAdjanceyRelationEntity();
		
		static public List<String> getCategoryListAsStringList(List<LSTCategoryBase> categoryList) {
		    
			List<String> stringList = new ArrayList<String>();
			
			for(LSTCategoryBase cat : categoryList)	{
				stringList.add(cat.getName());
			}
			
			return stringList;
		}		
		
		@Override
		public int compareTo(LSTCategoryBase o) {		
		    return this.getName().compareToIgnoreCase(o.getName());
		}
}
