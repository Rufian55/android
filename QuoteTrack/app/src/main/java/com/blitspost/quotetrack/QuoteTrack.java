package com.blitspost.quotetrack;
/* *************************************************************************************************
* Chris Kearns, kearnsc@oregonstate.edu, CS-496-400-S17, Final Project. 11 June 2017
* QuotTrack,java represents the main logic file for the QuotTrack app.
* Video demo: https://media.oregonstate.edu/media/t/1_gmvia08k
* QuoteTrack API: https://quotetrack-169521.appspot.com/
* AlphaAdvantage API: http://www.alphavantage.co/documentation/#dailyadj
*
* CITATIONS:
* [1] Lecture and materials from Prof. Justin Wolford, Oregon State University, Spring 2017
* [2] https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-
*           android-application
* [3] Documentation at https://square.github.io/okhttp/3.x/okhttp/okhttp3/Response.html, et. al.
* [4] https://stackoverflow.com/questions/9884246/how-to-set-text-of-text-view-in-another-thread
* [5] https://stackoverflow.com/questions/5030565/multiple-onactivityresult-for-1-activity
* [6] https://stackoverflow.com/questions/44445951/android-studio-extracting-data-from-json-object-not-working
* [7] http://www.java2s.com/Code/Java/Data-Type/Findtextbetweentwostrings.htm
* [8] https://www.myandroidsolutions.com/2014/04/06/run-code-on-mainui-thread-on-android/#.WTwZAmgrJhE
* [9] https://stackoverflow.com/questions/4602902/how-to-set-the-text-color-of-textview-in-code
* [10] https://developer.android.com/reference/android/graphics/Color.html
***************************************************************************************************/
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuoteTrack extends AppCompatActivity {
    // Constants and variable declarations.
    private OkHttpClient mOkHttpClient;
    String[] tickers = new String[10];
    String[] indices = new String[3];
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int REQUEST_CODE_1 = 1;
    public static final int REQUEST_CODE_2 = 2;
    public static int hasAccount = 1;
    public static String apiKey = "S08L";
    final String[] data = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotetrack);
        //Initialize tickers[] array.
        for (int i = 0; i < 10; i++){
            tickers[i] = "...";
        }

        // Initialize indices array.
        indices[0] = "^DJI";
        indices[1] = "^GSPC";
        indices[2] = "^IXIC";

        final String e_mail = getIntent().getStringExtra("E_MAIL");
        getAccountData(e_mail);

        (findViewById(R.id.btnNewAcct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                getUserTickers();
            }
        });

        (findViewById(R.id.btnDeleteAcct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                deleteAccount(e_mail);
            }
        });

        (findViewById(R.id.btnPatchAcct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                editUserTickers();
            }
        });

    }

    /* Retrieves user tickers from  QuoteTrack API and populates tickers array.
       Shows user current tickers array.  Sets hasAccount if no user account found. */
    public void getAccountData(final String token){
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
                    hasAccount = 0;
                    updateUI(hasAccount);
                    populateGrid();
                }
                else { // Get user's ticker array.
                    try {
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

    /* Populates grid layout with user tickers and ticker data from AlphaVantage API.
       Calls AlphaVantage API with parameters for indices and tickers. */
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

        /* Retrieves "3" indices data and passes "0" for control that
           differentiates indices vs. tickers parameter.  Ultimately,
           it is parameter "theCase" in showMapData(). */
        getTickerData(indices, 3, 0);
        /* Retrieves "10" tickers data and passes "1" for control (theCase) */
        getTickerData(tickers, 10, 1);

    }

    // Intent for create account functionality.
    public void getUserTickers(){
        Intent intent = new Intent(this, GetUserTickers.class);
        startActivityForResult(intent, REQUEST_CODE_1);
    }

    /* Intent for editing user tickers. tickers array passed in order to
       prepopulate editText input fields for user. */
    public void editUserTickers(){
        Intent intent = new Intent(this, EditUserTickers.class);
        intent.putExtra("strings", tickers);
        startActivityForResult(intent, REQUEST_CODE_2);
    }

    /* Routing for return from edit and create user account activities. */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_1) && (resultCode == RESULT_OK)){
            tickers = data.getStringArrayExtra("strings");
            updateUI(1);
            final String e_mail = getIntent().getStringExtra("E_MAIL");
            String jsonString = getJson(tickers, e_mail);
            if(hasAccount == 0){
                createAccount(jsonString);
                hasAccount = 1;
                populateGrid();
            }
            else{
                populateGrid();
            }
        } else if((requestCode == REQUEST_CODE_2) && (resultCode == RESULT_OK)){
            tickers = data.getStringArrayExtra("strings");
            updateUI(1);
            final String e_mail = getIntent().getStringExtra("E_MAIL");
            editAccount(e_mail, tickers);
            populateGrid();
        }
    }

    // Controls the display of buttons depending on if the user has an account or not.
    private void updateUI(final int hasAcct) {
        final Button btnNewAcct = (Button) findViewById(R.id.btnNewAcct);
        final Button btnDeleteAcct = (Button) findViewById(R.id.btnDeleteAcct);
        final Button btnEditAcct = (Button) findViewById(R.id.btnPatchAcct);
        btnNewAcct.post(new Runnable() {
            @Override
            public void run() {
                if (hasAcct == 1) {
                    btnNewAcct.setVisibility(View.GONE);
                    btnDeleteAcct.setVisibility(View.VISIBLE);
                    btnEditAcct.setVisibility(View.VISIBLE);
                } else {
                    btnNewAcct.setVisibility(View.VISIBLE);
                    btnDeleteAcct.setVisibility(View.GONE);
                    btnEditAcct.setVisibility(View.GONE);
                }
            }
        });
    }

    // Build and return JSON object from tickers array and token(currently user email string).
    public String getJson(String [] tickers, String token){
        String jsonString = "{" +
                "\"email\": \"" + token + "\"," +
                "\"ticker0\": \"" + tickers[0] + "\"," +
                "\"ticker1\": \"" + tickers[1] + "\"," +
                "\"ticker2\": \"" + tickers[2] + "\"," +
                "\"ticker3\": \"" + tickers[3] + "\"," +
                "\"ticker4\": \"" + tickers[4] + "\"," +
                "\"ticker5\": \"" + tickers[5] + "\"," +
                "\"ticker6\": \"" + tickers[6] + "\"," +
                "\"ticker7\": \"" + tickers[7] + "\"," +
                "\"ticker8\": \"" + tickers[8] + "\"," +
                "\"ticker9\": \"" + tickers[9] + "\"" +
                "}";
        return jsonString;
    }


    /* Executes user's new tickers array at QuoteTrack API from getUserTickers() intent. */
    public void createAccount(final String jsonString) {
        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://quotetrack-169521.appspot.com/User");
        RequestBody body = RequestBody.create(JSON, jsonString);
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
                    final String e_mail = getIntent().getStringExtra("E_MAIL");
                    getAccountData(e_mail);
                    populateGrid();
                } else { // Get user's ticker array.
                    try {
                        JSONObject json_object = new JSONObject(responseBody);
                        for (int i = 0; i < 10; i++) {
                            String temp = "ticker" + Integer.toString(i);
                            tickers[i] = json_object.getString(temp);
                        }
                        final String e_mail = getIntent().getStringExtra("E_MAIL");
                        getAccountData(e_mail);
                        populateGrid();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Delete call to QuoteTrack API when user clicks Delete Account button. No recourse!
    public void deleteAccount(String e_mail){
        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://quotetrack-169521.appspot.com/User/" + e_mail);
        final Request request = new Request.Builder()
                .url(reqUrl)
                .addHeader("Content-Type", "application/json")
                .delete()
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
                if (responseBody.equals("\"Account deleted!\"")) {
                    finish();
                } else {
                    Log.d("Error on delete: ", responseBody);
                }
            }
        });
    }

    /* Executes user's edited tickers array at QutoeTrack API from editUserTickers() intent. */
    public void editAccount(String e_mail, final String [] tickers){
        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://quotetrack-169521.appspot.com/User/" + e_mail);
        // Retrieve user defined string for building post body.  See function get_json().
        String jsonString = getJson(tickers, e_mail);
        RequestBody body = RequestBody.create(JSON, jsonString);
        final Request request = new Request.Builder()
                .url(reqUrl)
                .addHeader("Content-Type", "application/json")
                .patch(body)
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
                if (responseBody.equals("\"No such account...\"")) {
                    final String e_mail = getIntent().getStringExtra("E_MAIL");
                    getAccountData(e_mail);
                    //populateGrid();
                } else { // Get user's ticker array.
                    try {
                        JSONObject json_object = new JSONObject(responseBody);
                        for (int i = 0; i < 10; i++) {
                            String temp = "ticker" + Integer.toString(i);
                            tickers[i] = json_object.getString(temp);
                        }
                        final String e_mail = getIntent().getStringExtra("E_MAIL");
                        getAccountData(e_mail);
                        //populateGrid();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* Retrieves JSON formatted ticker data from AlphaVantage API, extracts data points, and
       populates user's data array for mapping to grid layout. Parameter theCase is pass through! */
    public void getTickerData(final String[] indices, int length, final int theCase){
        for (int i = 0; i < length; i++){
            final int Eye = i;
            Arrays.fill(data, "");
            mOkHttpClient = new OkHttpClient();
            HttpUrl reqUrl = HttpUrl.parse("http://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" +
                    indices[i] +
                    "&outputsize=compact&apikey=" +
                    apiKey);
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
                    int j = 0;
                    String responseBody = response.body().string();
                    if (responseBody.contains("\"Error Message\"")) {
                        data[j] = "No Data";
                        data[j+1] = "No Data";
                        data[j+2] = "No Data";
                        data[j+3] = "No Data";
                    } else { // Extract data points from json object.
                        try {
                            JSONObject baseObject = new JSONObject(responseBody);
                            JSONObject timeSeriesObj = baseObject.optJSONObject("Time Series (Daily)");
                            Iterator<String> iterator = timeSeriesObj.keys();
                            List<Map<String, String>> tickerData = new ArrayList<Map<String, String>>();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                if (key != null) {
                                    HashMap<String, String> m = new HashMap<String, String>();
                                    JSONObject finalObj = timeSeriesObj.optJSONObject(key);
                                    m.put("1. open", finalObj.optString("1. open"));
                                    m.put("2. high", finalObj.optString("2. high"));
                                    m.put("3. low", finalObj.optString("3. low"));
                                    m.put("4. close", finalObj.optString("4. close"));
                                    m.put("5. volume", finalObj.optString("5. volume"));
                                    tickerData.add(m);
                                }
                            }
                            int k = 0;
                            String str = tickerData.get(0).toString();
                            data[k] = StringUtils.substringBetween(str, "open=", ", ");
                            data[k+1] = StringUtils.substringBetween(str, "close=", ", ");
                            float tempOpen = Float.parseFloat(data[0]);
                            float tempClose = Float.parseFloat(data[1]);
                            float tempPoints = tempClose - tempOpen;
                            float tempPercent = tempPoints / tempOpen * 100;

                            data[k] = String.format("%.2f", tempOpen);              // data[0] = open.
                            data[k+1] = String.format("%.2f", tempClose);           // data[1] = close
                            data[k+2] = String.format("%.2f", tempPoints);          // data[2] = points
                            data[k+3] = String.format("%.2f", tempPercent) + "%";   // data[3] = % change

                            showMapData(data, Eye, theCase);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /*  Maps data array contents to directed location.
     *  int i is the row number in the grid layout.
     *  theCase is 0/1 depending on if data is for indices or tickers. */
    public void showMapData(final String[] data, int i, int theCase) {
        final String isNegative = Character.toString(data[2].charAt(0));
        final String test = "-";
        if(theCase == 1){ i+=3; }

        switch (i) {
            case 0:
                final TextView row_0_col_0 = (TextView) findViewById(R.id.textView0B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_0_col_0.setTextColor(Color.RED);
                        } else {
                            row_0_col_0.setTextColor(0xff288132);
                        }
                        row_0_col_0.setText(data[1]);
                    }
                });
                final TextView row_0_col_1 = (TextView) findViewById(R.id.textView0C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_0_col_1.setTextColor(Color.RED);
                        } else {
                            row_0_col_1.setTextColor(0xff288132);
                        }
                        row_0_col_1.setText(data[2]);
                    }
                });
                final TextView row_0_col_2 = (TextView) findViewById(R.id.textView0D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_0_col_2.setTextColor(Color.RED);
                        } else {
                            row_0_col_2.setTextColor(0xff288132);
                        }
                        row_0_col_2.setText(data[3]);
                    }
                });
                break;

            case 1:
                final TextView row_1_col_0 = (TextView) findViewById(R.id.textView1B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        row_1_col_0.setText(data[1]);
                        if(isNegative.equals(test)){
                            row_1_col_0.setTextColor(Color.RED);
                        } else {
                            row_1_col_0.setTextColor(0xff288132);
                        }
                    }
                });
                final TextView row_1_col_1 = (TextView) findViewById(R.id.textView1C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_1_col_1.setTextColor(Color.RED);
                        } else {
                            row_1_col_1.setTextColor(0xff288132);
                        }
                        row_1_col_1.setText(data[2]);
                    }
                });
                final TextView row_1_col_2 = (TextView) findViewById(R.id.textView1D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_1_col_2.setTextColor(Color.RED);
                        } else {
                            row_1_col_2.setTextColor(0xff288132);
                        }
                        row_1_col_2.setText(data[3]);
                    }
                });
                break;

            case 2:
                final TextView row_2_col_0 = (TextView) findViewById(R.id.textView2B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_2_col_0.setTextColor(Color.RED);
                        } else {
                            row_2_col_0.setTextColor(0xff288132);
                        }
                        row_2_col_0.setText(data[1]);
                    }
                });
                final TextView row_2_col_1 = (TextView) findViewById(R.id.textView2C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_2_col_1.setTextColor(Color.RED);
                        } else {
                            row_2_col_1.setTextColor(0xff288132);
                        }
                        row_2_col_1.setText(data[2]);
                    }
                });
                final TextView row_2_col_2 = (TextView) findViewById(R.id.textView2D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_2_col_2.setTextColor(Color.RED);
                        } else {
                            row_2_col_2.setTextColor(0xff288132);
                        }
                        row_2_col_2.setText(data[3]);
                    }
                });
                break;

            case 3:
                final TextView row_3_col_0 = (TextView) findViewById(R.id.textView3B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_3_col_0.setTextColor(Color.RED);
                        } else {
                            row_3_col_0.setTextColor(0xff288132);
                        }
                        row_3_col_0.setText(data[1]);
                    }
                });
                final TextView row_3_col_1 = (TextView) findViewById(R.id.textView3C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_3_col_1.setTextColor(Color.RED);
                        } else {
                            row_3_col_1.setTextColor(0xff288132);
                        }
                        row_3_col_1.setText(data[2]);
                    }
                });
                final TextView row_3_col_2 = (TextView) findViewById(R.id.textView3D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_3_col_2.setTextColor(Color.RED);
                        } else {
                            row_3_col_2.setTextColor(0xff288132);
                        }
                        row_3_col_2.setText(data[3]);
                    }
                });
                break;

            case 4:
                final TextView row_4_col_0 = (TextView) findViewById(R.id.textView4B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_4_col_0.setTextColor(Color.RED);
                        } else {
                            row_4_col_0.setTextColor(0xff288132);
                        }
                        row_4_col_0.setText(data[1]);
                    }
                });
                final TextView row_4_col_1 = (TextView) findViewById(R.id.textView4C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_4_col_1.setTextColor(Color.RED);
                        } else {
                            row_4_col_1.setTextColor(0xff288132);
                        }
                        row_4_col_1.setText(data[2]);
                    }
                });
                final TextView row_4_col_2 = (TextView) findViewById(R.id.textView4D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_4_col_2.setTextColor(Color.RED);
                        } else {
                            row_4_col_2.setTextColor(0xff288132);
                        }
                        row_4_col_2.setText(data[3]);
                    }
                });
                break;

            case 5:
                final TextView row_5_col_0 = (TextView) findViewById(R.id.textView5B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_5_col_0.setTextColor(Color.RED);
                        } else {
                            row_5_col_0.setTextColor(0xff288132);
                        }
                        row_5_col_0.setText(data[1]);
                    }
                });
                final TextView row_5_col_1 = (TextView) findViewById(R.id.textView5C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_5_col_1.setTextColor(Color.RED);
                        } else {
                            row_5_col_1.setTextColor(0xff288132);
                        }
                        row_5_col_1.setText(data[2]);
                    }
                });
                final TextView row_5_col_2 = (TextView) findViewById(R.id.textView5D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_5_col_2.setTextColor(Color.RED);
                        } else {
                            row_5_col_2.setTextColor(0xff288132);
                        }
                        row_5_col_2.setText(data[3]);
                    }
                });
                break;

            case 6:
                final TextView row_6_col_0 = (TextView) findViewById(R.id.textView6B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_6_col_0.setTextColor(Color.RED);
                        } else {
                            row_6_col_0.setTextColor(0xff288132);
                        }
                        row_6_col_0.setText(data[1]);
                    }
                });
                final TextView row_6_col_1 = (TextView) findViewById(R.id.textView6C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_6_col_1.setTextColor(Color.RED);
                        } else {
                            row_6_col_1.setTextColor(0xff288132);
                        }
                        row_6_col_1.setText(data[2]);
                    }
                });
                final TextView row_6_col_2 = (TextView) findViewById(R.id.textView6D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_6_col_2.setTextColor(Color.RED);
                        } else {
                            row_6_col_2.setTextColor(0xff288132);
                        }
                        row_6_col_2.setText(data[3]);
                    }
                });
                break;

            case 7:
                final TextView row_7_col_0 = (TextView) findViewById(R.id.textView7B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_7_col_0.setTextColor(Color.RED);
                        } else {
                            row_7_col_0.setTextColor(0xff288132);
                        }
                        row_7_col_0.setText(data[1]);
                    }
                });
                final TextView row_7_col_1 = (TextView) findViewById(R.id.textView7C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_7_col_1.setTextColor(Color.RED);
                        } else {
                            row_7_col_1.setTextColor(0xff288132);
                        }
                        row_7_col_1.setText(data[2]);
                    }
                });
                final TextView row_7_col_2 = (TextView) findViewById(R.id.textView7D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_7_col_2.setTextColor(Color.RED);
                        } else {
                            row_7_col_2.setTextColor(0xff288132);
                        }
                        row_7_col_2.setText(data[3]);
                    }
                });
                break;

            case 8:
                final TextView row_8_col_0 = (TextView) findViewById(R.id.textView8B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_8_col_0.setTextColor(Color.RED);
                        } else {
                            row_8_col_0.setTextColor(0xff288132);
                        }
                        row_8_col_0.setText(data[1]);
                    }
                });
                final TextView row_8_col_1 = (TextView) findViewById(R.id.textView8C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_8_col_1.setTextColor(Color.RED);
                        } else {
                            row_8_col_1.setTextColor(0xff288132);
                        }
                        row_8_col_1.setText(data[2]);
                    }
                });
                final TextView row_8_col_2 = (TextView) findViewById(R.id.textView8D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_8_col_2.setTextColor(Color.RED);
                        } else {
                            row_8_col_2.setTextColor(0xff288132);
                        }
                        row_8_col_2.setText(data[3]);
                    }
                });
                break;

            case 9:
                final TextView row_9_col_0 = (TextView) findViewById(R.id.textView9B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_9_col_0.setTextColor(Color.RED);
                        } else {
                            row_9_col_0.setTextColor(0xff288132);
                        }
                        row_9_col_0.setText(data[1]);
                    }
                });
                final TextView row_9_col_1 = (TextView) findViewById(R.id.textView9C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_9_col_1.setTextColor(Color.RED);
                        } else {
                            row_9_col_1.setTextColor(0xff288132);
                        }
                        row_9_col_1.setText(data[2]);
                    }
                });
                final TextView row_9_col_2 = (TextView) findViewById(R.id.textView9D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_9_col_2.setTextColor(Color.RED);
                        } else {
                            row_9_col_2.setTextColor(0xff288132);
                        }
                        row_9_col_2.setText(data[3]);
                    }
                });
                break;

            case 10:
                final TextView row_10_col_0 = (TextView) findViewById(R.id.textView10B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_10_col_0.setTextColor(Color.RED);
                        } else {
                            row_10_col_0.setTextColor(0xff288132);
                        }
                        row_10_col_0.setText(data[1]);
                    }
                });
                final TextView row_10_col_1 = (TextView) findViewById(R.id.textView10C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_10_col_1.setTextColor(Color.RED);
                        } else {
                            row_10_col_1.setTextColor(0xff288132);
                        }
                        row_10_col_1.setText(data[2]);
                    }
                });
                final TextView row_10_col_2 = (TextView) findViewById(R.id.textView10D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_10_col_2.setTextColor(Color.RED);
                        } else {
                            row_10_col_2.setTextColor(0xff288132);
                        }
                        row_10_col_2.setText(data[3]);
                    }
                });
                break;

            case 11:
                final TextView row_11_col_0 = (TextView) findViewById(R.id.textView11B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_11_col_0.setTextColor(Color.RED);
                        } else {
                            row_11_col_0.setTextColor(0xff288132);
                        }
                        row_11_col_0.setText(data[1]);
                    }
                });
                final TextView row_11_col_1 = (TextView) findViewById(R.id.textView11C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_11_col_1.setTextColor(Color.RED);
                        } else {
                            row_11_col_1.setTextColor(0xff288132);
                        }
                        row_11_col_1.setText(data[2]);
                    }
                });
                final TextView row_11_col_2 = (TextView) findViewById(R.id.textView11D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_11_col_2.setTextColor(Color.RED);
                        } else {
                            row_11_col_2.setTextColor(0xff288132);
                        }
                        row_11_col_2.setText(data[3]);
                    }
                });
                break;

            case 12:
                final TextView row_12_col_0 = (TextView) findViewById(R.id.textView12B);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_12_col_0.setTextColor(Color.RED);
                        } else {
                            row_12_col_0.setTextColor(0xff288132);
                        }
                        row_12_col_0.setText(data[1]);
                    }
                });
                final TextView row_12_col_1 = (TextView) findViewById(R.id.textView12C);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_12_col_1.setTextColor(Color.RED);
                        } else {
                            row_12_col_1.setTextColor(0xff288132);
                        }
                        row_12_col_1.setText(data[2]);
                    }
                });
                final TextView row_12_col_2 = (TextView) findViewById(R.id.textView12D);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNegative.equals(test)){
                            row_12_col_2.setTextColor(Color.RED);
                        } else {
                            row_12_col_2.setTextColor(0xff288132);
                        }
                        row_12_col_2.setText(data[3]);
                    }
                });
                break;

        }
    }
}
