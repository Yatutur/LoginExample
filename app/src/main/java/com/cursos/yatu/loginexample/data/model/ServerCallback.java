package com.cursos.yatu.loginexample.data.model;

import com.cursos.yatu.loginexample.data.Result;

public interface ServerCallback {
    void onSuccess(LoggedInUser result);
    void onError();
}
