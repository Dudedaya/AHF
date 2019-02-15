package xyz.dudedaya.myfriends;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public interface AuthClickListener {
        void authButtonClicked();
    }

    private AuthClickListener authClickListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.authClickListener = (AuthClickListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //add button onClick listener
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        layout.findViewById(R.id.auth_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authClickListener != null) {
                    authClickListener.authButtonClicked();
                }
            }
        });
        // Inflate the layout for this fragment
        return layout;
    }

}
