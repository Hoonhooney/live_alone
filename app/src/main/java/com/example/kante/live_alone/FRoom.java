package com.example.kante.live_alone;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FRoom extends Fragment {

    private FirebaseFirestore fs;
    //    static final int LIMIT = 50;
    private ArrayList<Post> mArrayList = new ArrayList<>();
    private List<Post> types;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar pgsBar;

    public FRoom() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_room, container, false);

        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();

        pgsBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        //피드 카드뷰 생성
        recyclerView = (RecyclerView) v.findViewById(R.id.feeds_room);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(getContext(), mArrayList, R.layout.fragment_room);

//        //데이터 정렬
//        getListItems();

        //스크롤
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    fetchData();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getListItems();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(pgsBar != null)
            pgsBar.setVisibility(ProgressBar.GONE);
    }

    private void fetchData() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = mArrayList.size();
                for (int i =0 ; i < 5; i++){
                    if(k + i < types.size()){
                        mArrayList.add(types.get(k + i));
                        mAdapter.notifyDataSetChanged();
                        pgsBar.setVisibility(ProgressBar.GONE);
                    }
                    else{
                        pgsBar.setVisibility(ProgressBar.GONE);
                        return;
                    }
                }
            }
        }, 1000);
    }

    public void getListItems(){
        if(!mArrayList.isEmpty())
            mArrayList.clear();
        Log.d("qpoqop","whiatqwdqw?");
        fs.collection("posts").whereEqualTo("category","FRoom").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d("qpoqop","whiat?");
                            return;
                        }else{
                            types = queryDocumentSnapshots.toObjects(Post.class);
                            types.sort(new CustomComparator().reversed());
                            if(types.size() < 10) {
                                for(int i = 0; i < types.size(); i++)
                                    mArrayList.add(types.get(i));

                            }
                            else{
                                for(int j = 0; j < 10; j++)
                                    mArrayList.add(types.get(j));

                            }
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public class CustomComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }
}
