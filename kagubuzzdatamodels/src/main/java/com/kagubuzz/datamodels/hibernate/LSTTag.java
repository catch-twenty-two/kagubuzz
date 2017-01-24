package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class LSTTag {

    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    Long id;
	    String name;
	    Integer popularity = 0;
		
		public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public String getName() { return name; }
	    public void setName(String tag) { this.name = tag; }
	    
       static public List<String> getTagListAsStringList(List<LSTTag> tagList) {
            
            List<String> stringList = new ArrayList<String>();
            
            for(LSTTag tag : tagList) {
                stringList.add(tag.getName());
            }
            
            return stringList;
        }
    public Integer getPopularity() {
        return popularity;
    }
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }   
}
