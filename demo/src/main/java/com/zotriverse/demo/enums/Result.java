package com.zotriverse.demo.enums;

public enum Result {
    CORRECT("CORRECT"),
    INCORRECT("INCORRECT"),
    UNRESULT("UNRESULT");
    private final String displayName;
    Result(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
