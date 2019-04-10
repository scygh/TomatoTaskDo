package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;
import com.scy.android.tomatotaskdo.view.adapter.GuideAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends BaseActivity {

    int d = 0;
    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    @BindView(R.id.guide_button_starttoexperience)
    Button guideButtonStarttoexperience;
    @BindView(R.id.guide_point_container)
    LinearLayout guidePointContainer;
    @BindView(R.id.point_selected)
    ImageView pointSelected;
    private int[] imageDrawable = {R.drawable.self_guide_01, R.drawable.self_guide_02, R.drawable.self_guide_03};
    private ArrayList<ImageView> mImageViews;

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, GuideActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < imageDrawable.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageDrawable[i]);
            mImageViews.add(imageView);
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.gudie_point_normal);
            guidePointContainer.addView(point);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                params.leftMargin = 25;
            }
            point.setLayoutParams(params);
        }
        guideViewpager.setAdapter(new GuideAdapter(mImageViews));

        guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int distance = (int) (d * v + 0.5f + i * d);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pointSelected.getLayoutParams();
                params.leftMargin = distance;
                pointSelected.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
                if (i == mImageViews.size() - 1) {
                    guideButtonStarttoexperience.setVisibility(View.VISIBLE);
                } else {
                    guideButtonStarttoexperience.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        pointSelected.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                d = guidePointContainer.getChildAt(1).getLeft() - guidePointContainer.getChildAt(0).getLeft();
                pointSelected.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        guideButtonStarttoexperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.setIsFirstVisit(GuideActivity.this, ConstValues.START_MAIN, true);
                startActivity(MainActivity.getIntent(GuideActivity.this));
                finish();
            }
        });
    }
}
