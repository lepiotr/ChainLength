package com.example.myapplication1;

import android.app.Application;
import android.content.Context;

public class Home extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));//, "en"));
    }
}