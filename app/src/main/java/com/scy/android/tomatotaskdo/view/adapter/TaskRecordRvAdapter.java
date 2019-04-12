package com.scy.android.tomatotaskdo.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.MyInterface.OnItemClickListener;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;

import java.util.Collections;
import java.util.List;

/**
 * 沈程阳
 * created by scy on 2019/4/10 11:14
 * 邮箱：1797484636@qq.com
 */
public class TaskRecordRvAdapter extends RecyclerView.Adapter<TaskRecordRvAdapter.ViewHolder>{

    private static final String TAG = "TaskFragmentRvAdapter";
    private List<Task> mTasks;
    private Context mContext;

    public TaskRecordRvAdapter(List<Task> tasks, Context context) {
        mTasks = tasks;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDetail,tvPriority,tvCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.text_counttitle);
            tvDetail = itemView.findViewById(R.id.text_detail);
            tvPriority = itemView.findViewById(R.id.text_priority_size);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.mytask_card_view, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            int count = i + 1;
            viewHolder.tvCount.setText(count + "");
            viewHolder.tvDetail.setText(mTasks.get(i).gettDescription());
            viewHolder.tvPriority.setText(mTasks.get(i).getPriority());

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

}
