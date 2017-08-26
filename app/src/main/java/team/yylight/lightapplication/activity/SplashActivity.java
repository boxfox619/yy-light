package team.yylight.lightapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.realm.Realm;
import team.yylight.lightapplication.MainActivity;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.activity.sign.SignActivity;
import team.yylight.lightapplication.data.UserInfo;

public class SplashActivity extends AppCompatActivity {
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Realm.init(SplashActivity.this);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetActivity;
                if(isLogined()){
                    targetActivity = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    targetActivity = new Intent(SplashActivity.this, SignActivity.class);
                }
                startActivity(targetActivity);
                finish();
            }
        });
        startActivity(new Intent(SplashActivity.this, SettingActivity.class));
    }

    private boolean isLogined(){
        Realm realm=Realm.getDefaultInstance();
        return realm.where(UserInfo.class).count()>0;
    }
}
