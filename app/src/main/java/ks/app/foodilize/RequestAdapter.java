package ks.app.foodilize;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.makeramen.roundedimageview.RoundedImageView;



public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    ArrayList<ObjectRequest> arrayList;
    LayoutInflater mInflater;
    Context context;

    public RequestAdapter(ArrayList<ObjectRequest> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @NotNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.request_card, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestAdapter.ViewHolder holder, int position) {
        ObjectRequest oR = arrayList.get(position);
        holder.title.setText(oR.getSuppName()+" created a request to Foodilize "+oR.getQuantity()+" kg food");
        //holder.distance.setText("3 km a");
        holder.time.setText(Utils.getTimeDifference(oR.getTime()));
        holder.accept.setOnClickListener(v->{
            if(oR.getId()!=null) {
                oR.setDeliveryStatus(1);
                oR.setNgoId(Utils.currentUser.getId());
                oR.setNgoName(Utils.currentUser.getName());
                oR.setTime(String.valueOf(LocalDateTime.now()));

                Utils.db.collection("requests")
                        .document(oR.getId())
                        .set(oR)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                arrayList.remove(oR);
                                notifyItemRemoved(position);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });
            }
        });
        holder.decline.setOnClickListener(v->{
            //Place this at last
            arrayList.remove(oR);
            notifyItemRemoved(position);
        });
        holder.container.setOnClickListener(v->{
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
        RoundedImageView imageView;
        TextView title;
        TextView time;
        TextView distance;
        TextView decline;
        TextView accept;
        CardView container;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iV_request_card_image);
            title = itemView.findViewById(R.id.tV_request_card_title);
            time = itemView.findViewById(R.id.tV_request_card_time);
            distance = itemView.findViewById(R.id.tV_request_card_distance);
            decline = itemView.findViewById(R.id.tV_request_card_decline);
            accept = itemView.findViewById(R.id.tV_request_card_accept);
            container = itemView.findViewById(R.id.container_request_card);
        }
    }
}
