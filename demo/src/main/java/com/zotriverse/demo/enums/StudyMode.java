package com.zotriverse.demo.enums;

public enum StudyMode {
    NOT_REMEMBERED("NOT_REMEMBERED"),
    REMEMBERED("REMEMBERED"),
    NOT_LEARNED("NOT_LEARNED"),
    RANDOM("RANDOM"),
    CUSTOM("CUSTOM");//Duoc chi dinh
    private final String displayName;
    StudyMode(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
