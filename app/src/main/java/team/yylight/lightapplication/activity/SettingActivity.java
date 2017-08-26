package team.yylight.lightapplication.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import io.realm.Realm;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.LightSetting;

public class SettingActivity extends AppCompatActivity {
    private LightSetting lightSetting;

    private SeekBar amount, tempeature;
    private Button clearBtn, saveBtn;

    private int selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Realm realm = Realm.getDefaultInstance();
        if(realm.where(LightSetting.class).count()==0){
            realm.beginTransaction();
            lightSetting = realm.createObject(LightSetting.class);
            realm.commitTransaction();
        }else{
            lightSetting = realm.where(LightSetting.class).findFirst();
        }
        initSnsButtons(R.id.iv_sns_facebook, R.id.iv_sns_twitter, R.id.iv_sns_twitter);
        clearBtn = (Button)findViewById(R.id.btn_clear);
        saveBtn = (Button)findViewById(R.id.btn_save);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                lightSetting.setAmount(amount.getProgress());
                lightSetting.setTempeature(tempeature.getProgress());
                lightSetting.setTime(selectedTime);
                realm.commitTransaction();
            }
        });
        amount = (SeekBar) findViewById(R.id.sb_color_amount);
        amount.setProgress(lightSetting.getAmount());
        tempeature = (SeekBar) findViewById(R.id.sb_color_temperature);
        tempeature.setProgress(lightSetting.getTempeature());
        tempeature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setSeekBarThumbColor(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setSeekBarThumbColor(0);
        initTimeChooser(R.id.time_10m,R.id.time_30m, R.id.time_1h, R.id.time_3h);
    }

    private void initSnsButtons(int ... ids){
        for(int id : ids){
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.iv_sns_facebook:

                            break;
                        case R.id.iv_sns_kakao:

                            break;
                        case R.id.iv_sns_twitter:

                            break;
                    }
                }
            });
        }
    }

    private void setSeekBarThumbColor(int i){
        Bitmap bitmap = ((BitmapDrawable)((ImageView)findViewById(R.id.iv_tempeature)).getDrawable()).getBitmap();
        int pixel = bitmap.getPixel((bitmap.getWidth()/100)*i,5);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        tempeature.getThumb().setColorFilter(Color.rgb(redValue, greenValue, blueValue), PorterDuff.Mode.SRC_IN);
    }

    private void initTimeChooser(int ... ids){
        for(int id : ids){
            findViewById(id).setBackground(getResources().getDrawable(R.drawable.time_selector_gray));
            findViewById(id).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    param.weight=1.0f;
                    ViewGroup parent = (ViewGroup)view.getParent();

                    for(int i = 0 ; i < parent.getChildCount(); i++){
                        View timeView = parent.getChildAt(i);
                        int idx = (parent).indexOfChild(timeView);
                        Button button = new Button(SettingActivity.this);
                        button.setLayoutParams(param);
                        button.setText(((Button)timeView).getText());
                        if(timeView.equals(view)){
                            button.setBackground(getResources().getDrawable(R.drawable.time_selector_active));
                            button.setTextColor(Color.WHITE);
                        }else
                        button.setBackground(getResources().getDrawable(R.drawable.time_selector_gray));
                        button.setOnClickListener(this);
                        parent.removeView(timeView);
                        parent.addView(button, idx);
                    }
                    switch(view.getId()){
                        case R.id.time_10m:
                            selectedTime = 10;
                            break;
                        case R.id.time_30m:
                            selectedTime = 30;
                            break;
                        case R.id.time_1h:
                            selectedTime = 60;
                            break;
                        case R.id.time_3h:
                            selectedTime = 180;
                            break;
                    }
                }
            });
        }
    }

}
