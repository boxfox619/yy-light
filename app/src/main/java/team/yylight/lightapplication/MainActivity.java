package team.yylight.lightapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import team.yylight.lightapplication.activity.ConnectActivity;
import team.yylight.lightapplication.activity.ItemCreateActivity;
import team.yylight.lightapplication.activity.items.ItemsActivity;
import team.yylight.lightapplication.data.UserInfo;

public class MainActivity extends AppCompatActivity {
    private TextView tvLightName, tvWate, tvTempeture;
    private Button btnCreateLight;
    private ImageButton ibItems, ibInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setBlurImage((ImageView) findViewById(R.id.iv_background));

        tvLightName = (TextView) findViewById(R.id.tv_name);
        tvWate = (TextView) findViewById(R.id.tv_wate);
        tvTempeture = (TextView) findViewById(R.id.tv_tempeture);
        btnCreateLight = (Button) findViewById(R.id.btn_create_light);
        btnCreateLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ItemCreateActivity.class));
            }
        });
        ibItems = (ImageButton) findViewById(R.id.iv_view_items);
        ibItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ItemsActivity.class));
            }
        });
        ibInfo = (ImageButton) findViewById(R.id.iv_view_info);
        ibInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConnectActivity.class));
            }
        });
    }

    private void updateDeviceState() {
        String deviceId = Realm.getDefaultInstance().where(UserInfo.class).findFirst().getDeviceId();
        if (deviceId != null) {
            AjaxCallback<JSONObject> ac = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject result, AjaxStatus status) {
                    try {
                        tvLightName.setText(result.getInt("Name") + "");
                        tvWate.setText(result.getInt("Wate") + "");
                        tvTempeture.setText(result.getInt("Tempeature") + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ac.param("deviceID", deviceId);
            ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
            AQuery aq = new AQuery(this);
            aq.ajax(getString(R.string.url_host) + getString(R.string.url_device_state), JSONObject.class, ac);
        }
    }

    /*private void setBlurImage(ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash);
        RenderScript rs = RenderScript.create(getApplicationContext());
        Allocation input = Allocation.createFromBitmap(rs, bitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(15.f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        imageView.setImageBitmap(bitmap);
    }*/
}
