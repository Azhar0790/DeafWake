package com.deafwake.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;


import com.deafwake.application.MyApp;
import com.deafwake.utils.ExceptionHandler;
import com.deafwake.utils.TouchEffect;

import org.json.JSONObject;




/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
public class CustomActivity extends AppCompatActivity implements
        OnClickListener {
    private ResponseCallback responseCallback;
    /**
     * Apply this Constant as touch listener for views to provide alpha touch
     * effect. The view must have a Non-Transparent background.
     */
    public static final TouchEffect TOUCH = new TouchEffect();
    protected OnBackPressedListener onBackPressedListener;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setupActionBar();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow()
                    .addFlags(
                            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // getWindow().setStatusBarColor(getResources().getColor(R.color.main_color_dk));
        }
    }

    public void setResponseListener(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will setup the top title bar (Action bar) content and display
     * values. It will also setup the custom background theme for ActionBar. You
     * can override this method to change the behavior of ActionBar for
     * particular Activity
     */
    protected void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowTitleEnabled(true);
        // actionBar.setDisplayUseLogoEnabled(true);
        // actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        actionBar.setTitle(null);

    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */


    /**
     * Sets the touch and click listeners for a view..
     *
     * @param id the id of View
     * @return the view
     */
    public View setTouchNClick(int id) {

        View v = setClick(id);
        v.setOnTouchListener(TOUCH);
        return v;
    }

    public View setTouchNClick(View v) {
        setClick(v);
        v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view.
     *
     * @param id the id of View
     * @return the view
     */
    public View setClick(int id) {

        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    public View setClick(View v) {


        v.setOnClickListener(this);
        return v;
    }

    public void setOnBackPressedListener(
            OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

    }


    public interface OnBackPressedListener {
        void doBack();
    }

/*
    public void postCallNamedValue(Context c, String url, RequestParams params, String loadingMsg, final int callNumber) {
        if (!TextUtils.isEmpty(loadingMsg))
            MyApp.spinnerStart(c, loadingMsg);
        Log.d("URl:", url);
        Log.d("Request:", params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                MyApp.spinnerStop();
                Log.d("error message:", throwable.getMessage());
                if (statusCode == 0)
                    responseCallback.onErrorReceived(getString(R.string.timeout));
                else
                    responseCallback.onErrorReceived(getString(R.string.something_wrong) + "_" + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                MyApp.spinnerStop();
                try {
                    String response = responseString.substring(responseString.indexOf("{"), responseString.length() - 9);
                    Log.d("Response:", response);
                    responseCallback.onStringResponseReceived(response, callNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void postCallJsonObject(Context c, String url, JSONObject params, String loadingMsg, final int callNumber) {
        if (!TextUtils.isEmpty(loadingMsg))
            MyApp.spinnerStart(c, loadingMsg);
        Log.d("URl:", url);
        Log.d("Request:", params.toString());
        StringEntity entity = null;
        try {
            entity = new StringEntity(params.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.post(c, url, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                MyApp.spinnerStop();
//                responseCallback.onJsonObjectResponseReceived(response, callNumber);
                Log.d("Response:", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                MyApp.spinnerStop();

                Log.d("error message:", throwable.getMessage());
                if (statusCode == 0)
                    responseCallback.onErrorReceived(getString(R.string.timeout));
                else
                    responseCallback.onErrorReceived(getString(R.string.something_wrong) + "_" + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                MyApp.spinnerStop();
                Log.d("error message:", throwable.getMessage());
                responseCallback.onErrorReceived(getString(R.string.something_wrong) + "_" + statusCode);
            }
        });
    }

    public void postCall(Context c, String url, String loadingMsg, final int callNumber) {
        if (!TextUtils.isEmpty(loadingMsg))
            MyApp.spinnerStart(c, loadingMsg);
        Log.d("URl:", url);
        Log.d("Request:", "no Params");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.post(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                MyApp.spinnerStop();
//                object = new JSONObject(response.substring(response.indexOf("{"), response.length() - 9));
//                responseCallback.onJsonObjectResponseReceived(response, callNumber);
                Log.d("Response:", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                MyApp.spinnerStop();
                Log.d("error message:", throwable.getMessage());
                responseCallback.onErrorReceived(getString(R.string.something_wrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                MyApp.spinnerStop();
                Log.d("error message:", throwable.getMessage());
                responseCallback.onErrorReceived(getString(R.string.something_wrong));
            }
        });
    }
*/

    public interface ResponseCallback {
        //        void onJsonObjectResponseReceived(JSONObject response, int callNumber);
        void onStringResponseReceived(String response, int callNumber);

        //        void onJsonArrayResponseReceived(JSONArray response, int callNumber);
        void onErrorReceived(String error);

    }

}
