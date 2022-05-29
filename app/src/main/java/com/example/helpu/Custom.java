package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom extends AppCompatActivity{
    Button btn_delete;//삭제
    Button btn_change;//수정
    Button btn_upload;//댓글등록
    TextView textTitle;
    //ImageView img;
    TextView textContent;
    EditText comment; //댓글
    ListView listView1;
    ArrayList<String> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        items = new ArrayList<String>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);

        listView1 = findViewById(R.id.listview1);
        btn_upload = findViewById(R.id.upload);
        btn_delete = findViewById(R.id.delete);
        btn_change = findViewById(R.id.change);
        textTitle = findViewById(R.id.txtTitle);
        //img = findViewById(R.id.img);
        textContent = findViewById(R.id.txtContent);
        comment  = findViewById(R.id.txtContent1);

        listView1.setAdapter(adapter);

        //보내온 intent를 얻는다.
        Intent intent = getIntent();
        textTitle.setText(intent.getStringExtra("title"));
        textContent.setText(intent.getStringExtra("content"));
        //img.setImageResource(intent.getIntExtra("img",0));
        //String text = intent.getExtras().getString("POSITION");
        //textView.setText(text);

        //댓글 등록
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                String comment1 = comment.getText().toString();
                if(comment1.length() != 0){
                    items.add(comment1);
                    comment.setText("댓글을 입력하세요.");
                    adapter.notifyDataSetChanged();
                }
//                items.add(comment.getText().toString());
//                adapter.notifyDataSetChanged();
//                comment.setText("");
            }
        });

        //삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ListViewItem item = new ListViewItem();
//                item.setTitle(textTitle.getText().toString());
//                item.setContent(textContent.getText().toString());
                Intent intent = new Intent(Custom.this, Community.class);
                startActivity(intent);
//                if(Integer.parseInt(textTitle.getText().toString()) > -1){
//                    item.remove(Integer.parseInt(textTitle.getText().toString()));
//
//                }
            }
        });
        //수정
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Custom.this, Change.class);
                final String title = textTitle.getText().toString();
                final String content = textContent.getText().toString();
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
    }
}
