package com.scy.android.tomatotaskdo.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.utils.SpUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.activity.FocusRecordActivity;
import com.scy.android.tomatotaskdo.view.activity.MyTaskActivity;
import com.scy.android.tomatotaskdo.view.activity.PersonalCenterActivity;
import com.scy.android.tomatotaskdo.view.activity.RankActivity;
import com.scy.android.tomatotaskdo.view.activity.SettingsActivity;
import com.scy.android.tomatotaskdo.view.activity.TomatoActivity;
import com.scy.android.tomatotaskdo.view.activity.WhyActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MineFragment extends BaseFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks{


    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.ll_mine_record)
    LinearLayout llMineRecord;
    @BindView(R.id.ll_mine_authcenter)
    LinearLayout llMineAuthcenter;
    @BindView(R.id.ll_mine_bankcard)
    LinearLayout llMineBankcard;
    @BindView(R.id.ll_mine_loan)
    LinearLayout llMineLoan;
    @BindView(R.id.ll_mine_msg)
    LinearLayout llMineMsg;
    @BindView(R.id.ll_mine_custom)
    LinearLayout llMineCustom;
    @BindView(R.id.ll_mine_setting)
    LinearLayout llMineSetting;
    Unbinder unbinder;
    @BindView(R.id.header_img)
    SimpleDraweeView headerImg;
    private PopupWindow mPopupWindow;
    private TextView tv1, tv2, tv3;
    private int flag = 0;
    private File cameraSavePath;
    private Uri mUri;
    private String photoPath;

    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUri = FileProvider.getUriForFile(mActivity,"com.scy.android.tomatotaskdo.fileprovider",cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            mUri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mUri);
        startActivityForResult(intent, ConstValues.ACTION_CAMERA);
    }

    private void goPhotoAlbum() {
        Intent  intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,ConstValues.ACTION_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstValues.ACTION_CAMERA && resultCode == mActivity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = mUri.getEncodedPath();
            }
            headerImg.setImageURI(mUri);
            DbRequest.saveHeaderImg(mUri.toString(),mActivity);
        }
        if (requestCode == ConstValues.ACTION_PICK && resultCode == mActivity.RESULT_OK) {
            headerImg.setImageURI(data.getData());
            DbRequest.saveHeaderImg(data.getData().toString(),mActivity);
        }
    }

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static boolean checkPermission(Activity context, String[] perms) { return EasyPermissions.hasPermissions(context, perms); }
    public static void requestPermission(Activity context,String tip,int requestCode,String[] perms) { EasyPermissions.requestPermissions(context, tip,requestCode,perms); }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        ToastHelper.showToast(mActivity,"用户授权成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastHelper.showToast(mActivity,"用户授权失败", Toast.LENGTH_SHORT);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) { new AppSettingsDialog.Builder(this).build().show(); }

    }

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, rootView);

        headerImg.setOnClickListener(this);
        llMineSetting.setOnClickListener(this);
        llMineRecord.setOnClickListener(this);
        llMineAuthcenter.setOnClickListener(this);
        //排行
        llMineBankcard.setOnClickListener(this);
        //疑问
        llMineLoan.setOnClickListener(this);
        //我的任务
        llMineMsg.setOnClickListener(this);
        //专注记录
        llMineCustom.setOnClickListener(this);

        //处理Popview
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.pop, null ,false);
        mPopupWindow = new PopupWindow(inflate, 220,350, true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
        tv1 = inflate.findViewById(R.id.one);
        tv2 = inflate.findViewById(R.id.two);
        tv3 = inflate.findViewById(R.id.three);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                requestPermission(mActivity,"打开摄像头", 3, permissions);
                if (checkPermission(mActivity,permissions)) {
                    goCamera();
                }
                mPopupWindow.dismiss();
                flag = 0;
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册挑选
                requestPermission(mActivity,"打开相册", 3, permissions);
                if (checkPermission(mActivity,permissions)) {
                    goPhotoAlbum();
                }
                mPopupWindow.dismiss();
                flag = 0;
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                mPopupWindow.dismiss();
                flag = 0;
            }
        });
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Apis.checkLogin(mActivity)) {
            String username = DbRequest.getCurrentUser(mActivity).getUsername();
            tvMineName.setText(username);
            headerImg.setImageURI(DbRequest.getHeaderImg(mActivity));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mine_record:
                startActivity(TomatoActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_authcenter:
                startActivity(PersonalCenterActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_bankcard:
                startActivity(RankActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_loan:
                startActivity(WhyActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_msg:
                startActivity(MyTaskActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_custom:
                startActivity(FocusRecordActivity.getIntent(mActivity));
                break;
            case R.id.ll_mine_setting:
                startActivity(SettingsActivity.getIntent(mActivity));
                break;
            case R.id.header_img:
                if (flag == 0) {
                    mPopupWindow.showAsDropDown(v, -5, 0);
                    flag = 1;
                } else {
                    mPopupWindow.dismiss();
                    flag = 0;
                }
                break;

            default:
                break;
        }
    }

}
