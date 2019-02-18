package xyz.dudedaya.myfriends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;


    public FriendsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = inflater.inflate(R.layout.fragment_friends, container, false);
        FriendsRecyclerAdapter friendsRecyclerAdapter = new FriendsRecyclerAdapter()
        return recyclerView;
    }


}
