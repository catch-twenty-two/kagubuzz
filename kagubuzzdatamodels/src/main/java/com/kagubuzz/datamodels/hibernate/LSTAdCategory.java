package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.kagubuzz.datamodels.enums.AdGroup;

@Entity
@Table(name="lst_ad_categories")
public class LSTAdCategory extends LSTCategoryBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private AdGroup adGroup;
    
    @Override
	public ARBase getAdjanceyRelationEntity() { return null; }

	public AdGroup getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(AdGroup responseType) {
		this.adGroup = responseType;
	}
}
