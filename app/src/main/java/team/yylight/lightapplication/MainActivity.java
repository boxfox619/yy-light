package team.yylight.lightapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import team.yylight.lightapplication.activity.SplashActivity;
import team.yylight.lightapplication.activity.sign.SignActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
    }
}
