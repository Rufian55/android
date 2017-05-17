package chris.adroid_sqlite_location;
/* ***************************************************************************************
 *  CS496-400-S17, Chris Kearns, kearnsc@oregonstate.edu, 21 May 2017
 *  Assignment: Android SQLite and Location
 *  Activity_sqlite.java is the source file for manipulating a local db with permissions.
 *  Dependencies, other than the Gradle Scripts and AS prebuilt xml files:
 *  MainActivity.java, associated layout/*.xml files and values/*.xml
 *  CITATIONS: Entire code base is adapted from:
 *  [*] Lectures and materials as professed by Mr. Justin Wolford, Oregon State
 *  University, 2017
 *  [*] https://developer.android.com/guide/index.html and associated links from there.
 *  [*] Several (approximately 5) stackoverflow.com searches for minor syntax issues.
 *  [1] https://github.com/googledrive/android-demos/blob/master/app/src/main/java/com
 *         /google/android/gms/drive/sample/demo/BaseDemoActivity.java
 *  APP is at:  https://oregonstate.instructure.com/courses/1627314/external_tools/115448
 ***************************************************************************************/
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class Activity_sqlite
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener {

    // DataBase variable declarations.
    SQLiteExample mSQLiteExample;
    Button mSQLSubmitButton;
    Button mSQLDeleteButton;
    Cursor mSQLCursor;
    SimpleCursorAdapter mSQLCursorAdapter;
    private static final String TAG = "Activity_sqlite.java";
    SQLiteDatabase mSQLDB;

    // Location and Permissions variable declarations.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLatText = "";
    private String mLonText = "";
    private LocationListener mLocationListener;
    private static final int LOCATION_PERMISSION_RESULT = 17;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Render the db input view.
        setContentView(R.layout.activity_sqlite);

        // Invoke  an instance of GoogleApiClient to use to connect to Google Play Services.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)// Longitude & Latitude.
                    .build();
        }

        // Default not connected / no user permissions GeoCoordinates.
        mLatText = "44.5";
        mLonText = "-123.2";

        // Request a high accuracy location every 1 minute.
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);

        // Listen for location changes. See function upodateLocation() for it's call.
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    mLatText = String.valueOf(location.getLatitude());
                    mLonText = String.valueOf(location.getLongitude());
                } else {
                    mLatText = "Location Unavailable!";
                    mLonText = "Is Device GPS Enabled?";
                }
            }
        };

        // Invoke a writable database instance.
        mSQLiteExample = new SQLiteExample(this);
        mSQLDB = mSQLiteExample.getWritableDatabase();

        // The "Add Location" Button definition and onclick() handler.
        mSQLSubmitButton = (Button) findViewById(R.id.sql_add_row_button);
        mSQLSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSQLDB != null){
                    ContentValues vals = new ContentValues();
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_STRING,
                            ((EditText)findViewById(R.id.sql_text_input)).getText().toString());
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LAT, mLatText);
                    vals.put(DBContract.DemoTable.COLUMN_NAME_DEMO_LONG, mLonText);
                    mSQLDB.insert(DBContract.DemoTable.TABLE_NAME, null, vals);
                    // Reload the newly modified db table.
                    populateTable();
                } else {
                    Log.d(TAG, "Unable to access database for writing (add).");
                }
            }
        });
        // Load the db table onCreate() [when app is started on the device.]
        populateTable();
    }

    /* Queries the mSQLDB db "demo" table for its contents and utilizing a simpleCursorAdapter,
       displays the table's contents via layout.sql_item.xml nested in sql_list_view_locations.xml */
    private void populateTable(){
        if(mSQLDB != null) {
            try {
                if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null){
                    if(!mSQLCursorAdapter.getCursor().isClosed()){
                        mSQLCursorAdapter.getCursor().close();
                    }
                }
                // Query "demo" table.
                mSQLCursor = mSQLDB.query(  DBContract.DemoTable.TABLE_NAME,                            // Table name.
                                            new String[]{DBContract.DemoTable._ID,                      // Table row index.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_STRING,  // Column 1.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_LAT,     // Column 2.
                                                         DBContract.DemoTable.COLUMN_NAME_DEMO_LONG},   // Column 3.
                                            null,                                                       // WHERE parametrized.
                                            null,                                                       // The parameters.
                                            null,                                                       // GROUP BY
                                            null,                                                       // HAVING
                                            null);                                                      // ORDER BY

                // Select * FROM Table and display using sql_item.xml activity.
                ListView SQLListView = (ListView) findViewById(R.id.sql_list_view_location);
                mSQLCursorAdapter = new SimpleCursorAdapter(
                        this,
                        R.layout.sql_item,
                        mSQLCursor,
                        new String[]{DBContract.DemoTable.COLUMN_NAME_DEMO_STRING,
                                     DBContract.DemoTable.COLUMN_NAME_DEMO_LAT,
                                     DBContract.DemoTable.COLUMN_NAME_DEMO_LONG},
                        new int[]{R.id.sql_listview_Location,
                                  R.id.sql_listview_long,
                                  R.id.sql_listview_lat},
                                  0);
                SQLListView.setAdapter(mSQLCursorAdapter);

            } catch (Exception e) {
                Log.d(TAG, "Error loading data from database");
            }
        }
    }

    // Connects app to the GoogleAPIClient when it starts.
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    // Disconnects app from the GoogleAPIClient when it is stopped.
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // On a successful connection to the GoogleAPIClient, checks for user provided permission to access current location.
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_RESULT);
            return;
        }
        updateLocation();
    }

    // Log GoogleApiClient connection suspended cause, GoogleAPIClient will auto reconnect. [1]
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        Dialog errDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0);
        errDialog.show();
    }

    // Log GoogleApiClient connection suspended cause, GoogleAPIClient will auto reconnect. [1]
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /* Updates mLonText and mLatText with device's current location following a user permission check.
       If first time device usage, LocationSerives.FusedLoaction method will be used. */
    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLonText = String.valueOf(mLastLocation.getLongitude());
            mLatText = String.valueOf(mLastLocation.getLatitude());
            // Start the mLocationListener.
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }
}


