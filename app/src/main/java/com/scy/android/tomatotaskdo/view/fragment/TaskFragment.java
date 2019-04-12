package com.scy.android.tomatotaskdo.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scy.android.tomatotaskdo.R;
import com.scy.android.tomatotaskdo.conpoment.MyInterface.OnItemClickListener;
import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;
import com.scy.android.tomatotaskdo.conpoment.manager.MyLinearLayoutManager;
import com.scy.android.tomatotaskdo.conpoment.utils.ToastHelper;
import com.scy.android.tomatotaskdo.entity.Task;
import com.scy.android.tomatotaskdo.entity.User;
import com.scy.android.tomatotaskdo.request.Apis;
import com.scy.android.tomatotaskdo.request.DbRequest;
import com.scy.android.tomatotaskdo.view.activity.AddTaskActivity;
import com.scy.android.tomatotaskdo.view.adapter.TaskFragmentRvAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskFragment extends BaseFragment {
    private static final String TAG = "TaskFragment";

    @BindView(R.id.task_recycler_view)
    RecyclerView taskRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.task_fb)
    FloatingActionButton taskFb;
    private TaskFragmentRvAdapter mTaskFragmentRvAdapter;
    private List<Task> mTasks;
    private User currentUser;

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.fragment_task, null);
        unbinder = ButterKnife.bind(this, rootView);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mActivity, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        taskRecyclerView.setLayoutManager(linearLayoutManager);
        mTaskFragmentRvAdapter = new TaskFragmentRvAdapter(
                init(), mActivity);
        taskRecyclerView.setAdapter(mTaskFragmentRvAdapter);
       mTaskFragmentRvAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                mActivity.startActivity(AddTaskActivity.getAddTaskAcivityIntent(mActivity, mTasks.get(position).gettDescription(),mTasks.get(position).getPriority()));
                mActivity.finish();
            }

        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //拖拽的方法标记
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //滑动方向标记
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //通过makeMovementFlags方法将将方向标记进行组合，并将复合的值返回
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                mTaskFragmentRvAdapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ToastHelper.showToast(mActivity, "滑动结束任务", Toast.LENGTH_SHORT);
                mTaskFragmentRvAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddTaskActivity.getAddTaskAcivityIntent(mActivity));
                mActivity.finish();
            }
        });
        return rootView;
    }

    public List<Task> init() {
        if (Apis.checkLogin(mActivity)) {
            currentUser = DbRequest.getCurrentUser(mActivity);
            mTasks = DbRequest.getCurrentUserTodayUnFinishTasks(mActivity, currentUser);
            SortUtil<Task> sortUtil = new SortUtil<>();
            mTasks = sortUtil.sort(mTasks, "priority", false);
        } else {
            mTasks = new ArrayList<>();
        }
        return mTasks;
    }

    public class SortUtil<T> {
        //数组排序的方法
        //传入list  传入排序字段 传入是否升序
        public List<T> sort(List<T> list, final String sortField, final Boolean Ascending) {

            Collections.sort(list, new Comparator<T>() {//排序
                @Override
                public int compare(T o1, T o2) {
                    int retVal = 0;
                    //首字母转大写
                    String newStr = sortField.substring(0, 1).toUpperCase() + sortField.replaceFirst("\\w", "");
                    String methodStr = "get" + newStr;
                    try {
                        Method method1 = ((T) o1).getClass().getMethod(methodStr, null);
                        Method method2 = ((T) o2).getClass().getMethod(methodStr, null);
                        retVal = method2.invoke(((T) o2), null).toString().compareTo(method1.invoke(((T) o1), null).toString()); // 倒序
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if (Ascending) {
                        return 0 - retVal;
                    } else {
                        return retVal;
                    }
                }
            });
            return list;
        }

    }
}
