package com.kagubuzz.message.types;

import com.kagubuzz.datamodels.enums.AdDiscussionState;

public enum AdMessageTypes {
	InitialContact, // ?
	ThinkOnIt,
	Accept,
	Decline,
	Offer, // ?
	KaguBuzz, // ?
	Unknown;
	
	public AdDiscussionState getOfferStateMapping() {
	    
	    switch(this) {
	    case Accept:
	        return AdDiscussionState.Accepted;
	    case ThinkOnIt:
	        return AdDiscussionState.ThinkingAboutIt;
	    case Decline:
	        return AdDiscussionState.Declined;
	    case InitialContact:
	    case Offer:
	    case KaguBuzz:
	    case Unknown:
	        break;
	    }
        return null;
	}
}