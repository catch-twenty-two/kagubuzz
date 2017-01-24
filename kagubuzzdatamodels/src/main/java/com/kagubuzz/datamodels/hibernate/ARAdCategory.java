package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ar_ad_categories")
public class ARAdCategory extends ARBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

}
