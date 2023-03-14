package com.example.todolist20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist20.adapter.ToDoAdapter;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    ToDoAdapter adapter;

    public SwipeHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final  int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder =  new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delte Task");
            builder.setMessage("Do you want to delete this task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset =20;

        if(dX > 0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.green));
        } else{
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.red));
        }

        int iconMargin = (itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop+icon.getIntrinsicHeight();

        if(dX>0){
            int iconLeft = itemView.getLeft() +iconMargin;
            int iconRight = itemView.getLeft() +iconMargin + icon.getIntrinsicHeight();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft()+((int) dX)+ backgroundCornerOffset, itemView.getBottom());
        } else if(dX<0){
            int iconLeft =itemView.getRight() - iconMargin - icon.getIntrinsicHeight();
            int icoRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, icoRight, iconBottom);            background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft()+((int) dX)+ backgroundCornerOffset, itemView.getBottom());
            background.setBounds(itemView.getRight()+((int) dX)+ backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }

        else{
            background.setBounds(0,0,0,0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
