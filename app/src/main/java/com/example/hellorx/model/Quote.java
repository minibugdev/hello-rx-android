package com.example.hellorx.model;

public class Quote {
    private String quote;
    private String category;
    private String author;
    private Image  authorImage;

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public Image getAuthorImage() {
        return authorImage;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorImage(Image authorImage) {
        this.authorImage = authorImage;
    }
}
