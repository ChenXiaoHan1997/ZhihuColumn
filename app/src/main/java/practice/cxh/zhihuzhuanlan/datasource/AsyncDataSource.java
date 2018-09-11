package practice.cxh.zhihuzhuanlan.datasource;

import java.util.List;

public interface AsyncDataSource<T> {

    void getDataAt(int positon, AsyncDataListener<T> listener);
    void getDataWithRange(int start, int count, AsyncDataListener<T> listener);
    int getItemCount();

    interface AsyncDataListener<T> {
        void onDataLoaded(T data);
        void onDataLoaded(List<T> dataList);
    }
}
