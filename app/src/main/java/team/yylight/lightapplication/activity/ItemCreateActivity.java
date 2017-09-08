package team.yylight.lightapplication.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import team.yylight.lightapplication.R;

public class ItemCreateActivity extends AppCompatActivity {

    private Switch sw_preview;
    private SeekBar amount, tempeature;
    private RadioButton visibleItemShow, visibleItemHide, visibleCommentShow, visibleCommentHide;
    private EditText tags;
    private Button randomSetup, uploadImage, create;

    private EditText etTitle, etContent;

    private String uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);

        sw_preview = (Switch) findViewById(R.id.sw_preview);
        visibleCommentHide = (RadioButton) findViewById(R.id.rb_comment_hide);
        visibleCommentShow = (RadioButton) findViewById(R.id.rb_comment_show);
        visibleItemShow = (RadioButton) findViewById(R.id.rb_item_show);
        visibleItemHide = (RadioButton) findViewById(R.id.rb_item_hide);
        tags = (EditText)findViewById(R.id.et_tags);

        etTitle = (EditText)findViewById(R.id.et_title);
        etContent = (EditText)findViewById(R.id.et_content);

        amount = (SeekBar) findViewById(R.id.sb_color_amount);
        tempeature = (SeekBar) findViewById(R.id.sb_color_temperature);
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

        randomSetup = (Button) findViewById(R.id.btn_random_setup);
        randomSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSetup();
            }
        });
        uploadImage = (Button) findViewById(R.id.btn_upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        create = (Button) findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });
    }

    private void setSeekBarThumbColor(int i){
        Bitmap bitmap = ((BitmapDrawable)((ImageView)findViewById(R.id.iv_tempeature)).getDrawable()).getBitmap();
        int pixel = bitmap.getPixel((bitmap.getWidth()/100)*i,5);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        tempeature.getThumb().setColorFilter(Color.rgb(redValue, greenValue, blueValue), PorterDuff.Mode.SRC_IN);
    }

    private void randomSetup(){
        amount.setProgress((int)(System.currentTimeMillis()%100));
        tempeature.setProgress((int)(System.currentTimeMillis()*2%100));
    }

    private void uploadImage(){

        Map<String,Object> params = new HashMap<>();

        AQuery aq = new AQuery(ItemCreateActivity.this);
        aq.ajax(getResources().getString(R.string.url_image_upload), params, String.class, new AjaxCallback<String>(){
            public void callback(String url, String object, AjaxStatus status){
                if(status.getCode()==200){
                    uploadedImage = object;
                    //success
                }else{

                }
            }
        });
    }

    private void createItem() {
            String tagsStr = tags.getText().toString().replaceAll(",", "+");
            Map<String,Object> params = new HashMap<>();
            params.put("title", etTitle.getText().toString());
            params.put("content", etContent.getText().toString());
            params.put("thumbnail", uploadedImage);
            params.put("tags", tagsStr);
            params.put("visibleItem", visibleItemShow.isChecked());
            params.put("visibleComment", visibleCommentShow.isChecked());
            params.put("amount", amount.getProgress());
            params.put("temperature", tempeature.getProgress());
            AQuery aq = new AQuery(ItemCreateActivity.this);
            aq.ajax(getString(R.string.url_host)+getResources().getString(R.string.url_upload_item), params, String.class, new AjaxCallback<String>(){
                public void callback(String url, String object, AjaxStatus status){
                    if(status.getCode()==200){
                        finish();
                    }else{
                        Toast.makeText(ItemCreateActivity.this, "아이템 생성에 실패했습니다.", Toast.LENGTH_SHORT);
                    }
                }
            });
    }


}
