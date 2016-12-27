package com.test.zhangtao.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by zhangtao on 16/12/26.
 */

public class MainActivity extends Activity
{
    private SliderImageLayout mSliderImgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        mSliderImgLayout = (SliderImageLayout) findViewById(R.id.slider_layout);
//        mSliderImgLayout.setSliderImg("http://192.168.1.100/pic1.jpg");
//        mSliderImgLayout.setSliderImg("http://192.168.1.100/pic2.jpg");
//        mSliderImgLayout.setSliderImg("http://192.168.1.100/pic3.jpg");

        mSliderImgLayout.setSliderImg(R.drawable.pic1);
        mSliderImgLayout.setTitleText("衣服");
        mSliderImgLayout.setSliderImg(R.drawable.pic2);
        mSliderImgLayout.setTitleText("手机");
        mSliderImgLayout.setSliderImg(R.drawable.pic3);
        mSliderImgLayout.setTitleText("电脑");
        mSliderImgLayout.setSliderImg(R.drawable.pic4);
        mSliderImgLayout.setTitleText("手表");
        mSliderImgLayout.setOnSliderItemClickListener(new SliderImageLayout.OnSliderItemClickListener()
        {
            @Override
            public void onSliderItemClick(int position)
            {
                Toast.makeText(MainActivity.this , position + "" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
