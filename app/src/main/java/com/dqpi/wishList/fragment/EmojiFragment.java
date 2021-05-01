package com.dqpi.wishList.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dqpi.wishList.R;
import com.dqpi.wishList.activity.LoginActivity;
import com.dqpi.wishList.adapter.StickerAdapter;
import com.dqpi.wishList.adapter.WishListAdapter;
import com.dqpi.wishList.databinding.FragmentEmojiBinding;
import com.dqpi.wishList.model.Sticker;
import com.dqpi.wishList.util.App;
import com.dqpi.wishList.viewmodel.NewWishViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmojiFragment extends Fragment implements StickerAdapter.onItemSelectListener {

    private FragmentEmojiBinding binding;
    private StickerAdapter adapter;
    private List<Sticker> stickers;
    private GridLayoutManager manager;

    public NewWishViewModel viewModel;

    public EmojiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmojiBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(requireActivity());
        binding.setData(this);
        viewModel=new ViewModelProvider(requireActivity()).get(NewWishViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stickers = new ArrayList<>();
        adapter = new StickerAdapter();
        adapter.setStickers(stickers);
        binding.emojiList.setAdapter(adapter);
        manager = new GridLayoutManager(requireActivity(), 4);
        binding.emojiList.setLayoutManager(manager);
        adapter.setListener(this);
        viewModel.searchId.observe(requireActivity(), s -> {
            if (s.isEmpty()) return;
            int id = Integer.parseInt(s);
            if (id > stickers.size()) return;
            manager.scrollToPosition(id);
        });
        App.mThreadPool.execute(this::LoadEmoji);
    }

    private void LoadEmoji() {
        String resultString = "";
        try {
            InputStream inputStream = requireActivity().getResources().getAssets().open("EmojiList.json");
            byte[] buffer = new byte[inputStream.available()];
            int read = inputStream.read(buffer);
            resultString = new String(buffer, "GB2312");
            JSONObject json=new JSONObject(resultString);
            Gson gson = new Gson();
            JSONArray jsonArray = json.getJSONArray("emoji_list");
            stickers.clear();
            //循环遍历
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Sticker sticker = gson.fromJson(jsonObject.toString(), Sticker.class);
                stickers.add(sticker);
            }
            requireActivity().runOnUiThread(()-> adapter.notifyDataSetChanged());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelect(View v, int id) {
        int visible = id == -1 ? View.INVISIBLE : View.VISIBLE;
        binding.okBut.setVisibility(visible);
    }

    public void onEmojiClick(View v) {
        Sticker sticker = stickers.get(adapter.selectIndex);
        viewModel.ReplaceSticker(sticker);
        requireActivity().onBackPressed();
    }
}
