package com.dqpi.wishList.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dqpi.wishList.R;
import com.dqpi.wishList.databinding.FragmentWishAddBinding;
import com.dqpi.wishList.model.Wish;
import com.dqpi.wishList.viewmodel.NewWishViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishAddFragment extends Fragment {

    private FragmentWishAddBinding binding;
    public NewWishViewModel viewModel;

    public WishAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentWishAddBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(requireActivity());
        binding.setData(this);
        viewModel=new ViewModelProvider(requireActivity()).get(NewWishViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.money.observe(requireActivity(), integer -> {
            viewModel.currentNum.setValue(integer+"¥");
        });
    }

    public void onEmojiPageClick(View v){
        NavController navController= Navigation.findNavController(v);
        navController.navigate(R.id.action_wishAddFragment_to_emojiFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onWishAddClick(View v) {
        RadioButton button = requireView().findViewById(binding.stickerGroup.getCheckedRadioButtonId());
        if (button == null || viewModel.wishName.getValue() == null || viewModel.wishName.getValue().isEmpty() || viewModel.money.getValue() == null) {
            Toast.makeText(requireActivity(), "愿望信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if(viewModel.money.getValue()==0){
            Toast.makeText(requireActivity(), "目标金额不能为0", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = button.getText().toString();
        BigDecimal savedMoney = new BigDecimal(viewModel.money.getValue());
        Wish wish = new Wish().SetSticker(code).
                SetSaved(new BigDecimal(0)).
                SetTarget(savedMoney).
                SetTitle(viewModel.wishName.getValue());
        Intent intent = new Intent();
        intent.putExtra("wish", wish);
        requireActivity().setResult(1, intent);
        requireActivity().finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMaxValue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());//创建对话框
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_money, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        AlertDialog dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框
        layout.findViewById(R.id.add_cancel).setOnClickListener((v) -> dialog.dismiss());
        EditText editAdd = layout.findViewById(R.id.add_edit);
        editAdd.setHint("输入愿望金额最大值");
        layout.findViewById(R.id.add_ok).setOnClickListener((v) -> {
            if (editAdd.getText() == null || editAdd.getText().toString().isEmpty()) {
                Toast.makeText(requireActivity(), "金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String text = editAdd.getText().toString();
            BigDecimal money = new BigDecimal(text);
            viewModel.setMaxMoney(money.intValue());
            dialog.dismiss();
        });
    }
}
