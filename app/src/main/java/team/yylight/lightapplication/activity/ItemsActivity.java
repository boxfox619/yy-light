package team.yylight.lightapplication.activity;

import android.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.yylight.lightapplication.R;
import team.yylight.lightapplication.item.LightItem;
import team.yylight.lightapplication.item.LightRecyclerViewAdapter;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LightRecyclerViewAdapter adapter;

    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        et_search = (EditText)findViewById(R.id.et_search);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title_view, null);
        ((TextView)view.findViewById(R.id.tv_title)).setText("Items");
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setCustomView(view);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_add:
                //item add

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadLightItems(){
        AQuery aq = new AQuery(ItemsActivity.this);
        aq.ajax(getResources().getString(R.string.url_load_items),JSONArray.class,  new AjaxCallback<JSONArray>(){
            public void callback(String url, JSONArray arr, AjaxStatus status)
            {
                List<LightItem> lightItemList = new ArrayList<LightItem>();
                for(int i = 0; i<arr.length(); i++){
                    try {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        lightItemList.add(new LightItem(jsonObject.getInt("number"), jsonObject.getString("title"),jsonObject.getString("subscribe"),jsonObject.getString("image"),jsonObject.getDouble("score")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new LightRecyclerViewAdapter(ItemsActivity.this, lightItemList);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
}
