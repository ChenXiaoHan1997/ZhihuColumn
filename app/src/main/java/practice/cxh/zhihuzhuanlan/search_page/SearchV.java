package practice.cxh.zhihuzhuanlan.search_page;

import practice.cxh.zhihuzhuanlan.entity.ColumnEntity;

public interface SearchV {
    void onColumnFound(ColumnEntity columnEntity);
    void onColumnNotFound();
}
