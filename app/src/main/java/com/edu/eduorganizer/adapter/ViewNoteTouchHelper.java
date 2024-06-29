package com.edu.eduorganizer.adapter;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ViewNoteTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ItemTouchListener itemTouchHelper;

    public ViewNoteTouchHelper(int dragDirs, int swipeDirs, ItemTouchListener itemTouchHelper) {
        super(dragDirs, swipeDirs);
        this.itemTouchHelper = itemTouchHelper;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if (itemTouchHelper!= null){
            itemTouchHelper.onSwiped(viewHolder);
        }

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder!=null){
            View foregroundView = ((NoteAdapter.MyViewHolder3) viewHolder).foreground;
            getDefaultUIUtil().onSelected(foregroundView);
        }
//        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((NoteAdapter.MyViewHolder3) viewHolder).foreground;
        getDefaultUIUtil().onDrawOver(c,recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);


//        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((NoteAdapter.MyViewHolder3) viewHolder).foreground;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);

//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((NoteAdapter.MyViewHolder3) viewHolder).foreground;
        getDefaultUIUtil().clearView(foregroundView);

//        super.clearView(recyclerView, viewHolder);
    }
}
