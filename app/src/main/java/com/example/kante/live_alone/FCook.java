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

public class FCook extends Fragment {

    final int ITEM_SIZE = 5;

    public FCook() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cook, container, false);

        /*피드 카드뷰 생성(Test)*/
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.feeds_cook);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<Post> items = new ArrayList<>();
        Post[] item = new Post[ITEM_SIZE];

        item[0] = new Post("id1", "Title1", "blablablaaaaddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddnjknjknjknjkn.jknjk.hugujdfyjrㅎ랄허ㅏㄹ허ㅏㄹ허ㅏㄹ허ㅏㄹ허ㅏㄹ허ㅏ러dddddddddddddddddddddddjnk");
        item[1] = new Post("id2", "Title2", "blablablaaaa");
        item[2] = new Post("id3", "Title3", "blablablaaaa");
        item[3] = new Post("id4", "Title4", "blablablaaaa");
        item[4] = new Post("id5", "Title5", "blablablaaaa");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(getContext(), items, R.layout.fragment_cook));

        return v;
    }
}
