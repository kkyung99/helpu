package com.example.helpu;

import static com.example.helpu.LoginActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class Custom extends AppCompatActivity{
    Button btn_delete;//삭제
    Button btn_change;//수정
    Button btn_upload;//댓글등록
    ListView listview1;
    ImageView btn_back;
    TextView textTitle;
    //ImageView img;
    TextView textContent;
    TextView comment; //댓글
    private ListView listView1;
    private ListViewAdapter adapter;
    private ArrayList<TextView> items = new ArrayList<TextView>();
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        adapter = new ListViewAdapter();
        listview1 = (ListView) findViewById(R.id.listview1);
        listview1.setAdapter(adapter);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);

        listView1 = findViewById(R.id.listview1);
        btn_upload = findViewById(R.id.upload);
        btn_delete = findViewById(R.id.delete);
        btn_change = findViewById(R.id.change);
        btn_back = findViewById(R.id.btn_back);
        textTitle = findViewById(R.id.txtTitle);
        //img = findViewById(R.id.img);
        textContent = findViewById(R.id.txtContent);
        comment = findViewById(R.id.txtContent1);

        listView1.setAdapter(adapter);

        //보내온 intent를 얻는다.
        Intent intent = getIntent();
        textTitle.setText(intent.getStringExtra("title"));
        textContent.setText(intent.getStringExtra("content"));
        //img.setImageResource(intent.getIntExtra("img",0));
        //String text = intent.getExtras().getString("POSITION");
        //textView.setText(text);

        //댓글등록
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                String comment1 = comment.getText().toString();
                if (comment1.length() == 0) {
                    comment.setText("댓글을 입력하세요.");
                    return;
                }
                Map<String, Object> comment = new HashMap<>();
                comment.put("comment", comment1);
                comment.put("postId", intent.getStringExtra("id"));
                comment.put("authorUid", auth.getCurrentUser().getUid());
                comment.put("authorName", auth.getCurrentUser().getDisplayName());
                comment.put("timeStamp", FieldValue.serverTimestamp());

                db.collection("communityComments").add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //데이터가 성공적으로 추가되었을 때
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    //    items.add(comment1);
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                //작업이 성공적으로 마쳤을때
//                if (task.isSuccessful()) {
//                    System.out.println("ㅅㅓㅇ공!!!!!!");
//                    //컬렉션 아래에 있는 모든 정보를 가져온다.
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        System.out.println(document.getData());
//                        //doucument 결과를 어뎁터에 저장
//                        adapter.addItem(document.get("authorName").toString(), document.get("comment").toString(),document.get("timeStamp").toString());
//                        adapter.notifyDataSetChanged();
//                        CommentItem CommentData = new CommentItem(); //listviewData객체 생성
//                        CommentData.setUidStr(document.get("uid").toString()); //수정페이지에서 uid값을 사용하기 위해
//                        CommentData.setIdStr(document.getId()); //고정id값 저장
//                        CommentData.setTitle(document.get("comment").toString()); //댓글
//                        items.add();//listviewData를 testlist배열안에 저장해준다. 그럼 쭈루룩 나옴.
//                        //document.getData() or document.getId() 등등 여러 방법으로
//                        //데이터를 가져올 수 있다.
//                    }
//                    //그렇지 않을때
//                } else {/
//                }
//            //}
        });
        //뒤로
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Community.class);
                startActivity(intent);
            }
        });
        //삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("intent.getStringExtra(id)");
                System.out.println(intent.getStringExtra("id"));
                db.collection("communityPosts").document(intent.getStringExtra("id")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    //고유값id으로 document를 가져온것 .이전까지 / delete는 가져온걸 삭제하겠다./add~ (파이어베이스에서 고유값이 다다른것을 알 수 있다.)
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //작업이 성공하면 넘겨줘서 삭제되서 다시 리스트를 불러왔을때 없어진것을 볼수있음.
                        Intent intent = new Intent(Custom.this, Community.class);
                        startActivity(intent);
                    }
                }
            });
//
            }
        });
        //수정
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = textTitle.getText().toString();
                final String content = textContent.getText().toString();
                final String id = intent.getStringExtra("id");
                final String uid = intent.getStringExtra("uid");
                Intent intent = new Intent(Custom.this, Change.class);
                intent.putExtra("uid", uid);// 파이어베이스와 아이디를 구분하기위한 내가 직접 지정한 고유아이디
                //커뮤니티에서 부터 계속 수정을 위해 uid값을 넘겨주고있다.
                intent.putExtra("id", id); //파이어베이스에서 사용하는 고유아이디
                intent.putExtra("title",title); //제목
                intent.putExtra("content",content); //내용
                startActivity(intent);
            }
        });
    }
}
