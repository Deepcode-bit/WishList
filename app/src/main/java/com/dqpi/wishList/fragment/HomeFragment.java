package com.dqpi.wishList.fragment;

import android.app.AlertDialog;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dqpi.wishList.R;
import com.dqpi.wishList.adapter.WishListAdapter;
import com.dqpi.wishList.model.Wish;
import com.dqpi.wishList.viewmodel.HostViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements WishListAdapter.onItemLongClickListener {
    private RecyclerView wishList;
    public WishListAdapter mAdapter;
    private HostViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel= new ViewModelProvider(requireActivity()).get(HostViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wishList = requireView().findViewById(R.id.wish_recycler);
        wishList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mAdapter = new WishListAdapter();
        wishList.setAdapter(mAdapter);
        mAdapter.setWishes(viewModel.wishes.getValue());
        mAdapter.setOnItemLongClickListener(this);
        viewModel.wishes.observe(requireActivity(), wishes -> mAdapter.notifyDataSetChanged());
        viewModel.averMoney.observe(requireActivity(),(averMoney)->{
            mAdapter.setAverNumText(averMoney);
            mAdapter.notifyDataSetChanged();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemLongClick(View v, int position) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(),v);
        popupMenu.getMenuInflater().inflate(R.menu.wish_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener((item)->{
            switch (item.getItemId()){
                case R.id.add_money:showAddMoneyDialog(position); break;
                case R.id.wish_delete:
                    viewModel.deleteWish(position);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        });
        popupMenu.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAddMoneyDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());//创建对话框
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_money, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        AlertDialog dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框
        layout.findViewById(R.id.add_cancel).setOnClickListener((v) -> dialog.dismiss());
        EditText editAdd = layout.findViewById(R.id.add_edit);
        layout.findViewById(R.id.add_ok).setOnClickListener((v) -> {
            if (editAdd.getText() == null || editAdd.getText().toString().isEmpty()) {
                Toast.makeText(requireActivity(), "金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String text = editAdd.getText().toString();
            BigDecimal money = new BigDecimal(text);
            String msg = viewModel.addMoney(position, money) ? "添加成功" : "超出目标金额";
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            mAdapter.notifyDataSetChanged();
        });
    }
}
