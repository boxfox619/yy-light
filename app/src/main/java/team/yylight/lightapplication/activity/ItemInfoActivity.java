package team.yylight.lightapplication.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.data.UserInfo;

public class ItemInfoActivity extends AppCompatActivity {
    private TextView tv_summary, tv_writer, tv_score;
    private ImageView iv_thumbnail;
    private MaterialRatingBar ratingBar;

    private LinearLayout layout_tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title_view, null);
        ((TextView) view.findViewById(R.id.tv_back_target)).setText("Items");
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setCustomView(view);

        tv_summary = (TextView) findViewById(R.id.tv_summary);
        tv_writer = (TextView) findViewById(R.id.tv_writer);
        tv_score = (TextView) findViewById(R.id.tv_score);
        iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);
        layout_tags = (LinearLayout) findViewById(R.id.layout_tags);
        loadItemInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:
                showRatingDialog();
                return true;
            case R.id.action_check:
                useImmediately();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadItemInfo() {
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("title"));
        tv_summary.setText(getIntent().getStringExtra("content"));
        double rate = getIntent().getDoubleExtra("score", 0);
        ratingBar.setMax(50);
        ratingBar.setProgress((int) rate*10);
        tv_score.setText(String.format("%.2f", rate));
        //ratingBar.setRating(Float.valueOf(String.format("%.2f", object.getDouble("score"))));
        try {
            JSONObject user = new JSONObject(getIntent().getStringExtra("writer"));
            tv_writer.setText(user.getString("username"));
            JSONArray tags = new JSONArray(getIntent().getStringExtra(("tags")));
            for (int i = 0; i < tags.length(); i++) {
                View view = getLayoutInflater().inflate(R.layout.tag_card, null);
                String tag = tags.getJSONObject(i).getString("name");
                if (tag.length() == 0) return;
                ((TextView) view.findViewById(R.id.tv_tag)).setText(tag);
                layout_tags.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AQuery aq = new AQuery(this);
        aq.id(iv_thumbnail).image(getIntent().getStringExtra("image"), false, true, 0, 0, null, Constants.FADE_IN);
    }

    private void useImmediately() {
        String deviceId = Realm.getDefaultInstance().where(UserInfo.class).findFirst().getDeviceId();
        if (deviceId == null) {
            Toast.makeText(this, "연결된 기기가 없습니다!", Toast.LENGTH_LONG).show();
        } else {
            AjaxCallback<JSONObject> ac = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    if (status.getCode() < 300 && status.getCode() >= 200) {
                        View view = getLayoutInflater().inflate(R.layout.layout_saved_dialog, null);
                        AlertDialog dialog = new AlertDialog.Builder(ItemInfoActivity.this)
                                .setView(view)
                                .create();

                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);
                        dialog.show();
                    }else{
                        Log.e("Error", status.getCode()+status.getError()+status.getMessage());
                    }
                }
            };
            ac.param("deviceID", deviceId);
            ac.param("light", getIntent().getIntExtra("number", 0));
            ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
            new AQuery(this).ajax(getString(R.string.url_host) + getString(R.string.url_use), JSONObject.class, ac);
        }

    }

    private void showRatingDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_rates, null);
        AlertDialog dialog = new AlertDialog.Builder(ItemInfoActivity.this)
                .setTitle("별점을 선택해 주세요")
                .setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float rate = ((RatingBar) view.findViewById(R.id.rb_score)).getRating();
                        AQuery aq = new AQuery(ItemInfoActivity.this);
                        AjaxCallback ac = new AjaxCallback<String>() {
                            @Override
                            public void callback(String url, String object, AjaxStatus status) {
                                Log.d("Rating", status.getCode() + status.getMessage() + status.getError());
                            }
                        };
                        ac.param("rate", rate);
                        ac.param("light", getIntent().getIntExtra("number", 0));
                        ac.header("Authorization", Realm.getDefaultInstance().where(UserInfo.class).findFirst().getToken());
                        aq.ajax(getString(R.string.url_host) + getString(R.string.url_rate), String.class, ac);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
    }
}
