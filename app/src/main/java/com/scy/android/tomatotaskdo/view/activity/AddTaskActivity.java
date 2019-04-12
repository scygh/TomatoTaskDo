package com.scy.android.tomatotaskdo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import org.litepal.LitePal;

import butterknife.BindView;

public class AddTaskActivity extends BaseActivity{
    private static final String TAG = "AddTaskActivity";

    @BindView(R.id.iv_add_ok)
    ImageView ivAddOk;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_priority)
    EditText etPriority;
    private String description,prioritystr;
    private boolean flag = false;
    Task oldTask;

    public static Intent getAddTaskAcivityIntent(Context context,String description,String prioity) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra("des", description);
        intent.putExtra("pri", prioity);
        return intent;
    }

    public static Intent getAddTaskAcivityIntent(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        description = intent.getStringExtra("des");
        prioritystr = intent.getStringExtra("pri");
        if (!TextUtils.isEmpty(description)) {
            etDescription.setText(description);
            etPriority.setText(prioritystr);
            flag = true;
            User user = DbRequest.getCurrentUser(this);
            oldTask = DbRequest.getCurrentUserTodayUnFinishSelectTask(AddTaskActivity.this,user,description);

        }
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
                        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(prioritystr)){
                            ToastHelper.showToast(AddTaskActivity.this,"任务内容不能为空", Toast.LENGTH_SHORT);
                            return;
                        }
                        if (!flag) {
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
                            //更新task
                            Log.d(TAG, "onClick: " + oldTask.gettDescription());
                            oldTask.setIsFinished("未完成");
                            oldTask.settDescription(description);
                            oldTask.setPriority(prioritystr);
                            oldTask.setStartTime(TimeUtil.formatTaskTime());
                            oldTask.update(oldTask.getId());
                            ToastHelper.showToast(AddTaskActivity.this,"修改成功", Toast.LENGTH_SHORT);
                            flag = false;
                        }
                } else {
                    ToastHelper.showToast(AddTaskActivity.this,"需要您登陆才能保存任务", Toast.LENGTH_SHORT);
                }
                finish();
                startActivity(MainActivity.getIntent(AddTaskActivity.this));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MainActivity.getIntent(AddTaskActivity.this));
    }
}
