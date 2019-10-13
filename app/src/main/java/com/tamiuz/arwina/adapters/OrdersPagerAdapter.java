package com.tamiuz.arwina.adapters;

import android.os.Bundle;
import android.os.Parcelable;

import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.fragments.CompletedFragment;
import com.tamiuz.arwina.fragments.InProcessFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OrdersPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private List<OrdersModel.OrderData> processing_list;
    private List<OrdersModel.OrderData> completed_list;

    public OrdersPagerAdapter(FragmentManager fm, int numOfTabs, List<OrdersModel.OrderData> process_list, List<OrdersModel.OrderData> completed_list) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.processing_list = process_list;
        this.completed_list = completed_list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new InProcessFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("process_list", (ArrayList<? extends Parcelable>) processing_list);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new CompletedFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("completed_list", (ArrayList<? extends Parcelable>) completed_list);
                fragment.setArguments(bundle2);
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
