package com.zotriverse.demo.enums;

public enum Status {
    NOT_LEARNED("NOT_LEARNED"),
    NOT_REMEMBERED("NOT_REMEMBERED"),
    REMEMBERED("REMEMBERED");
    private final String displayName;
    Status(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
