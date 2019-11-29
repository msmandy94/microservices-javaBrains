package com.example.ratingsdataservice.test;

public class Ractangle implements Shape {
    private String name;
    private String rectField;

    public String getRectField() {
        return rectField;
    }

    public void setRectField(String rectField) {
        this.rectField = rectField;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
