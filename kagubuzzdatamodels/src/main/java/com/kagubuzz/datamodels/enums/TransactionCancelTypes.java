package com.kagubuzz.datamodels.enums;

import com.kagubuzz.datamodels.IntegrityValues;

public enum TransactionCancelTypes implements IEnumExtendedValues {
    
    Flakiness("The other party flaked.", true, IntegrityValues.ExchangeFlake, false),
    Haggling("We couldn't come to an agreed apon price.", false, IntegrityValues.ExchangeHaggling, false),
    //NoContact("I never heard from them.", false, IntegrityValues.ExchangeNoContact, false),
    BetterPrice("I got a better deal from someone else.",false, IntegrityValues.ExchangeBetterDeal, true);     
    
    private EnumExtendedValues values;
    private int integrityPoints;
    boolean countsTowardsOwnIntegrity;
    
    private TransactionCancelTypes(String description, boolean defaultChoice, int integrityPoints, boolean countsTowardsOwnIntegrity) {
        
        this.countsTowardsOwnIntegrity = countsTowardsOwnIntegrity;
        this.integrityPoints = integrityPoints;
        
        values = new EnumExtendedValues(description,defaultChoice);
    }
    
    private TransactionCancelTypes(String description) {      
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }

    public int getIntegrityPoints() {
        return integrityPoints;
    }

    public boolean countsTowardsOwnIntegrity() {
        return countsTowardsOwnIntegrity;
    }
}
