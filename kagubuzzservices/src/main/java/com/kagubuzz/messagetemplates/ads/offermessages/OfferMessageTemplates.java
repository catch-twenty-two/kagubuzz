package com.kagubuzz.messagetemplates.ads.offermessages;

import java.text.NumberFormat;

import org.stringtemplate.v4.ST;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.AdMessageTypes;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.services.sms.SMSTransactionCode;
import com.kagubuzz.utilities.KaguTextFormatter;

public class OfferMessageTemplates extends TemplateMessage {

    TBLDiscussionAd discussion;
    boolean multipleOffers;
    
    protected OfferMessageTemplates(TBLUser recipient, TBLDiscussionAd discussion) {
        super(recipient);
        this.discussion = discussion;

        multipleOffers = (discussion.getOfferMessageInquiries().size() > 2);

    }
    
    protected String getSMSOfferSnippit() {
        
        TBLAd ad = discussion.getAd();
        
        switch(ad.getAdGroup()) {
        case ForSale:
            
            if (ad.getPrice() == 0) {
                return "Pick Up";
            }
            
            if(ad.getPrice() > 0) {            
                return "$" + discussion.getOfferAmount() + ".00";
            }
            
            break;
            
        case CommunityServices:
            
            if(discussion.getTimeBanking()) {
                return "Time Banking";
            }
            
            switch(ad.getAdType()) {
            case Request:                           
                
                if(ad.getPrice() == 0) {
                    return "Volunteer";
                }
                
                if(ad.getPrice() > 0) {  
                    return "$" + discussion.getOfferAmount() + ".00/hr";
                }
                
                break;
                
            case Offered:
                
                if(ad.getPrice() == 0) {
                    return "Interested";
                }
                
                if(ad.getPrice() > 0) {  
                    return "$" + discussion.getOfferAmount() + ".00/hr";
                }
                
                break;
            }
        }

        return "Error";        
    }
    
    public static String getOfferSnippit(TBLDiscussionAd discussion) {
        
        TBLAd ad = discussion.getAd();
        String offerAmount = NumberFormat.getCurrencyInstance().format(discussion.getOfferAmount());
        
        switch(ad.getAdGroup()) {
        case ForSale:
            
            if (ad.getPrice() == 0) {
                return "Offered to take it. (Item is Marked As Free)";
            }

            if(ad.getPrice() > 0) { 
                return offerAmount;
            }
            
            break;
            
        case CommunityServices:
            
            if(discussion.getTimeBanking()) {
                return "Time Banking Through Omnui";
            }
            
            switch(ad.getAdType()) {
            case Request:
                if(ad.getPrice() == 0) {
                    return "Offered to volunteer";
                }
                
                if(ad.getPrice() > 0) {  
                    return offerAmount + "<sup> " + ad.getPerUnit().getEnumExtendedValues().getDescription() + "</sup>";
                }
                
                break;
                
            case Offered:
                
                if(ad.getPrice() == 0) {
                    return "Interest expressed in free service";
                }
                
                if(ad.getPrice() > 0) {  
                    return offerAmount  + "<sup> " + ad.getPerUnit().getEnumExtendedValues().getDescription() + "</sup>";
                }
                
                break;
            }
        }

        return "Error";        
    }
    
    protected String create(String templateName) {
        
        KaguTextFormatter formatter= new KaguTextFormatter();
        
        ST offerTemplate = getTemplate(templateName);
        
        SMSTransactionCode otc = new SMSTransactionCode(discussion.getInquiryOrderNumber(),
                                                        discussion.getAd().getSMSKeyword());
        
        offerTemplate.add("new_offer", (multipleOffers ? "a <strong>new</strong>" : "an" ));
        offerTemplate.add("offer", getOfferSnippit(this.discussion));
        
        String title = discussion.getAd().getTitle();
        
        if(offerTemplate.getName().contains("sms")){
            title = formatter.getSummary(title, 10);
        }
        
        offerTemplate.add("adtitle", title);
        
        offerTemplate.add("sms_offer", getSMSOfferSnippit());
        offerTemplate.add("recipient", recipient.getFirstName());
        offerTemplate.add("other_party", discussion.getOppositeParty(recipient).getFirstName());
        offerTemplate.add("server_url", getServerURL());
        offerTemplate.add("buyername", discussion.getBuyer().getFirstName());
        offerTemplate.add("viewing_url", getServerURL() + discussion.getViewingURL() );        
        offerTemplate.add("sellername", discussion.getSeller().getFirstName());         
        offerTemplate.add("discussionid", discussion.getId());
        offerTemplate.add("buyercontactmethod", discussion.getBuyerContactMethod().name().toLowerCase());
        offerTemplate.add("buyercontactdetails", 
                           DeliveryMethod.getPreferredDeliveryMethodDetails(discussion.getBuyerContactMethod(), 
                           discussion.getBuyer()));
        offerTemplate.add("buyer_rep", "0");
        offerTemplate.add("sellercontactmethod", discussion.getAd().getContactMethod().name().toLowerCase());
        offerTemplate.add("sellercontactdetails", discussion.getSeller().getPreferredDeliveryMethodDetails().toLowerCase());
        offerTemplate.add("sellerreputationpoints", discussion.getSeller().getIntegrity());
        offerTemplate.add("UUID", discussion.getUUID()); 
        offerTemplate.add("smskeyword", discussion.getAd().getSMSKeyword());
        offerTemplate.add("accept", otc.getEncodedValueForResponse(AdMessageTypes.Accept));
        offerTemplate.add("decline", otc.getEncodedValueForResponse(AdMessageTypes.Decline));
        offerTemplate.add("thinkonit", otc.getEncodedValueForResponse(AdMessageTypes.ThinkOnIt));
        
        offerTemplate = wrapInEmailIfAppropriate(offerTemplate);
        
        return offerTemplate.render();
    }   

}