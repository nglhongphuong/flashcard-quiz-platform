package com.zotriverse.demo.enums;

public enum Star {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5");
    private final String displayName;
    Star(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }

    public static Star fromChar(Character c) {
        switch (c) {
            case '1': return ONE;
            case '2': return TWO;
            case '3': return THREE;
            case '4': return FOUR;
            case '5': return FIVE;
            default: throw new IllegalArgumentException("Invalid star: " + c);
        }
    }

    public int toInt() {
        return Integer.parseInt(displayName);
    }
}
