package com.kagubuzz.datamodels.enums;

public enum AdState {
	
	NoActivity(0),
	InquiriesMade(1),
	OfferAccepted(2),
	Complete(3);

	int id;
	
	private AdState(int id) { 
		this.id = id;
	}
	
	public int getId() { return id; }
	
	public static AdState getEnum(int id)
	{
		switch(id)
		{
			case 0: return AdState.InquiriesMade;
			case 1: return AdState.OfferAccepted;
		}
		
		return null;
	}
}
