package ks.app.foodilize;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

import ks.app.foodilize.ui.main.ActivityCardAdapter;

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
        holder.delivered.setOnClickListener(v->{
            oR.setTime(String.valueOf(LocalDateTime.now()));
            oR.setDeliveryStatus(2);
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
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView delivered;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            delivered = itemView.findViewById(R.id.tV_accepted_request_delivered);
            title = itemView.findViewById(R.id.tV_accepted_request_title);
        }
    }
}
