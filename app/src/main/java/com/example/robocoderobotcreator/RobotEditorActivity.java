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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.ComboBlock;
import com.example.robocoderobotcreator.model.ParametrizedBlock;
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
import com.example.robocoderobotcreator.model.movement.TurnLeft;
import com.example.robocoderobotcreator.model.movement.TurnRight;
import com.example.robocoderobotcreator.model.radar.SetAdjustRadarForGunTurn;
import com.example.robocoderobotcreator.model.radar.SetAdjustRadarForRobotTurn;
import com.example.robocoderobotcreator.model.radar.TurnRadarLeft;
import com.example.robocoderobotcreator.model.radar.TurnRadarRight;
import com.example.robocoderobotcreator.model.weapons.Fire;
import com.example.robocoderobotcreator.model.weapons.TurnGunLeft;
import com.example.robocoderobotcreator.model.weapons.TurnGunRight;
import com.example.robocoderobotcreator.support.RobotDataManager;
import com.example.robocoderobotcreator.view.BasicBlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class RobotEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RobotBlueprint rb;
    private EditText robotNameEditText;

    private FrameLayout canvas;
    private LinearLayout top_bar;
    private LinearLayout bottom_bar;
    private ImageView trash;

    private int window_height;
    private int window_width;

    List<BasicBlockView> blockList;

    int defaultBlockDimension;

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

        window_width = getResources().getDisplayMetrics().widthPixels;
        window_height = getResources().getDisplayMetrics().heightPixels;

        robotNameEditText = findViewById(R.id.robot_name_edit);
        canvas = findViewById(R.id.edit_canvas);
        top_bar = findViewById(R.id.top_bar);
        bottom_bar = findViewById(R.id.bottom_bar);

        defaultBlockDimension = 192;

        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", -1);

        //  rb = RobotDataManager.INSTANCE.getRobotData().get(pos);
        rb = new RobotBlueprint();
        blockList = new ArrayList<>();

        trash = (ImageView) findViewById(R.id.trash);
        trash.setTag("TRASH");
        trash.setOnDragListener(new CustomDragListener());
    }

    private class CustomDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            final int action = event.getAction();
            // System.out.println(action);

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        if (event.getLocalState().equals(v)) {
                            v.setVisibility(View.INVISIBLE);
                            trash.setVisibility(View.VISIBLE);
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
                    trash.setVisibility(View.INVISIBLE);

                    // View was dropped on itself
                    if (event.getLocalState().equals(v)) {
                        return false;
                    }

                    BasicBlockView draggedBlock = (BasicBlockView) event.getLocalState();

                    // View was dropped on trash
                    if (v.getTag() == "TRASH") {
                        if (draggedBlock.getBlockParent() != null) {
                            BasicBlockView parent = draggedBlock.getBlockParent();
                            ((ComboBlock) parent.getBlockRef()).getBlocks().remove(draggedBlock.getBlockRef());
                        } else {
                            rb.getBlockList().remove(draggedBlock.getBlockRef());
                        }
                        removeAllChildrenBlockViews(draggedBlock);

                        ((ViewGroup) draggedBlock.getParent()).removeView(draggedBlock);
                        Toast.makeText(v.getContext(), "Block was successfully deleted.", Toast.LENGTH_LONG).show();
                        return true;
                    }

                    // A block was dropped on another block
                    // Resolve which block was target
                    BasicBlockView targetBlock = (BasicBlockView) v;

                    if (targetBlock.getBlockRef() instanceof ComboBlock) {
                        rb.getBlockList().remove(draggedBlock.getBlockRef());
                        ((ComboBlock) targetBlock.getBlockRef()).getBlocks().add(draggedBlock.getBlockRef());

                        if (draggedBlock.getBlockParent() != null) {
                            BasicBlockView parent = draggedBlock.getBlockParent();

                            ((ComboBlock) parent.getBlockRef()).getBlocks().remove(draggedBlock.getBlockRef());

                            draggedBlock.setBlockParent(null);

                            RobotEditorActivity.this.adaptBlockDimensions(parent, ((ComboBlock) parent.getBlockRef()).getBlocks().size());

                            RobotEditorActivity.this.moveChildren(parent, parent.getX() + defaultBlockDimension / 2, parent.getY() + top_bar.getHeight() + defaultBlockDimension / 2);
                        }

                        draggedBlock.setBlockParent(targetBlock);

                        int targetBlockChildrenCount = ((ComboBlock) targetBlock.getBlockRef()).getBlocks().size();
                        int overlap = ((int) targetBlock.getX() + defaultBlockDimension * targetBlockChildrenCount + defaultBlockDimension) - window_width;
                        int blocksOverlapping = (int) Math.ceil((double) overlap / defaultBlockDimension);
                        int blocksPerRow = targetBlockChildrenCount - blocksOverlapping;
                        int rows = (int) Math.ceil((double) targetBlockChildrenCount / blocksPerRow);

                        // Position dragged block on top of target block
                        if (targetBlock.getX() + defaultBlockDimension * targetBlockChildrenCount + defaultBlockDimension > window_width) {
                            // Block would be put over the screen border
                            if (targetBlockChildrenCount % blocksPerRow == 0) {
                                draggedBlock.setX(targetBlock.getX() + defaultBlockDimension * (blocksPerRow));
                            } else {
                                draggedBlock.setX(targetBlock.getX() + defaultBlockDimension * (targetBlockChildrenCount % blocksPerRow));
                            }
                            draggedBlock.setY(targetBlock.getY() + 12 + defaultBlockDimension * (rows - 1));
                            draggedBlock.bringToFront();
                            RobotEditorActivity.this.moveChildren(draggedBlock, draggedBlock.getX() + defaultBlockDimension / 2, draggedBlock.getY() + top_bar.getHeight() + defaultBlockDimension / 2);
                        } else {
                            draggedBlock.setX(targetBlock.getX() + defaultBlockDimension * targetBlockChildrenCount);
                            draggedBlock.setY(targetBlock.getY() + 12);
                            draggedBlock.bringToFront();
                            RobotEditorActivity.this.moveChildren(draggedBlock, draggedBlock.getX() + defaultBlockDimension / 2, draggedBlock.getY() + top_bar.getHeight() + defaultBlockDimension / 2);
                        }
                        // Extend target block to create space for dropped block
                        RobotEditorActivity.this.adaptBlockDimensions(targetBlock, targetBlockChildrenCount);

                    } else {
                        Toast.makeText(v.getContext(), "This block is not a ComboBlock!", Toast.LENGTH_LONG).show();
                    }

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    trash.setVisibility(View.INVISIBLE);

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

                        draggedBlock = (BasicBlockView) v;

                        draggedBlock.setX(x - defaultBlockDimension / 2);
                        draggedBlock.setY(y - top_bar.getHeight() - defaultBlockDimension / 2);

                        RobotEditorActivity.this.moveChildren(draggedBlock, x, y);

                        if (draggedBlock.getBlockParent() != null) {
                            BasicBlockView parent = draggedBlock.getBlockParent();

                            ((ComboBlock) parent.getBlockRef()).getBlocks().remove(draggedBlock.getBlockRef());
                            rb.getBlockList().add(draggedBlock.getBlockRef());

                            draggedBlock.setBlockParent(null);

                            RobotEditorActivity.this.adaptBlockDimensions(parent, ((ComboBlock) parent.getBlockRef()).getBlocks().size());

                            RobotEditorActivity.this.moveChildren(parent, parent.getX() + defaultBlockDimension / 2, parent.getY() + top_bar.getHeight() + defaultBlockDimension / 2);
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
        }
    }

    private void removeAllChildrenBlockViews(BasicBlockView draggedBlock) {
        if (draggedBlock.getBlockRef() instanceof ComboBlock) {
            if (((ComboBlock) draggedBlock.getBlockRef()).getBlocks().size() > 0) {
                Set<Block> children = ((ComboBlock) draggedBlock.getBlockRef()).getBlocks();
                for (Block child : children) {
                    for (BasicBlockView basicBlockView : blockList) {
                        if (basicBlockView.getBlockRef().equals(child)) {
                            removeAllChildrenBlockViews(basicBlockView);
                            ((ViewGroup) basicBlockView.getParent()).removeView(basicBlockView);
                        }
                    }
                }
            }
        }
    }

    public void saveRobot(View view) {
        rb.setName(robotNameEditText.getText().toString());
        for (BasicBlockView basicBlockView : blockList) {
            if (basicBlockView.getBlockRef() instanceof ParametrizedBlock) {
                ParametrizedBlock pb = (ParametrizedBlock) basicBlockView.getBlockRef();
                pb.setParameter(basicBlockView.getParameterEditText().getText().toString());
            }
        }
        RobotDataManager.INSTANCE.writeRobotFileOnInternalStorage(getApplicationContext(), rb);
    }

    public void showRobotText(View view) {
        for (BasicBlockView basicBlockView : blockList) {
            if (basicBlockView.getBlockRef() instanceof ParametrizedBlock) {
                ParametrizedBlock pb = (ParametrizedBlock) basicBlockView.getBlockRef();
                pb.setParameter(basicBlockView.getParameterEditText().getText().toString());
            }
        }
        rb.setName(robotNameEditText.getText().toString());
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.editor_robot_text_popup, null);
        TextView tv = popupView.findViewById(R.id.robot_text_target);
        tv.setText(Translator.INSTANCE.translateRobotBlueprint(rb));
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        System.out.println(Translator.INSTANCE.translateRobotBlueprint(rb));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
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
            case R.id.turnleft_item:
                canvas.addView(createBasicBlock("TURNLEFT", new TurnLeft()));
                return true;
            case R.id.turnright_item:
                canvas.addView(createBasicBlock("TURNRIGHT", new TurnRight()));
                return true;
            case R.id.turnradarleft_item:
                canvas.addView(createBasicBlock("TURNRADARLEFT", new TurnRadarLeft()));
                return true;
            case R.id.turnradarright_item:
                canvas.addView(createBasicBlock("TURNRADARRIGHT", new TurnRadarRight()));
                return true;
            case R.id.setadjustradarforgunturn_item:
                canvas.addView(createBasicBlock("SETADJUSTRADARFORGUNTURN", new SetAdjustRadarForGunTurn()));
                return true;
            case R.id.setadjustradarforrobotturn_item:
                canvas.addView(createBasicBlock("SETADJUSTRADARFORROBOTTURN", new SetAdjustRadarForRobotTurn()));
                return true;
            default:
                return false;
        }
    }

    private BasicBlockView createBasicBlock(String param, Block type) {
        // Create BasicBlock
        BasicBlockView bb = new BasicBlockView(getApplicationContext(), type);

        // Add block reference for collisions checking
        blockList.add(bb);

        // Add block type to robot blueprint
        rb.getBlockList().add(bb.getBlockRef());

        bb.setTag(param);
        AtomicReference<FrameLayout.LayoutParams> layoutParams = new AtomicReference<>(new FrameLayout.LayoutParams(defaultBlockDimension, defaultBlockDimension));
        bb.setLayoutParams(layoutParams.get());
        bb.setOnDragListener(new CustomDragListener());
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

    private void adaptBlockDimensions(BasicBlockView targetBlock, int targetBlockChildrenCount) {
        int overlap = ((int) targetBlock.getX() + defaultBlockDimension * targetBlockChildrenCount + defaultBlockDimension) - window_width;
        int blocksOverlapping = (int) Math.ceil((double) overlap / defaultBlockDimension);
        int blocksPerRow = targetBlockChildrenCount - blocksOverlapping;
        int rows = (int) Math.ceil((double) targetBlockChildrenCount / blocksPerRow);
        AtomicReference<FrameLayout.LayoutParams> layoutParams = new AtomicReference<>(new FrameLayout.LayoutParams(defaultBlockDimension, defaultBlockDimension));
        if (targetBlockChildrenCount != 0) {
            if (overlap > 0) {
                layoutParams.set(new FrameLayout.LayoutParams(defaultBlockDimension + 24 + blocksPerRow * defaultBlockDimension, defaultBlockDimension + 24 + (rows - 1) * defaultBlockDimension));
            } else {
                layoutParams.set(new FrameLayout.LayoutParams(defaultBlockDimension + 24 + targetBlockChildrenCount * defaultBlockDimension, defaultBlockDimension + 24 + (rows - 1) * defaultBlockDimension)); // normal dimensions + 24 each, add 128 for another block
            }
            targetBlock.setLayoutParams(layoutParams.get());
            targetBlock.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            targetBlock.setPadding(defaultBlockDimension / 3, 0, 0, 0);
        } else {
            targetBlock.setLayoutParams(layoutParams.get());
            targetBlock.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            targetBlock.setPadding(0, 0, 0, 0);
        }
    }

    private void moveChildren(BasicBlockView draggedBlock, float x, float y) {
        if (draggedBlock.getBlockRef() instanceof ComboBlock) {
            int targetBlockChildrenCount = ((ComboBlock) draggedBlock.getBlockRef()).getBlocks().size();
            int overlap = ((int) draggedBlock.getX() + defaultBlockDimension * targetBlockChildrenCount + defaultBlockDimension) - window_width;
            int blocksOverlapping = (int) Math.ceil((double) overlap / defaultBlockDimension);
            int blocksPerRow = targetBlockChildrenCount - blocksOverlapping;
            if (((ComboBlock) draggedBlock.getBlockRef()).getBlocks().size() > 0) {
                Set<Block> children = ((ComboBlock) draggedBlock.getBlockRef()).getBlocks();
                int counter = 0;
                for (Block child : children) {
                    for (BasicBlockView basicBlockView : blockList) {
                        if (basicBlockView.getBlockRef().equals(child)) {
                            counter++;
                            if (overlap > 0 && blocksPerRow > 0) {
                                System.out.println(blocksPerRow);
                                int row = (int) Math.ceil((double) counter / blocksPerRow);

                                if (counter % blocksPerRow == 0) {
                                    draggedBlock.setX(x - defaultBlockDimension / 2 + defaultBlockDimension * (blocksPerRow));
                                } else {
                                    draggedBlock.setX(x - defaultBlockDimension / 2 + defaultBlockDimension * (counter % blocksPerRow));
                                }
                                draggedBlock.setY(y - top_bar.getHeight() - defaultBlockDimension / 2 + 12 + defaultBlockDimension * (row - 1));
                            } else {
                                basicBlockView.setX(x - defaultBlockDimension / 2 + defaultBlockDimension * counter);
                                basicBlockView.setY(y - top_bar.getHeight() - defaultBlockDimension / 2 + 12);
                            }
                            basicBlockView.bringToFront();
                            moveChildren(basicBlockView, x + defaultBlockDimension * counter, y + 12);
                        }
                    }
                }
            }
        }
    }
}