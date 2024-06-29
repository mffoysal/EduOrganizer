package com.edu.eduorganizer.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edu.eduorganizer.R;
import com.edu.eduorganizer.entity.Note;
import com.edu.eduorganizer.entity.Task;
import com.edu.eduorganizer.fragment.Tasks;
import com.edu.eduorganizer.note.NoteDetails;
import com.edu.eduorganizer.routine.RoutineSchedule;
import com.edu.eduorganizer.unique.Unique;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder3>{

    private Context context;
    private List<Task> dataList;
    private AdapterView.OnItemClickListener itemClickListener;
    private OnEditClickListener editClickListener;
    private OnCopyClickListener copyClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnSetScheduleListener onSetScheduleListener;
    private OnRemindClickListener remindClickListener;
    private OnDoneClickListener doneClickListener;


    public void setSearchList(List<Task> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public TaskAdapter(Context context, List<Task> dataList, AdapterView.OnItemClickListener listener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener, OnSetScheduleListener setScheduleListener, OnDoneClickListener doneClickListener, OnRemindClickListener remindClickListener){
        this.context = context;
        this.dataList = dataList;
        this.itemClickListener = listener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
        this.copyClickListener = copyClickListener;
        this.onSetScheduleListener = setScheduleListener;
        this.doneClickListener = doneClickListener;
        this.remindClickListener = remindClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false);
        return new MyViewHolder3(view,itemClickListener,editClickListener,deleteClickListener,copyClickListener,onSetScheduleListener,doneClickListener,remindClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        Task task = dataList.get(position);
        holder.recTitle.setText(dataList.get(position).getTask_name());
        holder.recDesc.setText(dataList.get(position).getTask_details());

        String day, dayM, month, time, date, dayC, monthC, m;
        m = dataList.get(position).getCalendar();
        date = dataList.get(position).getDateTime();
        try {
            day = getDayName(dataList.get(position).getDateTime());
            month = getMonthName(dataList.get(position).getDateTime());
            time = new Unique().getTimeWithAMPM(dataList.get(position).getDateTime());
            dayM = new Unique().getDayOfMonth(dataList.get(position).getDateTime());
            date = day+" "+dayM+" "+month+" "+time;


            dayC = new Unique().getDayMonth(dataList.get(position).getCalendar());
            monthC = new Unique().getMonthN(dataList.get(position).getCalendar());
            m = dayC+" "+monthC;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.details.setText(date);
        if (dataList.get(position).getDone()!=0){
            holder.radioButton.setChecked(true);
        }
        int randomColor = getRandomColor();
//        radioButton.setBackgroundColor(randomColor);

        holder.radioButton.setBackgroundColor(randomColor);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task!=null){
                    doneClickListener.onDoneClick(task);
                }
            }
        });
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, NoteDetails.class);
                intent.putExtra("task_name", dataList.get(holder.getAdapterPosition()).getTask_name());
                intent.putExtra("task_code", dataList.get(holder.getAdapterPosition()).getTask_code());
                intent.putExtra("task_location", dataList.get(holder.getAdapterPosition()).getTask_location());
                intent.putExtra("stdId", dataList.get(holder.getAdapterPosition()).getStdId());
                intent.putExtra("sId", dataList.get(holder.getAdapterPosition()).getSId());
                intent.putExtra("task",task);

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

        ((MyViewHolder3) holder).showData(task);

    }
    private int getRandomColor() {
        Random random = new Random();

        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        // Create a color from the RGB values
        return Color.rgb(red, green, blue);
    }

    public static String getDayName(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        String[] daysOfWeek = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        return daysOfWeek[dayOfWeek];
    }

    public static String getMonthName(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);

        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    public static String getTimeWithAMPM(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = sdf.parse(dateTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return timeFormat.format(date);
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
        void onSetClick(Task task);
    }

    public interface OnEditClickListener{
        void onEditClick(Task task);
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(Task task);
    }
    public interface OnCopyClickListener{
        void onCopyClick(Task task);
    }

    public void removeItem(int index){
        dataList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }
    public void undoItem(Task task, int index){
        dataList.add(index, task);
        notifyItemInserted(index);
    }

    public interface OnDoneClickListener{
        void onDoneClick(Task task);
    }

    public interface OnRemindClickListener{
        void onRemindClick(Task task);
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
        private OnRemindClickListener remindClickListener;
        private OnDoneClickListener doneClickListener;
        private Context context;
        private CheckBox radioButton;

        public MyViewHolder3(@NonNull View itemView, AdapterView.OnItemClickListener clickListener, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener, OnCopyClickListener copyClickListener, OnSetScheduleListener setScheduleListener, OnDoneClickListener doneClickListener, OnRemindClickListener remindClickListener) {
            super(itemView);
            this.context = itemView.getContext();
            this.clickListener = clickListener;
            this.editClickListener = editClickListener;
            this.deleteClickListener = deleteClickListener;
            this.copyClickListener = copyClickListener;
            this.setScheduleListener = setScheduleListener;
            this.doneClickListener = doneClickListener;
            this.remindClickListener = remindClickListener;
//            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recCard = itemView.findViewById(R.id.recCard);
            options = itemView.findViewById(R.id.options);
            radioButton = itemView.findViewById(R.id.radioButton);
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

        public void showData(Task task) {

        }
    }




}
