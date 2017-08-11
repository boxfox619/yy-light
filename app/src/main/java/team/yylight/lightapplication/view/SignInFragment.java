package team.yylight.lightapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import team.yylight.lightapplication.R;

/**
 * Created by boxfox on 2017-08-11.
 */

public class SignInFragment extends SignFragment {
    private EditText et_id, et_password;

    public static final SignFragment newInstance(String message)
    {
        SignInFragment f = new SignInFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);
        et_id = v.findViewById(R.id.input_id);
        et_password = v.findViewById(R.id.input_password);
        return v;
    }
}
