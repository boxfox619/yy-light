package team.yylight.lightapplication.activity.items;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import team.yylight.lightapplication.data.LightItem;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LightRecyclerViewAdapter adapter;

    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)&&et_search.getText().toString().length()>0) {
                    AQuery aq = new AQuery(ItemsActivity.this);
                    aq.ajax(getString(R.string.url_host) + getString(R.string.url_search_tag) + "?tags=" + et_search.getText().toString().replaceAll(",", "+"), JSONArray.class, new AjaxCallback<JSONArray>() {
                        @Override
                        public void callback(String url, JSONArray arr, AjaxStatus status) {
                            if (status.getCode() == 200) {
                                loadLightItems(arr);
                            } else {
                                Log.e("ItemsActivity", status.getMessage() + status.getError());
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = getLayoutInflater().inflate(R.layout.actionbar_title_view, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText("Items");
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setCustomView(view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LightRecyclerViewAdapter(ItemsActivity.this);
        mRecyclerView.setAdapter(adapter);
        loadLightItems();
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

    private void loadLightItems(JSONArray arr){
        adapter.clear();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject jsonObject = arr.getJSONObject(i);
                adapter.add(new LightItem(jsonObject.getInt("id"), jsonObject.getString("author"), jsonObject.getString("title"), jsonObject.getString("content"), getResources().getString(R.string.url_host) + jsonObject.getString("thumbnail"), jsonObject.getDouble("score"), jsonObject.getInt("amount"), jsonObject.getString("temperature"), jsonObject.getJSONArray("tags")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLightItems() {
        AQuery aq = new AQuery(ItemsActivity.this);
        aq.ajax(getResources().getString(R.string.url_host) + getResources().getString(R.string.url_load_items), JSONArray.class, new AjaxCallback<JSONArray>() {
            public void callback(String url, JSONArray arr, AjaxStatus status) {
                if (status.getCode() == 200) {
                    loadLightItems(arr);
                } else {
                    Log.e("ItemsActivity", status.getMessage() + status.getError());
                }
            }
        });
    }
}
