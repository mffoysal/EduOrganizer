package com.edu.eduorganizer.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder);
    void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);
}
