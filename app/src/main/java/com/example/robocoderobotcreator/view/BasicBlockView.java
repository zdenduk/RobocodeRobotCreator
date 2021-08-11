package com.example.robocoderobotcreator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.robocoderobotcreator.R;
import com.example.robocoderobotcreator.model.Block;
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

public class BasicBlockView extends androidx.appcompat.widget.AppCompatTextView {

    private final Block blockRef;
    private BasicBlockView blockParent;

    public BasicBlockView(@NonNull Context context, Block blockRef) {
        super(context);
        this.blockRef = blockRef;
        blockParent = null;
        initBlockViewParams(context);
    }

    private void initBlockViewParams(@NonNull Context context) {
        setColor(context);
        setText(context);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //setWidth(128);
        //setHeight(128);
        setTextColor(Color.WHITE);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "FontAwesome5FreeSolid900.otf");
        setTypeface(tf);
    }

    public BasicBlockView(@NonNull Context context, @Nullable AttributeSet attrs, Block blockRef) {
        super(context, attrs);
        this.blockRef = blockRef;
        setColor(context);
    }

    public BasicBlockView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Block blockRef) {
        super(context, attrs, defStyleAttr);
        this.blockRef = blockRef;
        setColor(context);
    }

    public Block getBlockRef() {
        return blockRef;
    }

    private void setColor(Context context) {
        switch (blockRef.getCategory()) {
            case EVENTS:
                setBackgroundColor(ContextCompat.getColor(context, R.color.events_color));
                break;
            case MOVEMENT:
                setBackgroundColor(ContextCompat.getColor(context, R.color.movement_color));
                break;
            case RADAR:
                setBackgroundColor(ContextCompat.getColor(context, R.color.radar_color));
                break;
            case WEAPONS:
                setBackgroundColor(ContextCompat.getColor(context, R.color.weapons_color));
                break;
        }
    }

    private void setText(Context context) {
        //IconHelper.resolveIcon(blockRef.getClass(), context, this);
        if (Run.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.run));
        }
        if (Fire.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.fire));
        }
        if (WhileBlock.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.while_));
        }
        if (OnHitWall.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.onhitwall));
        }
        if (Ahead.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.ahead));
        }
        if (Back.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.back));
        }
        if (TurnLeft.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turnleft));
        }
        if (TurnRight.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turnright));
        }
        if (OnScannedRobot.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.onscannedrobot));
        }
        if (TurnGunRight.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turngunright));
        }
        if (TurnGunLeft.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turngunleft));
        }
        if (SetAdjustRadarForGunTurn.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.setadjustradarforgunturn));
        }
        if (SetAdjustRadarForRobotTurn.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.setadjustradarforrobotturn));
        }
        if (TurnRadarLeft.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turnradarleft));
        }
        if (TurnRadarRight.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.turnradarright));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public BasicBlockView getBlockParent() {
        return blockParent;
    }

    public void setBlockParent(BasicBlockView blockParent) {
        this.blockParent = blockParent;
    }
}
