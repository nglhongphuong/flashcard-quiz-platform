package com.zotriverse.demo.enums;

public enum QuestionType {
    AUDIO("AUDIO"),
    TEXT_AUDIO("TEXT_AUDIO");
    private final String displayName;
    QuestionType(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
