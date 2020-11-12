package com.example.assemblyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.assemblyapp.R;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.SearchHistory;

import java.util.List;


public class SearhHistoryAdapter extends BaseAdapter {
    List<SearchHistory> searchHistoryList;
    Context context;
    AssemblyDatabase db;
    private LayoutInflater layoutInflater;

    public SearhHistoryAdapter(List<SearchHistory> searchHistoryList, Context context, AssemblyDatabase db) {
        this.searchHistoryList = searchHistoryList;
        this.context = context;
        this.db = db;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearhHistoryAdapter.ViewHolder holder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_searchhistory_items, null);
            holder = new SearhHistoryAdapter.ViewHolder();
            holder.imgTime = convertView.findViewById(R.id.imgTime);
            holder.txtContent = convertView.findViewById(R.id.txtHistory);
            convertView.setTag(holder);
        }
        else
            holder = (SearhHistoryAdapter.ViewHolder) convertView.getTag();
        final SearchHistory searchHistory = searchHistoryList.get(position);
        holder.txtContent.setText(searchHistory.getSearch_txt());
        return convertView;
    }

    static class ViewHolder{
        ImageView imgTime;
        TextView txtContent;
    }
}
