package team.yylight.lightapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton ibItems, ibInfo;
    private View light_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setBlurImage((ImageView) findViewById(R.id.iv_background));

        tvLightName = (TextView) findViewById(R.id.tv_name);
        tvWate = (TextView) findViewById(R.id.tv_wate);
        light_color = (View) findViewById(R.id.light_color);
        tvTempeture = (TextView) findViewById(R.id.tv_tempeture);
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
        updateDeviceState();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDeviceState();
    }

    private void updateDeviceState() {
        Log.d("Lightstate", "request");
        final String deviceId = Realm.getDefaultInstance().where(UserInfo.class).findFirst().getDeviceId();
        if (deviceId != null) {
            AjaxCallback ac = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    if (object != null) {
                        try {
                            JSONObject light = object.getJSONObject("light");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                light_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(light.getString("temperature"))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            tvLightName.setText(deviceId);
                            tvWate.setText(object.getDouble("consumption") + "");
                            tvTempeture.setText(object.getDouble("temperature") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("LightState", status.getCode() + status.getMessage() + "  " + status.getError());
                    }
                }
            };
            ac.param("deviceID", deviceId);
            ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
            new AQuery(this).ajax(getString(R.string.url_host) + getString(R.string.url_device_state), JSONObject.class, ac);
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
