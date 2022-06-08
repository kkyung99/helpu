package com.example.helpu;

import static com.example.helpu.LoginActivity.TAG;
import static com.example.helpu.Write.REQUEST_CODE;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Change extends AppCompatActivity {
    Button btn_save;//저장
    ImageView btn_back;
    EditText editTitle;
    ImageView img;
    EditText editContent;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        btn_save = findViewById(R.id.save1);
        btn_back = findViewById(R.id.btn_back);
        editTitle = findViewById(R.id.txtTitle1);
        img = findViewById(R.id.img1);
        editContent = findViewById(R.id.txtContent2);

        editTitle.setMovementMethod(new ScrollingMovementMethod());
        editContent.setMovementMethod(new ScrollingMovementMethod());

        //보내온 intent를 얻는다.
        Intent intent = getIntent();
        editTitle.setText(intent.getStringExtra("title"));
        editContent.setText(intent.getStringExtra("content"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("image")).into(img);
        //img.setImageResource(intent.getIntExtra("img",0));
        //String text = intent.getExtras().getString("POSITION");
        //textView.setText(text);

        storage = FirebaseStorage.getInstance("gs://help-u-32c8c.appspot.com");
        storageReference = storage.getReference();
        //뒤로
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Community.class);
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //저장
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> post = new HashMap<>();
                post.put("title", editTitle.getText().toString());
                post.put("content", editContent.getText().toString());
                post.put("name", intent.getStringExtra("name"));
                post.put("image", intent.getStringExtra("image"));
                post.put("uid", intent.getStringExtra("uid")); //커뮤니티에서 uid값을 받아서 상세페이지로 넘겨주고 상세에서 수정페이지로 넘겨준것. 이유는 고유값이 변경되면 안되므로.
                post.put("timeStamp", FieldValue.serverTimestamp());

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                if (photoUri != null && photoUri.getPath() != "") {
                    ref.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("success upload");
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    post.put("image", uri.toString());
                                    db.collection("communityPosts").document(intent.getStringExtra("id")).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        //delete와 동일하게 set하면 받아온 id값에있는 post값을 아에 변경(새로운내용으로 업데이트)해준다는 뜻.
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                String uid = intent.getStringExtra("uid");
                                                String id = intent.getStringExtra("id");
                                                String name = intent.getStringExtra("name");
                                                Intent intent = new Intent(Change.this, Custom.class);
                                                intent.putExtra("uid", uid); //수정할때 고정id값이 변하지 않게 하기위해 uid사용
                                                intent.putExtra("id", id); //아이디 값이 커스텀으로 넘어가서 삭제할때 사용
                                                intent.putExtra("title", editTitle.getText().toString());//제목
                                                intent.putExtra("content", editContent.getText().toString());//내용
                                                intent.putExtra("image", uri.toString());//이미지
                                                intent.putExtra("name", name);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    db.collection("communityPosts").document(intent.getStringExtra("id")).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //delete와 동일하게 set하면 받아온 id값에있는 post값을 아에 변경(새로운내용으로 업데이트)해준다는 뜻.
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String uid = intent.getStringExtra("uid");
                                String id = intent.getStringExtra("id");
                                String image = intent.getStringExtra("image");
                                String name = intent.getStringExtra("name");
                                Intent intent = new Intent(Change.this, Custom.class);
                                intent.putExtra("uid", uid); //수정할때 고정id값이 변하지 않게 하기위해 uid사용
                                intent.putExtra("id", id); //아이디 값이 커스텀으로 넘어가서 삭제할때 사용
                                intent.putExtra("title", editTitle.getText().toString());//제목
                                intent.putExtra("content", editContent.getText().toString());//내용
                                intent.putExtra("image", image);//이미지
                                intent.putExtra("name", name);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지 뷰를 클릭하면 시작되는 함수

        if(requestCode== REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            //response에 getData , return data 부분 추가해주어야 한다

            Object selectedImage = data.getData();
            photoUri = data.getData();
            Bitmap bitmap = null;
            //bitmap 이용
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoUri);
                bitmap = rotateImage(bitmap, 90);
                //사진이 돌아가 있는 경우 rotateImage 함수 이용해서 사진 회전 가능
            } catch (IOException e) {
                e.printStackTrace();
            }

            //이미지뷰에 이미지 불러오기
            img.setImageBitmap(bitmap);

            //아래 커서 이용해서 사진의 경로 불러오기
            Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            Object mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)); //왜 오류가나니..?
            Log.d("경로 확인 >> ", "$selectedImg  /  $absolutePath");

        }else{
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
