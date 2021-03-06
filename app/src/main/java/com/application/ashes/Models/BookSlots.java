package com.application.ashes.Models;

public class BookSlots {
    private String description;
    private String title;
    private int imgId;

    public BookSlots(int imgId) {
        this.imgId = imgId;
    }

    public BookSlots() {
    }

    public BookSlots(String description, String title, int imgId) {
        this.description = description;
        this.title = title;
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
