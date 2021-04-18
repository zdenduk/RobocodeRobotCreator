package com.example.robocoderobotcreator.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robocoderobotcreator.R;
import com.example.robocoderobotcreator.model.RobotBlueprint;

import java.util.ArrayList;
import java.util.List;

public class RobotsRecyclerAdapter extends RecyclerView.Adapter<RobotsRecyclerAdapter.ViewHolder> {

    private List<RobotBlueprint> mRobots = new ArrayList<>();
    private OnRobotListener mOnRobotListener;

    public RobotsRecyclerAdapter(List<RobotBlueprint> mRobots, OnRobotListener onRobotListener) {
        this.mRobots = mRobots;
        this.mOnRobotListener = onRobotListener;
    }

    @NonNull
    @Override
    public RobotsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.robot_editor_inventory_view_row, parent, false);
        return new ViewHolder(view, mOnRobotListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RobotsRecyclerAdapter.ViewHolder holder, int position) {
        holder.name.setText(mRobots.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRobots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        OnRobotListener mOnRobotListener;

        public ViewHolder(@NonNull View itemView, OnRobotListener onRobotListener) {
            super(itemView);
            name = itemView.findViewById(R.id.robot_name);
            mOnRobotListener = onRobotListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnRobotListener.onRobotClick(getAdapterPosition());
        }
    }

    public interface OnRobotListener {
        void onRobotClick(int position);
    }
}
