package com.example.robocoderobotcreator.model.events;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class OnHitWall extends ComboBlock {
    public OnHitWall() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return "public void onHitWall(HitWallEvent event)";
    }
}
