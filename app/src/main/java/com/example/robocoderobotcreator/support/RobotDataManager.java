package com.example.robocoderobotcreator.support;

import android.content.Context;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum RobotDataManager {
    INSTANCE;

    ObjectMapper objectMapper;
    List<RobotBlueprint> robotData;

    RobotDataManager() {
        objectMapper = JsonMapper.builder()
                .activateDefaultTyping(new LaissezFaireSubTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING)
                .build();
    }

    public void writeRobotFileOnInternalStorage(Context mcoContext, RobotBlueprint rb) {
        String sBody = null;
        try {
            sBody = objectMapper.writeValueAsString(rb);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        File dir = new File(mcoContext.getFilesDir(), "robots");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, rb.getName());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAllRobotsFilesFromInternalStorage(Context context) {
        List<RobotBlueprint> ret = new ArrayList<>();

        File dir = new File(context.getFilesDir(), "robots");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                RobotBlueprint rb = null;
                try {
                    rb = objectMapper.readValue(file, RobotBlueprint.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ret.add(rb);
            }
        }

        robotData = ret;
    }

    public List<RobotBlueprint> getRobotData() {
        return robotData;
    }
}
