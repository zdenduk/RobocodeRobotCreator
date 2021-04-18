package com.example.robocoderobotcreator.model.events;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class ElseBlock extends ComboBlock {

    private String code = "else";

    public ElseBlock() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return code;
    }
}
