package com.genonbeta.Trichain.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.genonbeta.Trichain.R;
import com.genonbeta.Trichain.activity.AdMobSingleton;
import com.genonbeta.Trichain.adapter.SmartFragmentPagerAdapter;
import com.genonbeta.Trichain.app.Activity;
import com.genonbeta.Trichain.ui.callback.TitleSupport;
import com.genonbeta.android.framework.ui.callback.SnackbarSupport;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment
        extends com.genonbeta.android.framework.app.Fragment
        implements TitleSupport, SnackbarSupport, com.genonbeta.android.framework.app.FragmentImpl, Activity.OnBackPressedListener {
    private ViewPager mViewPager;
    private SmartFragmentPagerAdapter mAdapter;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_home_fragment, container, false);

        final BottomNavigationView bottomNavigationView = view.findViewById(R.id.layout_home_bottom_navigation_view);
        mViewPager = view.findViewById(R.id.layout_home_view_pager);
        mAdapter = new SmartFragmentPagerAdapter(getContext(), getChildFragmentManager());

        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(0, TransferGroupListFragment.class, null));
        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(1, FileExplorerFragment.class, null));
        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(2, TextStreamListFragment.class, null));

        mAdapter.createTabs(bottomNavigationView);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.setSelectedItemId(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mViewPager.setCurrentItem(menuItem.getOrder());
                return true;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAds();
            }
        }, 20000);
        return view;
    }

    private void showAds() {
        if (getActivity()==null)
            return;
        AdMobSingleton adMobSingleton;
        adMobSingleton = AdMobSingleton.getInstance(getActivity());
        Log.e(TAG, "loadAdAndStartIntent: Attempting to load ads ");
        adMobSingleton.loadInterstitial(getActivity());
        adMobSingleton.setShowOnLoad(true);
        adMobSingleton.setMyAdListener(new AdMobSingleton.MyAdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "onAdLoaded: Ad loaded");
            }

            @Override
            public void onAdFailed() {
                Log.e(TAG, "onAdFailed: failed");
            }

            @Override
            public void onAdClosed() {
                Log.e(TAG, "onAdClosed: closed");
            }
        });
    }

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.text_home);
    }

    @Override
    public boolean onBackPressed() {
        Object activeItem = mAdapter.getItem(mViewPager.getCurrentItem());

        if ((activeItem instanceof Activity.OnBackPressedListener
                && ((Activity.OnBackPressedListener) activeItem).onBackPressed()))
            return true;

        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(0, true);
            return true;
        }

        return false;
    }
}
