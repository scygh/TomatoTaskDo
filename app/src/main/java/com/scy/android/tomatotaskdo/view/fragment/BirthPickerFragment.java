package com.scy.android.tomatotaskdo.view.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.scy.android.tomatotaskdo.R;

import java.util.Calendar;
import java.util.Date;

public class BirthPickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    public static final String EXTRA_DATE = "com.bignerdranch.android.TaskIntent.date";


    public static BirthPickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);

        BirthPickerFragment fragment = new BirthPickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @TargetApi(24)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Date date = (Date)getArguments().getSerializable(ARG_DATE);
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           int year = calendar.get(Calendar.YEAR);
           int month = calendar.get(Calendar.MONTH);
           int day = calendar.get(Calendar.DAY_OF_MONTH);
           View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_birth,null);
           mDatePicker = view.findViewById(R.id.dialog_date_picker);
           mDatePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("日期")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       int year = mDatePicker.getYear();
                       int month = mDatePicker.getMonth();
                       int day =mDatePicker.getDayOfMonth();
                        Date date1 = new GregorianCalendar(year,month,day).getTime();
                       //sendResult(Activity.RESULT_OK,date1);
                        mlistener.onDialogClick(date1);
                    }
                })
                .create();
    }

    /*private void sendResult(int resultCode,Date date){
        if (getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }*/

    private OnDialogListener mlistener;
    public interface OnDialogListener {
        void onDialogClick(Date date);
    }
    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mlistener = dialogListener;
    }
}
