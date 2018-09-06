package practice.cxh.zhihuzhuanlan.column_page;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ArticleListFragment extends Fragment implements ArticleListV {

    private ColumnEntity mColumnEntity;

    private ArticleListPagePresenter mPresenter;

    private View mLayoutRoot;
    private AppBarLayout mAppBar;
    private LinearLayout mHeader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvName;
    private CircleImageView ivAvatar;
    private TextView tvDescription;
    private RecyclerView rvArticles;
    private Toolbar toolbar;

    private ArticleEntityAdapter mAdapter;
    private List<ArticleEntity> mArticleEntityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.fragment_article_list, container, false);
        return mLayoutRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        mColumnEntity = (ColumnEntity) intent.getSerializableExtra(ArticleListActivity.COLUMN_ENTITY);
        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAppBar = mLayoutRoot.findViewById(R.id.app_bar);
        mHeader = mLayoutRoot.findViewById(R.id.ll_header);
        mCollapsingToolbarLayout = mLayoutRoot.findViewById(R.id.collapsing_toolbar);
        mAppBar.addOnOffsetChangedListener(mOnOffsetChangedListener);
        tvName = mLayoutRoot.findViewById(R.id.tv_name);
        tvName.setText(mColumnEntity.getName());
        ivAvatar = mLayoutRoot.findViewById(R.id.iv_avatar);
        Glide.with(this).load(mColumnEntity.getPicUrl()).into(ivAvatar);
        tvDescription = mLayoutRoot.findViewById(R.id.tv_description);
        tvDescription.setText(mColumnEntity.getDescription());
        rvArticles = mLayoutRoot.findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new ArticleEntityAdapter(this.getContext(), mArticleEntityList);
        rvArticles.setAdapter(mAdapter);
        toolbar = mLayoutRoot.findViewById(R.id.tb_article_list);
    }

    private void initData() {
        mPresenter = new ArticleListPagePresenter(this);
        mPresenter.loadArticleList(mColumnEntity.getSlug(), 0);
        rvArticles.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    @Override
    public void onArticleListLoaded(List<ArticleEntity> articleEntityList, boolean clearOld) {
        mArticleEntityList.addAll(articleEntityList);
        mAdapter.setLoadState(ArticleEntityAdapter.LOADING_COMPLETE);
        mAdapter.notifyDataSetChanged();
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset <= -mHeader.getHeight() / 2) {
                mCollapsingToolbarLayout.setTitle(mColumnEntity.getName());
            } else {
                mCollapsingToolbarLayout.setTitle("");
            }
        }
    };

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            mAdapter.setLoadState(ArticleEntityAdapter.LOADING);
            if (mArticleEntityList.size() < mColumnEntity.getPostsCount()) {
                mPresenter.loadArticleList(mColumnEntity.getSlug(), mArticleEntityList.size());
            } else {
                mAdapter.setLoadState(ArticleEntityAdapter.LOADING_END);
            }
        }
    };
}
