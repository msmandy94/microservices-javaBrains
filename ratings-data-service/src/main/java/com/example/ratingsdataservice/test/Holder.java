package com.example.ratingsdataservice.test;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Holder {
    String hid;
    List<Shape> shapes;

    Shape shape;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
