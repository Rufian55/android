package com.blitspost.quotetrack;
/* *************************************************************************************************
*
*
*
*
*
*
* CITATIONS:
* [1] https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-
*           android-application
* [2] https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
* [3] Documentation at https://square.github.io/okhttp/3.x/okhttp/okhttp3/Response.html, et. al.
* [4] https://stackoverflow.com/questions/9884246/how-to-set-text-of-text-view-in-another-thread
***************************************************************************************************/
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.security.MessageDigest;

public class QuoteTrack extends AppCompatActivity {
    // Constants and variable declarations.
    private OkHttpClient mOkHttpClient;
    String[] tickers = new String[10];
    String[] tickers2 = new String[10];
    String[] tickers3 = new String[10];
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotetrack);
        String e_mail = getIntent().getStringExtra("E_MAIL");
        //String token = getSha256(e_mail);
        //getAccountData(token);
        getAccountData(e_mail);
        //getIndexData();
        //getTickerData();

    }

    // Returns a SHA256 hash of the users email string that was used on G+ sign in. [2]
    private static String getSha256(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    // Helper function for getSha256()
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }


    public void getAccountData(final String token){
        //token = "traveler-403@msn.com";
        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://quotetrack-169521.appspot.com/User/" + token);
        Request request = new Request.Builder().url(reqUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Show user error message if not connected to internet, et. al.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.Toast_1);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (responseBody.equals("\"No such account...\"")) {
                    Log.d("Whoops1", responseBody);
                    createAccount(token);
                    populateGrid();
                    // Toast...?
                }
                else { // Get user's ticker array.
                    try {
                        //Log.d("Whoops2", responseBody);
                        JSONArray json_array = new JSONArray(responseBody);
                        JSONObject json_object = json_array.getJSONObject(0);
                        for (int i = 0; i < 10; i++) {
                           String temp = "ticker" + Integer.toString(i);
                            tickers[i] = json_object.getString(temp);
                       }
                       populateGrid();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void populateGrid() {
        // Populate ticker0 TextViews.
        final TextView ticker_0 = (TextView) findViewById(R.id.textView3A);
        ticker_0.post(new Runnable() {
            @Override
            public void run() {
                ticker_0.setText(tickers[0]);
            }
        });
        // Populate ticker1 TextViews.
        final TextView ticker_1 = (TextView) findViewById(R.id.textView4A);
        ticker_1.post(new Runnable() {
            @Override
            public void run() {
                ticker_1.setText(tickers[1]);
            }
        });
        // Populate ticker2 TextViews.
        final TextView ticker_2 = (TextView) findViewById(R.id.textView5A);
        ticker_2.post(new Runnable() {
            @Override
            public void run() {
                ticker_2.setText(tickers[2]);
            }
        });
        // Populate ticker3 TextViews.
        final TextView ticker_3 = (TextView) findViewById(R.id.textView6A);
        ticker_3.post(new Runnable() {
            @Override
            public void run() {
                ticker_3.setText(tickers[3]);
            }
        });
        // Populate ticker4 TextViews.
        final TextView ticker_4 = (TextView) findViewById(R.id.textView7A);
        ticker_4.post(new Runnable() {
            @Override
            public void run() {
                ticker_4.setText(tickers[4]);
            }
        });
        // Populate ticker5 TextViews.
        final TextView ticker_5 = (TextView) findViewById(R.id.textView8A);
        ticker_5.post(new Runnable() {
            @Override
            public void run() {
                ticker_5.setText(tickers[5]);
            }
        });
        // Populate ticker6 TextViews.
        final TextView ticker_6 = (TextView) findViewById(R.id.textView9A);
        ticker_6.post(new Runnable() {
            @Override
            public void run() {
                ticker_6.setText(tickers[6]);
            }
        });
        // Populate ticker7 TextViews.
        final TextView ticker_7 = (TextView) findViewById(R.id.textView10A);
        ticker_7.post(new Runnable() {
            @Override
            public void run() {
                ticker_7.setText(tickers[7]);
            }
        });
        // Populate ticker8 TextViews.
        final TextView ticker_8 = (TextView) findViewById(R.id.textView11A);
        ticker_8.post(new Runnable() {
            @Override
            public void run() {
                ticker_8.setText(tickers[8]);
            }
        });
        // Populate ticker9 TextViews.
        final TextView ticker_9 = (TextView) findViewById(R.id.textView12A);
        ticker_9.post(new Runnable() {
            @Override
            public void run() {
                ticker_9.setText(tickers[9]);
            }
        });
    }

    // Post request for new account setup.
    public void createAccount(final String token){
        //token = "traveler-403@msn.com";
        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://quotetrack-169521.appspot.com/User");
        // Retrieve user defined string for building post body.  See function get_json().
        String someJson = get_json(token);
        Log.d("json_string=", someJson);// RETURNING NULL??????????????????????
        RequestBody body = RequestBody.create(JSON, someJson);

        final Request request = new Request.Builder()
                .url(reqUrl)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Show user error message if not connected to internet, et. al.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getApplicationContext();
                        CharSequence text = getResources().getString(R.string.Toast_1);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (responseBody.equals("\"dupe\"")) {
                    getAccountData(token);
                    populateGrid();
                }
                else { // Get user's ticker array.
                    try {
                        Log.d("Whoops3", responseBody);
                        JSONObject json_object = new JSONObject(responseBody);
                        for (int i = 0; i < 10; i++) {
                            String temp = "ticker" + Integer.toString(i);
                            tickers[i] = json_object.getString(temp);
                        }
                        getAccountData(token);
                        populateGrid();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public String get_json(String token) {
        //Arrays.fill(tickers, null);
        getTickersFromUser(tickers2);

        //ANY ATTEMPT TO LOG tickers here gives printing Null string Exception.
        //Program does not wait for getTickersFromUser() to complete.
        // Hardcoded value "OK" is sent and is in account on Google NDB.
        /*
        TextView temp = (TextView) findViewById(R.id.textView3A);
        tickers2[0] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView4A);
        tickers2[1] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView5A);
        tickers2[2] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView6A);
        tickers2[3] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView7A);
        tickers2[4] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView8A);
        tickers2[5] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView9A);
        tickers2[6] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView10A);
        tickers2[7] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView11A);
        tickers2[8] = temp.getText().toString();
        temp = (TextView) findViewById(R.id.textView12A);
        tickers2[9] = temp.getText().toString();
        */

        // Build JSON object.
        String JSONstring = "{" +
                "\"email\": \"" + token + "\"," +
                "\"ticker0\": \"" + "OK" + "\"," +
                "\"ticker1\": \"" + tickers2[1] + "\"," +
                "\"ticker2\": \"" + tickers2[2] + "\"," +
                "\"ticker3\": \"" + tickers2[3] + "\"," +
                "\"ticker4\": \"" + tickers2[4] + "\"," +
                "\"ticker5\": \"" + tickers2[5] + "\"," +
                "\"ticker6\": \"" + tickers2[6] + "\"," +
                "\"ticker7\": \"" + tickers2[7] + "\"," +
                "\"ticker8\": \"" + tickers2[8] + "\"," +
                "\"ticker9\": \"" + tickers2[9] + "\"" +
                "}";
        return JSONstring;
    }

    public int getTickersFromUser(String[] tickers){
        Intent intent = new Intent(this, EditTickers.class);
        intent.putExtra("strings", tickers);
        startActivityForResult(intent, REQUEST_CODE);
        return 1;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE) && (resultCode == RESULT_OK)){
            tickers3 = data.getStringArrayExtra("strings");

            for (int i = 0; i < 10; i++) {
                tickers2[i] = tickers3[i];
            }


            // Logs updated tickers - USER ENTERED TICKERS ARE ALL THERE!!!???
            for(int i =0; i < 10; i++){
                Log.d("QuoteTrack_1:null?", tickers[i]);
            }

            /*
            TextView temp = (TextView) findViewById(R.id.textView3A);
            temp.setText(tickers[0]);
            temp = (TextView) findViewById(R.id.textView4A);
            temp.setText(tickers[1]);
            temp = (TextView) findViewById(R.id.textView5A);
            temp.setText(tickers[2]);
            temp = (TextView) findViewById(R.id.textView6A);
            temp.setText(tickers[3]);
            temp = (TextView) findViewById(R.id.textView7A);
            temp.setText(tickers[4]);
            temp = (TextView) findViewById(R.id.textView8A);
            temp.setText(tickers[5]);
            temp = (TextView) findViewById(R.id.textView9A);
            temp.setText(tickers[6]);
            temp = (TextView) findViewById(R.id.textView10A);
            temp.setText(tickers[7]);
            temp = (TextView) findViewById(R.id.textView11A);
            temp.setText(tickers[8]);
            temp = (TextView) findViewById(R.id.textView12A);
            temp.setText(tickers[9]);
            */
        }
    }

}
