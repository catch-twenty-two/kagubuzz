package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name="tbl_word_ranks")
public class TBLWordRank implements Serializable, Comparable<TBLWordRank> {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    String word;
    int rank;
    
	public Long getId() { return id;}
	public void setId(Long id) {this.id = id;}

	public String getWord() {return word;}
	public void setWord(String word) {	this.word = word;}
	
	public int getRank() {	return rank;}
	public void setRank(int rank) {	this.rank = rank;}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		TBLWordRank other = (TBLWordRank) obj;
		
		if (word == null) {
			
			if (other.word != null)	return false;
			
		} else if (!word.equals(other.word)) return false;
		
		return true;
	}
	

	@Override
	public int compareTo(TBLWordRank arg0) {
		return (arg0.getRank() > this.getRank() ? -1 : (arg0.getRank() == this.getRank() ? 0 : 1));
	}

}
