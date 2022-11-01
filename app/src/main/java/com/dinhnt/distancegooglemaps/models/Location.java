package com.dinhnt.distancegooglemaps.models;

public class Location {
    private int id;
    private String name;
    private String lglt;

    public Location(int id, String name, String lglt) {
        this.id = id;
        this.name = name;
        this.lglt = lglt;
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

    public String getLglt() {
        return lglt;
    }

    public void setLglt(String lglt) {
        this.lglt = lglt;
    }
}
