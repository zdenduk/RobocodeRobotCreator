package com.example.robocoderobotcreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.example.robocoderobotcreator.model.events.ElseBlock;
import com.example.robocoderobotcreator.model.events.IfBlock;
import com.example.robocoderobotcreator.model.events.OnHitWall;
import com.example.robocoderobotcreator.model.events.OnScannedRobot;
import com.example.robocoderobotcreator.model.events.Run;
import com.example.robocoderobotcreator.model.events.WhileBlock;
import com.example.robocoderobotcreator.model.movement.Ahead;
import com.example.robocoderobotcreator.model.movement.Back;
import com.example.robocoderobotcreator.model.weapons.Fire;
import com.example.robocoderobotcreator.model.weapons.TurnGunLeft;
import com.example.robocoderobotcreator.model.weapons.TurnGunRight;
import com.example.robocoderobotcreator.support.RobotDataManager;
import com.example.robocoderobotcreator.view.BasicBlock;
import com.example.robocoderobotcreator.view.DragController;
import com.example.robocoderobotcreator.view.DragLayer;
import com.example.robocoderobotcreator.view.DropSpot;

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, View.OnLongClickListener, View.OnClickListener, View.OnTouchListener {

    RobotBlueprint rb;

    private DragController mDragController;   // Object that sends out drag-drop events while a view is being moved.
    private DragLayer mDragLayer;             // The ViewGroup that supports drag-drop.
    // Otherwise, any touch event starts a drag.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_robot_editor);

        Button eventsShowMenu = findViewById(R.id.events_show_menu);
        eventsShowMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(RobotEditorActivity.this, v);
            popup.setOnMenuItemClickListener(RobotEditorActivity.this);
            popup.inflate(R.menu.robot_editor_events_menu);
            popup.show();
        });

        Button movementShowMenu = findViewById(R.id.movement_show_menu);
        movementShowMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(RobotEditorActivity.this, v);
            popup.setOnMenuItemClickListener(RobotEditorActivity.this);
            popup.inflate(R.menu.robot_editor_movement_menu);
            popup.show();
        });

        Button radarShowMenu = findViewById(R.id.radar_show_menu);
        radarShowMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(RobotEditorActivity.this, v);
            popup.setOnMenuItemClickListener(RobotEditorActivity.this);
            popup.inflate(R.menu.robot_editor_radar_menu);
            popup.show();
        });

        Button weaponsShowMenu = findViewById(R.id.weapons_show_menu);
        weaponsShowMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(RobotEditorActivity.this, v);
            popup.setOnMenuItemClickListener(RobotEditorActivity.this);
            popup.inflate(R.menu.robot_editor_weapons_menu);
            popup.show();
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mDragController = new DragController(this);
        setupViews();

        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", -1);

        //  rb = RobotDataManager.INSTANCE.getRobotData().get(pos);
    }

    public void saveRobot(View view) {
        EditText robotNameEditText = (EditText) findViewById(R.id.robot_name_edit);
        rb.setName(robotNameEditText.getText().toString());

        RobotDataManager.INSTANCE.writeRobotFileOnInternalStorage(getApplicationContext(), rb);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.else_item:
                createBlock(new ElseBlock());
                return true;
            case R.id.if_item:
                createBlock(new IfBlock());
                return true;
            case R.id.onhitwall_item:
                createBlock(new OnHitWall());
                return true;
            case R.id.onscannedrobot_item:
                createBlock(new OnScannedRobot());
                return true;
            case R.id.run_item:
                createBlock(new Run());
                return true;
            case R.id.while_item:
                createBlock(new WhileBlock());
                return true;
            case R.id.fire_item:
                createBlock(new Fire());
                return true;
            case R.id.turngunleft_item:
                createBlock(new TurnGunLeft());
                return true;
            case R.id.turngunright_item:
                createBlock(new TurnGunRight());
                return true;
            case R.id.ahead_item:
                createBlock(new Ahead());
                return true;
            case R.id.back_item:
                createBlock(new Back());
                return true;
            default:
                return false;
        }
    }

    private void createBlock(Block param) {
        BasicBlock bb = new BasicBlock(getApplicationContext(), param);
        bb.setOnClickListener(this);
        bb.setOnLongClickListener(this);
        bb.setOnTouchListener(this);
        DropSpot dp = new DropSpot(getApplicationContext());

        dp.addView(bb);
        mDragLayer.addView(dp);
        dp.setup(mDragLayer, mDragController, R.color.background_color);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;

        final int action = ev.getAction();

        // In the situation where a long click is not needed to initiate a drag, simply start on the down event.
        if (action == MotionEvent.ACTION_DOWN) {
            handledHere = startDrag(v);
            if (handledHere) v.performClick();
        }

        return handledHere;
    }

    public boolean startDrag(View v) {
        // Let the DragController initiate a drag-drop sequence.
        // I use the dragInfo to pass along the object being dragged.
        // I'm not sure how the Launcher designers do this.
        Object dragInfo = v;
        mDragController.startDrag(v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
        return true;
    }

    private void setupViews() {
        DragController dragController = mDragController;

        mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
        mDragLayer.setDragController(dragController);
        dragController.addDropTarget(mDragLayer);

        //mDragController = dragController;
    }
}