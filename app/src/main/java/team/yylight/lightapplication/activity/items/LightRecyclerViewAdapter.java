package team.yylight.lightapplication.activity.items;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.Constants;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import team.yylight.lightapplication.R;
import team.yylight.lightapplication.activity.ItemInfoActivity;
import team.yylight.lightapplication.data.LightItem;

public class LightRecyclerViewAdapter extends RecyclerView.Adapter<LightRecyclerViewAdapter.LightItemViewHolder> {
    private List<LightItem> lightItemList;
    private Context mContext;

    public LightRecyclerViewAdapter(Context context, List<LightItem> lightItemList) {
        this.lightItemList = lightItemList;
        this.mContext = context;
    }

    @Override
    public LightItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycleritem, null);
        LightItemViewHolder viewHolder = new LightItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LightItemViewHolder lightItemViewHolder, int i) {
        LightItem lightItem = lightItemList.get(i);

        AQuery aq = new AQuery(mContext);
        aq.id(lightItemViewHolder.imageView).image(lightItem.getImageUrl(), false, true, 0, 0, null, Constants.FADE_IN);
        lightItemViewHolder.itemNumber = lightItem.getNumber();
        lightItemViewHolder.tv_title.setText(lightItem.getTitle());
        lightItemViewHolder.tv_subscribe.setText(lightItem.getSubscribe());
        lightItemViewHolder.ratingBar.setRating((float)lightItem.getScore());
    }

    @Override
    public int getItemCount() {
        return (null != lightItemList ? lightItemList.size() : 0);
    }

    class LightItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView tv_title, tv_subscribe;
        protected MaterialRatingBar ratingBar;

        protected int itemNumber;

        public LightItemViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ItemInfoActivity.class);
                    intent.putExtra("number", itemNumber);
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