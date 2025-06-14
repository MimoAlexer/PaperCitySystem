package com.mimo;

public enum WarTypes {
    TRIBUTARY,
    RAID,
    CONQUEST;

    public static WarTypes getWarType(String type) {
        try {
            return WarTypes.valueOf(type.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null; // or handle the error as needed
        }
    }

    public String asString() {
        return this.name();
    }
}