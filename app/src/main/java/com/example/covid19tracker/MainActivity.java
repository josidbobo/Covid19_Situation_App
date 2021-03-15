package com.example.covid19tracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    TextView tvCases, tvAffectedCountries, tvActive, tvTotalDeath, tvRecovered, tvTodayCases,  tvTodayDeath;
    private ProgressBar spinner;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner= findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          //      .setAction("Action", null).show());

        tvCases = findViewById(R.id.tvCases);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);
        tvActive = findViewById(R.id.tvActive);
        tvTotalDeath = findViewById(R.id.tvTotalDeath);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTodayDeath = findViewById(R.id.tvTodayDeath);
        scrollView = findViewById(R.id.scrollStats);
        pieChart = findViewById(R.id.pieChart);

        fetchData();
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all";
        spinner.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        tvCases.setText(jsonObject.getString("cases"));
                        tvAffectedCountries.setText(jsonObject.getString( "affectedCountries"));
                        tvActive.setText(jsonObject.getString("active"));
                        tvTotalDeath.setText(jsonObject.getString("deaths"));
                        tvRecovered.setText(jsonObject.getString("recovered"));
                        tvTodayCases.setText(jsonObject.getString("todayCases"));
                        tvTodayDeath.setText(jsonObject.getString("todayDeaths"));

                        pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                        pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                        pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(tvTotalDeath.getText().toString()), Color.parseColor("#EF5350")));
                        pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                        pieChart.startAnimation();

                        spinner.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                    } catch (JSONException e){
                        e.printStackTrace();
                        spinner.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                }, error -> {
                    spinner.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void goTrackCountries(View view) {
        startActivity(new Intent(getApplicationContext(),AffectedCountries.class));
    }
}