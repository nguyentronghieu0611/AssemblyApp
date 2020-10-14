package com.example.giaothongapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.giaothongapp.R;
import com.example.giaothongapp.config.TrafficDatabase;
import com.example.giaothongapp.model.Error;
import com.example.giaothongapp.view.ErrorFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ErrorAdapter extends BaseAdapter {
    List<Error> errorList;
    Context context;
    FragmentManager fragmentManager;
    TrafficDatabase db;
    private LayoutInflater layoutInflater;

    public ErrorAdapter(List<Error> errorList, Context context,FragmentManager fragmentManager, TrafficDatabase db) {
        this.errorList = errorList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.db = db;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return errorList.size();
    }

    @Override
    public Object getItem(int position) {
        return errorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_error_items, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imgAvatar);
            holder.txtContactName = convertView.findViewById(R.id.txtErrorName);
            holder.txtIcon = convertView.findViewById(R.id.txtIcon);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        final Error error = errorList.get(position);
        holder.txtContactName.setText(error.getName());
        if(error.getImage() != null){
            Glide.with(context).load(error.getImage()).centerCrop().into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.txtIcon.setVisibility(View.INVISIBLE);
        }
        else {
            holder.txtIcon.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView txtIcon, txtContactName;
        CircleImageView imageView;
    }
}
