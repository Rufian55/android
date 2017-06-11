package com.blitspost.quotetrack;

// https://stackoverflow.com/questions/3598770/java-check-whether-a-string-is-not-null-and-not-empty
// https://stackoverflow.com/questions/20241857/android-intent-cannot-resolve-constructor

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditUserTickers extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editusertickers);

        // Receive the arguments from the previous Activity
        Intent intent = getIntent();

        // Retrieve the original tickers array with whatever values it currently contains.
        final String[] tickers = intent.getStringArrayExtra("strings");

        EditText temp = ((EditText)findViewById(R.id.editInput_0));
        temp.setText(tickers[0]);
        temp = ((EditText)findViewById(R.id.editInput_1));
        temp.setText(tickers[1]);
        temp = ((EditText)findViewById(R.id.editInput_2));
        temp.setText(tickers[2]);
        temp = ((EditText)findViewById(R.id.editInput_3));
        temp.setText(tickers[3]);
        temp = ((EditText)findViewById(R.id.editInput_4));
        temp.setText(tickers[4]);
        temp = ((EditText)findViewById(R.id.editInput_5));
        temp.setText(tickers[5]);
        temp = ((EditText)findViewById(R.id.editInput_6));
        temp.setText(tickers[6]);
        temp = ((EditText)findViewById(R.id.editInput_7));
        temp.setText(tickers[7]);
        temp = ((EditText)findViewById(R.id.editInput_8));
        temp.setText(tickers[8]);
        temp = ((EditText)findViewById(R.id.editInput_9));
        temp.setText(tickers[9]);

        (findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update tickers[] array with user inputted values or existing as populated.

                // Ticker 0.
                String temp = ((EditText)findViewById(R.id.editInput_0)).getText().toString();
                if (!empty(temp)){
                    tickers[0] = temp.toUpperCase();
                }

                // Ticker 1.
                temp = ((EditText)findViewById(R.id.editInput_1)).getText().toString();
                if (!empty(temp)){
                    tickers[1] = temp.toUpperCase();
                }

                // Ticker 2.
                temp = ((EditText)findViewById(R.id.editInput_2)).getText().toString();
                if (!empty(temp)){
                    tickers[2] = temp.toUpperCase();
                }

                // Ticker 3.
                temp = ((EditText)findViewById(R.id.editInput_3)).getText().toString();
                if (!empty(temp)){
                    tickers[3] = temp.toUpperCase();
                }

                // Ticker 4.
                temp = ((EditText)findViewById(R.id.editInput_4)).getText().toString();
                if (!empty(temp)){
                    tickers[4] = temp.toUpperCase();
                }

                // Ticker 5.
                temp = ((EditText)findViewById(R.id.editInput_5)).getText().toString();
                if (!empty(temp)){
                    tickers[5] = temp.toUpperCase();
                }

                // Ticker 6.
                temp = ((EditText)findViewById(R.id.editInput_6)).getText().toString();
                if (!empty(temp)){
                    tickers[6] = temp.toUpperCase();
                }

                // Ticker 7.
                temp = ((EditText)findViewById(R.id.editInput_7)).getText().toString();
                if (!empty(temp)){
                    tickers[7] = temp.toUpperCase();
                }

                // Ticker 8.
                temp = ((EditText)findViewById(R.id.editInput_8)).getText().toString();
                if (!empty(temp)){
                    tickers[8] = temp.toUpperCase();
                }

                // Ticker 9.
                temp = ((EditText)findViewById(R.id.editInput_9)).getText().toString();
                if (!empty(temp)){
                    tickers[9] = temp.toUpperCase();
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
