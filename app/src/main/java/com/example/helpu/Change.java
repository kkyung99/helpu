package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Change extends AppCompatActivity {
    Button btn_save;//저장
    EditText editTitle;
    //ImageView img;
    EditText editContent;

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
                Intent intent = new Intent(Change.this, Community.class);
//                ListViewItem item = new ListViewItem();
//                item.setTitle(editTitle.getText().toString());
//                item.setContent(editContent.getText().toString());
//                SubActivity.testList.add(item); //해당 아이템 포지션 획득이 안되고 있음.
                startActivity(intent);
            }
        });
    }
}
