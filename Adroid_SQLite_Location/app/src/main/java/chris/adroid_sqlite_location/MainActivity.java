package chris.adroid_sqlite_location;

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

    /* Clicking the button takes you to Acitivity_01 page. */
    public void goToActivity_01(View view) {
        Intent intent = new Intent(this, Activity_01.class);
        LinearLayout seeDemo = (LinearLayout) findViewById(R.id.activityview_01);
        startActivity(intent);
    }

    /* Clicking the button takes you to Acitivity_01 page. */
    public void goToActivity_sqlite(View view) {
        Intent intent = new Intent(this, Activity_sqlite.class);
        RelativeLayout seeDemo = (RelativeLayout) findViewById(R.id.activity_sqlite);
        startActivity(intent);
    }
}

// https://github.com/codepath/android_guides/wiki/Genymotion-2.0-Emulators-with-Google-Play-support
