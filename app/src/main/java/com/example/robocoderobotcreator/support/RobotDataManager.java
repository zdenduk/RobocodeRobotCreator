package com.example.robocoderobotcreator.support;

import android.content.Context;

import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public enum RobotDataManager {
    INSTANCE;

    Gson gson;
    List<RobotBlueprint> robotData;

    private RobotDataManager() {
        gson = new Gson();
    }

    public void writeRobotFileOnInternalStorage(Context mcoContext, RobotBlueprint rb) {

        String sBody = gson.toJson(rb);

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
                try {
                    JsonReader reader = new JsonReader(new FileReader(file));
                    RobotBlueprint rb = gson.fromJson(reader, RobotBlueprint.class);
                    ret.add(rb);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        robotData = ret;
    }

    public List<RobotBlueprint> getRobotData() {
        return robotData;
    }
}
