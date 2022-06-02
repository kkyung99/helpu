package com.example.helpu;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Write extends Activity {
    Button btn_write;//저장
    EditText txt_write;//제목
    EditText txt_write2;//본문
    ImageView imageView;
    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        btn_write = findViewById(R.id.button_write); //저장버튼
        imageView = findViewById(R.id.select_image); //이미지선택
        txt_write = (EditText) findViewById(R.id.title_name); //제목
        txt_write2 = (EditText) findViewById(R.id.content); //본문


        //저장버튼
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list.add(txt_write.getText().toString()); //입력된 값을 ArrayList에 추가하기
//                adapter.notifyDataSetChanged();
                //txt_write.setText(""); //입력된 값 지우기
                Intent intent = new Intent(Write.this, Community.class);
//                intent.putExtra("title",txt_write.getText().toString());
//                intent.putExtra("content",txt_write2.getText().toString());
                ListViewItem item = new ListViewItem();
                item.setTitle(txt_write.getText().toString());
                item.setContent(txt_write2.getText().toString());
                item.setIcon(imageView.getImageAlpha());
                Community.testList.add(item);
                startActivity(intent);
                //finish();
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
            Uri photoUri = data.getData();
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
