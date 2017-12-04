package com.example.asutosh.aebug.fragments;

/**
 * Created by Asutosh on 22-08-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asutosh.aebug.Adapter.twoAdapter;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.todaysbean;

import java.util.ArrayList;
import java.util.List;

public class status_fragment extends Fragment {
    ListView listView;
    private List<todaysbean> appoointmentList=new ArrayList<todaysbean>();
    String[]name={"john","Andy","rAm","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy","john","Andy"};
    twoAdapter adapter;
    public static status_fragment newInstance() {
        status_fragment fragment = new status_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.status_frag,container,false);
        listView=(ListView)rootview.findViewById(R.id.list1);

        for (int i=0;i<name.length;i++){
            todaysbean item=new todaysbean();
            item.setName(name[i]);
            appoointmentList.add(item);
            adapter=new twoAdapter(getActivity(),appoointmentList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return rootview;
    }
}