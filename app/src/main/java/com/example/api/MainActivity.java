package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView  lvData;
    EditText edtSearch;


    List<Data> dataListSon;
    List<Data> dataListParents;
    ApiAdapter apiAdapter ;


    View dataView;
    boolean isLoading = false;
    androidx.appcompat.widget.SearchView searchView;



    mHandler mHandler;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvData =findViewById(R.id.lvData);
        edtSearch = findViewById(R.id.edtSearch);
        searchView = findViewById(R.id.searchView);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        dataView = inflater.inflate(R.layout.loadmore_listview, null);
        mHandler = new mHandler();
        getData();
        setListcron();



//nút search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after < count) {
                    apiAdapter.resetData();
                    System.out.println("thoat");
                }
                apiAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                apiAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void setListcron(){
        lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && totalItemCount !=0 && isLoading == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }
    public void getData(){
        dataListParents = new ArrayList<>();
        AndroidNetworking.get("http://www.json-generator.com/api/json/get/cfQPOdYijS?indent=2")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DATA",response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    Data data = new Data(response.getJSONObject(i));
                                    dataListParents.add(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        dataListSon = new ArrayList<>(dataListParents.subList(0,5));
                        apiAdapter = new ApiAdapter(MainActivity.this, dataListSon);
                        lvData.setAdapter(apiAdapter);
                        apiAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(ANError error) {

                    }
                });

    }
    private void addMoreItems(){
        int size = dataListSon.size();
        for(int i=1;i<=5;i++){
            if((size + i) < dataListParents.size()){
                dataListSon.add(dataListParents.get(size + i));
            }else {
                lvData.removeFooterView(dataView);
                Toast.makeText(this,"Hết Data",Toast.LENGTH_SHORT).show();
            }
        }
        apiAdapter.notifyDataSetChanged();
//        progressBar.setVisibility(View.GONE);
    }
    public class mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    lvData.addFooterView(dataView);
                    break;
                case 1:
                    addMoreItems();
                    isLoading = false;
            }
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            super.run();
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
        }
    }
}
