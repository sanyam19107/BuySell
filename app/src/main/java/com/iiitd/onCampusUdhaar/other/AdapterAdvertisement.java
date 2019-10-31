package com.iiitd.onCampusUdhaar.other;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.iiitd.onCampusUdhaar.R;

public class AdapterAdvertisement  extends RecyclerView.Adapter<AdapterAdvertisement.MyViewHolder> {

    private List<Advertisement> advertisements;
    private Context context;


    public AdapterAdvertisement(List<Advertisement> advertisements, Context context) {
        this.advertisements = advertisements;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_advertisement, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Advertisement advertisement = advertisements.get(position);
        holder.title.setText(advertisement.getTitle());
        holder.value.setText("₹"+advertisement.getValue());

        if(advertisement.getRentSell()!=null && advertisement.getRentSell().equalsIgnoreCase("Rent")) {
            char backslash = '/';
            String expr = backslash + advertisement.getRentTime();
            holder.rentTime.setText(expr.toLowerCase());
            holder.salerent.setImageResource(R.drawable.rent);
        }

        //lloading image using library picasso
        String urlCover= advertisement.getPhoto();

        Picasso.get().load(urlCover).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView rentSell;
        TextView rentTime;
        TextView title;
        TextView value;
        ImageView picture;
        ImageView salerent;


        public MyViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            value = itemView.findViewById(R.id.textValue);
            picture = itemView.findViewById(R.id.imageAdvertisement);
            salerent = itemView.findViewById(R.id.rent_sell);
            rentTime=itemView.findViewById(R.id.textValue3);
        }
    }

}
