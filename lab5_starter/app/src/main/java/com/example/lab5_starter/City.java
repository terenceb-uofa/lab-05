package com.example.lab5_starter;

import androidx.annotation.NonNull;

public class City implements java.io.Serializable  {
    private String name;
    private String province;
    public City(String name, String province) {
        this.name = name;
        this.province = province;
    }
    public String getName() {
        return name;
    }
    public String getProvince() {
        return province;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", this.name, this.province);
    }
}