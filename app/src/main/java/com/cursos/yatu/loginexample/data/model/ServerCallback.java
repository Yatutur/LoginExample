package com.cursos.yatu.loginexample.data.model;

public interface ServerCallback {
    void onSuccess(LoggedInUser result);
    void onError();
}
