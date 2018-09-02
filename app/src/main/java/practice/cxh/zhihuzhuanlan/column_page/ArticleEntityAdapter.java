package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.article_page.ArticleContentActivity;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;

public class ArticleEntityAdapter extends RecyclerView.Adapter<ArticleEntityAdapter.ViewHolder> {

    List<ArticleEntity> mArticleEntities;
    Context mContext;

    public ArticleEntityAdapter(Context context, List<ArticleEntity> articleEntities) {
        this.mContext = context;
        this.mArticleEntities = articleEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articleItemView = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(articleItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ArticleEntity articleEntity = mArticleEntities.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleContentActivity.launch((Activity) mContext, articleEntity.getSlug());
            }
        });
        holder.tvTitle.setText(articleEntity.getTitle());
        holder.tvLikesCount.setText(String.format(mContext.getString(R.string.likes_count), articleEntity.getLikesCount()));
        holder.tvDate.setText(articleEntity.getPublishedTime());
        if (TextUtils.isEmpty(articleEntity.getTitleImage())) {
            holder.ivPic.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(articleEntity.getTitleImage()).into(holder.ivPic);
        }
    }

    @Override
    public int getItemCount() {
        return mArticleEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvLikesCount;
        TextView tvDate;
        ImageView ivPic;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLikesCount = itemView.findViewById(R.id.tv_likes_count);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivPic = itemView.findViewById(R.id.iv_pic);
        }
    }
}
