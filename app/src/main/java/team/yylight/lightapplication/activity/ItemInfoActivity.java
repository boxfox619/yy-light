package team.yylight.lightapplication.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import team.yylight.lightapplication.R;

public class ItemInfoActivity extends AppCompatActivity {
    private TextView tv_summary, tv_score, tv_writer, tv_date;
    private ImageView iv_thumbnail;
    private MaterialRatingBar ratingBar;

    private LinearLayout layout_tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        int itemNumber = getIntent().getIntExtra("number", 0);

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
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_writer = (TextView) findViewById(R.id.tv_writer);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_thumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
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
                saveItem();
                return true;
            case R.id.action_check:
                //item check

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.commitTransaction();

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
    }

    private void loadItemInfo() {
        final AQuery aq = new AQuery(ItemInfoActivity.this);
        aq.ajax(getString(R.string.url_host)+getString(R.string.url_load_item_info), JSONObject.class, new AjaxCallback<JSONObject>() {
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (status.getCode() == 200)
                    try {
                        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.tv_title)).setText(object.getString("name"));
                        tv_summary.setText(object.getString("summary"));
                        tv_writer.setText(object.getString("writer"));
                        tv_score.setText(String.format("%.2f", object.getDouble("score")));
                        ratingBar.setRating(Float.valueOf(String.format("%.2f", object.getDouble("score"))));
                        tv_date.setText(object.getString("date"));
                        JSONArray tags = object.getJSONArray("tags");
                        for (int i = 0; i < tags.length(); i++) {
                            View view = getLayoutInflater().inflate(R.layout.tag_card, null);
                            ((TextView) view.findViewById(R.id.tv_tag)).setText(tags.getString(i));
                            layout_tags.addView(view);
                        }
                        aq.id(iv_thumbnail).image(object.getString("imageUrl"), false, true, 0, 0, null, Constants.FADE_IN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        });
    }

    private void showRatingDialog(){
        final View view = getLayoutInflater().inflate(R.layout.dialog_rates, null);
        AlertDialog dialog = new AlertDialog.Builder(ItemInfoActivity.this)
                .setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float rate = ((RatingBar)view.findViewById(R.id.rb_score)).getRating();
                        AQuery aq = new AQuery(ItemInfoActivity.this);
                        aq.ajax(getString(R.string.url_host)+getString(R.string.url_rate)+"?rate="+rate, String.class, new AjaxCallback<String>(){
                            @Override
                            public void callback(String url, String object, AjaxStatus status) {
                            }
                        });
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}
