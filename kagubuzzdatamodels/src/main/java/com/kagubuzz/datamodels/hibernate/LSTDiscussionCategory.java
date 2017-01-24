package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="lst_discussion_categories")
public class LSTDiscussionCategory extends LSTCategoryBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Override
	public ARBase getAdjanceyRelationEntity() { return new ARDiscussionCategory();	}
	
}
