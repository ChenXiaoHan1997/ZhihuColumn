package practice.cxh.zhihuzhuanlan.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class ZhihuActivity<V, P extends BasePresenter<V>> extends BaseActivity<V, P> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initToolbar();
        registerListenerReceiver();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterListenerReceiver();
    }

    protected abstract void initView();

    protected abstract void initToolbar();

    protected abstract void registerListenerReceiver();

    protected abstract void initData();

    protected abstract void unregisterListenerReceiver();
}
