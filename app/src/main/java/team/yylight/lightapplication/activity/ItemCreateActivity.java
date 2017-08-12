package team.yylight.lightapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;

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

        amount = (SeekBar) findViewById(R.id.sb_color_amount);
        tempeature = (SeekBar) findViewById(R.id.sb_color_temperature);

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

    private void randomSetup(){

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
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("visibleItem", visibleItemShow.isChecked());
            jsonObject.put("visibleComment", visibleCommentShow.isChecked());
            jsonObject.put("colorAmount", amount.getProgress());
            jsonObject.put("colorTempeture", tempeature.getProgress());
            jsonObject.put("image", uploadedImage);
            JSONArray arr = new JSONArray();
            for (String str : tags.getText().toString().split(",")) {
                arr.put(str);
            }
            jsonObject.put("tags", arr);
            Map<String,Object> params = new HashMap<>();
            params.put("Data", jsonObject);
            AQuery aq = new AQuery(ItemCreateActivity.this);
            aq.ajax(getResources().getString(R.string.url_image_upload), params, String.class, new AjaxCallback<String>(){
                public void callback(String url, String object, AjaxStatus status){
                    if(status.getCode()==200){

                    }else{

                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
