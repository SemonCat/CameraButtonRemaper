package com.semoncat.camerabuttonremaper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class BaseActivity extends ActionBarActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayoutID());
        setupView();
        setupAdapter();
        setupEvent();
    }

    protected int setupLayoutID(){
        return 0;
    }

    protected void setupView(){

    }

    protected void setupAdapter(){

    }

    protected void setupEvent(){

    }

}
