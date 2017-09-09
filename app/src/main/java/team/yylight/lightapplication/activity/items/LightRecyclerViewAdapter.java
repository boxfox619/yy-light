package team.yylight.lightapplication.activity.items;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.activity.ItemInfoActivity;
import team.yylight.lightapplication.data.LightItem;

public class LightRecyclerViewAdapter extends RecyclerView.Adapter<LightRecyclerViewAdapter.LightItemViewHolder> {
    private List<LightItem> lightItemList;
    private Context mContext;

    public LightRecyclerViewAdapter(Context context) {
        this.lightItemList = new ArrayList();
        this.mContext = context;
    }

    @Override
    public LightItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycleritem, viewGroup, false);
        LightItemViewHolder viewHolder = new LightItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LightItemViewHolder lightItemViewHolder, int i) {
        LightItem lightItem = lightItemList.get(i);
        Picasso.with(mContext).load(lightItem.getImageUrl()).resize(370, 300).into(lightItemViewHolder.imageView);
        lightItemViewHolder.itemNumber = lightItem.getNumber();
        lightItemViewHolder.tv_title.setText(lightItem.getTitle());
        lightItemViewHolder.tv_subscribe.setText(lightItem.getSubscribe());
        lightItemViewHolder.ratingBar.setRating((float)lightItem.getScore());
        lightItemViewHolder.setItem(lightItem);
    }

    @Override
    public int getItemCount() {
        return lightItemList.size();
    }

    public void add(LightItem item){
        lightItemList.add(item);
        this.notifyDataSetChanged();
    }

    public void clear(){
        lightItemList.removeAll(lightItemList);
        this.notifyDataSetChanged();
    }

    class LightItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView tv_title, tv_subscribe;
        protected MaterialRatingBar ratingBar;

        protected int itemNumber;
        private LightItem item;

        public void setItem(LightItem item){
            this.item = item;
        }

        public LightItemViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ItemInfoActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("content", item.getSubscribe());
                    intent.putExtra("image", item.getImageUrl());
                    intent.putExtra("score", item.getScore());
                    intent.putExtra("number", item.getNumber());
                    intent.putExtra("amount", item.getAmount());
                    intent.putExtra("temperature", item.getTemperature());
                    intent.putExtra("writer", item.getWriter());
                    mContext.startActivity(intent);
                }
            });
            this.imageView = view.findViewById(R.id.iv_thumbnail);
            this.tv_title = view.findViewById(R.id.tv_title);
            this.tv_subscribe = view.findViewById(R.id.tv_subscribe);
            this.ratingBar = view.findViewById(R.id.ratingBar);
        }
    }
}