package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.manager.MyLinearLayoutManager;
import com.scy.android.tomatotaskdo.conpoment.utils.SortUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.entity.FocusTime;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import java.io.FileNotFoundException;
import java.util.List;
import butterknife.BindView;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class RankActivity extends BaseActivity {
    private static final String TAG = "RankActivity";
    @BindView(R.id.rank_iv_share)
    ImageView rankIvShare;
    @BindView(R.id.rank_header_img)
    SimpleDraweeView rankHeaderImg;
    @BindView(R.id.rank_header_text)
    TextView rankHeaderText;
    @BindView(R.id.rank_header_rl)
    FrameLayout rankHeaderRl;
    @BindView(R.id.rank_rv)
    RecyclerView rankRv;
    private RankRvAdapter mRankRvAdapter;
    private User user;
    private String imguri;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RankActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initViews() {
        user = DbRequest.getCurrentUser(this);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rankRv.setLayoutManager(linearLayoutManager);
        rankRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        List<User> users = DbRequest.findUsers();
        final List<User> sortUsers = SortUtil.sort(users,this);
        mRankRvAdapter = new RankRvAdapter(sortUsers,this);
        rankRv.setAdapter(mRankRvAdapter);
        User firstUser = sortUsers.get(0);
        try {
            rankHeaderText.setText(firstUser.getUsername()+"占领了封面");
            imguri = firstUser.getHeaderImageUri();

            if (imguri != null) {
                Uri uri = Uri.parse(imguri);
                rankHeaderImg.setImageURI(uri);
                rankHeaderRl.setBackground(Drawable.createFromStream(getContentResolver().openInputStream(uri),null));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        rankIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare(); //关闭sso授权 oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("番茄任务");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("https://github.com/scygh/TomatoTaskDo");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我今天在番茄任务里专注了" + DbRequest.getCurrentUserTodayFocusTime(this, user) + "分钟呐。");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imguri);//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("https://github.com/scygh/TomatoTaskDo");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("很认真");
        // 启动分享GUI
        oks.show(this);
    }

    public class RankRvAdapter extends RecyclerView.Adapter<RankRvAdapter.ViewHolder>{

        private static final String TAG = "RankRvAdapter";
        private List<User> mUsers;
        private Context mContext;

        public RankRvAdapter(List<User> users, Context context) {
            mUsers = users;
            mContext = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView tvName,tvFocusTime,tvCount,tvdz;
            public SimpleDraweeView headerImg;
            public ImageView ivHeart;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCount = itemView.findViewById(R.id.rank_item_count);
                tvName = itemView.findViewById(R.id.rank_item_username);
                tvFocusTime = itemView.findViewById(R.id.rank_item_focustime);
                tvdz = itemView.findViewById(R.id.rank_item_zan_count);
                headerImg = itemView.findViewById(R.id.rank_item_header_img);
                ivHeart = itemView.findViewById(R.id.rank_item_heart);
                ivHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivHeart.setImageResource(R.drawable.heartselect);
                        User user = mUsers.get(getAdapterPosition());
                        int old = user.getDz();
                        int n  = old + 1;
                        user.setDz(n);
                        user.update(user.getId());
                        tvdz.setText(n+"");
                    }
                });
            }

        }

        @NonNull
        @Override
        public RankRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.rank_view_item, viewGroup, false);
            final RankRvAdapter.ViewHolder holder = new RankRvAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RankRvAdapter.ViewHolder viewHolder, int i) {
            int count = i + 1;
            viewHolder.tvCount.setText(count + "");
            if (mUsers.get(i).getHeaderImageUri()!=null) {
                viewHolder.headerImg.setImageURI(Uri.parse(mUsers.get(i).getHeaderImageUri()));
            }
            if (count == 1) {
                viewHolder.tvFocusTime.setTextColor(Color.parseColor("#FC4D52"));
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
            if ( mUsers.get(i).getDz() > 0) {
                viewHolder.tvdz.setText(mUsers.get(i).getDz()+"");
            }
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }


}

