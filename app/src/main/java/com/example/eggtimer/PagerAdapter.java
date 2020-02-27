package com.example.eggtimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PagerAdapter extends RecyclerView.Adapter {
    private List<PagerM> pagerMList;

    class PagerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDescription;

        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }

    public PagerAdapter(List<PagerM> pagerMList) {
        this.pagerMList = pagerMList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pager, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PagerViewHolder viewHolder = (PagerViewHolder) holder;
        PagerM pagerM = pagerMList.get(position);
        viewHolder.txtDescription.setText(pagerM.getPagerDescription());
    }

    @Override
    public int getItemCount() {
        return pagerMList.size();
    }
}