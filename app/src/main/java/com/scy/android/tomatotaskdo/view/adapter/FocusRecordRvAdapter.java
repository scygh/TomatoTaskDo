package com.scy.android.tomatotaskdo.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.Task;

import java.util.List;

/**
 * 沈程阳
 * created by scy on 2019/4/10 11:14
 * 邮箱：1797484636@qq.com
 */
public class FocusRecordRvAdapter extends RecyclerView.Adapter<FocusRecordRvAdapter.ViewHolder>{

    private static final String TAG = "TaskFragmentRvAdapter";
    private List<FocusTime> mFocusTimes;
    private Context mContext;

    public FocusRecordRvAdapter(List<FocusTime> focusTimes, Context context) {
        mFocusTimes = focusTimes;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPriority,tvCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.focus_counttitle);
            tvPriority = itemView.findViewById(R.id.focus_priority_size);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.myfocustimerecord_card_view, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.tvCount.setText(mFocusTimes.get(i).getTime() + "分钟");
            viewHolder.tvPriority.setText(mFocusTimes.get(i).getDate().substring(6));

    }

    @Override
    public int getItemCount() {
        return mFocusTimes.size();
    }

}
