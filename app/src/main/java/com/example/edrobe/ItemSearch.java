package com.example.edrobe;


public class ItemSearch {
    private String Name, Description, ImageUrl;
    private String Price;

    public ItemSearch() {
    }

    public ItemSearch(String name, String description, String imageUrl, String price) {
        Name = name;
        Description = description;
        ImageUrl = imageUrl;
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
