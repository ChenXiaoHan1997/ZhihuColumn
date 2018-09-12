package practice.cxh.zhihuzhuanlan.datasource;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ListDataSource<T> implements DataSource<T> {

    protected List<T> mDataList;
    protected RecyclerView.Adapter mAdapter;

    public ListDataSource(RecyclerView.Adapter adapter) {
        this.mDataList = new ArrayList<>();
        this.mAdapter = adapter;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public List<T> getDataWithRange(int start, int count) {
        return null;
    }
}
