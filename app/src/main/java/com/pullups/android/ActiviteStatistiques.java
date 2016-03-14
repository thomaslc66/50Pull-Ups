package com.pullups.android;

/**
 * Created by Thomas on 12.07.2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.pullups.android.Statistiques.StatByDayFragment;
import com.pullups.android.Statistiques.StatByLevelFragment;
import com.pullups.android.viewpagerindicator.CirclePageIndicator;
import com.pullups.android.viewpagerindicator.IconPagerAdapter;

public class ActiviteStatistiques extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        circlePageIndicator.setViewPager(pager);
    }

    /****************************************************************************
     *
     * Goal: manage the backward navigation from stats activity to accueilActivity
     *
     ********************************************************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //on est va vers l'accueil alors on tue l'activité
        Intent accueil = new Intent(ActiviteStatistiques.this,ActiviteAccueil.class);
        accueil.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(accueil);
        finish();

    }

    private class MyPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return StatByDayFragment.newInstance("StatByDayFragment, Instance 1");
                case 1: return StatByLevelFragment.newInstance("StatByLevelFragment, Instance 1");
                default: return StatByDayFragment.newInstance("ThirdFragment, Default");
            }
        }


        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
