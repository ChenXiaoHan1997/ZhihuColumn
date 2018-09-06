package practice.cxh.zhihuzhuanlan.column_page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import practice.cxh.zhihuzhuanlan.R;

public class ArticleDownloadFragment extends Fragment {

    private View mLayoutRoot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutRoot = inflater.inflate(R.layout.fragment_article_download, container, false);
        return mLayoutRoot;
    }
}
