package com.example.helpu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private ImageView iconImageView;
    private TextView titleTextView;
    private TextView contentTextView;
    private ArrayList<ListViewItem>listViewItemList=new ArrayList<ListViewItem>();
    private boolean[] flg = new boolean[2];
    private ImageView[] imageView = new ImageView[2];

    public  ListViewAdapter(){

    }
    @Override
    public  int getCount(){
        return listViewItemList.size();
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
        ListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        iconImageView.setImageResource(listViewItem.getIcon());
        contentTextView.setText(listViewItem.getContent());

        flg[0] = flg[1] = false; // 기본값을 설정합니다
        imageView[0] = (ImageView) convertView.findViewById(R.id.unlike); // ID 설정을 합니다
        //imageView[1] = (ImageView) convertView.findViewById(R.id.like);
        // 그림 터치시에, 이벤트를 발생하게 해주는 함수입니다
        imageView[0].setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {

                Drawable drawable; // 대리자를 선언합니다

                if (flg[0]==true) {

                    flg[0] = false;
                    drawable = context.getResources().getDrawable(R.drawable.unlike1);
                }
                else{
                    flg[0] = true;
                    drawable = context.getResources().getDrawable(R.drawable.like1);
                }
                imageView[0].setImageDrawable(drawable); // 이미지를 적용합니다
            }
        });

        //Button button = (Button)convertView.findViewById(R.id.delete);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                int count, checked;
////                count = listViewItemList.size();
////                if(count > 0){
////                    checked = listView.getCheckedItemPosition();
////                    if(checked>-1 && checked<count){
////                        listViewItemList.remove(checked);
////                        listView.clearChoices();
////                        adapter.notifyDataSetChanged();
////                    }
////                }
//                Toast.makeText(v.getContext(),listViewItem.getContent(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        Button button1 = (Button)convertView.findViewById(R.id.details);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),listViewItem.getContent(),Toast.LENGTH_SHORT).show();
//
//            }
//        });

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

    public void delete (String title, int icon, String content){
        ListViewItem item = new ListViewItem();
        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);
        listViewItemList.remove(item);
        notifyDataSetChanged();
    }

    public void addItem(String title, int icon, String content){
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setIcon(icon);
        item.setContent(content);

        listViewItemList.add(item);
    }
}
