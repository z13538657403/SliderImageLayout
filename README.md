# SliderImageLayout
自定义顶部广告条
# 控件简介

该自定义广告条实现了图片的循环轮播，支持本地图片（测试用）以及网络图片，你可以设置图片的标题也可以不设置。你还可以为它设置点击事件。

# 控件用法

(1)在布局文件引入该控件，如下：

<com.test.zhangtao.activitytest.SliderImageLayout
    android:id="@+id/slider_layout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>

(2)在Activity中初始化该控件，然后设置图片资源的id或图片的Url

使用本地图片并设置标题：
       
mSliderImgLayout.setSliderImg(R.drawable.pic1);
mSliderImgLayout.setTitleText("衣服");
mSliderImgLayout.setSliderImg(R.drawable.pic2);
mSliderImgLayout.setTitleText("手机");
mSliderImgLayout.setSliderImg(R.drawable.pic3);
mSliderImgLayout.setTitleText("电脑");
mSliderImgLayout.setSliderImg(R.drawable.pic4);

使用网络图片不设置标题：

mSliderImgLayout.setSliderImg("http://xxx.xxx.x.xxx/pic1.jpg");
mSliderImgLayout.setSliderImg("http://xxx.xxx.x.xxx/pic2.jpg");
mSliderImgLayout.setSliderImg("http://xxx.xxx.x.xxx/pic3.jpg");

(3)设置点击事件

mSliderImgLayout.setOnSliderItemClickListener(new SliderImageLayout.OnSliderItemClickListener()
{
     @Override
     public void onSliderItemClick(int position)
     {
         Toast.makeText(MainActivity.this , position + "" , Toast.LENGTH_LONG).show();
     }
});

(4)运行截图

![image](https://github.com/z13538657403/SliderImageLayout/blob/master/app/src/main/res/mipmap-xhdpi/sliderView.gif)
