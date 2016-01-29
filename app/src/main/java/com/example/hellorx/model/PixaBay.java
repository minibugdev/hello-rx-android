package com.example.hellorx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PixaBay {
    private int         total;
    @JsonProperty("totalHits")
    private int         totalImages;
    @JsonProperty("hits")
    private List<Image> images;

    public int getTotal() {
        return total;
    }

    public int getTotalImages() {
        return totalImages;
    }

    public List<Image> getImages() {
        return images;
    }
}
