package com.project.eggtimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<TimerTab> tabs;

    public PagerAdapter(List<TimerTab> tabs) {
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pager, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PagerViewHolder) holder).bind(tabs.get(position));
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }
}

class PagerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;

    public PagerViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }

    void bind(TimerTab timerTab) {
        imageView.setImageResource(timerTab.image);
    }
}