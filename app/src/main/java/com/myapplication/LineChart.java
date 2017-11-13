package com.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChart extends View {

    private Paint paint;
    private List<ChartData> values;
    private List<Float> horizontal_width_list = new ArrayList<>();
    private String description;
    private float border = 30, horstart = border * 2, circleSize = 5f, colwidth;
    private int parentHeight, parentWidth;
    private float minY_values;
    private Canvas canvas;
    private List<ChartData> list_cordinate = new ArrayList<>();
    private float x_cordinate, y_cordinate, height, width, maxY_values, maxX_values, graphheight, graphwidth;
    private List<ChartData> trendlines;
    private List<ChartData> trendzones;
    private AxisFormatter axisFormatter = new AxisFormatter();
    private Boolean stepline = false;
    private Boolean stepArea = false;
    private Boolean inverseAxis = false;
    private Boolean areaChart = false;
    private Context mContext;

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Paint paint = new Paint();
        this.paint = paint;
    }

    public void setData(List<ChartData> values) {

        if (values != null)
            this.values = values;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStepline(Boolean stepline) {
        this.stepline = stepline;
    }

    public void setStepArea(Boolean stepArea) {
        this.stepArea = stepArea;
    }

    public void setCircleSize(Float circleSize) {
        this.circleSize = circleSize;
    }

    public void setTrendZones(List<ChartData> trendzones) {
        this.trendzones = trendzones;
    }

    public void setTrendlines(List<ChartData> trendlines) {
        this.trendlines = trendlines;
    }

    public void setInverseY_Axis(boolean inverseAxis) {
        this.inverseAxis = inverseAxis;
    }

    public void setAreaChart(boolean areaChart) {
        this.areaChart = areaChart;
    }

    // Get the Width and Height defined in the activity xml file
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {

        intilaizeValue(canvas);

        axisFormatter.PlotXYLabels(graphheight, width, graphwidth, height, null, maxY_values, canvas,
                horizontal_width_list, paint, values, maxX_values, description, inverseAxis, true);

        if (values != null) {

            colwidth = horizontal_width_list.get(1) - horizontal_width_list.get(0);

            list_cordinate = StoredCordinate(graphheight);


            if (trendzones != null) {
                ChartHelper chartHelper1 = new ChartHelper(trendzones, canvas, paint);
                chartHelper1.DrawTrendzone(values.size(), colwidth, graphheight, maxY_values);
            }

            if (trendlines != null) {
                ChartHelper chartHelper2 = new ChartHelper(trendlines, canvas, paint);
                chartHelper2.DrawTrendlines(graphheight, maxY_values, graphwidth);
            }

            paint.setColor(Color.parseColor(axisFormatter.getColorList().get(0)));
            paint.setStrokeWidth(2);
            if (stepline) {
                DrawStepLines();

            }

            paint.setStrokeWidth(0);
            DrawCircle();
        }
    }

    private void intilaizeValue(Canvas canvas) {

        height = parentHeight - 60;
        width = parentWidth;
        AxisFormatter axisFormatter = new AxisFormatter();
        maxY_values = axisFormatter.getMaxY_Values(values);

        if (values.get(0).getLabels() == null)
            maxX_values = axisFormatter.getMaxX_Values(values);

        minY_values = axisFormatter.getMinValues(values);
        graphheight = height - (3 * border);
        graphwidth = width - (3 * border);
        this.canvas = canvas;
    }

    private void DrawCircle() {
        Paint mPaint;
        float styleSize;
        for (int i = 0; i < list_cordinate.size(); i++) {

            float endX = list_cordinate.get(i).getX_values();
            float endY = list_cordinate.get(i).getY_values();
//Draw big circle at particular point
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
//            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            canvas.drawCircle(endX, endY, ChartHelper.dip2px(mContext,10), mPaint);
//Draw small circle above big circle at particular point
            mPaint = new Paint();
            styleSize = 2f;
//            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(ContextCompat.getColor(mContext, android.R.color.white));
            canvas.drawCircle(endX, endY, ChartHelper.dip2px(mContext,5), mPaint);
            canvas.drawCircle(list_cordinate.get(i).getX_values(),
                    list_cordinate.get(i).getY_values(),
                    circleSize, paint);
        }
    }

    private List<ChartData> StoredCordinate(Float graphheight) {

        for (int i = 0; i < values.size(); i++) {

            float ver_ratio = maxY_values / values.size();
            float line_height = (graphheight / (maxY_values + (int) ver_ratio)) * (values.get(i).getY_values() - minY_values);
            if (inverseAxis == true) {
                y_cordinate = line_height + border;

            } else {

                y_cordinate = (border - line_height) + graphheight;
            }

            if (values.get(0).getLabels() == null) {

                float x_ratio = (maxX_values / (values.size() - 1));
                x_cordinate = (colwidth / x_ratio) * values.get(i).getX_values();

                list_cordinate.add(new ChartData(y_cordinate, x_cordinate + horstart));

            } else {

                x_cordinate = (i * colwidth) + horstart;
                list_cordinate.add(new ChartData(y_cordinate, x_cordinate + horstart));

            }
        }

        return list_cordinate;
    }

    private void DrawStepLines() {

        for (int i = 0; i < list_cordinate.size() - 1; i++) {

            Paint paintLine = new Paint();
            paintLine.setStrokeWidth(5);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                paintLine.setColor(mContext.getColor(R.color.colorAccent));
            }
            canvas.drawLine(list_cordinate.get(i).getX_values(),
                    list_cordinate.get(i).getY_values(),
                    list_cordinate.get(i).getX_values(),
                    list_cordinate.get(i + 1).getY_values(), paintLine);
            canvas.drawLine(list_cordinate.get(i).getX_values(),
                    list_cordinate.get(i + 1).getY_values(),
                    list_cordinate.get(i + 1).getX_values(),
                    list_cordinate.get(i + 1).getY_values(),
                    paintLine);

        }
    }

    public void saveChart(LineChart lineChart) {

        axisFormatter.saveChart(lineChart.getBitmap(), height + border, width);
    }

    public Bitmap getBitmap() {

        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);


        return bmp;
    }

}
