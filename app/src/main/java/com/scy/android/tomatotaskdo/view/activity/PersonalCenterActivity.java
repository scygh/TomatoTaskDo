package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.DbRequest;

import butterknife.BindView;

public class PersonalCenterActivity extends BaseActivity {

    @BindView(R.id.update_email)
    AutoCompleteTextView updateEmail;
    @BindView(R.id.update_password)
    EditText updatePassword;
    @BindView(R.id.update_button)
    Button updateButton;
    User user;

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
        user= DbRequest.getCurrentUser(this);
        updateEmail.setText(user.getUsername());
        updatePassword.requestFocus();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = updatePassword.getText().toString();
                user.setPassword(password);
                user.update(user.getId());
                finish();
                ToastHelper.showToast(PersonalCenterActivity.this,"修改成功",Toast.LENGTH_SHORT);
            }
        });
    }


}
