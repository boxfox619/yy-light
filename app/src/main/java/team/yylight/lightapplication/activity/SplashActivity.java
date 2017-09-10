package team.yylight.lightapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Realm.init(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent targetActivity;
                if(isLogined()){
                    targetActivity = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    targetActivity = new Intent(SplashActivity.this, SignActivity.class);
                }
                startActivity(targetActivity);
                finish();
            }
        }, 3000);

    }

    private boolean isLogined(){
        Realm realm=Realm.getDefaultInstance();
        return realm.where(UserInfo.class).count()>0;
    }
}
