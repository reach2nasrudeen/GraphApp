package com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        LineChart lineChart = findViewById(R.id.lineChart);
        lineChart.setStepline(true);
//        lineChart.setInverseY_Axis(true);
        List<ChartData> values = new ArrayList();
        //values.add(new ChartData(y,x));


        values.add(new ChartData(4f, 0.5f));

        values.add(new ChartData(8f, 1f));
        values.add(new ChartData(18f, 2f));
        values.add(new ChartData(2f, 4f));
        values.add(new ChartData(12f, 5f));
        values.add(new ChartData(9f, 7f));


        lineChart.setData(values);
//        lineChart.setData(getChartData());
    }

    private List<ChartData> getChartData() {
        List<ChartData> values = new ArrayList();
        values.add(new ChartData(2f, 0.5f));
        values.add(new ChartData(2f, 0.5f));
        values.add(new ChartData(4f, 1f));
        values.add(new ChartData(6f, 1.5f));
        values.add(new ChartData(8f, 2f));
        values.add(new ChartData(10f, 2.5f));
        values.add(new ChartData(12f, 3f));
        values.add(new ChartData(14f, 3.5f));
        values.add(new ChartData(16f, 4f));
        values.add(new ChartData(16f, 4.5f));
        values.add(new ChartData(18f, 5f));
        return values;
    }
}
