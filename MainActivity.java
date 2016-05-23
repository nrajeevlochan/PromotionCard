package com.promotioncard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private List<Promotion> promotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        promotionList = new ArrayList<>();

        getJsonData();
    }

    //This method will get data from the web api
    private void getJsonData(){
        Log.i(TAG, "getJsonData()");
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        //calling method to parse json array
                        parseJsonData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "volly error response" + error);
                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    //This method will parse json data
    private void parseJsonData(JSONObject jsonObject){
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(Constants.TAG_PROMOTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Promotion promotion = new Promotion();
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    promotion.setTitle(object.getString(Constants.TAG_TITLE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    promotion.setImageUrl(object.getString(Constants.TAG_IMAGE_URL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    promotion.setFooter(object.getString(Constants.TAG_FOOTER));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    promotion.setDescription(object.getString(Constants.TAG_DESCRIPTION));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject buttonObject = null;
                try {
                    buttonObject = object.getJSONObject(Constants.TAG_BUTTON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (buttonObject != null) {
                    try {
                        promotion.setButtonTitle(buttonObject.getString(Constants.TAG_BUTTON_TITLE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        promotion.setButtonWebUrl(buttonObject.getString(Constants.TAG_TARGET));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                promotionList.add(promotion);
            }
        }

        mRecyclerView.setAdapter(new MyRecyclerAdapter(promotionList, this, new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Promotion promotion, int position) {
                Intent intent = new Intent(view.getContext(), SubActivity.class);
                intent.putExtra("myData", promotion);
                startActivity(intent);
            }
        }));

    }
}
