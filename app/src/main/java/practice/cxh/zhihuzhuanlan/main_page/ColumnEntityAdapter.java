package practice.cxh.zhihuzhuanlan.main_page;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.column_page.ArticleListActivity;
import practice.cxh.zhihuzhuanlan.column_page.ArticleListActivity2;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ColumnEntityAdapter extends RecyclerView.Adapter<ColumnEntityAdapter.ViewHolder> {

    private List<ColumnEntity> mColumnEntities;
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    public ColumnEntityAdapter(Context context, List<ColumnEntity> columnEntities) {
        this.mColumnEntities = columnEntities;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_column, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ColumnEntity columnEntity = mColumnEntities.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleListActivity2.launch((Activity) mContext, columnEntity);
            }
        });
        holder.tvName.setText(columnEntity.getName());
        holder.tvDescription.setText(columnEntity.getDescription());
        holder.tvPostsCount.setText(String.format(mContext.getString(R.string.posts_count), columnEntity.getPostsCount()));
        holder.tvFollowersCount.setText(String.format(mContext.getString(R.string.followers_count), columnEntity.getFollowersCount()));
        Glide.with(mContext)
                .load(columnEntity.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.liukanshan_square))
                .into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return mColumnEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;
        TextView tvName;
        TextView tvDescription;
        TextView tvPostsCount;
        TextView tvFollowersCount;

        ViewHolder(View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPostsCount = itemView.findViewById(R.id.tv_posts_count);
            tvFollowersCount = itemView.findViewById(R.id.tv_followers_count);
        }
    }
}
