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
import java.util.List;

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
        //TODO

        //mRobots = RobotDataManager.INSTANCE.getRobotData();

        List<RobotBlueprint> robots = new ArrayList<>();

        RobotBlueprint rb = createDefaultRobot();
        robots.add(rb);
        RobotBlueprint rb2 = createDefaultRobot();
        robots.add(rb2);

        mRobots.addAll(robots);

        mRobotRecycleAdapter.notifyDataSetChanged();
    }

    private RobotBlueprint createDefaultRobot() {
        RobotBlueprint rb = new RobotBlueprint();
        rb.setName("MyFirstRobot");
        List<Block> blockList = new ArrayList<>();
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
        //TODO
        System.out.println(position);
        System.out.println(mRobots.get(position).getName());

        Intent intent = new Intent(this, RobotEditorActivity.class);
        intent.putExtra("robot", position);
        startActivity(intent);
    }

    public void createNewRobot(View view) {
        Intent intent = new Intent(this, RobotEditorActivity.class);
        intent.putExtra("robot", -1);
        startActivity(intent);
    }
}