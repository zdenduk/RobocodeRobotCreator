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

import com.asitrack.fontawesome2.FontAwesome.FontAwesomeUtils;
import com.example.robocoderobotcreator.R;
import com.example.robocoderobotcreator.model.Block;
import com.example.robocoderobotcreator.model.events.OnScannedRobot;
import com.example.robocoderobotcreator.model.events.Run;
import com.example.robocoderobotcreator.model.events.WhileBlock;
import com.example.robocoderobotcreator.model.movement.Ahead;
import com.example.robocoderobotcreator.model.movement.Back;
import com.example.robocoderobotcreator.model.weapons.Fire;
import com.example.robocoderobotcreator.model.weapons.TurnGunLeft;
import com.example.robocoderobotcreator.model.weapons.TurnGunRight;

public class BasicBlock extends androidx.appcompat.widget.AppCompatTextView {

    private final Block blockRef;

    public BasicBlock(@NonNull Context context, Block blockRef) {
        super(context);
        this.blockRef = blockRef;
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

    public BasicBlock(@NonNull Context context, @Nullable AttributeSet attrs, Block blockRef) {
        super(context, attrs);
        this.blockRef = blockRef;
        setColor(context);
    }

    public BasicBlock(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Block blockRef) {
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
        if (Ahead.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.ahead));
        }
        if (Back.class.equals(blockRef.getClass())) {
            this.setText(getResources().getString(R.string.back));
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
