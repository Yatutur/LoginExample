package com.cursos.yatu.loginexample.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cursos.yatu.loginexample.R;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by For on 4/24/2017.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;
    private static final String TAG = "MySingleton";

    private MySingleton (Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance (Context context)
    {
        if (mInstance==null)
        {
            mInstance =new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {

        if (requestQueue==null)
        {
            HttpsURLConnection.setDefaultSSLSocketFactory(buildSslSocketFactory(mCtx));
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(),
                    new HurlStack(null, buildSslSocketFactory(mCtx)));
        }
        return requestQueue;

    }

    private static SSLSocketFactory buildSslSocketFactory(Context context) {
        try {
            InputStream inStream = mCtx.getApplicationContext().getResources().openRawResource(R.raw.server_yatu);

            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(inStream,
                    mCtx.getApplicationContext().getResources().getString(R.string.keystore_yatu_password).toCharArray());
            inStream.close();

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(ks, "dw22DW22=".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//            hurlStack = new HurlStack(null, sslSocketFactory);
            return sslSocketFactory;
        } catch (Exception e){
            Log.d("Exception:",e.toString());
        }
        return null;

    }

    public<T> void addToRequestQueue(StringRequest request)
    {
        requestQueue.add(request);
    }
}