package com.yessel.arangam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class PrivacyActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(values);
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Privacy Policy" + "</font>")));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textView = (TextView) findViewById(R.id.em);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
