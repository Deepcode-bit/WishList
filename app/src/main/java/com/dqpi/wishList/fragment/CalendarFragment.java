package com.dqpi.wishList.fragment;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dqpi.wishList.databinding.FragmentCalendarBinding;
import com.dqpi.wishList.model.SavedMoney;
import com.dqpi.wishList.viewmodel.HostViewModel;
import com.haibin.calendarview.Calendar;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    public FragmentCalendarBinding binding;
    public HostViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CalendarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel=new ViewModelProvider(requireActivity()).get(HostViewModel.class);
        binding= FragmentCalendarBinding.inflate(inflater,container,false);
        binding.setData(this);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.calendarView.setOnMonthChangeListener(((year, month) -> {
            viewModel.year.setValue(year + "年");
            viewModel.month.setValue(month + "月");
        }));
        try {
            addSavedDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addSavedDate() throws ParseException {
        List<SavedMoney> monies=viewModel.savedMonies.getValue();
        if(monies==null)return;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for(SavedMoney money :monies){
            Date date=formatter.parse(money.getDateStamp());
            android.icu.util.Calendar calendar=formatter.getCalendar();
            calendar.setTime(date);
            //两种calendar互转(非常头疼)
            Calendar mCalendar=new Calendar();
            mCalendar.setYear(calendar.get(android.icu.util.Calendar.YEAR));
            mCalendar.setMonth(calendar.get(android.icu.util.Calendar.MONTH)+1);
            mCalendar.setDay(calendar.get(android.icu.util.Calendar.DATE));
            binding.calendarView.addSchemeDate(mCalendar);
        }
    }
}
