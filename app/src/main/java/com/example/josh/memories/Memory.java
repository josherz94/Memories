package com.example.josh.memories;

public class Memory {

    private String title, date, description;
    private long id;
    private int listPosition;
    private byte[] image;


    public String getTitle() { return title; }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public byte[] getImage() {
        return getImage();
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}


