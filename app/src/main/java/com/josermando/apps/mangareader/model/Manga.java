package com.josermando.apps.mangareader.model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/15/2016.
 */
public class Manga implements Serializable{
    private String id;
    private String name;
    private String imgSrc;

    public Manga() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {

        return getName() +" "+ getId();
    }
}
