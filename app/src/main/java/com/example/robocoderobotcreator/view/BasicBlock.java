package com.example.robocoderobotcreator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

        initBlockViewParameters(context);
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

    private void initBlockViewParameters(@NonNull Context context) {
        setColor(context);
        setText(context);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        setWidth(128);
        setHeight(128);
        setTextColor(Color.WHITE);
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
            setText("RUN");//FontAwesomeUtils.changeFontAwesomeForRegularIconColor(context, this, "\uf04b", Color.WHITE);
        } else if (Fire.class.equals(blockRef.getClass())) {
            setText("FIRE");//FontAwesomeUtils.changeFontAwesomeForRegularIconColor(context, this, "\uf05b", Color.WHITE);
        } else if (TurnGunLeft.class.equals(blockRef.getClass())) {
            setText("TURNGUNLEFT");
        } else if (TurnGunRight.class.equals(blockRef.getClass())) {
            setText("TURNGUNRIGHT");
        } else if (OnScannedRobot.class.equals(blockRef.getClass())) {
            setText("ONSCANNEDROBOT");
        } else if (WhileBlock.class.equals(blockRef.getClass())) {
            setText("WHILE");
        } else if (Ahead.class.equals(blockRef.getClass())) {
            setText("AHEAD");
        } else if (Back.class.equals(blockRef.getClass())) {
            setText("BACK");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
