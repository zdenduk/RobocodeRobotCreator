package com.example.robocoderobotcreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robocoderobotcreator.support.RobotDataManager;
import com.example.robocoderobotcreator.view.adapters.RobotsRecyclerAdapter;
import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.example.robocoderobotcreator.model.events.*;
import com.example.robocoderobotcreator.model.movement.*;
import com.example.robocoderobotcreator.model.weapons.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RobotEditorInventoryActivity extends AppCompatActivity implements RobotsRecyclerAdapter.OnRobotListener {

    private RecyclerView mRecyclerView;

    private List<RobotBlueprint> mRobots = new ArrayList<>();
    private RobotsRecyclerAdapter mRobotRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RobotDataManager.INSTANCE.readAllRobotsFilesFromInternalStorage(getApplicationContext());

        setContentView(R.layout.activity_robot_editor_inventory);
        mRecyclerView = findViewById(R.id.roboteditorview);

        initRecyclerView();
        retrieveRobots();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void retrieveRobots() {
        mRobots.addAll(RobotDataManager.INSTANCE.getRobotData());

        mRobotRecycleAdapter.notifyDataSetChanged();
    }

    private RobotBlueprint createDefaultRobot() {
        RobotBlueprint rb = new RobotBlueprint();
        rb.setName("MyFirstRobot");
        Set<Block> blockList = new HashSet<>();
        Run run = new Run();
        WhileBlock whileBlock = new WhileBlock();
        run.getBlocks().add(whileBlock);
        Ahead ahead1 = new Ahead();
        ahead1.setParameter("100");
        TurnGunRight turnGunRight1 = new TurnGunRight();
        turnGunRight1.setParameter("360");
        Ahead ahead2 = new Ahead();
        ahead2.setParameter("100");
        TurnGunRight turnGunRight2 = new TurnGunRight();
        turnGunRight2.setParameter("360");
        whileBlock.getBlocks().add(ahead1);
        whileBlock.getBlocks().add(turnGunRight1);
        whileBlock.getBlocks().add(ahead2);
        whileBlock.getBlocks().add(turnGunRight2);
        OnScannedRobot onScannedRobot = new OnScannedRobot();
        Fire fire = new Fire();
        fire.setParameter("1");
        onScannedRobot.getBlocks().add(fire);
        blockList.add(run);
        blockList.add(onScannedRobot);
        rb.setBlockList(blockList);
        return rb;
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRobotRecycleAdapter = new RobotsRecyclerAdapter(mRobots, this);
        mRecyclerView.setAdapter(mRobotRecycleAdapter);
    }

    @Override
    public void onRobotClick(int position) {
        Intent intent = new Intent(this, RobotEditorActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void createNewRobot(View view) {
        Intent intent = new Intent(this, RobotEditorActivity.class);
        startActivity(intent);
    }
}