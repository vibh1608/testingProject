package com.example.user.to_do_project_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 01-12-2017.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> mList;
    private LayoutInflater mLayoutInflator;

    public CustomAdapter(Context MainActivity , ArrayList<Task> mListData)
    {
        context = MainActivity;
        mList = mListData;
        mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mLayoutInflator.inflate(R.layout.row,viewGroup,false);
        TextView date1 = view.findViewById(R.id.head_date);
        TextView title = view.findViewById(R.id.title_tv);
        TextView description = view.findViewById(R.id.decription_tv);
        TextView date2 = view.findViewById(R.id.tale_date);
        ImageView status = view.findViewById(R.id.completion_status);

        date1.setText(mList.get(i).mdate);
        date2.setText(mList.get(i).mdate);
        title.setText(mList.get(i).mtitle);
        description.setText(mList.get(i).mdescription);

        //Setting image based on status of object in ImageView.
        if(mList.get(i).mstatus==1)
            status.setImageResource(R.drawable.completed);
        else
            status.setImageResource(R.drawable.to_complete);

        return view;
    }
}
