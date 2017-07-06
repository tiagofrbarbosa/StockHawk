package com.udacity.stockhawk.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
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
import timber.log.Timber;


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

        String[] projection = new String[]{Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                                           Contract.Quote.COLUMN_ABSOLUTE_CHANGE, Contract.Quote.COLUMN_PERCENTAGE_CHANGE, Contract.Quote.COLUMN_HISTORY};

        String selection = Contract.Quote.COLUMN_SYMBOL + " = ?";
        String[] selectionArgs = new String[]{s};
        String sortOrder = null;

        Cursor cursor = mResolver.query(mUri, projection, selection, selectionArgs, sortOrder);


        try{
            cursor.moveToFirst();
        }catch (Exception e){
            Timber.e("Cursor error");
        }

        String[] lines = cursor.getString(5).split("\\r?\\n");

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();


        for(int i=0; i < lines.length; i++){
            float v =  Float.parseFloat(lines[i].substring(15,21));
            entries.add(new Entry(v, i));
        }

        LineDataSet dataset = new LineDataSet(entries, "# of quotes");

        ArrayList<String> labels = new ArrayList<String>();

        for(int i=0; i < lines.length; i++){
            labels.add(String.valueOf(i));
        }


        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setDescription(s);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setData(data);
        lineChart.animateY(5000);
    }
}
