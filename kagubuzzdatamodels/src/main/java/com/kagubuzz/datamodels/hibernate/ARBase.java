package com.kagubuzz.datamodels.hibernate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ARBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	Long ancestorId;
	Long descendantId;
	
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	public Long getAncestorId() {return ancestorId;}
	public void setAncestorId(Long ancestorId) {this.ancestorId = ancestorId;}
	
	public Long getDescendantId() {	return descendantId;}
	public void setDescendantId(Long descendantId) {this.descendantId = descendantId;}
}
