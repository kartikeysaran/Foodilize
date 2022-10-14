package ks.app.foodilize.ui.main;

import android.annotation.SuppressLint;
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

import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;

public class ActivityCardAdapter extends RecyclerView.Adapter<ActivityCardAdapter.ViewHolder> {

    ArrayList<ObjectRequest> arrayList;
    Context context;
    private LayoutInflater mInflater;

    ActivityCardAdapter(ArrayList<ObjectRequest> arrayList, Context context) {
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
            holder.iV_status_img.setImageResource(R.drawable.icon_waiting);
            holder.tV_status_msg.setText(oR.getSuppName()+ " is waiting for someone to foodilize their request of"+ oR.getQuantity()+" kg food.");
            //holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
            holder.cV_container.setCardBackgroundColor(Color.parseColor("#ec2e5f"));
        } else if (oR.getDeliveryStatus() == 1) {
            holder.iV_status_img.setImageResource(R.drawable.icon_otw);
            holder.tV_status_time.setTextColor(Color.parseColor("#3d5a71"));
            holder.tV_status_msg.setTextColor(Color.parseColor("#3d5a71"));

            holder.tV_status_msg.setText(oR.getNgoName()+" is otw to foodilize "+oR.getQuantity()+"kg food from "+oR.getSuppName());
            //holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
            holder.cV_container.setCardBackgroundColor(Color.parseColor("#ffeb00"));
        } else {
            holder.iV_status_img.setImageResource(R.drawable.icon_stareyes);
            holder.tV_status_msg.setText(oR.getNgoName()+" has foodilized "+oR.getQuantity()+"kg food on request from "+oR.getSuppName());
            //holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
            holder.cV_container.setCardBackgroundColor(Color.parseColor("#5A896F"));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iV_status_img;
        TextView tV_status_msg;
        TextView tV_status_time;
        CardView cV_container;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iV_status_img = itemView.findViewById(R.id.iV_activity_card_status_img);
            tV_status_msg = itemView.findViewById(R.id.tV_activity_card_status_text);
            tV_status_time = itemView.findViewById(R.id.tV_activity_card_status_time);
            cV_container = itemView.findViewById(R.id.cV_activity_card);

            //TODO: Perform onCLick response
            cV_container.setOnClickListener(v->{

            });
        }
    }
}
