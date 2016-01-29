package com.example.hellorx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    @JsonProperty("webformatURL")
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }
}