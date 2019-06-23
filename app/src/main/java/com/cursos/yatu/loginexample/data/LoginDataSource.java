package com.cursos.yatu.loginexample.data;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cursos.yatu.loginexample.R;
import com.cursos.yatu.loginexample.data.model.Connecting;
import com.cursos.yatu.loginexample.data.model.LoggedInUser;
import com.cursos.yatu.loginexample.data.model.ServerCallback;
import com.cursos.yatu.loginexample.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends AppCompatActivity {

    private static final String SERVER_URL =
            "https://yatuandroidserver.ddns.net:4546/login_example/userLogin.php";

    JSONObject jsonObjectResponse = null;
    boolean userPassIncorrect = false;
    LoggedInUser userLogin = null;
    private final String TAG = "LoginDataSource";

    public void login(final String email, final String password,
                                      ServerCallback serverCallback) {

        Log.d (TAG, "Comienza login");
        callServer (email, password, serverCallback);

    }

    private void callServer(final String email, final String password,
                            final ServerCallback serverCallback) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SERVER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d (TAG, "Entra en onResponse");
                if (response.indexOf("Wrong email or password") == -1) {
                    try {
                        Log.d (TAG, getString(R.string.correct_password));
                        Log.d (TAG, "Response: " + response);
                        jsonObjectResponse = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        userLogin = new LoggedInUser(
                                jsonObjectResponse.getString("id"),
                                jsonObjectResponse.getString("firstname"),
                                jsonObjectResponse.getString("lastname"),
                                jsonObjectResponse.getString("birthday"),
                                jsonObjectResponse.getString("email"),
                                jsonObjectResponse.getString("password"),
                                jsonObjectResponse.getString("address"),
                                jsonObjectResponse.getString("notas"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d (TAG, "Antes del callback");
                    serverCallback.onSuccess(userLogin);

                } else {
                    Log.d (TAG, "Usuario y contraseña incorrecta");
                    Log.d (TAG , "Server response: " + response);
                    userPassIncorrect = true;
                    Toast.makeText(LoginActivity.getContext(),
                            "Server response: " + response,
                            Toast.LENGTH_LONG).show();
                    serverCallback.onError();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e (TAG, "Error en respuesta del servidor: " +
                        error.getMessage());
                Connecting.onErrorResponseGeneral(error);
                serverCallback.onError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(LoginActivity.getContext()).addToRequestQueue(stringRequest);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
