package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Change extends AppCompatActivity {
    Button btn_save;//저장
    EditText editTitle;
    //ImageView img;
    EditText editContent;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        btn_save = findViewById(R.id.save1);
        editTitle = findViewById(R.id.txtTitle1);
        //img = findViewById(R.id.img);
        editContent = findViewById(R.id.txtContent2);

        //보내온 intent를 얻는다.
        Intent intent = getIntent();
        editTitle.setText(intent.getStringExtra("title"));
        editContent.setText(intent.getStringExtra("content"));
        //img.setImageResource(intent.getIntExtra("img",0));
        //String text = intent.getExtras().getString("POSITION");
        //textView.setText(text);

        //저장
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> post = new HashMap<>();
                post.put("title", editTitle.getText().toString());
                post.put("content", editContent.getText().toString());
                post.put("uid", intent.getStringExtra("uid")); //커뮤니티에서 uid값을 받아서 상세페이지로 넘겨주고 상세에서 수정페이지로 넘겨준것. 이유는 고유값이 변경되면 안되므로.
                post.put("timeStamp", FieldValue.serverTimestamp());
                db.collection("communityPosts").document(intent.getStringExtra("id")).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    //delete와 동일하게 set하면 받아온 id값에있는 post값을 아에 변경해준다는 뜻.
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Change.this, Community.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
