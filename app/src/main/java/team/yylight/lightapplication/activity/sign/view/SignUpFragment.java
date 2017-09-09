package team.yylight.lightapplication.activity.sign.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import team.yylight.lightapplication.MainActivity;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.UserInfo;

/**
 * Created by boxfox on 2017-08-11.
 */

public class SignUpFragment extends SignFragment {
    private EditText et_id, et_password, et_password_confirm;
    private Button btn_action;
    private TextView tv_birthday;

    private RadioButton rbSexMan;

    private View tab1, tab2;

    private Calendar myCalendar = Calendar.getInstance();


    private boolean nextStep = false;
    private View tab_first, tab_second;

    public static final SignFragment newInstance(String message) {
        SignUpFragment f = new SignUpFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        et_id = v.findViewById(R.id.input_id);
        et_password = v.findViewById(R.id.input_password);
        et_password_confirm = v.findViewById(R.id.input_password_confirm);
        et_password_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        btn_action = v.findViewById(R.id.btn_action);
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction();
            }
        });
        tv_birthday = v.findViewById(R.id.tv_birthday);
        rbSexMan = v.findViewById(R.id.radio_sex_man);

        tab1 = v.findViewById(R.id.step_tab1);
        tab2 = v.findViewById(R.id.step_tab2);

        tab_first = v.findViewById(R.id.tab_first);
        tab_second = v.findViewById(R.id.tab_second);
        Calendar c = Calendar.getInstance();
        tv_birthday.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
        tv_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });
        return v;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tv_birthday.setText(myCalendar.get(Calendar.YEAR) + "-" + (myCalendar.get(Calendar.MONTH) + 1) + "-" + myCalendar.get(Calendar.DAY_OF_MONTH));
        }

    };


    private void doAction() {
        if (!nextStep) {
            View focusView = null;
            if (et_id.getText().length() == 0) {
                et_id.setError("아이디를 입력해 주세요");
                focusView = et_id;
            }
            if (et_password.getText().length() == 0) {
                et_password.setError("비밀번호를 입력해 주세요");
                focusView = et_password;
            }
            if (et_password_confirm.getText().length() == 0 || !et_password_confirm.getText().toString().equals(et_password.getText().toString())) {
                et_password_confirm.setError("비밀번호를 확인해 주세요");
                focusView = et_password_confirm;
            }
            if (focusView != null) {
                focusView.requestFocus();
            } else {
                tab_first.setVisibility(View.GONE);
                tab_second.setVisibility(View.VISIBLE);
                tab1.setBackground(getResources().getDrawable(R.drawable.circle_bright));
                tab2.setBackground(getResources().getDrawable(R.drawable.circle_dark));
                btn_action.setText("회원가입");
                nextStep = !nextStep;
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            final UserInfo info = new UserInfo(et_id.getText().toString(), et_password.getText().toString(), tv_birthday.getText().toString(), rbSexMan.isChecked());
            Map<String, Object> params = new HashMap<>();
            params.put("username", info.getId());
            params.put("password", info.getPassword());
            params.put("birthday", info.getBirthday());
            params.put("gender", info.getSex());
            AQuery aq = new AQuery(getActivity());
            aq.ajax(getResources().getString(R.string.url_host) + getResources().getString(R.string.url_register), params, JSONObject.class, new AjaxCallback<JSONObject>() {
                public void callback(String url, JSONObject result, AjaxStatus status) {
                    Log.e("SIGNUP", status.getCode()+"asda");
                    if (status.getCode() == 200) {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            info.setToken(result.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        realm.beginTransaction();
                        realm.copyToRealm(info);
                        realm.commitTransaction();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Log.e("SIGNUP", status.getMessage()+"  "+status.getError()+"   "+result);
                        Toast.makeText(getActivity(), "회원가입 실패", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
