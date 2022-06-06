package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Community extends AppCompatActivity {
    ImageView btn_write;
    private ListView listview;
    private ListViewAdapter adapter;
    TextView receiveView;
    TextView receiveView2;
    SearchView search_view;
    //파이어베이스
    public static ArrayList<ListViewItem> testList = new ArrayList<ListViewItem>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        adapter = new ListViewAdapter();

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        //listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        testList.clear();
        db.collection("communityPosts").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //order by해서 최근시간부터 나오도록 정렬해줌
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                if (task.isSuccessful()) {
                    System.out.println("ㅅㅓㅇ공!!!!!!");
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        adapter.addItem(document.get("title").toString(), R.drawable.login_logo, document.get("content").toString());
                        adapter.notifyDataSetChanged();
                        ListViewItem listviewData = new ListViewItem();
                        listviewData.setUidStr(document.get("uid").toString());
                        listviewData.setIdStr(document.getId());
                        listviewData.setTitle(document.get("title").toString());
                        listviewData.setIcon(R.drawable.login_logo);
                        listviewData.setContent(document.get("content").toString());
                        testList.add(listviewData);
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
                    }
                    //그렇지 않을때
                } else {

                }
            }
        });
        System.out.println(testList.size());

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
                intent.putExtra("uid", testList.get(position).getUidStr());
                intent.putExtra("id", testList.get(position).getIdStr()); //아이디 값이 커스텀으로 넘어가서 삭제할때 사용
                intent.putExtra("title", testList.get(position).getTitle());
                intent.putExtra("content", testList.get(position).getContent());
                //intent.putExtra("img", testList.get(position).getIcon()); testList에 사진을 저장시켜주면 나옴
                //intent.putExtra("POSITION", position);
                startActivity(intent);

            }
        });
    }
}