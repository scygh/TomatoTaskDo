package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.utils.TimeUtil;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;
import butterknife.BindView;

public class AddTaskActivity extends BaseActivity{

    @BindView(R.id.iv_add_ok)
    ImageView ivAddOk;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_priority)
    EditText etPriority;
    private String description,prioritystr;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        return intent;
    }

    interface Anotify {
        void anotify();
    }
    private Anotify mAnotify;
    public void setOnANotify(Anotify mAnotify) {
        this.mAnotify = mAnotify;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void initViews() {
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPriority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prioritystr = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivAddOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存到数据库
                if (Apis.checkLogin(AddTaskActivity.this)) {
                    User user = DbRequest.getCurrentUser(AddTaskActivity.this);
                    Task task = new Task();
                    task.setIsFinished("未完成");
                    task.settDescription(description);
                    task.setPriority(prioritystr);
                    task.setStartTime(TimeUtil.formatTaskTime());
                    task.setUser(user);
                    task.save();
                    user.update(user.getId());
                } else {
                    ToastHelper.showToast(AddTaskActivity.this,"需要您登陆才能保存任务", Toast.LENGTH_SHORT);
                }
                finish();
                mAnotify.anotify();

            }
        });

    }

}
