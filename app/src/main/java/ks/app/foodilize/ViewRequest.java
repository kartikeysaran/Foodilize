package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import pl.droidsonroids.gif.GifImageView;

public class ViewRequest extends AppCompatActivity {

    TextView tV_title, tV_desc, tV_foodQ, tV_accepted_head, tV_supp_name, tV_ngo_name;
    Button btn_accept, btn_decline;
    ImageView iV_supp_profile, iV_ngo_profile, iV_bg;
    GifImageView iV_status_img;
    LinearLayout lL_accepted, lL_accept_reject, lL_supp;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        tV_title = findViewById(R.id.tV_view_request_title);
        tV_desc = findViewById(R.id.tV_view_request_desc);
        tV_foodQ = findViewById(R.id.tV_view_request_quant);
        tV_accepted_head = findViewById(R.id.tV_view_request_accepted_head);
        tV_supp_name = findViewById(R.id.tV_view_request_supp_name);
        tV_ngo_name = findViewById(R.id.tV_view_request_ngo_name);
        iV_ngo_profile = findViewById(R.id.iV_view_request_profile_ngo);
        iV_supp_profile = findViewById(R.id.iV_view_request_profile);
        iV_bg = findViewById(R.id.iV_view_request_bg);
        iV_status_img = findViewById(R.id.iV_view_request_status_img);
        lL_accept_reject = findViewById(R.id.layout_view_request_accept_decline);
        lL_accepted = findViewById(R.id.layout_view_request_accepted);
        btn_accept = findViewById(R.id.btn_view_request_accept);
        btn_decline = findViewById(R.id.btn_view_request_decline);
        lL_supp = findViewById(R.id.layout_view_request_supp);

        Intent intent = getIntent();
        if(intent.getSerializableExtra("REQUEST")!=null) {
            ObjectRequest mRequest = (ObjectRequest) getIntent().getSerializableExtra("REQUEST");
            loadData(mRequest);


        } else {
            finish();
            Toast.makeText(this, "Error! Please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(ObjectRequest request) {
        if(request.getDeliveryStatus() == 0) {
            iV_status_img.setImageResource(R.drawable.foodcreated);
            iV_bg.setImageResource(R.drawable.bg_crockery);
            tV_title.setText(request.getSuppName()+ " is waiting for someone to foodilize their request of "+ request.getQuantity()+" kg food.");
            //tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        } else if (request.getDeliveryStatus() == 1) {
            iV_status_img.setImageResource(R.drawable.fooddelivery_unscreen);
            iV_bg.setImageResource(R.drawable.bg_otw);
            tV_title.setText(request.getNgoName()+" is otw to foodilize "+request.getQuantity()+" kg food from "+request.getSuppName());
            //holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        } else {
            iV_status_img.setImageResource(R.drawable.celebrate);
            iV_bg.setImageResource(R.drawable.delivered);
            tV_title.setText(request.getNgoName()+" has foodilized "+request.getQuantity()+" kg food on request from "+request.getSuppName());
            //holder.tV_status_time.setText(Utils.getTimeDifference(oR.getTime()));
        }
        tV_supp_name.setText(request.getSuppName());
        tV_foodQ.setText(request.getQuantity()+"Kg");
        tV_desc.setText(request.getDesc());
        Utils.db.collection("users")
                .whereEqualTo("Id", request.getSuppId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ObjectNC object = document.toObject(ObjectNC.class);
                                    if(!object.getImgUrl().isEmpty() && object.getImgUrl()!=null) {
                                        iV_supp_profile.setImageBitmap(Utils.StringToBitMap(object.getImgUrl()));
                                        lL_supp.setOnClickListener(v->{
                                            Intent i = new Intent(ViewRequest.this, ViewNGO.class);
                                            i.putExtra("USER", object);
                                            startActivity(i);
                                        });
                                    }

                                }
                            } else {
                            }

                        } else {
                        }
                    }
                });

        if(request.getNgoId()!=null && !request.getNgoName().isEmpty() && !request.getNgoId().isEmpty()) {
            lL_accepted.setVisibility(View.VISIBLE);
            lL_accept_reject.setVisibility(View.GONE);
            tV_accepted_head.setVisibility(View.VISIBLE);
            tV_ngo_name.setText(request.getNgoName());
            Utils.db.collection("users")
                    .whereEqualTo("Id", request.getNgoId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        ObjectNC object = document.toObject(ObjectNC.class);
                                        if(!object.getImgUrl().isEmpty() && object.getImgUrl()!=null) {
                                            iV_ngo_profile.setImageBitmap(Utils.StringToBitMap(object.getImgUrl()));
                                            lL_accepted.setOnClickListener(v->{
                                                Intent i = new Intent(ViewRequest.this, ViewNGO.class);
                                                i.putExtra("USER", object);
                                                startActivity(i);
                                            });
                                        }
                                    }
                                } else {
                                }

                            } else {
                            }
                        }
                    });
        } else {
            lL_accepted.setVisibility(View.GONE);
            tV_accepted_head.setVisibility(View.GONE);
            if(Utils.currentUser.getType() == 0) {
                lL_accept_reject.setVisibility(View.VISIBLE);
                btn_decline.setOnClickListener(v->{
                    finish();
                });
                btn_accept.setOnClickListener(v->{
                    if(request.getId()!=null) {
                        request.setDeliveryStatus(1);
                        request.setNgoId(Utils.currentUser.getId());
                        request.setNgoName(Utils.currentUser.getName());
                        request.setTime(String.valueOf(LocalDateTime.now()));

                        Utils.db.collection("requests")
                                .document(request.getId())
                                .set(request)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewRequest.this, "Error! Please try again later: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            } else {
                lL_accept_reject.setVisibility(View.GONE);
                lL_accepted.setVisibility(View.GONE);
                tV_accepted_head.setVisibility(View.GONE);
            }


        }
    }
}