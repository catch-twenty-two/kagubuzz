package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="lst_discussion_public_tags")
public class LSTDiscussionPublicTag extends LSTTag implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
}
