package chris.adroid_sqlite_location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Activity_sqlite extends AppCompatActivity {
    SQLiteExample mSQLiteExample;
    Button mSQLSubmitButton;
    Cursor mSQLCursor;
    SimpleCursorAdapter mSQLCursorAdapter;
    private static final String TAG = "SQLActivity";
    SQLiteDatabase mSQLDB;
    public Double mLatDefault = 44.5;
    private Double mLonDefault = -123.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Render the db input view.
        setContentView(R.layout.activity_sqlite);

        // Invoke a writable database instance.
        mSQLiteExample = new SQLiteExample(this);
        mSQLDB = mSQLiteExample.getWritableDatabase();

        mSQLSubmitButton = (Button) findViewById(R.id.sql_add_row_button);
        mSQLSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSQLDB != null){
                    ContentValues vals = new ContentValues();
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_STRING, ((EditText)findViewById(R.id.sql_text_input)).getText().toString());
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LONG, mLonDefault);
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LAT, mLatDefault);
                    mSQLDB.insert(DBContract.DemoTable.TABLE_NAME,null,vals);
                    populateTable();
                } else {
                    Log.d(TAG, "Unable to access database for writing.");
                }
            }
        });

        populateTable();
    }

    private void populateTable(){
        if(mSQLDB != null) {
            try {
                if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null){
                    if(!mSQLCursorAdapter.getCursor().isClosed()){
                        mSQLCursorAdapter.getCursor().close();
                    }
                }
                // Insert into table.
                mSQLCursor = mSQLDB.query(  DBContract.DemoTable.TABLE_NAME,                            // Table name.
                                            new String[]{DBContract.DemoTable._ID,                      // Table row index.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_STRING,  // Column 1.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_LONG,    // Column 2.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_LAT},    // Column 3.
                                            null,                                                       // WHERE paramterized.
                                            null,                                                       // The parameters.
                                            null,                                                       // GROUP BY
                                            null,                                                       // HAVING
                                            null);                                                      // ORDER BY

                // Select * FROM Table and display using sql_item.xml activity.
                ListView SQLListView = (ListView) findViewById(R.id.sql_list_view_location);
                mSQLCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.sql_item,
                        mSQLCursor,
                        new String[]{DBContract.DemoTable.COLUMN_NAME_DEMO_STRING,
                                     DBContract.DemoTable.COLUMN_NAME_DEMO_LONG,
                                     DBContract.DemoTable.COLUMN_NAME_DEMO_LAT},
                        new int[]{R.id.sql_listview_Location, R.id.sql_listview_long, R.id.sql_listview_lat},
                        0);
                SQLListView.setAdapter(mSQLCursorAdapter);

            } catch (Exception e) {
                Log.d(TAG, "Error loading data from database");
            }
        }
    }
}

class SQLiteExample extends SQLiteOpenHelper {

    public SQLiteExample(Context context) {
        super(context, DBContract.DemoTable.DB_NAME, null, DBContract.DemoTable.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.DemoTable.SQL_CREATE_DEMO_TABLE);

        ContentValues testValues = new ContentValues();
        testValues.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LONG, 0);
        testValues.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LAT, 0);
        testValues.put(DBContract.DemoTable.COLUMN_NAME_DEMO_STRING, "Hello SQLite");
        db.insert(DBContract.DemoTable.TABLE_NAME,null,testValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.DemoTable.SQL_DROP_DEMO_TABLE);
        onCreate(db);
    }

}

final class DBContract {
    private DBContract(){};

    public final class DemoTable implements BaseColumns {
        public static final String DB_NAME = "demo_db";
        public static final String TABLE_NAME = "demo";
        public static final String COLUMN_NAME_DEMO_STRING = "demo_string";
        public static final String COLUMN_NAME_DEMO_LONG = "demo_long";
        public static final String COLUMN_NAME_DEMO_LAT = "demo_lat";
        public static final int DB_VERSION = 5;


        public static final String SQL_CREATE_DEMO_TABLE = "CREATE TABLE " +
                DemoTable.TABLE_NAME + "(" + DemoTable._ID + " INTEGER PRIMARY KEY NOT NULL," +
                DemoTable.COLUMN_NAME_DEMO_STRING + " VARCHAR(255)," +
                DemoTable.COLUMN_NAME_DEMO_LAT + " REAL," +
                DemoTable.COLUMN_NAME_DEMO_LONG + " REAL);";

        public static final String SQL_TEST_DEMO_TABLE_INSERT = "INSERT INTO " + TABLE_NAME +
                " (" + COLUMN_NAME_DEMO_STRING + "," + COLUMN_NAME_DEMO_LONG + "," + COLUMN_NAME_DEMO_LAT + ") VALUES ('test', 101, 102);";

        public  static final String SQL_DROP_DEMO_TABLE = "DROP TABLE IF EXISTS " + DemoTable.TABLE_NAME;
    }
}