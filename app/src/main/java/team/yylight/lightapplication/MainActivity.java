package team.yylight.lightapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import team.yylight.lightapplication.activity.ItemsActivity;
import team.yylight.lightapplication.activity.ProfileActivity;
import team.yylight.lightapplication.activity.SignActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, ItemsActivity.class));
    }
}
