package com.example.helpu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ImageView iconImageView;
    private TextView titleTextView;
    private TextView contentTextView;
    private TextView nameTextView;
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    Context context;

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        titleTextView = (TextView) convertView.findViewById(R.id.title);
        iconImageView = (ImageView) convertView.findViewById(R.id.icon);
        contentTextView = (TextView) convertView.findViewById(R.id.content);
        nameTextView = (TextView) convertView.findViewById(R.id.name);
        ListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        contentTextView.setText(listViewItem.getContent());
        nameTextView.setText(listViewItem.getNameStr());

        if (!"".equals(listViewItem.getIcon())) {
            Glide.with(context).load(listViewItem.getIcon()).into(iconImageView);
        } else {
            iconImageView.setVisibility(View.GONE);
        }

        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {

        return listViewItemList.get(position);
    }

    public void delete(String title, String icon, String content) {
        ListViewItem item = new ListViewItem();
        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);
        listViewItemList.remove(item);
        notifyDataSetChanged();
    }

    public void addItem(String id, String title, String icon, String content, String name) {
        ListViewItem item = new ListViewItem();

        item.setIdStr(id);
        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);
        item.setNameStr(name);

        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList.clear();
    }
}
