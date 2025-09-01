package com.zotriverse.demo.enums;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");
    private final String displayName;
    Role(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
