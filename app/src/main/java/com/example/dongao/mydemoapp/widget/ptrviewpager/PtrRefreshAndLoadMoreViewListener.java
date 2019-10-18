package com.example.dongao.mydemoapp.widget.ptrviewpager;

public interface PtrRefreshAndLoadMoreViewListener {
    void pull(float percent);
    void needRelease();
    void loading();
    void finish();
    void end();
    void error();
}
