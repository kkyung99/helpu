package com.example.helpu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    TextView comment;
    Button comment_change;
    Button commnet_delete;
    ArrayList<CommentItem> CommentItemList=new ArrayList<CommentItem>();

    public  CommentAdapter(){
    }
    @Override
    public  int getCount(){
        return CommentItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  int pos = position;
        final Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item,parent,false);
            comment = (TextView) convertView.findViewById(R.id.comment);
            comment_change = (Button) convertView.findViewById(R.id.comment_change);
            commnet_delete = (Button) convertView.findViewById(R.id.comment_delete);

            //comment.setText(CommentItem.getComment());
        }
        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {

        return CommentItemList.get(position);
    }
}
