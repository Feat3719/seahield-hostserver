package com.seahield.hostserver.domain;

public enum UserType {
    ADMIN("관리자"),
    BUSINESS("사업자"),
    GENERAL("일반");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static UserType fromDescription(String description) {
        for (UserType userType : UserType.values()) {
            if (userType.getDescription().equals(description)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("INCORRECT USERTYPE");
    }
}