package com.example.kante.live_alone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FTips extends Fragment {

    final int ITEM_SIZE = 5;

    public FTips() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tips, container, false);

        /*피드 카드뷰 생성(Test)*/
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.feeds_tips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<Post> items = new ArrayList<>();
        Post[] item = new Post[ITEM_SIZE];

        item[0] = new Post("id1", "Title1", "tiptiptiptiptipti");
        item[1] = new Post("id2", "Title2", "tiptiptiptiptipti");
        item[2] = new Post("id3", "Title3", "tiptiptiptiptipti");
        item[3] = new Post("id4", "Title4", "tiptiptiptiptipti");
        item[4] = new Post("id5", "Title5", "tiptiptiptiptipti");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(v.getContext(), items, R.layout.fragment_tips));

        return v;
    }

}
