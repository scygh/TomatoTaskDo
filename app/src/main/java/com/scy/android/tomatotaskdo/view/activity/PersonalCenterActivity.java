package com.scy.android.tomatotaskdo.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.fragment.BirthPickerFragment;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PersonalCenterActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.update_email)
    AutoCompleteTextView updateEmail;
    @BindView(R.id.update_password)
    EditText updatePassword;
    @BindView(R.id.update_button)
    Button updateButton;
    User user;
    @BindView(R.id.rank_header_img)
    SimpleDraweeView rankHeaderImg;
    @BindView(R.id.rank_header_text)
    TextView rankHeaderText;
    @BindView(R.id.personal_address)
    TextView personalAddress;
    @BindView(R.id.personal_birthday)
    TextView personalBirthday;
    @BindView(R.id.personal_sex)
    EditText personalSex;
    private static final int REQUEST_CODE_PICK_CITY = 0;
    BirthPickerFragment mDatePickerFragment;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initViews() {
        user = DbRequest.getCurrentUser(this);
        updateEmail.setText(user.getUsername());
        updatePassword.setText(user.getPassword());
        personalSex.setText(user.getSex());
        personalBirthday.setText(user.getBirthday());
        personalAddress.setText(user.getAddress());
        updatePassword.requestFocus();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = updatePassword.getText().toString();
                String address = personalAddress.getText().toString();
                String birthday = personalBirthday.getText().toString();
                String sex = personalSex.getText().toString();
                user.setPassword(password);
                user.setSex(sex);
                user.setAddress(address);
                user.setBirthday(birthday);
                user.update(user.getId());
                finish();
                ToastHelper.showToast(PersonalCenterActivity.this, "修改成功", Toast.LENGTH_SHORT);
            }
        });
        rankHeaderImg.setImageURI(user.getHeaderImageUri());
        rankHeaderText.setText(user.getUsername());


        personalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动
                requestPermission(PersonalCenterActivity.this, "定位", 5, permissions);
                if (checkPermission(PersonalCenterActivity.this, permissions)) {
                    startActivityForResult(new Intent(PersonalCenterActivity.this, CityPickerActivity.class),
                            REQUEST_CODE_PICK_CITY);
                }

            }
        });
        mDatePickerFragment = BirthPickerFragment.newInstance(new Date());
        personalBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                mDatePickerFragment.show(fragmentManager, "date");
            }
        });
        mDatePickerFragment.setOnDialogListener(new BirthPickerFragment.OnDialogListener() {
            @Override
            public void onDialogClick(Date date) {
                //根据date 来更新列表
                personalBirthday.setText(TimeUtil.formatMyTaskTime(date));
            }
        });
    }

    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                personalAddress.setText(city);
            }
        }
    }

    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean checkPermission(Activity context, String[] perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    public static void requestPermission(Activity context, String tip, int requestCode, String[] perms) {
        EasyPermissions.requestPermissions(context, tip, requestCode, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //ToastHelper.showToast(this,"用户授权成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastHelper.showToast(this, "用户授权失败", Toast.LENGTH_SHORT);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
