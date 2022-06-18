package com.example.helpu;

import static com.example.helpu.LoginActivity.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimalCustom extends AppCompatActivity {
    Button btn_delete;//삭제
    Button btn_change;//수정
    Button btn_upload;//댓글등록
    TextView name;
    ListView listview1;
    ImageView btn_back;
    TextView textTitle;
    ImageView img;
    TextView textContent;
    TextView comment; //댓글
    private ListView listView1;
    private CommentAdapter1 adapter;
    private ArrayList<TextView> items = new ArrayList<TextView>();
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listview;
    public static ArrayList<ListViewItem> testList = new ArrayList<ListViewItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom1);


        listView1 = findViewById(R.id.listview1);
        btn_upload = findViewById(R.id.upload);
        btn_delete = findViewById(R.id.delete);
        btn_change = findViewById(R.id.change);
        btn_back = findViewById(R.id.btn_back);
        textTitle = findViewById(R.id.txtTitle);
        img = findViewById(R.id.img);
        textContent = findViewById(R.id.txtContent);
        comment = findViewById(R.id.txtContent1);
        name = findViewById(R.id.name);

        textTitle.setMovementMethod(new ScrollingMovementMethod());
        textContent.setMovementMethod(new ScrollingMovementMethod());

        //보내온 intent를 얻는다.
        Intent intent = getIntent();

        adapter = new CommentAdapter1(getApplicationContext(), intent.getStringExtra("title"), intent.getStringExtra("content"), intent.getStringExtra("image"), intent.getStringExtra("id"), intent.getStringExtra("name"));
        listView1.setAdapter(adapter);
        textTitle.setText(intent.getStringExtra("title"));
        textContent.setText(intent.getStringExtra("content"));
        name.setText(intent.getStringExtra("name"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("image")).into(img);
        System.out.println(intent.getStringExtra("id"));
        testList.clear();
        db.collection("communityAComments").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            //orderBy로 최근시간부터 나오도록 정렬
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //작업이 성공적으로 마쳤을때
                if (task.isSuccessful()) {
                    //컬렉션 아래에 있는 모든 정보를 가져온다.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        if (document.get("postId").equals(intent.getStringExtra("id"))) {
                            //doucument 결과를 어뎁터에 저장
                            adapter.addItem(document.get("comment").toString(), document.get("authorName").toString(), document.get("authorUid").toString(), document.getId());
                            adapter.notifyDataSetChanged();
                        }
//                        CommentItem listviewData = new CommentItem(); //listviewData객체 생성
//                        testList.add(listviewData);//listviewData를 testlist배열안에 저장해준다. 그럼 쭈루룩 나옴.
//                        document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
                    }
                    //그렇지 않을때
                } else {

                }
            }
        });

        //댓글등록
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                String comment1 = comment.getText().toString();
                if (comment1.length() == 0) {
                    comment.setText("댓글을 입력하세요.");
                    return;
                }
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("comment", comment1);
                commentData.put("postId", intent.getStringExtra("id"));
                commentData.put("authorUid", auth.getCurrentUser().getUid());
                commentData.put("authorName", auth.getCurrentUser().getDisplayName());
                commentData.put("timeStamp", FieldValue.serverTimestamp());

                db.collection("communityAComments").add(commentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //데이터가 성공적으로 추가되었을 때
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // db에 추가 후 adapter에 아이템 추가해줘서 뷰에서 바로 보이게끔 처리
                        adapter.addItem(comment1, auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getUid(), intent.getStringExtra("id"));
                        adapter.notifyDataSetChanged();
                        comment.setText("");
                        recreate();//액티비티를 한번더 그려줘서 댓글 수정 삭제시 다시 해당 액티비티를 불러오도록
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
//
        });
        //뒤로
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    System.out.println("intent.getStringExtra(id)");
                    System.out.println(intent.getStringExtra("id"));
                    db.collection("communityAnimal").document(intent.getStringExtra("id")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        //고유값id으로 document를 가져온것 .이전까지 / delete는 가져온걸 삭제하겠다./add~ (파이어베이스에서 고유값이 다다른것을 알 수 있다.)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //작업이 성공하면 넘겨줘서 삭제되서 다시 리스트를 불러왔을때 없어진것을 볼수있음.
                                Intent intent = new Intent(AnimalCustom.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
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
                final String image = intent.getStringExtra("image");
                String name = intent.getStringExtra("name");
                Intent intent = new Intent(AnimalCustom.this, AnimalChange.class);
                intent.putExtra("uid", uid);// 파이어베이스와 아이디를 구분하기위한 내가 직접 지정한 고유아이디
                //커뮤니티에서 부터 계속 수정을 위해 uid값을 넘겨주고있다.
                intent.putExtra("id", id); //파이어베이스에서 사용하는 고유아이디
                intent.putExtra("title",title); //제목
                intent.putExtra("content",content); //내용
                intent.putExtra("image", image); //내용
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
