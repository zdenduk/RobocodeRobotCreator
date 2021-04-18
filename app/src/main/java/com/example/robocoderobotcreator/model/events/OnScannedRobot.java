package com.example.robocoderobotcreator.model.events;

import com.example.robocoderobotcreator.model.Category;
import com.example.robocoderobotcreator.model.ComboBlock;

public class OnScannedRobot extends ComboBlock {

    private String code = "public void onScannedRobot(ScannedRobotEvent e)";

    public OnScannedRobot() {
        super(Category.EVENTS);
    }

    @Override
    public String getCode() {
        return code;
    }
}