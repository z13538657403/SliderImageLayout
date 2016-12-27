package com.test.zhangtao.activitytest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangtao on 16/12/26.
 */

public class SliderImageLayout extends RelativeLayout implements ViewPager.OnPageChangeListener
{
    private LayoutInflater mInflater;
    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mDotLayout;
    private TextView mTitleTV;
    private FixedSpeedScroller mScroller;
    private OnSliderItemClickListener mOnSliderItemClickListener;

    private int imgSize = 0;

    private List<ImageView> mListImgView;
    private List<String> titleTexts;
    private int previousPosition = 0;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0x112)
            {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
            else if (msg.what == 0x123)
            {
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null)
                {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setImageBitmap(bitmap);
                    mListImgView.add(imageView);
                    if (mListImgView.size() == imgSize)
                    {
                        setViewPagerAdapter();
                    }
                }
            }
        }
    };

    public SliderImageLayout(Context context)
    {
        this(context , null);
    }

    public SliderImageLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs , 0);
    }

    public SliderImageLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView()
    {
        mListImgView = new ArrayList<>();
        titleTexts = new ArrayList<>();

        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.view_slider_layout , this);
        mDotLayout = (LinearLayout) view.findViewById(R.id.ll_dot_group);
        mTitleTV = (TextView) view.findViewById(R.id.tv_img_desc);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setPageTransformer(true , new DepthPageTransformer());
        mViewPager.setOnPageChangeListener(this);

        try
        {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(mContext ,new AccelerateInterpolator());
            mField.set(mViewPager , mScroller);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setSliderImg(final String imgUrl)
    {
        imgSize += 1;
        new Thread()
        {
            @Override
            public void run()
            {
                InputStream in = null;
                try
                {
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    in = new BufferedInputStream(conn.getInputStream());
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                    conn.disconnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (in != null)
                    {
                        try
                        {
                            in.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    public void setSliderImg(int imgId)
    {
        imgSize += 1;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources() , imgId);
        Message msg = new Message();
        msg.what = 0x123;
        msg.obj = bitmap;
        handler.sendMessage(msg);
    }

    public void setTitleText(String titleText)
    {
        if (!TextUtils.isEmpty(titleText))
        {
            titleTexts.add(titleText);
        }
    }

    private void setViewPagerAdapter()
    {
        initDotViews();

        mViewPager.setAdapter(new ViewPagerAdapter());
        mScroller.setmDuration(700);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mListImgView.size()));

        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.sendEmptyMessage(0x112);
            }
        } , 0 , 4500);
    }

    private void initDotViews()
    {
        View dotView;
        for (int i = 0 ; i < mListImgView.size() ; i++)
        {
            dotView = new View(mContext);
            dotView.setBackgroundResource(R.drawable.selector_dot);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15 , 15);
            if (i != 0)
            {
                lp.leftMargin = 15;
            }
            dotView.setLayoutParams(lp);
            dotView.setEnabled(true);
            mDotLayout.addView(dotView);
        }
    }

    @Override
    public void onPageSelected(int position)
    {
        int newPosition = position % mListImgView.size();
        mDotLayout.getChildAt(newPosition).setEnabled(false);
        if (titleTexts != null && titleTexts.size() > 0)
        {
            mTitleTV.setVisibility(VISIBLE);
            mTitleTV.setText(titleTexts.get(newPosition));
        }
        mDotLayout.getChildAt(previousPosition).setEnabled(true);
        previousPosition = newPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= mListImgView.size();
            if (position<0){
                position = mListImgView.size()+position;
            }
            final ImageView view = mListImgView.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewGroup vp = (ViewGroup) view.getParent();
            if (vp!=null)
            {
                vp.removeView(view);
            }
            container.addView(view);
            final int newPosition = position;
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mOnSliderItemClickListener != null)
                    {
                        mOnSliderItemClickListener.onSliderItemClick(newPosition);
                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }

    public void setOnSliderItemClickListener(OnSliderItemClickListener onSliderItemClickListener)
    {
        mOnSliderItemClickListener = onSliderItemClickListener;
    }

    public interface OnSliderItemClickListener
    {
        void onSliderItemClick(int position);
    }
}
