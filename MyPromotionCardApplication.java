package com.promotioncard;

import android.app.Application;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import java.util.PriorityQueue;

/**
 * Created by ${} on 5/22/16.
 */
public class MyPromotionCardApplication extends Application {

    private static MyPromotionCardApplication mInstance = new MyPromotionCardApplication();
    private static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        //mInstance = this;

        imageLoader = MyVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
        //Log.d("MyPromotionCardApplication", "imageLoader" + imageLoader);
    }

    public static MyPromotionCardApplication getInstance() {
        return mInstance;
    }

    public ImageLoader getImageLoader()
    {
        // Initialize the instance of MySingleton
        return imageLoader;
    }
}
