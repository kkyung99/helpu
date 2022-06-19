package com.example.helpu;

import static com.example.helpu.LoginActivity.TAG;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class Write extends Activity {
    Button btn_write;//저장
    ImageView btn_back;
    EditText txt_write;//제목
    EditText txt_write2;//본문
    ImageView imageView;
    public static final int REQUEST_CODE = 1000;
    /*startActivityForResult(intent, REQUEST_CODE);을 사용 할 때
    내가 누른 값이 무엇인지 구별하기 위해 REQUEST_CODE 값을 설정해주고
    누른 값이 onActivityResult에서 일치하면 누른 값을 구분하기 위해서 사용한다.
    (구분을 해서 여기서는 이미지를 띄울 수 있도록 사용할 수 있게 되는 것이다.)*/
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri photoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        btn_write = findViewById(R.id.button_write); //저장버튼
        btn_back = findViewById(R.id.btn_back);
        imageView = findViewById(R.id.select_image); //이미지선택
        txt_write = (EditText) findViewById(R.id.title_name); //제목
        txt_write2 = (EditText) findViewById(R.id.content); //본문

        storage = FirebaseStorage.getInstance("gs://help-u-32c8c.appspot.com");//이미지 저장경로
        storageReference = storage.getReference();

        //뒤로
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //저장버튼
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Write.this, Community.class);

                if(photoUri == null) {
                    String title = txt_write.getText().toString();
                    String content = txt_write2.getText().toString();
                    Integer image = imageView.getImageAlpha();

                    Map<String, Object> post = new HashMap<>();
                    post.put("title", title);
                    post.put("content", content);
                    post.put("image", "");
                    post.put("uid", auth.getCurrentUser().getUid());
                    post.put("name", auth.getCurrentUser().getDisplayName());
                    post.put("timeStamp", FieldValue.serverTimestamp());

                    db.collection("communityPosts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //데이터가 성공적으로 추가되었을 때
                            startActivity(intent);
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //에러가 발생했을 때
                            Log.w(TAG, "Error ", e);
                        }
                    });
                    return;
                }
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("success upload");
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println(uri.toString());

                                String title = txt_write.getText().toString();
                                String content = txt_write2.getText().toString();
                                Integer image = imageView.getImageAlpha();

                                Map<String, Object> post = new HashMap<>();
                                post.put("title", title);
                                post.put("content", content);
                                post.put("image",uri.toString());
                                post.put("uid", auth.getCurrentUser().getUid());
                                post.put("name", auth.getCurrentUser().getDisplayName());
                                post.put("timeStamp", FieldValue.serverTimestamp());

                                db.collection("communityPosts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //데이터가 성공적으로 추가되었을 때
                                        startActivity(intent);
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //에러가 발생했을 때
                                        Log.w(TAG, "Error ", e);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
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
            imageView.setImageBitmap(bitmap);

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
