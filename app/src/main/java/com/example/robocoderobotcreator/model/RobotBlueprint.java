package com.example.robocoderobotcreator.model;

import java.util.ArrayList;
import java.util.List;

public class RobotBlueprint {
    public RobotBlueprint() {
        this.blockList = new ArrayList<>();
    }

    private String name;
    private List<Block> blockList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }
}
