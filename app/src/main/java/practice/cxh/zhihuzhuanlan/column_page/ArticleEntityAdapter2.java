package practice.cxh.zhihuzhuanlan.column_page;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import practice.cxh.zhihuzhuanlan.datasource.AsyncDataSource;
import practice.cxh.zhihuzhuanlan.datasource.DataSource;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.article_page.ArticleContentActivity;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.util.TimeUtil;

public class ArticleEntityAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOADING = 0;
    public static final int LOADING_COMPLETE = 1;
    public static final int LOADING_END = 2;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private int mLoadState;
    private int mLastPosition;

    private DataSource<ArticleEntity> mDataSource;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private LayoutInflater mLayoutInflater;

    public ArticleEntityAdapter2(Context context, RecyclerView recyclerView) {
        this.mContext = context;
//        this.mDataSource = dataSource;
        this.mRecyclerView = recyclerView;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View articleItemView = mLayoutInflater.inflate(R.layout.item_article, parent, false);
            return new ItemViewHolder(articleItemView);
        } else if (viewType == TYPE_FOOTER) {
            View footerView = mLayoutInflater.inflate(R.layout.layout_refresh_footer, parent, false);
            return new FooterViewHolder(footerView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final ArticleEntity articleEntity = mDataSource.getDataAt(position);

            switch (articleEntity.getDownloadState()) {
                case ArticleEntity.DOWNLOAD_SUCCESS:
                    itemViewHolder.tvState.setText(mContext.getString(R.string.downloaded));
                    itemViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.limeGreen));
                    break;
                case ArticleEntity.DOWNLOADING:
                    itemViewHolder.tvState.setText(mContext.getString(R.string.downloading));
                    itemViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.shaddleBrown));
                    break;
                default:
                    itemViewHolder.tvState.setText("");
                    break;
            }
            if (TextUtils.isEmpty(articleEntity.getTitleImage())) {
                itemViewHolder.ivPic.setVisibility(View.GONE);
            } else {
                itemViewHolder.ivPic.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(articleEntity.getTitleImage().replaceAll("_r", "_l"))
                        .apply(new RequestOptions().placeholder(R.drawable.liukanshan))
                        .into(itemViewHolder.ivPic);
            }
            itemViewHolder.tvTitle.setText(articleEntity.getTitle());
            itemViewHolder.tvLikesCount.setText(String.format(mContext.getString(R.string.likes_count), articleEntity.getLikesCount()));
            itemViewHolder.tvDate.setText(TimeUtil.convertPublishTime(articleEntity.getPublishedTime()));
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleContentActivity.launch((Activity) mContext, articleEntity);
                }
            });
        }
    }

    private void clearItemView(ItemViewHolder holder) {
        holder.tvTitle.setText("");
        holder.tvLikesCount.setText("");
        holder.tvDate.setText("");
        holder.tvState.setText("");
        holder.ivPic.setImageResource(R.drawable.liukanshan);
    }

    @Override
    public int getItemCount() {
        return mDataSource.getItemCount();
    }


    public void setLoadState(int loadState) {
        this.mLoadState = loadState;
        notifyDataSetChanged();
    }

    public int getLastPosition() {
        return mLastPosition;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvLikesCount;
        TextView tvDate;
        TextView tvState;
        ImageView ivPic;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLikesCount = itemView.findViewById(R.id.tv_likes_count);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvState = itemView.findViewById(R.id.tv_state);
            ivPic = itemView.findViewById(R.id.iv_pic);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView tvLoading;
        TextView tvLoadEnd;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tvLoading = itemView.findViewById(R.id.tv_loading);
            tvLoadEnd = itemView.findViewById(R.id.tv_load_end);
        }
    }

    private abstract class ArticleDataListener implements AsyncDataSource.AsyncDataListener<ArticleEntity> {

        @Override
        public void onDataLoaded(ArticleEntity data) {

        }

        @Override
        public void onDataLoaded(List<ArticleEntity> dataList) {

        }
    }
}
