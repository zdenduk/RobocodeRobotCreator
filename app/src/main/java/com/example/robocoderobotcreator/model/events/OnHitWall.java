package com.example.robocoderobotcreator.model.events;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.Category;

public class OnHitWall extends Block {
    public OnHitWall() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return null;
    }
}
