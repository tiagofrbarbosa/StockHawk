package com.udacity.stockhawk.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by tfbarbosa on 03/07/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private ContentResolver mResolver;
    private Uri mUri;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mResolver = this.getContentResolver();

        mUri = Contract.Quote.URI;

        String s = getIntent().getExtras().getString("symbol");

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        String[] projection = new String[]{Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                                           Contract.Quote.COLUMN_ABSOLUTE_CHANGE, Contract.Quote.COLUMN_PERCENTAGE_CHANGE, Contract.Quote.COLUMN_HISTORY};

        String selection = Contract.Quote.COLUMN_SYMBOL + " = ?";
        String[] selectionArgs = new String[]{s};
        String sortOrder = null;

        Cursor cursor = mResolver.query(mUri, projection, selection, selectionArgs, sortOrder);

        int mCursorCount;

        try{
            cursor.moveToFirst();
            mCursorCount = cursor.getCount();
        }catch (Exception e){
            mCursorCount = 0;
        }

        Toast.makeText(this, String.valueOf(mCursorCount), Toast.LENGTH_LONG).show();

        Log.i("TABELA", cursor.getString(0) + " " +  cursor.getString(1)+ " " +  cursor.getString(2)
                + " " +  cursor.getString(3)+ " " +  cursor.getString(4)+ " " +  cursor.getString(5));

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);
    }
}
