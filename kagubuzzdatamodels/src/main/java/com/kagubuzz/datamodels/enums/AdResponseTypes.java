package com.kagubuzz.datamodels.enums;

public enum AdResponseTypes {
	
	MakeAnOffer(0),
	MakeAnAppointment(1),		
	MakeAnInquiry(2);

	int id;
	
	private AdResponseTypes(int id) { 
		this.id = id;
	}

	public int getId() { return id; }
	
	public static AdResponseTypes getEnum(int id)
	{
		switch(id)
		{
			case 0: return AdResponseTypes.MakeAnOffer;
			case 1: return AdResponseTypes.MakeAnAppointment;
			case 2: return AdResponseTypes.MakeAnInquiry;
		}
		
		return null;
	}

	public String getNameJSP(){ return "test";};
}
