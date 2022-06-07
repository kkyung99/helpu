package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
        comment  = findViewById(R.id.comment);

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
            public  void onClick(View v){
                String comment1 = comment.getText().toString();
                if(comment1.length() != 0){
                    items.add(comment);
                    comment.setText("댓글을 입력하세요.");
                    adapter.notifyDataSetChanged();
                }
//                items.add(comment.getText().toString());
//                adapter.notifyDataSetChanged();
//                comment.setText("");
            }
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
