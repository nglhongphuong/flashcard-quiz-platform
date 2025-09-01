package com.zotriverse.demo.enums;

public enum Visibility {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");
    private final String displayName;
    Visibility(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
