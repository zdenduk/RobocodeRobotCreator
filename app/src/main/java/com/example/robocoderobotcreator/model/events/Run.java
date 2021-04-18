package com.example.robocoderobotcreator.model.events;


import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class Run extends ComboBlock {

    private String code = "public void run()";

    public Run() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return code;
    }
}