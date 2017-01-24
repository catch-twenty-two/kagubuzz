package com.kagubuzz.services.sms;

import com.kagubuzz.message.types.AdMessageTypes;

public class SMSTransactionCode {
	
	String encodedSelection;
	String transactionCode;
	String keyword;
	String header;
	int offerId;
	
	public AdMessageTypes response;
	
	public SMSTransactionCode(int offerId, String keyword) {
		this.keyword = keyword;
		this.offerId = offerId;
	}
	
	public SMSTransactionCode(String encodedSelection) {
		
		this.encodedSelection = encodedSelection;
		
		// Save header character
		
		this.header = encodedSelection.substring(0, 1);
		
		// Parse out the keyword
		
		this.keyword = encodedSelection.replaceAll("[^\\p{L}]", "").toLowerCase();		

		// Parse out the offer id and response type id
		
		transactionCode = encodedSelection.replaceAll("[^\\p{N}]", "");	
		
		// Parse out the transaction code (offer)
		
		offerId = decodeOfferId(transactionCode.substring(1, transactionCode.length()));
		
		// Parse out the offer code
		
		response = AdMessageTypes.values()[(Integer.valueOf(transactionCode.substring(0, 1)))];		
	}

	public SMSTransactionCode(String encodedSelection, String temp) {
		
		this.encodedSelection = encodedSelection;
		
		// Save header character
		
		this.header = encodedSelection.substring(0, 1);
		
		// Parse out the keyword
		
		this.keyword = encodedSelection.replaceAll("[^\\p{L}]", "").toLowerCase();			
	}

	public Boolean hasValidHeader() {
		return this.header.substring(0, 1).equals("#"); 
	}
	
	public int getOfferId() {
		return offerId;
	}
	
	public AdMessageTypes getSelectionId() { return response; }
	
	public String getKeyword() { return keyword; }
	
	public String getEncodedValueForResponse(AdMessageTypes response) {
		
		String responsePart = Integer.toString(response.ordinal());
		
		String offerIdPart = encodeOfferId(this.offerId);
		
		return responsePart + offerIdPart + keyword;
	}

	private String encodeOfferId(int offerIdToEncode) {
		
		offerIdToEncode *= 10;
		offerIdToEncode += Integer.toString(offerIdToEncode).hashCode()%9;
		
		 return Integer.toString(offerIdToEncode);
	}
	
	private int decodeOfferId(String offerIdToDecodeStr) {
		
		int offerIdToDecode;
		int decodedValue;
		int compareValue;
		
		compareValue = decodedValue = offerIdToDecode  = Integer.valueOf(offerIdToDecodeStr);
		 
		decodedValue /= 10;
		compareValue /= 10;
		compareValue *= 10;
		
		compareValue = (compareValue + (Integer.toString(compareValue).hashCode()%9));
				
		if(compareValue == offerIdToDecode)	return decodedValue;
		
		return -1;
	}
	
	public AdMessageTypes getResponse() { return response; }
}
