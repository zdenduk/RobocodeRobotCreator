package com.example.robocoderobotcreator;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
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
import com.example.robocoderobotcreator.model.ComboBlock;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RobotBlueprint rb;
    EditText robotNameEditText;

    FrameLayout canvas;
    LinearLayout top_bar;
    LinearLayout bottom_bar;

    int window_height;

    List<BasicBlock> blockList;

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
        blockList = new ArrayList<>();
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
        // Create BasicBlock
        BasicBlock bb = new BasicBlock(getApplicationContext(), type);

        // Add block reference for collisions checking
        blockList.add(bb);

        // Add block type to robot blueprint
        rb.getBlockList().add(bb.getBlockRef());

        bb.setTag(param);
        AtomicReference<FrameLayout.LayoutParams> layoutParams = new AtomicReference<>(new FrameLayout.LayoutParams(128, 128));
        bb.setLayoutParams(layoutParams.get());
        bb.setOnDragListener((v, event) -> {

            final int action = event.getAction();
            // System.out.println(action);

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        if (event.getLocalState().equals(v)) {
                            v.setVisibility(View.INVISIBLE);
                        }

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
                    v.setVisibility(View.VISIBLE);

                    // View was dropped on itself
                    if (event.getLocalState().equals(v)) {
                        return false;
                    }

                    // A block was dropped on another block
                    // Resolve which block was target and which was dragged onto target
                    BasicBlock targetBlock = (BasicBlock) v;
                    BasicBlock draggedBlock = (BasicBlock) event.getLocalState();

                    if (targetBlock.getBlockRef() instanceof ComboBlock) {
                        rb.getBlockList().remove(draggedBlock.getBlockRef());
                        ((ComboBlock) targetBlock.getBlockRef()).getBlocks().add(draggedBlock.getBlockRef());

                        if (draggedBlock.getBlockParent() != null) {
                            BasicBlock parent = draggedBlock.getBlockParent();

                            ((ComboBlock) parent.getBlockRef()).getBlocks().remove(draggedBlock.getBlockRef());

                            draggedBlock.setBlockParent(null);

                            adaptBlockDimensions(parent, ((ComboBlock) parent.getBlockRef()).getBlocks().size());
                        }

                        draggedBlock.setBlockParent(targetBlock);

                        // Extend target block to create space for dropped block
                        int targetBlockChildrenCount = ((ComboBlock) targetBlock.getBlockRef()).getBlocks().size();
                        adaptBlockDimensions(targetBlock, targetBlockChildrenCount);

                        // Position dragged block on top of target block
                        draggedBlock.setX(targetBlock.getX() + 128 * targetBlockChildrenCount);
                        draggedBlock.setY(targetBlock.getY() + 12);
                        draggedBlock.bringToFront();
                        moveChildren(draggedBlock, draggedBlock.getX() + 64, draggedBlock.getY() + top_bar.getHeight() + 64);

                        Toast.makeText(v.getContext(), draggedBlock.getBlockRef() + " connected to " + targetBlock.getBlockRef(), Toast.LENGTH_LONG).show();
                    } else {
                        // TODO this block doesnt accept
                    }

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    // View was dropped on canvas
                    // Check whether dragged view equals any of the blocks
                    if (event.getLocalState().equals(v)) {
                        float x = event.getX();
                        float y = event.getY();

                        // Detect bounds
                        if (y < top_bar.getHeight() || y > window_height - bottom_bar.getHeight()) {
                            v.setVisibility(View.VISIBLE);
                            return false;
                        }

                        draggedBlock = (BasicBlock) v;

                        draggedBlock.setX(x - 64);
                        draggedBlock.setY(y - top_bar.getHeight() - 64);

                        moveChildren(draggedBlock, x, y);

                        if (draggedBlock.getBlockParent() != null) {
                            BasicBlock parent = draggedBlock.getBlockParent();

                            ((ComboBlock) parent.getBlockRef()).getBlocks().remove(draggedBlock.getBlockRef());
                            rb.getBlockList().add(draggedBlock.getBlockRef());

                            draggedBlock.setBlockParent(null);
                        }

                        v.setVisibility(View.VISIBLE);
                        // Invalidates the view to force a redraw
                        v.invalidate();
                        /*
                        Debug purposes
                        for (BasicBlock basicBlock : blockList) {
                            System.out.println(basicBlock.getBlockRef() + " " + basicBlock.getX() + " " + basicBlock.getY() + "visible: " + basicBlock.getVisibility());
                        }
                        */
                    }
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop", "Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        });
        bb.setOnLongClickListener(v -> {
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
        });
        return bb;
    }

    private void adaptBlockDimensions(BasicBlock targetBlock, int targetBlockChildrenCount) {
        AtomicReference<FrameLayout.LayoutParams> layoutParams = new AtomicReference<>(new FrameLayout.LayoutParams(128, 128));
        layoutParams.set(new FrameLayout.LayoutParams(128 + 24 + targetBlockChildrenCount * 128, 128 + 24)); // normal dimensions + 24 each, add 128 for another block
        targetBlock.setLayoutParams(layoutParams.get());
        targetBlock.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        targetBlock.setPadding(32, 0, 0, 0);
    }

    private void moveChildren(BasicBlock draggedBlock, float x, float y) {
        if (draggedBlock.getBlockRef() instanceof ComboBlock) {
            if (((ComboBlock) draggedBlock.getBlockRef()).getBlocks().size() > 0) {
                Set<Block> children = ((ComboBlock) draggedBlock.getBlockRef()).getBlocks();
                int counter = 0;
                for (Block child : children) {
                    for (BasicBlock basicBlock : blockList) {
                        if (basicBlock.getBlockRef().equals(child)) {
                            counter++;
                            basicBlock.setX(x - 64 + 128 * counter);
                            basicBlock.setY(y - top_bar.getHeight() - 64 + 12);
                            basicBlock.bringToFront();
                            moveChildren(basicBlock, x + 128 * counter, y + 12);
                        }
                    }
                }
            }
        }
    }
}