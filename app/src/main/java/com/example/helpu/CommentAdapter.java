package com.example.helpu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    TextView comment;
    Button comment_change;
    Button comment_delete;
    TextView name;
    ArrayList<CommentItem> CommentItemList = new ArrayList<CommentItem>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private String title;
    private String content;
    private String image;
    private String postId;
    private String authorName;
    private String postAuthorUid;
    final FirebaseAuth auth = FirebaseAuth.getInstance();

    public CommentAdapter(Context context, String title, String content, String image, String postId, String authorName, String postAuthorUid) {
        this.context = context;
        this.title = title;
        this.content = content;
        this.image = image;
        this.postId = postId;
        this.authorName = authorName;
        this.postAuthorUid = postAuthorUid;
    }

    @Override
    public int getCount() {
        return CommentItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item, parent, false);
        }
        comment = (TextView) convertView.findViewById(R.id.comment);
        name = convertView.findViewById(R.id.name);
        comment_change = (Button) convertView.findViewById(R.id.comment_change);
        comment_delete = (Button) convertView.findViewById(R.id.comment_delete);

        if (!CommentItemList.get(pos).getAuthorUid().equals(auth.getCurrentUser().getUid())) {
            comment_change.setVisibility(View.GONE);
            comment_delete.setVisibility(View.GONE);
        } else {
            comment_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentChange.class);
                    intent.putExtra("authorUid", CommentItemList.get(pos).getAuthorUid());// ????????????????????? ???????????? ?????????????????? ?????? ?????? ????????? ???????????????
                    //?????????????????? ?????? ?????? ????????? ?????? uid?????? ??????????????????.
                    intent.putExtra("authorName", CommentItemList.get(pos).getAuthorName()); //???????????????????????? ???????????? ???????????????
                    intent.putExtra("comment", CommentItemList.get(pos).getComment()); //??????
                    intent.putExtra("id", CommentItemList.get(pos).getId()); //??????
                    intent.putExtra("postId", postId);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("image", image);
                    intent.putExtra("postAuthorName", authorName);
                    intent.putExtra("postAuthorUid", postAuthorUid);
                    context.startActivity(intent);
                }
            });
            comment_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("communityComments").document(CommentItemList.get(pos).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        //?????????id?????? document??? ???????????? .???????????? / delete??? ???????????? ???????????????./add~ (???????????????????????? ???????????? ??????????????? ??? ??? ??????.)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //????????? ???????????? ???????????? ???????????? ?????? ???????????? ??????????????? ??????????????? ????????????.
                                Intent intent = new Intent(context, Custom.class);
                                intent.putExtra("title", title);
                                intent.putExtra("content", content);
                                intent.putExtra("image", image);
                                intent.putExtra("id", postId);
                                intent.putExtra("name", authorName);
                                intent.putExtra("uid", postAuthorUid);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            });
        }

        comment.setText(CommentItemList.get(pos).getComment());//commentxml??? ?????? comment??? ????????? ???????????? ???????????????.
        name.setText(CommentItemList.get(pos).getAuthorName());
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

    public void addItem(String comment, String authorName, String authorUid, String id) {
        CommentItem item = new CommentItem(comment, authorName, authorUid, id);
        CommentItemList.add(item);
    }
}
