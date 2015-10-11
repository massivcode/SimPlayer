package com.example.massivcode.simplayer.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.massivcode.simplayer.Adapter.CategoryAdapter;
import com.example.massivcode.simplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by massivCode on 2015-10-10.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mList;
    private CategoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment bottomFragment = new MiniPlayerFragment();
        fragmentTransaction.add(R.id.main_fragment_container, bottomFragment);
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabLayout.addTab(mTabLayout.newTab().setText("Playlist"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Artist"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Songs"));

        mList = new ArrayList<>();
        mList.add(new PlaylistFragment());
        mList.add(new ArtistFragment());
        mList.add(new SongFragment());

        mAdapter = new CategoryAdapter(getChildFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
