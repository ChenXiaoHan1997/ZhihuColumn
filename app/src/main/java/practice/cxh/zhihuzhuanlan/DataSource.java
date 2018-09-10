package practice.cxh.zhihuzhuanlan;

import java.util.List;

public interface DataSource<T> {
    T getDataAt(int position);
    List<T> getDataWithRange(int start, int count);
    int getItemCount();
}
