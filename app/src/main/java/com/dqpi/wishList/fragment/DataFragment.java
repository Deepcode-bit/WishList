package com.dqpi.wishList.fragment;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dqpi.wishList.R;
import com.dqpi.wishList.databinding.FragmentDataBinding;
import com.dqpi.wishList.model.PieData;
import com.dqpi.wishList.model.SavedMoney;
import com.dqpi.wishList.view.CurveGraph;
import com.dqpi.wishList.view.PieView;
import com.dqpi.wishList.viewmodel.HostViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {

    FragmentDataBinding binding;
    HostViewModel viewModel;
    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel=new ViewModelProvider(requireActivity()).get(HostViewModel.class);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_data,container,false);
        binding.setData(this);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AddDate();
    }

    private void AddDate() {
        List<Integer> list= viewModel.dayMonies;
        List<String> titles=Arrays.asList("周一","周二","周三","周四","周五","周六","周日");
        binding.curveGraph.setStaValues(list);
        binding.curveGraph.setTitles(titles);
        ArrayList<PieData> pieDataList= new ArrayList<>();
        for(int i=0;i<7;i++){
            PieData pie=new PieData(titles.get(i),list.get(i));
            pieDataList.add(pie);
        }
        binding.pieView.setData(pieDataList);
    }
}
