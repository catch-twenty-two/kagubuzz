package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ar_event_categories")
public class AREventCategory extends ARBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

}
