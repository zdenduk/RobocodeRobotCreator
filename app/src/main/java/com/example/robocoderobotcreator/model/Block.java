package com.example.robocoderobotcreator.model;

public abstract class Block {
    private Category category;

    protected Block(Category category) {
        this.category = category;
    }

    public abstract String getCode();

    public Category getCategory() {
        return category;
    }
}
