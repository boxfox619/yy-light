package team.yylight.lightapplication.activity.sign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import team.yylight.lightapplication.activity.sign.view.PagerSlidingTabStrip;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.activity.sign.view.SignFragment;
import team.yylight.lightapplication.activity.sign.view.SignInFragment;
import team.yylight.lightapplication.activity.sign.view.SignUpFragment;

public class SignActivity extends AppCompatActivity {
    private ViewPager pager;
    private PagerSlidingTabStrip pagertab;
    private MyPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        pager = (ViewPager) findViewById(R.id.activity_main_pager);
        pagertab = (PagerSlidingTabStrip) findViewById(R.id.activity_main_pagertabstrip);
        List<SignFragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);
        pagertab.setViewPager(pager);
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<SignFragment> fragments;


        public MyPageAdapter(FragmentManager fm, List<SignFragment> fragments) {
            super(fm);
            this.fragments = fragments;

        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.fragments.get(position).getArguments().getString(SignFragment.EXTRA_MESSAGE);
        }
    }

    private List<SignFragment> getFragments() {
        List<SignFragment> fList = new ArrayList<SignFragment>();
        fList.add(SignInFragment.newInstance("로그인"));
        fList.add(SignUpFragment.newInstance("회원가입"));
        return fList;
    }
}
