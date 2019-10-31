package com.iiitd.onCampusUdhaar.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iiitd.onCampusUdhaar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOrder  extends RecyclerView.Adapter<AdapterOrder.MyViewHolder> {

    private List<BookingOrder> orders;
    private Context context;


    public AdapterOrder(List<BookingOrder> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_order, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookingOrder BookingOrder = orders.get(position);
        holder.title.setText(BookingOrder.getAdvertisementtitle());
        holder.value.setText("Order Id:"+BookingOrder.getidOrder());
        //holder.value.setText(B);

        if(BookingOrder.getStatus()!=null && BookingOrder.getStatus()==1) {
            holder.status.setText(R.string.bookstatus1);
        }
        else if(BookingOrder.getStatus()!=null && BookingOrder.getStatus()==2){
            holder.status.setText(R.string.bookstatus2);
        }
        else if(BookingOrder.getStatus()!=null && BookingOrder.getStatus()==3){
            holder.status.setText(R.string.bookstatus3);
        }
        else if (BookingOrder.getStatus()!=null && BookingOrder.getStatus()==4){
            holder.status.setText(R.string.bookstatus4);
        }
        else if (BookingOrder.getStatus()!=null && BookingOrder.getStatus()==5){
            holder.status.setText(R.string.bookstatus5);
        }

        //lloading image using library picasso
       // String urlCover= BookingOrder.getPhoto();

        //Picasso.get().load(urlCover).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView value;
        ImageView picture;
        TextView status;


        public MyViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            value = itemView.findViewById(R.id.textValue);
            status=itemView.findViewById(R.id.status);
//            picture = itemView.findViewById(R.id.imageBookingOrder);
//            salerent = itemView.findViewById(R.id.rent_sell);
//            rentTime=itemView.findViewById(R.id.textValue3);
        }
    }

}
