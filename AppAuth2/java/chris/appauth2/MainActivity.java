package chris.appauth2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Clicking the button takes you to AuthActivity.java page. */
    public void goToActivity_01(View view) {
        Intent intent = new Intent(this, AuthActivity.class);
        LinearLayout seeDemo = (LinearLayout) findViewById(R.id.activity_api);
        startActivity(intent);
    }
}
