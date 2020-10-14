package com.example.assemblyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.assemblyapp.R;
import com.example.assemblyapp.config.AssemblyDatabase;
import com.example.assemblyapp.model.Command;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommandAdapter extends BaseAdapter {
    List<Command> commandList;
    Context context;
    FragmentManager fragmentManager;
    AssemblyDatabase db;
    private LayoutInflater layoutInflater;

    public CommandAdapter(List<Command> commandList, Context context, FragmentManager fragmentManager, AssemblyDatabase db) {
        this.commandList = commandList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.db = db;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commandList.size();
    }

    @Override
    public Object getItem(int position) {
        return commandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_command_items, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imgAvatar);
            holder.txtContactName = convertView.findViewById(R.id.txtErrorName);
            holder.txtIcon = convertView.findViewById(R.id.txtIcon);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        final Command command = commandList.get(position);
        holder.txtContactName.setText(command.getName());
        if(command.getImage() != null){
            Glide.with(context).load(command.getImage()).centerCrop().into(holder.imageView);
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