// On app start, instantiates, creates, and manages versions (for subsequent builds).
class SQLiteExample extends SQLiteOpenHelper {

    SQLiteExample(Context context) {
        super(context, DBContract.DemoTable.DB_NAME, null, DBContract.DemoTable.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.DemoTable.SQL_CREATE_DEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.DemoTable.SQL_DROP_DEMO_TABLE);
        onCreate(db);
    }
}


// DataBase and Table Structure.
final class DBContract {
    private DBContract(){};

    // The db and table.
    final class DemoTable implements BaseColumns {
        static final String DB_NAME = "demo_db";
        static final String TABLE_NAME = "demo";
        static final String COLUMN_NAME_DEMO_STRING = "demo_string";
        static final String COLUMN_NAME_DEMO_LAT = "demo_lat";
        static final String COLUMN_NAME_DEMO_LONG = "demo_long";
        static final int DB_VERSION = 1;

        // Create table query string definition.
        static final String SQL_CREATE_DEMO_TABLE = "CREATE TABLE " +
                DemoTable.TABLE_NAME + "(" + DemoTable._ID + " INTEGER PRIMARY KEY NOT NULL," +
                DemoTable.COLUMN_NAME_DEMO_STRING + " VARCHAR(25)," +
                DemoTable.COLUMN_NAME_DEMO_LAT + " VARCHAR(10)," +
                DemoTable.COLUMN_NAME_DEMO_LONG + " VARCHAR(10));";

        // Drop table query string definition.
        static final String SQL_DROP_DEMO_TABLE = "DROP TABLE IF EXISTS " + DemoTable.TABLE_NAME;
    }
}