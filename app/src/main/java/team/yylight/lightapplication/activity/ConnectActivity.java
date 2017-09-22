package team.yylight.lightapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import io.realm.Realm;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.UserInfo;

/**
 * Created by boxfox on 2017-09-09.
 */

public class ConnectActivity extends AppCompatActivity {
    private View first,second,finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        first = findViewById(R.id.layout_first);
        second = findViewById(R.id.layout_second);
        finish = findViewById(R.id.layout_finish);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(((EditText)findViewById(R.id.et_code)).getText().toString());
            }
        });
    }

    private void connect(final String str){
        AQuery aq = new AQuery(this);
        AjaxCallback ac = new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                second.setVisibility(View.GONE);
                if(status.getCode() >= 200){
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.where(UserInfo.class).findFirst().setDeviceId(str);
                    realm.commitTransaction();
                    finish.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ConnectActivity.this, "기기 연결에 실패했습니다!", Toast.LENGTH_SHORT).show();
                    first.setVisibility(View.VISIBLE);
                }
            }
        };
        ac.param("device", str);
        ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
        first.setVisibility(View.GONE);
        second.setVisibility(View.VISIBLE);
        aq.ajax(getString(R.string.url_host)+getString(R.string.url_connect), JSONObject.class, ac);
    }
}
