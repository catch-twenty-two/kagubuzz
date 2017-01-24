package com.kagubuzz.filesystem;

public enum UserSubfolder{
    
    Images("images"),
	Transactions("transactions");
    
    String folderName;
    
    private UserSubfolder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
