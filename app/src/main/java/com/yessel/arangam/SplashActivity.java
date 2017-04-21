package com.yessel.arangam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yessel.arangam.model.APIResponse;
import com.yessel.arangam.model.Artists;
import com.yessel.arangam.model.Segment;
import com.yessel.arangam.util.Mixp;
import com.yessel.arangam.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    int totalPages = -1;
    int currentPage = 0;
    int countPerPage = 100;
    List<Segment> segmentList = new ArrayList<Segment>();

    Map<String, Segment> segmentMap = new HashMap<String, Segment>();

    Map<String, Artists> artistsMap = new HashMap<String, Artists>();
    List<Artists> artistsList = new ArrayList<Artists>();

    ProgressBar progress;
    TextView progressLoadingText;
    Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        getSupportActionBar().hide();
        List<Segment> segmentList = PreferenceUtil.getSegmentList(this);
        if (segmentList != null && segmentList.size() > 0) {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
//            showList();
        } else {
            showLoadingView();
            syncData();
        }

        Mixp.track(this, "App_Initialising");

    }

    void initViews() {
        progress = (ProgressBar) findViewById(R.id.progress);
        progressLoadingText = (TextView) findViewById(R.id.progressloading);
        retry = (Button) findViewById(R.id.retry);
        addListeners();
    }

    void addListeners() {
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView();
                syncData();
            }
        });
    }

    private void syncData() {

        new RestClient(new RequestCallBack() {
            @Override
            public void success(String response) {
                Log.d("response", response);

                try {
                    APIResponse apiResponse = PreferenceUtil.GSON.fromJson(response, APIResponse.class);

                    segmentList.addAll(apiResponse.getData().getSegmentList());

                    Log.d("response", "apiResponse" + apiResponse);

                    if (totalPages == -1) {
                        int totalServerCount = apiResponse.getData().getCount();
                        totalPages = (totalServerCount % countPerPage == 0) ?
                                (totalServerCount / countPerPage) : (totalServerCount / countPerPage) + 1;
                    }


                    if (isNextPageRequired()) {
                        currentPage = currentPage + 1;
                        syncData();
                    } else {

                        Artists artists = null;
                        for (Segment segment : segmentList) {
                            segmentMap.put(segment.getId(), segment);
                            artists = segment.getArtists();
                            if (artists != null) {
                                artistsMap.put(artists.getId(), artists);
                            }
                        }


                        segmentList.clear();
                        segmentList.addAll(segmentMap.values());

                        artistsList.clear();
                        artistsList.addAll(artistsMap.values());
                        PreferenceUtil.saveSegments(SplashActivity.this, segmentList);
                        PreferenceUtil.saveArtists(SplashActivity.this, artistsList);
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();

//                        showList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail() {
                segmentList.clear();
                totalPages = -1;
                currentPage = 0;
                ///Fail Logic
                Log.d("fail", "API Failed");
                showRetry();
            }
        }).execute("http://nb-139-162-26-19.singapore.nodebalancer.linode.com/api/1.0/segments/composite?delta=" + currentPage);
//        }).execute("http://nb-139-162-26-19.singapore.nodebalancer.linode.com/api/1.0/segments/composite?page="+currentPage);
    }

    private static final String TAG = "SplashActivity";

    private void showList() {
        List<Segment> segmentList = PreferenceUtil.getSegmentList(SplashActivity.this);
//
//        for (int i = 0; i < segmentList.size(); i++) {
//
//            Segment segment = segmentList.get(i);
//
//            Log.e(TAG, "success: " + segment.getSegId() + "  " + segment.getArtists() + "    " + segmentList.size());
//        }
//        Toast.makeText(SplashActivity.this, "Ok", Toast.LENGTH_SHORT).show();
    }

    private boolean isNextPageRequired() {
        return (currentPage < totalPages);
    }

    private void showLoadingView() {
        progressLoadingText.setText("Initializing");
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    private void showRetry() {
        progressLoadingText.setText("Please check your connection and try again");
        progress.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
    }

}
