package com.example.figma.controller.notice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.figma.R;
import com.example.figma.model.notice_DB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private ArrayList<notice_DB> arrayList;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;



    public NoticeAdapter(ArrayList<notice_DB> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list_recycler, parent, false);
        NoticeAdapter.ViewHolder holder = new NoticeAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getNotice_image())
                .into(holder.iv_profile);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_studentNumber.setText(arrayList.get(position).getStudentNumber());
        holder.tv_userName.setText(arrayList.get(position).getUserName());
        holder.tv_time.setText(arrayList.get(position).getNotice_time());

        String userName = arrayList.get(position).getUserName();
        String title = arrayList.get(position).getTitle();
        String content = arrayList.get(position).getContent();
        String time = arrayList.get(position).getNotice_time();
        String idToken = arrayList.get(position).getIdToken();
        String notice_key = arrayList.get(position).getKey();
        String notice_image = arrayList.get(position).getNotice_image();




        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("notice Board");

        holder.tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notice_intent = new Intent(context, NoticeDetails.class);
                notice_intent.putExtra("username", userName);
                notice_intent.putExtra("title", title);
                notice_intent.putExtra("content", content);
                notice_intent.putExtra("time", time);
                notice_intent.putExtra("idToken",idToken);
                notice_intent.putExtra("key", notice_key);
                notice_intent.putExtra("image", notice_image);

                context.startActivity(notice_intent);
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }




    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        Button tv_title;
        Button tv_studentNumber;
        Button tv_userName, tv_time;

        Button tv_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_time = itemView.findViewById(R.id.tv_time);

            this.tv_studentNumber = itemView.findViewById(R.id.tv_studentNumber);
            this.tv_userName = itemView.findViewById(R.id.tv_userName);

            this.tv_detail = itemView.findViewById(R.id.tv_detail);
        }

    }
}