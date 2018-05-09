
package com.sdt.tv.launcher;

import java.util.ArrayList;

import com.sdt.tv.launcher.adapter.MainActivityAdapter;
import com.sdt.tv.launcher.features.LocalServiceFragment;
import com.sdt.tv.launcher.features.indicator.TabIndicator;
import com.sdt.tv.launcher.fragment.AppFragment;
import com.sdt.tv.launcher.fragment.BaseFragment;
import com.sdt.tv.launcher.fragment.SettingFragment;
import com.sdt.tv.launcher.ui.BaseTitleActivity;
import com.sdt.tv.launcher.ui.DefaultTransformer;
import com.sdt.tv.launcher.ui.FixViewpagerScrollerSpeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends BaseTitleActivity
        implements View.OnClickListener, TabIndicator.onTabChangeListener, TabIndicator.onTabClickListener {

    private ViewPager mViewPager;
    private int currentIndex;
    private static final int PAGE_NUMBER = 3;
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();
    private Context context;

    public ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            mTabIndicator.setCurrentIndex(position);
            if (isFocusOnPage) {
                mTabIndicator.setNoFocusState();
                mFragmentList.get(mTabIndicator.getCurrentIndex()).requestInitFocus();
            }
        }
    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        }
    };

    private TabIndicator mTabIndicator;
    private boolean isFocusOnPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mTabIndicator = (TabIndicator) findViewById(R.id.main_indicator);
        mViewPager = (ViewPager) this.findViewById(R.id.main_viewpager);
        setListener();

        mViewPager.setPageTransformer(true, new DefaultTransformer());
        FixViewpagerScrollerSpeed.setViewPagerScrollSpeed(mViewPager);

        context.registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        initFragment();

        // test focus
        // mTabIndicator.requestFocus();
        // mTabIndicator.requestTabFocus(0);
        mViewPager.requestFocus();
    }

    private void setListener() {
        mTabIndicator.setOnTabChangeListener(this);
        mTabIndicator.setOnTabClickListener(this);
    }

    private void initFragment() {
        mFragmentList.clear();
        int count = PAGE_NUMBER;

        FragmentManager manager;
        FragmentTransaction transaction;

        manager = this.getSupportFragmentManager();
        transaction = manager.beginTransaction();

        LocalServiceFragment interactTV = new LocalServiceFragment();
        SettingFragment setting = new SettingFragment();
        AppFragment app = new AppFragment();

        mFragmentList.add(interactTV);
        mFragmentList.add(setting);
        mFragmentList.add(app);

        transaction.commitAllowingStateLoss();

        MainActivityAdapter mAdapter = new MainActivityAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setCurrentItem(0);
    }

    public void requestTabFocus() {
        isFocusOnPage = false;
        mTabIndicator.requestTabFocus(mTabIndicator.getCurrentIndex());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isFocusOnPage && keyCode != KeyEvent.KEYCODE_BACK) {
            return mFragmentList.get(mTabIndicator.getCurrentIndex()).onKeyDown(keyCode, event);
        }
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            finish();
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            return false;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (mTabIndicator.getCurrentIndex() == mTabIndicator.getVisibleChildCount() - 1) {
                return true;
            }
            // Log.i("ZYTFOCU", " right key is pressed ");
            return false;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            isFocusOnPage = true;
            mFragmentList.get(mTabIndicator.getCurrentIndex()).requestInitFocus();
            mTabIndicator.setNoFocusState();
            return true;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (mTabIndicator.getCurrentIndex() == 0) {
                return true;
            }
            return false;
        case KeyEvent.KEYCODE_DPAD_CENTER:
            return false;
        }
        return false;
    }

    @Override
    public void onTabChange(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onTabClick(int index) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mConnReceiver);
    }

    @Override
    public void onClick(View v) {

    }
}
