package com.flyjingfish.lightrouter.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.flyjingfish.lightrouter.R;
import com.flyjingfish.lightrouter.databinding.ActivityImagesBinding;
import com.flyjingfish.openimagelib.OpenImage;
import com.flyjingfish.openimagelib.beans.OpenImageUrl;
import com.flyjingfish.openimagelib.listener.OnSelectMediaListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Route(path = "/test/activity")
public class ImagesActivity extends ComponentActivity {

    private ActivityImagesBinding binding;
    private static final String[] imgUrls = new String[]{
            "https://5b0988e595225.cdn.sohucs.com/images/20200504/47bc66e063f64d969dbe7e7ab915525c.gif",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F9%2F57c4f3db0ff7a_120_80.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1662179613&t=105ec8c89e77a853ba57e2a2dc056eab",
            "https://pics4.baidu.com/feed/50da81cb39dbb6fd95aa0c599b8d0d1e962b3708.jpeg?token=bf17224f51a6f4bb389e787f9c487940",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.tt98.com%2Fd%2Ffile%2Fpic%2F201811082010742%2F5be40536abdd2.jpg&refer=http%3A%2F%2Fimg.tt98.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=2d03e79dd2eb007d30a330479093ecf4",
            "https://pics4.baidu.com/feed/d439b6003af33a87448a8829a29e9f3c5243b561.jpeg?token=9f2982c975e4bc132340abf3e89cd7e0&s=FDFEA55749534BCC467D7DEA0300A032",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0529%252Fd082a060j00qtvk4j007cd200u001o0g00it011m.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661701773&t=0ad52eb2d08c70ddafdab9e8aa04840d",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
    }

    private void loadData() {
        List<ImageEntity> datas = new ArrayList<>();

        for (int i = 0; i < imgUrls.length; i++) {
            ImageEntity itemData = new ImageEntity();
            String url = imgUrls[i];
            itemData.url = url;
            datas.add(itemData);
        }
        setData(datas);
    }

    private void setData(List<ImageEntity> datas) {
        Glide.with(binding.iv1).load(datas.get(0).getCoverImageUrl()).into(binding.iv1);
        Glide.with(binding.iv2).load(datas.get(1).getCoverImageUrl()).into(binding.iv2);
        Glide.with(binding.iv3).load(datas.get(2).getCoverImageUrl()).into(binding.iv3);
        Glide.with(binding.iv4).load(datas.get(3).getCoverImageUrl()).into(binding.iv4);
        Glide.with(binding.iv5).load(datas.get(4).getCoverImageUrl()).into(binding.iv5);
        Glide.with(binding.iv6).load(datas.get(5).getCoverImageUrl()).into(binding.iv6);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = 0;
                if (v.getId() == R.id.iv1){
                    position = 0;
                }else if (v.getId() == R.id.iv2){
                    position = 1;
                }else if (v.getId() == R.id.iv3){
                    position = 2;
                }else if (v.getId() == R.id.iv4){
                    position = 3;
                }else if (v.getId() == R.id.iv5){
                    position = 4;
                }else if (v.getId() == R.id.iv6){
                    position = 5;
                }
                List<ImageView> imageViews = Arrays.asList(binding.iv1,binding.iv2,binding.iv3,binding.iv4,binding.iv5,binding.iv6);
                OpenImage.with(ImagesActivity.this)
                        .setClickImageViews(imageViews)
//                        .setNoneClickView()
                        .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
                        .setImageUrlList(datas)
                        .setAutoScrollScanPosition(true)
                        .setOnSelectMediaListener(new OnSelectMediaListener() {
                            boolean isFirstBacked = false;
                            @Override
                            public void onSelect(OpenImageUrl openImageUrl, int position) {
                                if (isFirstBacked){
                                    binding.scrollView.post(() -> binding.scrollView.scrollTo(0,imageViews.get(position).getTop()));
                                }
                                isFirstBacked = true;
                            }
                        })
//                        .setOpenImageStyle(R.style.DefaultPhotosTheme)
                        .setClickPosition(position).show();
            }
        };

        binding.iv1.setOnClickListener(onClickListener);
        binding.iv2.setOnClickListener(onClickListener);
        binding.iv3.setOnClickListener(onClickListener);
        binding.iv4.setOnClickListener(onClickListener);
        binding.iv5.setOnClickListener(onClickListener);
        binding.iv6.setOnClickListener(onClickListener);

    }
}
