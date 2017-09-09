package team.yylight.lightapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.UserInfo;

public class ItemCreateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1234;

    private Switch sw_preview;
    private SeekBar amount, tempeature;
    private RadioButton visibleItemShow, visibleItemHide, visibleCommentShow, visibleCommentHide;
    private EditText tags;
    private Button randomSetup, uploadImage, create;

    private EditText etTitle, etContent;

    private String currentTempeature;
    private File selectedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);

        getSupportActionBar().setTitle("조명 추가");

        sw_preview = (Switch) findViewById(R.id.sw_preview);
        visibleItemShow = (RadioButton) findViewById(R.id.rb_item_show);
        visibleItemHide = (RadioButton) findViewById(R.id.rb_item_hide);
        tags = (EditText) findViewById(R.id.et_tags);

        etTitle = (EditText) findViewById(R.id.et_title);
        etContent = (EditText) findViewById(R.id.et_content);

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
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 || checkPermission()) {
                    showImageChooser();
                }
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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_IMAGE);
            return false;
        }
        return true;
    }

    private void showImageChooser() {
        if (Build.VERSION.SDK_INT <= 19) {
            Intent i = new Intent();
            i.setType("image/*");
            startActivityForResult(i, PICK_IMAGE);
        } else if (Build.VERSION.SDK_INT > 19) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getRealPathFromURI(selectedImageUri);
                selectedImageFile = new File(selectedImagePath);
                Toast.makeText(this, "이미지를 선택을 완료했습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImageChooser();
                } else {
                    Toast.makeText(ItemCreateActivity.this, "권한을 허용하지 않아 이미지를 업로드할 수 없습니다!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void setSeekBarThumbColor(int i) {
        Bitmap bitmap = ((BitmapDrawable) ((ImageView) findViewById(R.id.iv_tempeature)).getDrawable()).getBitmap();
        int pixel = bitmap.getPixel((bitmap.getWidth() / 100) * i, 5);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        tempeature.getThumb().setColorFilter(Color.rgb(redValue, greenValue, blueValue), PorterDuff.Mode.SRC_IN);
        currentTempeature = String.format("#%02x%02x%02x", redValue, greenValue, blueValue);
    }

    private void randomSetup() {
        amount.setProgress((int) (System.currentTimeMillis() % 100));
        tempeature.setProgress((int) (System.currentTimeMillis() * 2 % 100));
    }

    private void createItem() {
        if(etTitle.getText().length()==0){
            etTitle.setError("타이틀을 입력해 주세요");
            return;
        }
        String tagsStr = tags.getText().toString().replaceAll(",", "+");
        AjaxCallback ac = new AjaxCallback<String>() {
            public void callback(String url, String object, AjaxStatus status) {
                if (status.getCode() == 201) {
                    Toast.makeText(ItemCreateActivity.this, "아이템을 성공적으로 등록했습니다!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ItemCreateActivity.this, "아이템 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("CreateItem", status.getMessage() + "   " + status.getError() + "   " + result);
                }
            }
        };
        ac.param("title", etTitle.getText().toString());
        ac.param("content", etContent.getText().toString());
        ac.param("thumbnail", selectedImageFile);
        ac.param("tags", tagsStr);
        ac.param("visible", visibleItemShow.isChecked());
        ac.param("amount", amount.getProgress());
        ac.param("temperature", currentTempeature);
        ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
        AQuery aq = new AQuery(ItemCreateActivity.this);
        aq.ajax(getString(R.string.url_host) + getResources().getString(R.string.url_upload_item), String.class, ac);
    }


}
