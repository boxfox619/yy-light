package team.yylight.lightapplication.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import team.yylight.lightapplication.R;

/**
 * Created by boxfox on 2017-08-11.
 */

public class SignUpFragment extends SignFragment {
    private EditText et_id, et_password, et_password_confirm;
    private Button btn_action;
    private TextView tv_birthday;


    private boolean nextStep = false;
    private View tab_first, tab_second;

    private String birthday;

    public static final SignFragment newInstance(String message)
    {
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
        btn_action = v.findViewById(R.id.btn_action);
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction();
            }
        });
        tv_birthday = v.findViewById(R.id.tv_birthday);

        tab_first = v.findViewById(R.id.tab_first);
        tab_second = v.findViewById(R.id.tab_second);
        return v;
    }

    private void doAction(){
        if(!nextStep){
            View focusView = null;
            if(et_id.getText().length()==0){
                et_id.setError("아이디를 입력해 주세요");
                focusView = et_id;
            }
            if(et_password.getText().length()==0){
                et_password.setError("비밀번호를 입력해 주세요");
                focusView = et_password;
            }
            if(et_password_confirm.getText().length()==0||!et_password_confirm.getText().equals(et_password.getText())){
                et_password_confirm.setError("비밀번호를 확인해 주세요");
                focusView = et_password_confirm;
            }
            if(focusView!=null){
                focusView.requestFocus();
            }else{
                tab_first.setVisibility(View.GONE);
                tab_second.setVisibility(View.VISIBLE);
                nextStep = !nextStep;
            }
        }else{
            //register
        }
    }
}
