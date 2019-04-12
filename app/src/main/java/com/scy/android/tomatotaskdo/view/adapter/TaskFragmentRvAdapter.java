package com.scy.android.tomatotaskdo.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

/**
 * 沈程阳
 * created by scy on 2019/4/10 11:14
 * 邮箱：1797484636@qq.com
 */
public class TaskFragmentRvAdapter extends RecyclerView.Adapter<TaskFragmentRvAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private static final String TAG = "TaskFragmentRvAdapter";
    private List<Task> mTasks;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public TaskFragmentRvAdapter(List<Task> tasks, Context context) {
        mTasks = tasks;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDetail,tvPriority,tvCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCount = itemView.findViewById(R.id.text_counttitle);
            tvDetail = itemView.findViewById(R.id.text_detail);
            tvPriority = itemView.findViewById(R.id.text_priority_size);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.card_view, viewGroup, false);
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


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(mTasks,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        mTasks.remove(position);
        notifyItemRemoved(position);
        User user = DbRequest.getCurrentUser(mContext);
        List<Task> tasks = DbRequest.getCurrentUserTodayUnFinishTasks(mContext, user);
        Log.d(TAG, "onItemDissmiss: " + position);
        Task task = tasks.get(position);
        task.setIsFinished("已完成");
        task.update(task.getId());
        user.update(user.getId());
    }
}
