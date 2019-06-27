package com.cursos.yatu.loginexample.data.model;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cursos.yatu.loginexample.R;
import com.cursos.yatu.loginexample.ui.login.LoginActivity;

/*
    Author: Yatu
    Time: 2:06 PM
    Date: 6/17/2019
*/public class Connecting {
    public static void onErrorResponseGeneral (VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.error_network_timeout,
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.error_no_connection,
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.authentification_error,
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.server_error,
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.network_error,
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(LoginActivity.getContext(),
                    R.string.parse_error,
                    Toast.LENGTH_LONG).show();
        }
        error.printStackTrace();
    }
}
