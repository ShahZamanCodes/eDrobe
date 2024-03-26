package com.example.edrobe;

public class CardItem {
    private String Name, Price, Description;
    private String ImageUrl;

    public CardItem(String name, String imageUrl, String price, String description) {
        this.Name = name;
        this.ImageUrl = imageUrl;
        this.Price = price;
        this.Description = description;
    }

    public String getName() {
        return Name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}

