package com.kagubuzz.datamodels.enums;

public enum SpringRoles {
	
	Admin("ADMIN"),
	Partner("PARTNER"),		
	User("USER");

	String roleName;
	
	private SpringRoles(String roleName) {
        this.roleName = "ROLE_" + roleName;
    }

    public String getRoleName() {
        return roleName;
    }
	
}
