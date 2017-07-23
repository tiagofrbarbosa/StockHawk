package com.udacity.stockhawk.widget;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.Contract.Quote;

import com.udacity.stockhawk.R;

import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    private Cursor data = null;
    Context mContext = null;
    private ContentResolver mResolver;
    private Uri mUri;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        //initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

        if(data != null){
            data.close();
        }

    }

    @Override
    public int getCount() {
        if(data == null){
            return 0;
        }else{
            return data.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION ||data == null || !data.moveToPosition(position)) {
            return null;
        }

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_collection_item);

        //view.setTextViewText(android.R.id.text1, mCollection.get(position));

        view.setTextViewText(R.id.stock_symbol, data.getString(data.getColumnIndex("symbol")));

        String bidPrice = data.getString(data.getColumnIndex(Quote.COLUMN_PRICE));
        String symbol = data.getString(data.getColumnIndex(Quote.COLUMN_SYMBOL));
        view.setTextViewText(R.id.bid_price, bidPrice);
        view.setTextViewText(R.id.change, data.getString(4));


            view.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);

        final Intent fillInIntent = new Intent();
        //final Bundle extras = new Bundle();
        //extras.putString(QuoteWidgetProvider.EXTRA_QUOTE, symbol);
        fillInIntent.putExtra("Symbol", symbol);
        view.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);


        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        if (data != null) {
            data.close();
        }

        final long token = Binder.clearCallingIdentity();


        mResolver = mContext.getContentResolver();

        mUri = Contract.Quote.URI;


        String[] projection = new String[]{Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                Contract.Quote.COLUMN_ABSOLUTE_CHANGE, Contract.Quote.COLUMN_PERCENTAGE_CHANGE, Contract.Quote.COLUMN_HISTORY};

        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        data = mResolver.query(mUri, projection, selection, selectionArgs, sortOrder);

        Binder.restoreCallingIdentity(token);
    }

}