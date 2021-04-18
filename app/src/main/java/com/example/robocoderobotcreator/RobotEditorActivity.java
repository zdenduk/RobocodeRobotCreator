package com.example.robocoderobotcreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.example.robocoderobotcreator.model.events.ElseBlock;
import com.example.robocoderobotcreator.model.events.IfBlock;
import com.example.robocoderobotcreator.model.events.OnHitWall;
import com.example.robocoderobotcreator.model.events.OnScannedRobot;
import com.example.robocoderobotcreator.model.events.Run;
import com.example.robocoderobotcreator.model.events.WhileBlock;
import com.example.robocoderobotcreator.model.weapons.Fire;
import com.example.robocoderobotcreator.model.weapons.TurnGunLeft;
import com.example.robocoderobotcreator.model.weapons.TurnGunRight;
import com.example.robocoderobotcreator.support.RobotDataManager;
import com.example.robocoderobotcreator.view.BasicBlock;

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RobotBlueprint rb;
    AbsoluteLayout canvas;

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

        canvas = findViewById(R.id.edit_canvas);

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
                canvas.addView(new BasicBlock(getApplicationContext(), new ElseBlock()));
                return true;
            case R.id.if_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new IfBlock()));
                return true;
            case R.id.onhitwall_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new OnHitWall()));
                return true;
            case R.id.onscannedrobot_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new OnScannedRobot()));
                return true;
            case R.id.run_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new Run()));
                return true;
            case R.id.while_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new WhileBlock()));
                return true;
            case R.id.fire_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new Fire()));
                return true;
            case R.id.turngunleft_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new TurnGunLeft()));
                return true;
            case R.id.turngunright_item:
                canvas.addView(new BasicBlock(getApplicationContext(), new TurnGunRight()));
                return true;
            default:
                return false;
        }
    }
}