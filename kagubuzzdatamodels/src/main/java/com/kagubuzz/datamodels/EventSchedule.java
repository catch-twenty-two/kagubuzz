package com.kagubuzz.datamodels;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.kagubuzz.datamodels.enums.EventPeriod;

public class EventSchedule {

	  	EventPeriod eventPeriod;
	  	
	  	Date time;
	  	
	  	long duration;
	  	
	  	boolean monday;
	  	boolean tuesday;
	  	boolean wednesday;
	  	boolean thursday;
	  	boolean friday;
	  	boolean saturday;
	  	boolean sunday;
}
