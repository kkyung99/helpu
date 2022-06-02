package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Community extends AppCompatActivity {
    Button btn_write;
    private ListView listview;
    private ListViewAdapter adapter;
    TextView receiveView;
    TextView receiveView2;
    SearchView search_view;
    //파이어베이스
    public static ArrayList<ListViewItem> testList = new ArrayList<ListViewItem>();

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        adapter = new ListViewAdapter();

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        //listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        int size = testList.size();
        for (int i = 0; i < size; i++) {
            ListViewItem item = testList.get(i);
            String title = item.getTitle();
            String content = item.getContent();
            //int icon = item.getIcon();
            adapter.addItem(title, R.drawable.login_logo, content);
        }
        adapter.notifyDataSetChanged();

        btn_write = findViewById(R.id.button_write);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.community);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),Map.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.community:
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //글쓰기
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Write.class);
                startActivity(intent);
            }
        });
        //상세보기
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                                parent.getItemAtPosition(position).toString(),
//                                Toast.LENGTH_LONG).show();
                Intent intent =
                        new Intent(getApplicationContext(), Custom.class);
                intent.putExtra("title", testList.get(position).getTitle());
                intent.putExtra("content", testList.get(position).getContent());
                //intent.putExtra("img", testList.get(position).getIcon()); testList에 사진을 저장시켜주면 나옴
                //intent.putExtra("POSITION", position);
                startActivity(intent);

            }
        });
    }
}