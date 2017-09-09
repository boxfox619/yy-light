package team.yylight.lightapplication.activity.sign.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
        if (checkInputs()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            final String id = et_id.getText().toString();
            final String password = et_password.getText().toString();

            AQuery aq = new AQuery(getActivity());
            AjaxCallback ac = new AjaxCallback<JSONObject>() {
                public void callback(String url, JSONObject result, AjaxStatus status) {
                    if (status.getCode() == 200) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        UserInfo info = realm.createObject(UserInfo.class);
                        info.setId(id);
                        info.setPassword(password);
                        try {
                            info.setToken(result.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        realm.commitTransaction();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_LONG).show();
                    }
                }
            };
            ac.param("username", id);
            ac.param("password", et_password.getText());
            aq.ajax(getResources().getString(R.string.url_host) + getResources().getString(R.string.url_login), JSONObject.class, ac);
        }
    }
}
