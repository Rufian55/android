/****************************************************************************************
 *  CS496-400-S17, Chris Kearns, kearnsc@oregonstate.edu, 14 May 2017
 *  Assignment: AndroidUI
 *  Activity_02.java member class of the BlitsPost.com App, aka, MyApplication
 *  Dependencies, other than the Gradle Scripts and AS prebuilt xml files:
 *  activity_02.xml
 *  CITATIONS: Entire code base is adapted from:
 *  [1] Lectures and materials as professed by Mr. Justin Wolford, Oregon State
 *  University, 2017
 *  [2] https://developer.android.com/guide/index.html and associated links from there.
 *  [3] DisplayMessageActivity.java, activity_display_messages.xml, and function
 *  sendMessages() is as developed from the Developer's Training section and the
 *  implemented tutorial found at https://developer.android.com/training/index.html
 *  [4] Numerous (approximately 20) stackoverflow.com searchs for minor syntax issues.
 **************************************************************************************/
package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_02 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_02);
    }
}
