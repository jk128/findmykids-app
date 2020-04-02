package com.thesis.findmykidschildren.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thesis.findmykidschildren.R;
import com.thesis.findmykidschildren.entity.Parent;

import java.util.ArrayList;

class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }
}

public class RVAdapter extends RecyclerView.Adapter {
    ArrayList<Parent> parents = new ArrayList<>();
    Context context;

    public RVAdapter(ArrayList<Parent> parents, Context context) {
        super();
        this.parents = parents;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myHolder = (MyViewHolder) holder;
        if (myHolder == null) {
            return;
        }
        myHolder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_row_activity, parent, false);
        return new MyViewHolder(container);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView parentName;
        public TextView parentEmail;

        public MyViewHolder(View v) {
            super(v);
            parentName = v.findViewById(R.id.parentName);
            parentEmail = v.findViewById(R.id.parentEmail);
        }

        private void bindView(int pos) {
            Parent item = parents.get(pos);
            parentName.setText(item.name);
            parentEmail.setText(item.email);
        }
    }
}