package com.example.robocoderobotcreator;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.RobotBlueprint;
import com.example.robocoderobotcreator.model.Translator;
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

import java.util.ArrayList;
import java.util.List;

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RobotBlueprint rb;
    EditText robotNameEditText;

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
        robotNameEditText = findViewById(R.id.robot_name_edit);
        canvas = findViewById(R.id.edit_canvas);
        top_bar = findViewById(R.id.top_bar);
        bottom_bar = findViewById(R.id.bottom_bar);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", -1);

        //  rb = RobotDataManager.INSTANCE.getRobotData().get(pos);
        rb = new RobotBlueprint();
    }

    public void saveRobot(View view) {
        rb.setName(robotNameEditText.getText().toString());
        RobotDataManager.INSTANCE.writeRobotFileOnInternalStorage(getApplicationContext(), rb);
    }

    public void showRobotText(View view) {
        rb.setName(robotNameEditText.getText().toString());
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.editor_robot_text_popup, null);
        TextView tv = popupView.findViewById(R.id.robot_text_target);
        tv.setText(Translator.INSTANCE.translateRobotBlueprint(rb));
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
                System.out.println(action);

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                            v.setVisibility(View.INVISIBLE);

                            // Invalidate the view to force a redraw in the new tint
                            v.invalidate();

                            // returns true to indicate that the View can accept the dragged data.
                            return true;
                        }

                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        // Ignore the event
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        System.out.println(item);

                        // Gets the text data from the item.
                        CharSequence dragData = item.getText();

                        // Displays a message containing the dragged data.
                        Toast.makeText(v.getContext(), "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Returns true. DragEvent.getResult() will return true.
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Check whether dragged view equals any of the blocks
                        if (event.getLocalState().equals(v)) {
                            float x = event.getX();
                            float y = event.getY();

                            // Detect bounds
                            if (y < top_bar.getHeight() || y > window_height - bottom_bar.getHeight()) {
                                v.setVisibility(View.VISIBLE);
                                return false;
                            }

                            // Invalidates the view to force a redraw
                            v.invalidate();

                            v.setX(x - 64);
                            v.setY(y - top_bar.getHeight() - 64);
                            v.setVisibility(View.VISIBLE);
                        }
                        return true;

                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                        break;
                }
                return false;
            }
        });
        bb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item1 = new ClipData.Item((CharSequence) v.getTag());

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item1);

                v.startDrag(dragData,  // the data to be dragged
                        new View.DragShadowBuilder(v),  // the drag shadow builder
                        bb,      // reference to block
                        0          // flags (not currently used, set to 0)
                );
                return true;
            }
        });
        return bb;
    }
}