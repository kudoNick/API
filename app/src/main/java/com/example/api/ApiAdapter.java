package com.example.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApiAdapter extends BaseAdapter implements Filterable  {
    TextView tvLogin;
    ImageView  imgAva;

private Context context;

private List<Data> dataList;
private List<Data> getDataList;


private Filter filter;

    public ApiAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.getDataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.data,parent,false);


        tvLogin = view.findViewById(R.id.tvLogin);
        imgAva = view.findViewById(R.id.imgAva);

        Data data = (Data) getItem(position);

        tvLogin.setText(data.getLogin());

        String url = data.getAvatar();
        Glide.with(context).load(url).into(imgAva);

        return view;
    }

    public void resetData(){
        dataList = getDataList;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            Toast.makeText(context,"moi nhap",Toast.LENGTH_SHORT).show();
        }
        filter = new CustomFilter();
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = getDataList;
                results.count = getDataList.size();
            } else {
                List<Data> dataArrayList = new ArrayList<Data>();
                for (Data p : dataList) {
                    if (p.getLogin().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        dataArrayList.add(p);
                }
                results.values = dataArrayList;
                results.count = dataArrayList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                dataList = (List<Data>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
