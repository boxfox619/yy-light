package team.yylight.lightapplication.activity.sign.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import team.yylight.lightapplication.MainActivity;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.UserInfo;

/**
 * Created by boxfox on 2017-08-11.
 */

public class SignInFragment extends SignFragment {
    private EditText et_id, et_password;
    private Button btnLogin;

    public static final SignFragment newInstance(String message) {
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
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        btnLogin = v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        return v;
    }

    private boolean checkInputs() {
        boolean check = false;
        View focusView = null;
        if (et_id.getText().length() == 0) {
            et_id.setError("아이디를 입력해 주세요");
            focusView = et_id;
        }
        if (et_password.getText().length() == 0) {
            et_password.setError("비밀번호를 입력해 주세요");
            focusView = et_password;
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else {
            check = true;
        }
        return check;
    }

    private void login() {
        if (!checkInputs()) {
            final String id = et_id.getText().toString();
            final String password = et_password.getText().toString();
            Map<String, Object> params = new HashMap<>();
            params.put("username", id);
            params.put("password", et_password.getText());
            AQuery aq = new AQuery(getActivity());
            aq.ajax(getResources().getString(R.string.url_host) + getResources().getString(R.string.url_login), params, String.class, new AjaxCallback<String>() {
                public void callback(String url, String result, AjaxStatus status) {
                    if (status.getCode() == 200) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        UserInfo info = realm.createObject(UserInfo.class);
                        info.setId(id);
                        info.setPassword(password);
                        realm.commitTransaction();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
