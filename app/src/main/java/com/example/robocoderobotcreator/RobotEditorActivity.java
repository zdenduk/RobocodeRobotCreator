package com.example.robocoderobotcreator;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

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

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RobotBlueprint rb;
    FrameLayout canvas;
    LinearLayout top_bar;
    LinearLayout bottom_bar;
    int window_height;

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

        window_height = getResources().getDisplayMetrics().heightPixels;

        canvas = findViewById(R.id.edit_canvas);
        top_bar = findViewById(R.id.top_bar);
        bottom_bar = findViewById(R.id.bottom_bar);

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
                canvas.addView(createBasicBlock("ELSE", new ElseBlock()));
                return true;
            case R.id.if_item:
                canvas.addView(createBasicBlock("IF", new IfBlock()));
                return true;
            case R.id.onhitwall_item:
                canvas.addView(createBasicBlock("ONHITWALL", new OnHitWall()));
                return true;
            case R.id.onscannedrobot_item:
                canvas.addView(createBasicBlock("ONSCANNEDROBOT", new OnScannedRobot()));
                return true;
            case R.id.run_item:
                canvas.addView(createBasicBlock("RUN", new Run()));
                return true;
            case R.id.while_item:
                canvas.addView(createBasicBlock("WHILE", new WhileBlock()));
                return true;
            case R.id.fire_item:
                canvas.addView(createBasicBlock("FIRE", new Fire()));
                return true;
            case R.id.turngunleft_item:
                canvas.addView(createBasicBlock("TURNGUNLEFT", new TurnGunLeft()));
                return true;
            case R.id.turngunright_item:
                canvas.addView(createBasicBlock("TURNGUNRIGHT", new TurnGunRight()));
                return true;
            case R.id.ahead_item:
                canvas.addView(createBasicBlock("AHEAD", new Ahead()));
                return true;
            case R.id.back_item:
                canvas.addView(createBasicBlock("BACK", new Back()));
                return true;
            default:
                return false;
        }
    }

    private BasicBlock createBasicBlock(String param, Block type) {
        BasicBlock bb = new BasicBlock(getApplicationContext(), type);
        bb.setTag(param);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(128, 128);
        bb.setLayoutParams(layoutParams);
        bb.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            bb.setVisibility(View.INVISIBLE);

                            // As an example of what your application might do,
                            // applies a blue color tint to the View to indicate that it can accept
                            // data.
                            //v.setColorFilter(Color.BLUE);

                            // Invalidate the view to force a redraw in the new tint
                            v.invalidate();

                            // returns true to indicate that the View can accept the dragged data.
                            return true;

                        }

                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        // Applies a green tint to the View. Return true; the return value is ignored.

                        //v.setColorFilter(Color.GREEN);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:

                        // Ignore the event
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:

                        // Re-sets the color tint to blue. Returns true; the return value is ignored.
                        //v.setColorFilter(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        CharSequence dragData = item.getText();

                        // Displays a message containing the dragged data.
                        Toast.makeText(v.getContext(), "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                        // Turns off any color tints
                        //v.clearColorFilter();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Returns true. DragEvent.getResult() will return true.
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        float x = event.getX();
                        float y = event.getY();

                        // detect bounds
                        if (y < top_bar.getHeight() || y > window_height - bottom_bar.getHeight()) {
                            bb.setVisibility(View.VISIBLE);
                            return false;
                        }

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        bb.setX(x - 64);
                        bb.setY(y - top_bar.getHeight() - 64);
                        bb.setVisibility(View.VISIBLE);


                        // Does a getResult(), and displays what happened.
                        if (event.getResult()) {
                            Toast.makeText(v.getContext(), "The drop was handled.", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(v.getContext(), "The drop didn't work.", Toast.LENGTH_LONG).show();
                        }

                        // returns true; the value is ignored.
                        return true;

                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }
                return false;
            }
        });
        bb.setOnLongClickListener(v -> {
            ClipData.Item item1 = new ClipData.Item((CharSequence) v.getTag());

            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item1);

            v.startDrag(dragData,  // the data to be dragged
                    new View.DragShadowBuilder(v),  // the drag shadow builder
                    null,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );
            return true;
        });
        return bb;
    }
}