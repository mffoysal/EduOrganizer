package com.edu.eduorganizer.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.routine.EduRoutineSchedule;
import com.edu.eduorganizer.routine.Routine;
import com.edu.eduorganizer.routine.RoutineSchedule;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SchoolRoutineAdapter extends RecyclerView.Adapter<SchoolRoutineAdapter.MyViewHolder3>{

    private Context context;
    private List<Routine> dataList;
    private AdapterView.OnItemClickListener itemClickListener;
    private OnEditClickListener editClickListener;
    private OnCopyClickListener copyClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnSetScheduleListener onSetScheduleListener;

    public void setSearchList(List<Routine> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public SchoolRoutineAdapter(Context context, List<Routine> dataList, AdapterView.OnItemClickListener listener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener, OnSetScheduleListener setScheduleListener){
        this.context = context;
        this.dataList = dataList;
        this.itemClickListener = listener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
        this.copyClickListener = copyClickListener;
        this.onSetScheduleListener = setScheduleListener;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_list, parent, false);
        return new MyViewHolder3(view,itemClickListener,editClickListener,deleteClickListener,copyClickListener,onSetScheduleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        Routine routine = dataList.get(position);
        holder.recTitle.setText(dataList.get(position).getTemp_name());
        holder.recDesc.setText(dataList.get(position).getTemp_num());
        holder.details.setText(dataList.get(position).getTemp_code());



        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EduRoutineSchedule.class);
                intent.putExtra("tempName", dataList.get(holder.getAdapterPosition()).getTemp_name());
                intent.putExtra("tempCode", dataList.get(holder.getAdapterPosition()).getTemp_code());
                intent.putExtra("tempNum", dataList.get(holder.getAdapterPosition()).getTemp_num());
                intent.putExtra("stdId", dataList.get(holder.getAdapterPosition()).getStdId());
                intent.putExtra("sId", dataList.get(holder.getAdapterPosition()).getSId());
                intent.putExtra("routine",routine);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//        holder.options.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showPopUpMenu(holder,view);
//
//            }
//        });

        ((MyViewHolder3) holder).showData(routine);

    }

    private void showPopUpMenu(MyViewHolder3 holder3 ,View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.routine_popup_menu_item_);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_edit){
                    Toast.makeText(view.getContext(),"Edit Button is clicked "+dataList.get(holder3.getAdapterPosition()).getStdId(),Toast.LENGTH_SHORT).show();
                    return true;
                }else if (menuItem.getItemId()==R.id.menu_delete){

                    return true;
                }else {
                    return false;
                }

            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnSetScheduleListener {
        void onSetClick(Routine routine);
    }

    public interface OnEditClickListener{
        void onEditClick(Routine routine);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(Routine routine);
    }
    public interface OnCopyClickListener{
        void onCopyClick(Routine routine);
    }

    public void removeItem(int index){
        dataList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }
    public void undoItem(Routine routine, int index){
        dataList.add(index, routine);
        notifyItemInserted(index);
    }



    public class MyViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recImage;
        TextView recTitle, recDesc, recLang, faculty, room;
        MaterialButton details;
        AppCompatTextView time;
        CardView recCard;
        FrameLayout notification;
        ImageView options;
        RelativeLayout foreground;
        private AdapterView.OnItemClickListener clickListener;
        private OnEditClickListener editClickListener;
        private OnDeleteClickListener deleteClickListener;
        private OnCopyClickListener copyClickListener;
        private OnSetScheduleListener setScheduleListener;
        private Context context;

        public MyViewHolder3(@NonNull View itemView, AdapterView.OnItemClickListener clickListener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener, OnSetScheduleListener setScheduleListener) {
            super(itemView);
            this.context = itemView.getContext();
            this.clickListener = clickListener;
            this.editClickListener = editClickListener;
            this.deleteClickListener = deleteClickListener;
            this.copyClickListener = copyClickListener;
            this.setScheduleListener = setScheduleListener;
//            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recCard = itemView.findViewById(R.id.recCard);
            options = itemView.findViewById(R.id.options);

            foreground = itemView.findViewById(R.id.foregroundId);
            details = itemView.findViewById(R.id.btnCode);

            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = details.getText().toString();
                    ClipData clipData = ClipData.newPlainText("eduRoutine:", buttonText);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context.getApplicationContext(), "Routine Code copied to clipboard "+buttonText, Toast.LENGTH_SHORT).show();

                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("eee","clicked");
                    showPopUpMenu(v);
                }
            });

        }

        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.options){
                Log.d("eee","clicked");
                showPopUpMenu(view);
            }
        }

        private void showPopUpMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.routine_popup_menu_item_);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId()==R.id.menu_editId){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            editClickListener.onEditClick(dataList.get(pos));
                        }
//                        Toast.makeText(view.getContext(),"Edit Button is clicked",Toast.LENGTH_SHORT).show();
                        return true;
                    }else if (menuItem.getItemId()==R.id.menu_duplicateId){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            copyClickListener.onCopyClick(dataList.get(pos));
                        }
                        return true;
                    }else if (menuItem.getItemId()==R.id.menu_SetOnScheduleId){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            setScheduleListener.onSetClick(dataList.get(pos));
                        }
                        return true;
                    }else if (menuItem.getItemId()==R.id.menu_deleteId){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            deleteClickListener.onDeleteClick(dataList.get(pos));
                        }
                        return true;
                    }else {
                        return false;
                    }

                }
            });
            popupMenu.show();
        }

        public void showData(Routine routine) {

        }
    }




}
