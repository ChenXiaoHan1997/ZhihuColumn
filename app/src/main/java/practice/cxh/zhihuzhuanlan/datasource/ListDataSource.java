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
        List<T> dataList = new ArrayList<>();
        start = Math.max(start, 0);
        int end = Math.min(start + count, mDataList.size());
        if (start < end) {
            for (int i = start; i < end; i++) {
                dataList.add(mDataList.get(i));
            }
        }
        return dataList;
    }
}
