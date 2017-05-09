package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Called when user taps Send button_1 */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /* Called when user taps "SEE IT!" button_2 */
    public void goToLinearLayoutHorizontal(View view) {
        Intent intent = new Intent(this, Activity_02.class);
        TextView editText = (TextView) findViewById(R.id.activityView_2);
        startActivity(intent);
    }

    /* Called when user taps "GO!" button_3 */
    public void goToLinearLayoutVertical(View view) {
        Intent intent = new Intent(this, Activity_03.class);
        TextView editText = (TextView) findViewById(R.id.activityView_3);
        startActivity(intent);
    }

    /* Called when user taps "Ta DAA!" button_4 */
    public void goToGridLayout(View view) {
        Intent intent = new Intent(this, Activity_04.class);
        TextView editText = (TextView) findViewById(R.id.activityView_4);
        startActivity(intent);
    }

    /* Called when user taps "FINALLY" button_5 */
    public void goToRelativeLayout(View view) {
        Intent intent = new Intent(this, Activity_05.class);
        TextView editText = (TextView) findViewById(R.id.activityView_5);
        startActivity(intent);
    }


}
