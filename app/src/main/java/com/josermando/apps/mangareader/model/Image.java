package com.josermando.apps.mangareader.model;

import java.io.Serializable;

/**
 * Created by Josermando Peralta on 4/22/2016.
 */
public class Image implements Serializable{
    private String url;
    private String pageNumber;
    private String imageWidth;
    private String imageHeight;

    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }
}
