package com.mcholwey.catforemanapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 3/8/2016.
 */
public class TractorListAdapter extends BaseAdapter {

    public static List<Tractor> tractors = new ArrayList();
    LayoutInflater inflater;
    Context context;

//    public TractorListAdapter(JobSiteOverviewActivity jobSiteOverviewActivity){
//        this.jobSiteOverviewActivity = jobSiteOverviewActivity;
//    }

    public TractorListAdapter(Context context, ArrayList tractors){
        this.context = context;
        this.tractors = tractors;
        inflater = LayoutInflater.from(this.context);
    }

//    @Override
//    public boolean areAllItemsEnabled() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return true;
//    }

    @Override
    public int getCount() {
        return tractors.size();
    }

    @Override
    public Object getItem(int position) {
        return tractors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Tractor)getItem(position)).getSerialNumber().hashCode();
    }
//
//    @Override
//    public boolean hasStableIds() {
//        return true;
//    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view;
//
//        if(convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) jobSiteOverviewActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = layoutInflater.inflate(R.layout.tractor_list_text_view, parent, false);
//            ((TextView) view.findViewById(R.id.tractor_text)).setText(tractors.get(position).getName());
//        }
//        else {
//            view = (View) convertView;
//        }
//
//        return view;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tractor_list_text_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tractor tractor = (Tractor) getItem(position);
        viewHolder.title.setText(tractor.getName());

        return convertView;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return IGNORE_ITEM_VIEW_TYPE;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 1;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return tractors.isEmpty();
//    }

    private class ViewHolder {
        TextView title;

        public ViewHolder(View item) {
            title = (TextView) item.findViewById(R.id.tractor_text);
        }
    }
}