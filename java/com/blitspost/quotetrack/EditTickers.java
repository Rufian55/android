package com.blitspost.quotetrack;

// https://stackoverflow.com/questions/3598770/java-check-whether-a-string-is-not-null-and-not-empty
// https://stackoverflow.com/questions/20241857/android-intent-cannot-resolve-constructor

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTickers extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittickers);

        // Receive the arguments from the previous Activity
        Intent intent = getIntent();

        // Retrieve the original tickers array with whatever values it currently contains.
        final String[] tickers = intent.getStringArrayExtra("strings");

        (findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Assign the values to string-arguments.

                // Ticker 0.
                String temp = ((EditText)findViewById(R.id.editInput_0)).getText().toString();
                if (!empty(temp)){
                    tickers[0] = temp;
                }

                // Ticker 1.
                temp = ((EditText)findViewById(R.id.editInput_1)).getText().toString();
                if (!empty(temp)){
                    tickers[1] = temp;
                }

                // Ticker 2.
                temp = ((EditText)findViewById(R.id.editInput_2)).getText().toString();
                if (!empty(temp)){
                    tickers[2] = temp;
                }

                // Ticker 3.
                temp = ((EditText)findViewById(R.id.editInput_3)).getText().toString();
                if (!empty(temp)){
                    tickers[3] = temp;
                }

                // Ticker 4.
                temp = ((EditText)findViewById(R.id.editInput_4)).getText().toString();
                if (!empty(temp)){
                    tickers[4] = temp;
                }

                // Ticker 5.
                temp = ((EditText)findViewById(R.id.editInput_5)).getText().toString();
                if (!empty(temp)){
                    tickers[5] = temp;
                }

                // Ticker 6.
                temp = ((EditText)findViewById(R.id.editInput_6)).getText().toString();
                if (!empty(temp)){
                    tickers[6] = temp;
                }

                // Ticker 7.
                temp = ((EditText)findViewById(R.id.editInput_7)).getText().toString();
                if (!empty(temp)){
                    tickers[7] = temp;
                }

                // Ticker 8.
                temp = ((EditText)findViewById(R.id.editInput_8)).getText().toString();
                if (!empty(temp)){
                    tickers[8] = temp;
                }

                // Ticker 9.
                temp = ((EditText)findViewById(R.id.editInput_9)).getText().toString();
                if (!empty(temp)){
                    tickers[9] = temp;
                }

                for(int i =0; i < 10; i++){
                    Log.d("EditTicker:null?", tickers[i]);
                }

                finish(tickers);
            }
        });
    }

    public static boolean empty(final String ticker ) {
        // Null-safe, short-circuit evaluation.
        return ticker == null || ticker.trim().isEmpty();
    }

    public void finish(String [] tickers) {
        Intent data = new Intent();
        data.putExtra("strings", tickers);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
