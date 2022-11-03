package ks.app.foodilize;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

import ks.app.foodilize.ui.main.ActivityCardAdapter;
import pl.droidsonroids.gif.GifImageView;

public class AcceptedCardAdapter extends RecyclerView.Adapter<AcceptedCardAdapter.ViewHolder> {

    ArrayList<ObjectRequest> arrayList;
    Context context;
    private LayoutInflater mInflater;

    public AcceptedCardAdapter(ArrayList<ObjectRequest> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.accepted_request_card, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ObjectRequest oR = arrayList.get(position);
        holder.title.setText("You are on your way to accept "+oR.getSuppName()+" foodilize request of "+ oR.getQuantity() +" food");
        holder.container.setOnClickListener(v->{
            Intent i = new Intent(context, ViewRequest.class);
            i.putExtra("REQUEST", oR);
            context.startActivity(i);
        });
        holder.delivered.setOnClickListener(v->{
            holder.title.setText("Thankyou! You are a Superhero");
            holder.bg.setImageResource(R.drawable.delivered);
            holder.gifs.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.INVISIBLE);
            oR.setTime(String.valueOf(LocalDateTime.now()));
            oR.setDeliveryStatus(2);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
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
                    },
                    1000
            );
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button delivered;
        LinearLayout gifs;
        ImageView bg;
        ConstraintLayout container;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            delivered = itemView.findViewById(R.id.btn_accepted_request_delivered);
            title = itemView.findViewById(R.id.tV_accepted_request_title);
            gifs = itemView.findViewById(R.id.lL_gifs);
            bg = itemView.findViewById(R.id.iV_accepted_card_bg);
            container = itemView.findViewById(R.id.container_accepted_request);
        }
    }
}
