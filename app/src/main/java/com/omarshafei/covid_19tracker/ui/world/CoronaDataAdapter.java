package com.omarshafei.covid_19tracker.ui.world;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshafei.covid_19tracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CoronaDataAdapter extends RecyclerView.Adapter<CoronaDataAdapter.CustomViewHolder> implements Filterable {

    private List<Module> data;
    private List<Module> dataCopy;
    CoronaDataAdapter(List<Module> data) {
        this.data = data;
        dataCopy = new ArrayList<>(data);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        //Views inside the list_item xml file
        private ImageView image;
        private TextView countryName;
        private TextView totalCases;
        private TextView totalDeaths;
        private TextView totalRecovered;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.country_flag);
            countryName = itemView.findViewById(R.id.country_name);
            totalCases = itemView.findViewById(R.id.infected);
            totalDeaths = itemView.findViewById(R.id.deaths);
            totalRecovered = itemView.findViewById(R.id.recovered);

        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false));
    }

    @Override
    //Bind data with the views
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Module currentElement = data.get(position);

        Picasso.get().load(currentElement.getImageUrl()).into(holder.image);
        holder.countryName.setText(currentElement.getCountryName());
        holder.totalCases.setText(currentElement.getCases());
        holder.totalDeaths.setText(currentElement.getDeaths());
        holder.totalRecovered.setText(currentElement.getRecovered());

    }
    @Override
    //number of items in the list
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Module> filteredData = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredData.addAll(dataCopy);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Module module : dataCopy){
                    if(module.getCountryName().toLowerCase().contains(filterPattern)){
                        filteredData.add(module);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredData;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
