package com.example.japp.model;

public class Requirement {

    public Requirement() {

    }

    public Requirement(String text, int value) {
        this.text = text;
        this.value = value;
    }

    String text;
    int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}