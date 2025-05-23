package com.example.pickmycoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickmycoach.model.Coach;

import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> {
    private List<Coach> coachList;
    private OnCoachClickListener listener;

    public interface OnCoachClickListener {
        void onCoachClick(Coach coach);
    }

    public CoachAdapter(List<Coach> coachList, OnCoachClickListener listener) {
        this.coachList = coachList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coach, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.bind(coach, listener);
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    static class CoachViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpeciality, tvExperience, tvRating, tvPrice;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCoachName);
            tvSpeciality = itemView.findViewById(R.id.tvCoachSpeciality);
            tvExperience = itemView.findViewById(R.id.tvCoachExperience);
            tvRating = itemView.findViewById(R.id.tvCoachRating);
            tvPrice = itemView.findViewById(R.id.tvCoachPrice);
        }

        public void bind(Coach coach, OnCoachClickListener listener) {
            tvName.setText(coach.getName());
            tvSpeciality.setText(coach.getSpeciality());
            tvExperience.setText("Experience: " + coach.getExperience());
            tvRating.setText("Rating: " + coach.getRating() + "★");
            tvPrice.setText(coach.getPrice());

            itemView.setOnClickListener(v -> listener.onCoachClick(coach));
        }
    }
}
