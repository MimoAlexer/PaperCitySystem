package com.mimo;

import lombok.Getter;

@Getter
public enum CityTypes {
    CITY("City"),
    TOWN("Town"),
    VILLAGE("Village"),
    // what's a hamlet?
    // Hamlet is a cool small settlement, often smaller than a village
    // Hamlet is like... hmmm, I think Troskowice is a hamlet in kcd2
    HAMLET("Hamlet"),
    SETTLEMENT("Settlement");

    private final String name;

    CityTypes(String name) {
        this.name = name;
    }
}
