package ks.app.foodilize.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;
import ks.app.foodilize.ViewRequest;
import pl.droidsonroids.gif.GifImageView;

public class ActivityCardAdapter extends RecyclerView.Adapter<ActivityCardAdapter.ViewHolder> {

    ArrayList<ObjectRequest> arrayList;
    Context context;
    private LayoutInflater mInflater;

    public ActivityCardAdapter(ArrayList<ObjectRequest> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_nc_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ObjectRequest oR = arrayList.get(position);

        //TODO:get time and subtract that from the original one and then show time difference
        if(oR.getDeliveryStatus() == 0) {
            holder.iV_status_img.setImageResource(R.drawable.foodcreated);
            holder.iV_background.setImageResource(R.drawable.bg_crockery);
            holder.tV_status_msg.setText(oR.getSuppName()+ " is waiting for someone to foodilize their request of "+ oR.getQuantity()+" kg food.");
            holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        } else if (oR.getDeliveryStatus() == 1) {
            holder.iV_status_img.setImageResource(R.drawable.fooddelivery_unscreen);
            holder.iV_background.setImageResource(R.drawable.bg_otw);
            holder.tV_status_msg.setText(oR.getNgoName()+" is otw to foodilize "+oR.getQuantity()+" kg food from "+oR.getSuppName());
            holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        } else {
            holder.iV_status_img.setImageResource(R.drawable.celebrate);
            holder.iV_background.setImageResource(R.drawable.delivered);
            holder.tV_status_msg.setText(oR.getNgoName()+" has foodilized "+oR.getQuantity()+" kg food on request from "+oR.getSuppName());
            holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        }
        holder.cV_container.setOnClickListener(v->{
            Intent i = new Intent(context, ViewRequest.class);
            i.putExtra("REQUEST", oR);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GifImageView iV_status_img;
        TextView tV_status_msg;
        TextView tV_status_time;
        CardView cV_container;
        RoundedImageView iV_background;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iV_status_img = itemView.findViewById(R.id.iV_activity_card_status_img);
            tV_status_msg = itemView.findViewById(R.id.tV_activity_card_status_text);
            tV_status_time = itemView.findViewById(R.id.tV_activity_card_status_time);
            cV_container = itemView.findViewById(R.id.cV_activity_card);
            iV_background = itemView.findViewById(R.id.iV_activity_card_status_bg);

            //TODO: Perform onCLick response
            cV_container.setOnClickListener(v->{

            });
        }
    }
}
