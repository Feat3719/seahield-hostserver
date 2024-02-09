package com.seahield.hostserver.domain;

public enum ContractStatus {

    WAITING("승인대기"),
    REJECTED("거절"),
    APPROVED("승인");

    private final String description;

    ContractStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ContractStatus fromDescription(String description) {
        for (ContractStatus userType : ContractStatus.values()) {
            if (userType.getDescription().equals(description)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("INCORRECT USERTYPE");
    }

}
