package com.example.helpu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    TextView comment;
    Button comment_change;
    Button commnet_delete;
    ArrayList<CommentItem> CommentItemList=new ArrayList<CommentItem>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public  CommentAdapter(Context context){
        this.context = context;
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
        }
        comment = (TextView) convertView.findViewById(R.id.comment);
        comment_change = (Button) convertView.findViewById(R.id.comment_change);
        commnet_delete = (Button) convertView.findViewById(R.id.comment_delete);
        comment_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentChange.class);
                intent.putExtra("authorUid", CommentItemList.get(pos).getAuthorUid());// 파이어베이스와 아이디를 구분하기위한 내가 직접 지정한 고유아이디
                //커뮤니티에서 부터 계속 수정을 위해 uid값을 넘겨주고있다.
                intent.putExtra("authorName", CommentItemList.get(pos).getAuthorName()); //파이어베이스에서 사용하는 고유아이디
                intent.putExtra("comment",CommentItemList.get(pos).getComment()); //제목
                intent.putExtra("id",CommentItemList.get(pos).getId()); //제목
                context.startActivity(intent);
            }
        });
        commnet_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("communityComments").document(CommentItemList.get(pos).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    //고유값id으로 document를 가져온것 .이전까지 / delete는 가져온걸 삭제하겠다./add~ (파이어베이스에서 고유값이 다다른것을 알 수 있다.)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //작업이 성공하면 넘겨줘서 삭제되서 다시 리스트를 불러왔을때 없어진것을 볼수있음.
                            Intent intent = new Intent(context, Custom.class);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
        comment.setText(CommentItemList.get(pos).getComment());//commentxml에 있는 comment를 여기에 받아온걸 입혀주겠다.
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

    public void addItem(String comment, String authorName, String authorUid, String id){
        CommentItem item = new CommentItem(comment,authorName,authorUid, id);
        CommentItemList.add(item);
    }
}
