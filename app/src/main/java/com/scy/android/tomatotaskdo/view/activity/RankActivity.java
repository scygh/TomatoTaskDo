package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.MyInterface.OnItemClickListener;
import com.scy.android.tomatotaskdo.conpoment.manager.MyLinearLayoutManager;
import com.scy.android.tomatotaskdo.conpoment.utils.SortUtil;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.adapter.RankRvAdapter;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class RankActivity extends BaseActivity {

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
        mRankRvAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*User user = sortUsers.get(position);
                int dz = user.getDz();
                dz = dz + 1;
                user.setDz(dz);
                user.update(user.getId());
                mRankRvAdapter.notifyItemChanged(position);*/
            }
        });

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
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我今天在番茄任务专注了" + DbRequest.getCurrentUserTodayFocusTime(this, user) + "分钟");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imguri);//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("很认真");
        // 启动分享GUI
        oks.show(this);
    }
}

