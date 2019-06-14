package com.example.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.Arrays;
import java.util.List;

import static com.gyf.barlibrary.ImmersionBar.getActionBarHeight;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private NestedScrollView nestedScrollView;
    private int mActionBarHeight;
    private int topHeight;
    private LinearLayout toolbar;
    private MagicIndicator magic_indicator,magic_indicator2;
    int[] position = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        magic_indicator2 = findViewById(R.id.magic_indicator2);
        magic_indicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.viewpager);
        nestedScrollView = findViewById(R.id.scrollView);
        topHeight= ViewUtils.dip2px(this,310);
        Log.d("topHeight",topHeight+"topHeight");
        mActionBarHeight = getActionBarHeight(this)*2;
        toolbar = findViewById(R.id.toolbar);
        ImmersionBar.with(this)
                .titleBar(toolbar, false)
                .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
                .init();

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        initMagicIndicator4();
        initMagicIndicator3();
        getStatusBarHeight();
        int cao=ViewUtils.dip2px(this,48);
        final int gao=cao+getStatusBarHeight();
        Log.d("--------",gao+"");
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int scrollY, int i2, int oldScrollY) {
                magic_indicator.getLocationInWindow(position);
                Log.e("getLocationInWindow","getLocationInWindow:" + position[0] + "," + position[1]);
                if (scrollY > oldScrollY && scrollY <= mActionBarHeight) {
                    ImmersionBar.with(MainActivity.this)
                            .addViewSupportTransformColor(toolbar, R.color.colorPrimary)
                            .statusBarColorTransform(R.color.text_color_white)
                            .statusBarAlpha((float) scrollY / mActionBarHeight)
                            .init();
                } else if (scrollY==0){
                    ImmersionBar.with(MainActivity.this)
                            .statusBarAlpha(0.f)
                            .titleBar(toolbar, false)
                            .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
                            .init();
                }else if (scrollY < oldScrollY && scrollY <= mActionBarHeight){
                    ImmersionBar.with(MainActivity.this)
                            .addViewSupportTransformColor(toolbar, R.color.colorPrimary)
                            .statusBarColorTransform(R.color.text_color_white)
                            .statusBarAlpha((float) scrollY / mActionBarHeight)
                            .init();
                }else {
                    ImmersionBar.with(MainActivity.this)
                            .addViewSupportTransformColor(toolbar, R.color.colorPrimary)
                            .statusBarColorTransform(R.color.text_color_white)
                            .statusBarAlpha(1.0f)
                            .init();
                }
                if (position[1]<gao){
                    magic_indicator2.setVisibility(View.VISIBLE);
                }else {
                    magic_indicator2.setVisibility(View.GONE);
                }


//
            }
        });
    }
    private void initMagicIndicator3() {
        String[] CHANNELS = new String[]{"全部", "待付款", "待发货", "待收货", "已收货", "已完成"};
        final List<String> mDataList = Arrays.asList(CHANNELS);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ff0400"));
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(Color.parseColor("#ff0400"));
                return linePagerIndicator;
            }
        });
        magic_indicator2.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(MainActivity.this, 13);
            }
        });
        ViewPagerHelper.bind(magic_indicator2, mViewPager);
    }
    private void initMagicIndicator4() {
        String[] CHANNELS = new String[]{"全部", "待付款", "待发货", "待收货", "已收货", "已完成"};
        final List<String> mDataList = Arrays.asList(CHANNELS);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ff0400"));
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(Color.parseColor("#ff0400"));
                return linePagerIndicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(MainActivity.this, 13);
            }
        });
        ViewPagerHelper.bind(magic_indicator, mViewPager);
    }

    private int nestedScrollViewTop;
    //控制nestedScrollView滑动到一定的距离
    public void scrollByDistance(int dy){
        if(nestedScrollViewTop==0){
            int[] intArray=new int[2];
            nestedScrollView.getLocationOnScreen(intArray);
            nestedScrollViewTop=intArray[1];
        }
        int distance=dy-nestedScrollViewTop;//必须算上nestedScrollView本身与屏幕的距离
        nestedScrollView.fling(distance);//添加上这句滑动才有效
        nestedScrollView.smoothScrollBy(0,distance);
    }


    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

}
