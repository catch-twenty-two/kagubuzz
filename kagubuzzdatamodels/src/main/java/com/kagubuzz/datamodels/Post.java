package com.kagubuzz.datamodels;

import java.util.Date;
import java.util.Set;

import com.kagubuzz.datamodels.hibernate.LSTTag;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;

public interface Post extends JSPMessageRenderer {

        public abstract String getIconName();
        
		public abstract Date getStartDate();
		
		public abstract Date getEndDate();
		
		public abstract void setRenewDate(int daysFromNow);
		
		public abstract void setActive(boolean active);
		
		public abstract void setLongitude(double longitude);
	        
		public abstract void setLatitude(double latitude);
		
		public abstract void setTblKaguLocation(TBLKaguLocation location);
	    public abstract TBLKaguLocation getTblKaguLocation();
	    
	    public abstract Set<? extends LSTTag> getTags();
}
