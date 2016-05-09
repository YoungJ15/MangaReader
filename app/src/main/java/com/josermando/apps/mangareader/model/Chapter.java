package com.josermando.apps.mangareader.model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/15/2016.
 */
public class Chapter implements Serializable {
    private String id;
    private String name;

    public Chapter() {
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

    @Override
    public String toString() {
        return getName()+" "+getId();
    }
}
