package com.example.rick.rickbakker_pset6;

/**
 * This class is used for saving in and retrieving data easily from the database.
 */

class PaintingClass {

    public String title;
    private String maker;
    private String url;

    public PaintingClass() {
    }

    PaintingClass(String title, String maker, String url) {
        this.title = title;
        this.maker = maker;
        this.url = url;
    }

    String getTitle() {
        return title;
    }

    String getMaker() {
        return maker;
    }

    String getUrl() {
        return url;
    }

}

