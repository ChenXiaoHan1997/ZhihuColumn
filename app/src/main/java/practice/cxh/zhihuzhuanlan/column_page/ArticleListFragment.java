package practice.cxh.zhihuzhuanlan.column_page;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import practice.cxh.zhihuzhuanlan.EndlessRecyclerOnScrollListener;
import practice.cxh.zhihuzhuanlan.R;
import practice.cxh.zhihuzhuanlan.entity.ArticleEntity;
import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public class ArticleListFragment extends Fragment implements ArticleListV {

    private ColumnEntity mColumnEntity;

    private ArticleListPagePresenter mPresenter;

    private View mLayoutRoot;
    private RecyclerView rvArticles;

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
        initView();
        initData();
    }

    private void initView() {
        rvArticles = mLayoutRoot.findViewById(R.id.rv_articles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new ArticleEntityAdapter(this.getContext(), mArticleEntityList);
        rvArticles.setAdapter(mAdapter);
    }

    private void initData() {
        Intent intent = getActivity().getIntent();
        mColumnEntity = (ColumnEntity) intent.getSerializableExtra(ArticleListActivity.COLUMN_ENTITY);
        mPresenter = new ArticleListPagePresenter(this);
        mPresenter.loadArticleList(mColumnEntity.getSlug());
    }

    @Override
    public void onArticleListLoaded(List<ArticleEntity> articleEntityList) {
        mArticleEntityList.addAll(articleEntityList);
        mAdapter.notifyDataSetChanged();
    }

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {

        }
    };
}
