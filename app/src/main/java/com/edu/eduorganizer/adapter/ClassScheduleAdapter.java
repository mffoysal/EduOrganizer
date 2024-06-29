package com.edu.eduorganizer.adapter;

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
import com.edu.eduorganizer.routine.ClassScheduleItem;
import com.edu.eduorganizer.routine.schedule.DetailsSchedule;
import com.edu.eduorganizer.schedule.ScheduleDetails;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ClassScheduleAdapter extends RecyclerView.Adapter<ClassScheduleAdapter.MyViewHolder3>{

    private Context context;
    private List<ClassScheduleItem> dataList;
    private AdapterView.OnItemClickListener itemClickListener;
    private OnEditClickListener editClickListener;
    private OnCopyClickListener copyClickListener;
    private OnDeleteClickListener deleteClickListener;

    public void setSearchList(List<ClassScheduleItem> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }
    public void addItem(ClassScheduleItem item, int position) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }
    public void removeAt(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public ClassScheduleItem getItem(int position) {
        return dataList.get(position);
    }
    public ClassScheduleAdapter(Context context, List<ClassScheduleItem> dataList, AdapterView.OnItemClickListener listener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener){
        this.context = context;
        this.dataList = dataList;
        this.itemClickListener = listener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
        this.copyClickListener = copyClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new MyViewHolder3(view,itemClickListener,editClickListener,deleteClickListener,copyClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        ClassScheduleItem scheduleItem = dataList.get(position);
        holder.recTitle.setText(dataList.get(position).getSub_name());
        holder.recDesc.setText(dataList.get(position).getSub_code()+"_"+dataList.get(position).getSection());
        holder.time.setText(dataList.get(position).getStart_time()+"-"+dataList.get(position).getEnd_time());
        holder.faculty.setText(dataList.get(position).getT_name());
        holder.room.setText(dataList.get(position).getRoom());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsSchedule.class);
                intent.putExtra("subName", dataList.get(holder.getAdapterPosition()).getSub_name());
                intent.putExtra("subCode", dataList.get(holder.getAdapterPosition()).getSub_code());
                intent.putExtra("stdId", dataList.get(holder.getAdapterPosition()).getStdId());
                intent.putExtra("sId", dataList.get(holder.getAdapterPosition()).getSId());
                intent.putExtra("ClassSchedule",scheduleItem);

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

        ((MyViewHolder3) holder).showData(scheduleItem);

    }

    private void showPopUpMenu(MyViewHolder3 holder3 ,View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.schedule_popup_menu_item);
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

    public interface OnEditClickListener{
        void onEditClick(ClassScheduleItem scheduleItem);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(ClassScheduleItem scheduleItem);
    }
    public interface OnCopyClickListener{
        void onCopyClick(ClassScheduleItem scheduleItem);
    }

    public void removeItem(int index){
        dataList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }
    public void undoItem(ClassScheduleItem scheduleItem, int index){
        dataList.add(index, scheduleItem);
        notifyItemInserted(index);
    }



    public class MyViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recImage;
        TextView recTitle, recDesc, recLang, faculty, room;
        MaterialButton details;
        AppCompatTextView time;
        CardView recCard;
        FrameLayout options, notification;
        RelativeLayout foreground;
        private AdapterView.OnItemClickListener clickListener;
        private OnEditClickListener editClickListener;
        private OnDeleteClickListener deleteClickListener;
        private OnCopyClickListener copyClickListener;

        public MyViewHolder3(@NonNull View itemView, AdapterView.OnItemClickListener clickListener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener) {
            super(itemView);
            this.clickListener = clickListener;
            this.editClickListener = editClickListener;
            this.deleteClickListener = deleteClickListener;
            this.copyClickListener = copyClickListener;
//            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.item_class_title);
            recDesc = itemView.findViewById(R.id.item_class_code);
            recCard = itemView.findViewById(R.id.item_class_holder);
            options = itemView.findViewById(R.id.item_class_more_button);
            notification = itemView.findViewById(R.id.item_class_notification_button);
            foreground = itemView.findViewById(R.id.foregroundId);
            details = itemView.findViewById(R.id.item_class_details_button);
            time = itemView.findViewById(R.id.item_class_time);
            faculty = itemView.findViewById(R.id.item_class_teacher);
            room = itemView.findViewById(R.id.item_class_room);

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("eee","clicked");
                    showPopUpMenu(v);
                }
            });
            notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("eee","clicked");

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
            popupMenu.inflate(R.menu.schedule_popup_menu_item);
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

        public void showData(ClassScheduleItem scheduleItem) {

        }
    }




}
