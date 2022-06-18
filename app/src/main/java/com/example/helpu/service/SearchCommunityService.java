package com.example.helpu.service;

import com.example.helpu.ListViewAdapter;
import com.example.helpu.ListViewItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchCommunityService {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void searchCommunity(String keyword, ArrayList<ListViewItem> searchItemList, ListViewAdapter adapter) {
        Task<QuerySnapshot> task = db.collection("communityPosts")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .get();
        task.addOnCompleteListener(t -> {
            searchItemList.clear();
            adapter.clear();

            if (t.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) { //getResult() 배열에서 가져옴
                    if (!"".equals(keyword) && !(document.get("title").toString().contains(keyword) //빈문자열이면 다 패스, 빈문자열이 아니면 키워드 검색
                            || document.get("content").toString().contains(keyword))) {
                        continue;
                    }
                    adapter.addItem(document.getId(), document.get("title").toString(), document.get("image").toString(), document.get("content").toString(), document.get("name").toString());
                    ListViewItem listviewData = new ListViewItem(); //listviewData객체 생성
                    listviewData.setUidStr(document.get("uid").toString()); //수정페이지에서 uid값을 사용하기 위해
                    listviewData.setIdStr(document.getId()); //고정id값 저장
                    listviewData.setTitle(document.get("title").toString()); //제목
                    listviewData.setIcon(document.get("image").toString()); //이미지
                    listviewData.setNameStr(document.get("name").toString());
                    listviewData.setContent(document.get("content").toString());//내용
                    searchItemList.add(listviewData);
                }
            }
            adapter.notifyDataSetChanged();
        });

    }
}
