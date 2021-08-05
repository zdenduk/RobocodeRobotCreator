package com.example.robocoderobotcreator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RobotBlueprint {
    public RobotBlueprint() {
        this.blockList = new HashSet<>();
    }

    private String name;
    private Set<Block> blockList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(Set<Block> blockList) {
        this.blockList = blockList;
    }
}
