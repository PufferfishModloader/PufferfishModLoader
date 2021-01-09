package me.dreamhopping.pml.mods.core;

public enum MCVersion {
    V1_8_9("1.8.9"),
    V1_9_4("1.9.4"),
    V1_10_2("1.10.2"),
    V1_11("1.11"),
    V1_12("1.12"),
    V1_13_2("1.13.2"),
    V1_14_4("1.14.4"),
    V1_15_2("1.15.2"),
    V1_16_4("1.16.4"),
    ALL_VERSIONS("ALL");

    public final String version;

    MCVersion(String version) {
        this.version = version;
    }
}
