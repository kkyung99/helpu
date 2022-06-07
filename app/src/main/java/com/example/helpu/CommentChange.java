package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CommentChange extends AppCompatActivity{
    Button btn_change;//저장
    ImageView btn_back;
    EditText edit_change;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        btn_change = findViewById(R.id.button_change);
        btn_back = findViewById(R.id.btn_back);
        edit_change = findViewById(R.id.comment_change);

        //보내온 intent를 얻는다.
        Intent intent = getIntent();
        edit_change.setText(intent.getStringExtra("comment"));

        //뒤로
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Community.class);
                startActivity(intent);
            }
        });
        //저장
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> post = new HashMap<>();
                post.put("comment", edit_change.getText().toString());
                post.put("uid", intent.getStringExtra("uid"));
                post.put("timeStamp", FieldValue.serverTimestamp());
                db.collection("communityComments").document(intent.getStringExtra("id")).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(CommentChange.this, CommentChange.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}