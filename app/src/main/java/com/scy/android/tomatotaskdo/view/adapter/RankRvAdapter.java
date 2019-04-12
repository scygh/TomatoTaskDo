package com.scy.android.tomatotaskdo.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.MyInterface.OnItemClickListener;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;

import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

/**
 * 沈程阳
 * created by scy on 2019/4/10 11:14
 * 邮箱：1797484636@qq.com
 */
public class RankRvAdapter extends RecyclerView.Adapter<RankRvAdapter.ViewHolder>{

    private static final String TAG = "RankRvAdapter";
    private List<User> mUsers;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public RankRvAdapter(List<User> users, Context context) {
        mUsers = users;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName,tvFocusTime,tvCount,tvdz;
        SimpleDraweeView headerImg;
        ImageView ivHeart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.rank_item_count);
            tvName = itemView.findViewById(R.id.rank_item_username);
            tvFocusTime = itemView.findViewById(R.id.rank_item_focustime);
            tvdz = itemView.findViewById(R.id.rank_item_zan_count);
            headerImg = itemView.findViewById(R.id.rank_item_header_img);
            ivHeart = itemView.findViewById(R.id.rank_item_header_img);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.rank_view_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int count = i + 1;
        viewHolder.tvCount.setText(count + "");
        if (mUsers.get(i).getHeaderImageUri()!=null) {
            viewHolder.headerImg.setImageURI(Uri.parse(mUsers.get(i).getHeaderImageUri()));
        }
        viewHolder.tvName.setText(mUsers.get(i).getUsername());
        List<FocusTime> focusTimes = mUsers.get(i).getFocusTimes();
        int time = 0;
        for (FocusTime ft : focusTimes) {
            if (ft.getDate().equals(TimeUtil.formatFocusTime())) {
                time = ft.getTime();
            }
        }
        //time = time - 1;
        if (time < 0) {
            time = 0;
        }
        viewHolder.tvFocusTime.setText(""+time);
        if ( mUsers.get(i).getDz() == 1) {
            viewHolder.ivHeart.setImageDrawable(mContext.getDrawable(R.drawable.heartselect));
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


}
