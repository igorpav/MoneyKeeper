package team.e.com.moneykeeper.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private static DatePickerDialog.OnDateSetListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
        return new DatePickerFragment();
    }
}
