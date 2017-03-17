package com.example.gauravagarwal.quotes;

import android.os.AsyncTask;

/**
 * Created by Gaurav Agarwal on 17-03-2017.
 */

public class SetFilterTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        Utils.changeFilter("quotes");
        Utils.changeFilter("tagWise");
        Utils.changeFilter("authorWise");
        return null;
    }
}
