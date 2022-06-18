package com.example.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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
    public static ArrayList<ListViewItem> testList = new ArrayList<ListViewItem>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        adapter = new ListViewAdapter(getApplicationContext());

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        testList.clear();
        db.collection("communityPosts").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //orderBy로 최근시간부터 나오도록 정렬
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                if (task.isSuccessful()) {
                    System.out.println("ㅅㅓㅇ공!!!!!!");
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        //doucument 결과를 어뎁터에 저장
                        adapter.addItem(document.getId(), document.get("title").toString(), document.get("image").toString(), document.get("content").toString(), document.get("name").toString());
                        adapter.notifyDataSetChanged();
                        ListViewItem listviewData = new ListViewItem(); //listviewData객체 생성
                        listviewData.setUidStr(document.get("uid").toString()); //수정페이지에서 uid값을 사용하기 위해
                        listviewData.setIdStr(document.getId()); //고정id값 저장
                        listviewData.setTitle(document.get("title").toString()); //제목
                        listviewData.setIcon(document.get("image").toString()); //이미지
                        listviewData.setNameStr(document.get("name").toString());
                        listviewData.setContent(document.get("content").toString());//내용
                        testList.add(listviewData);//listviewData를 testlist배열안에 저장해준다. 그럼 쭈루룩 나옴.
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {;
                Intent intent =
                        new Intent(getApplicationContext(), Custom.class);
                intent.putExtra("uid", testList.get(position).getUidStr()); //수정할때 고정id값이 변하지 않게 하기위해 uid사용
                intent.putExtra("id", testList.get(position).getIdStr()); //아이디 값이 커스텀으로 넘어가서 삭제할때 사용
                intent.putExtra("title", testList.get(position).getTitle());//제목
                intent.putExtra("content", testList.get(position).getContent());//내용
                intent.putExtra("image", testList.get(position).getIcon());//이미지
                intent.putExtra("name", testList.get(position).getNameStr());
                startActivity(intent);
            }
        });
    }
}