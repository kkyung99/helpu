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

public class AnimalAdapter  extends BaseAdapter {
    /*어뎁터를 사용하는 이유
    안드로이드에도 기본적인 리스트뷰가 있는데 이것은 내가 원하는 리스뷰xml을 짤수없어
    리스트뷰안에 원하는 디자인이나 기능을 넣기위해 어뎁터로 만들어서 원래의 리스트뷰에 합쳐주는것.*/

    private ImageView iconImageView; //사진
    private TextView titleTextView; //제목
    private TextView contentTextView; //내용
    private TextView nameTextView; //아이디
    private ArrayList<ListViewItem>listViewItemList=new ArrayList<ListViewItem>();// 배열로 저장해서 불러올꺼임

    Context context;
    public  AnimalAdapter(Context context){
        this.context = context;
    }
    @Override
    public  int getCount(){
        return listViewItemList.size(); //리스트 아이템만큼 사이즈
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final  int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent,false);
        }
        titleTextView = (TextView) convertView.findViewById(R.id.title);
        iconImageView = (ImageView) convertView.findViewById(R.id.icon);
        contentTextView =(TextView) convertView.findViewById(R.id.content);
        nameTextView =(TextView) convertView.findViewById(R.id.name);
        ListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        Glide.with(context).load(listViewItem.getIcon()).into(iconImageView);
        contentTextView.setText(listViewItem.getContent());
        nameTextView.setText(listViewItem.getNameStr());

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

    public void delete (String title, String icon, String content){
        ListViewItem item = new ListViewItem();
        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);
        listViewItemList.remove(item);
        notifyDataSetChanged();
    }

    public void addItem(String id, String title, String icon, String content, String name){
        ListViewItem item = new ListViewItem();

        item.setIdStr(id);
        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);
        item.setNameStr(name);

        listViewItemList.add(item);
    }
}
