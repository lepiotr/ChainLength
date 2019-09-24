package com.example.SQLiteBike;

public class Bike {
    private int id;
    private String name;
    private String type;
    private String chainset;
    private String cassette;
    private String bracket;
    private String chainlink;
    private byte[] image;

    public Bike(String name, String type, String chainset, String cassette, String bracket, String chainlink, byte[] image, int id) {
        this.name = name;
        this.type = type;
        this.chainset = chainset;
        this.cassette = cassette;
        this.bracket = bracket;
        this.chainlink = chainlink;
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getChainset() {
        return chainset;
    }

    public void setChainset(String chainset) {
        this.chainset = chainset;
    }

    public String getCassette() {
        return cassette;
    }

    public void setCassette(String cassette) {
        this.cassette = cassette;
    }

    public String getBracket() {
        return bracket;
    }

    public void setBracket(String bracket) {
        this.bracket = bracket;
    }

    public String getChainlink() {
        return chainlink;
    }

    public void setChainlink(String chainlink) {
        this.chainlink = chainlink;
    }
}
