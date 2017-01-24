package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.utilities.KaguTextFormatter;
/*
@Entity
@Table(name="tbl_do_it")
public class TBLDoIt implements Serializable, JSPMessageRenderer {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long ownerId;
    private String body;
    private String title;
	private String address;
    private String price;
    private Date createdDate;
    private int buzzRating;
    private int flags;
    private Boolean active;
    private String imagePath1;
	private String imagePath2;
    private String imagePath3;
    private String imagePath4;
    private long categoryId;
    private Boolean openForCollaboration;
    
	@Transient
	KaguTextFormatter textFormatter = new KaguTextFormatter();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId", insertable = false, updatable = false)
    private TBLUser owner;
	
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    @Where(clause="message_type = 'AdQuestion'") // Message type is comment on Ad
    @OrderBy("createdDate")
    private List<TBLMessage> comments = new ArrayList<TBLMessage>();
    
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName ="zip", name = "zipCode", insertable = false, updatable = false)
    private TBLKaguLocation tblKaguLocation;
    
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "categoryId", insertable = false, updatable = false)
	private LSTAdCategory category;
	 
    public TBLKaguLocation getTblKaguLocation() {return tblKaguLocation;}
	public void setTblKaguLocation(TBLKaguLocation tblKaguLocation) {this.tblKaguLocation = tblKaguLocation;}
	
	public Boolean getActive() { return active;  }
    public void setActive(Boolean active) { this.active = active; }

    public int getBuzzRating() { return buzzRating; }
    public void setBuzzRating(int buzzRating) { this.buzzRating = buzzRating; }

    public int getFlags() { return flags; }
    public void setFlags(int flags) {this.flags = flags; }

    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }

    public String getAddress() {return this.address;  }
    public void setAddress(String address) {this.address = address;}

    public String getBody() {return this.body;}
    public void setBody(String body) { this.body = body;}

    public Date getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate;}

    public String getPrice() {return this.price; }
    public void setPrice(String price) { this.price = price; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
	
	public String getImagePath1() {	return imagePath1;}
	public void setImagePath1(String imagePath1) {this.imagePath1 = imagePath1;	}
	
	public String getImagePath2() {	return imagePath2;}
	public void setImagePath2(String imagePath2) {this.imagePath2 = imagePath2;}
	
	public String getImagePath3() {return imagePath3;}
	public void setImagePath3(String imagePath3) {this.imagePath3 = imagePath3;}
	
	public String getImagePath4() {return imagePath4;}
	public void setImagePath4(String imagePath4) {this.imagePath4 = imagePath4;}
		
	public long getCategoryId() {return categoryId;}
	public void setCategoryId(long categoryId) {	this.categoryId = categoryId;}
	
	public LSTAdCategory getCategory() 
	{	
		return category; 
	}
	
	public void setCategory(LSTAdCategory category) 
	{ 
		this.categoryId = category.getId();
		this.category = category; 
	}
	
	// Formatted text
	
	public String getFormattedDateShortForm(){	return textFormatter.getFormattedDateShortForm(createdDate); }
	
	public String getFormattedDateLongForm() {return textFormatter.getFormattedDateLongForm(createdDate);}
	
	public String getBodySummary()	{return textFormatter.getSummary(this.getBody());}
	
	public String getTitleSummary()	{return textFormatter.getSummary(this.getTitle(),20);}
	
	@Override
	public String getMessage() {return this.getBodySummary();}
	
	@Override
	public Boolean recipientCanReply() {return null;}
	
	@Override
	public MessageType messageType() {return null;};
	
	public List<String> getImagePaths()
	{
		ArrayList<String> imagePaths = new ArrayList<String>();
    	
    	imagePaths.add(this.getImagePath1());
    	imagePaths.add(this.getImagePath2());
    	imagePaths.add(this.getImagePath3());
    	imagePaths.add(this.getImagePath4());
    
		return imagePaths;	
	}
	
	@Override
	public TBLUser getSender() {
		// TODO Auto-generated method stub
		return null;
	}
	public TBLUser getSeller() {
		return owner;
	}
	public void setOwner(TBLUser owner) {
		this.owner = owner;
	}
	public List<TBLMessage> getComments() {
		return comments;
	}
	public void setComments(List<TBLMessage> comments) {
		this.comments = comments;
	}
	public Boolean getOpenForCollaboration() {
		return openForCollaboration;
	}
	public void setOpenForCollaboration(Boolean openForCollaboration) {
		this.openForCollaboration = openForCollaboration;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
}*/