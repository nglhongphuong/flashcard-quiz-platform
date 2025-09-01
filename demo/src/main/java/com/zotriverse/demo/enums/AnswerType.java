package com.zotriverse.demo.enums;

public enum AnswerType {
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"),
    TEXT("TEXT"),
    TRUE_FALSE("TRUE_FALSE");
    private final String displayName;
    AnswerType(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
