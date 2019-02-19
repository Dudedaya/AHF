package xyz.dudedaya.myfriends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.dudedaya.myfriends.DataHelper.User;

public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DataHelper dataHelper = DataHelper.getInstance();
        List<User> friends = dataHelper.getFriends();
        View layout = inflater.inflate(R.layout.fragment_friends, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        if (friends != null) {
            FriendsRecyclerAdapter friendsRecyclerAdapter = new FriendsRecyclerAdapter(friends);
            recyclerView.setAdapter(friendsRecyclerAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setVisibility(View.GONE);
            TextView textView = layout.findViewById(R.id.text_view);
            textView.setVisibility(View.VISIBLE);
        }
        return layout;
    }

}
