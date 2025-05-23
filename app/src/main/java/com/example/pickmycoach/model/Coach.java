package com.example.pickmycoach.model;

public class Coach {
    private String id;
    private String name;
    private String speciality;
    private String experience;
    private String rating;
    private String price;
    private String imageUrl;
    private String description;
    private String availability;

    public Coach() {
        // Required empty constructor for Firestore
    }

    public Coach(String id, String name, String speciality, String experience,
                 String rating, String price, String imageUrl, String description, String availability) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.experience = experience;
        this.rating = rating;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.availability = availability;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
