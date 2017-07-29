package com.example.savi.rssfeedaplication;

import java.util.List;

public interface OnOperationCompleteListener {
    void onOperationComplete(List<Article> articleList);

    void onOperationFailed();

}
