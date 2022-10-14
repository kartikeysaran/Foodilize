package ks.app.foodilize.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import ks.app.foodilize.MainActivity;
import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;


public class fragment_ngo_profile extends Fragment {

    RoundedImageView rIV_pic;
    TextView tV_ngo_name, tV_ngo_cont_name, tV_sign_out, tV_delete_account;
    SwipeRefreshLayout swipeRefreshLayout;
    Button status_away, status_available, status_working;

    public fragment_ngo_profile() {
    }

    public static fragment_ngo_profile newInstance() {
        fragment_ngo_profile fragment = new fragment_ngo_profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ngo_profile, container, false);
        rIV_pic = view.findViewById(R.id.iV_ngo_profile_dp);
        tV_ngo_name = view.findViewById(R.id.tV_ngo_profile_role);
        tV_ngo_cont_name = view.findViewById(R.id.tV_ngo_profile_name);
        tV_delete_account = view.findViewById(R.id.tV_ngo_profile_delete);
        tV_sign_out = view.findViewById(R.id.tV_ngo_profile_signout);
        swipeRefreshLayout = view.findViewById(R.id.sRL_ngo_profile);
        status_available = view.findViewById(R.id.btn_profile_status_available);
        status_away = view.findViewById(R.id.btn_profile_status_away);
        status_working = view.findViewById(R.id.btn_profile_status_working);

        //TODO: Set the status by fetching it from the database
        if(Utils.currentUser.getStatus()!=null || !Utils.currentUser.getStatus().isEmpty()) {
            if(Utils.currentUser.getStatus() == "away") {
                status_away.setAlpha(1f);
                status_working.setAlpha(0.5f);
                status_available.setAlpha(0.5f);
            } else if (Utils.currentUser.getStatus() == "working") {
                status_away.setAlpha(0.5f);
                status_working.setAlpha(1f);
                status_available.setAlpha(0.5f);
            } else {
                status_away.setAlpha(0.5f);
                status_working.setAlpha(0.5f);
                status_available.setAlpha(1f);
            }
        } else {
            status_away.setAlpha(0.5f);
            status_working.setAlpha(0.5f);
            status_available.setAlpha(1f);
        }

        swipeRefreshLayout.setEnabled(false);

        status_available.setOnClickListener(v->{
            if(Utils.currentUser.getStatus()!="available") {
                status_away.setAlpha(0.5f);
                status_working.setAlpha(0.5f);
                status_available.setAlpha(1f);
                setStatus("available");
            }
        });

        status_working.setOnClickListener(v->{
            if(Utils.currentUser.getStatus()!="working") {
            status_away.setAlpha(0.5f);
            status_working.setAlpha(1f);
            status_available.setAlpha(0.5f);
                setStatus("working");
            }
        });
        status_away.setOnClickListener(v->{
            if(Utils.currentUser.getStatus()!="away") {
                status_away.setAlpha(1f);
                status_working.setAlpha(0.5f);
                status_available.setAlpha(0.5f);
                setStatus("away");
            }
        });

        rIV_pic.setImageBitmap(Utils.StringToBitMap(Utils.currentUser.getImgUrl()));
        tV_ngo_name.setText(Utils.currentUser.getName());
        tV_ngo_cont_name.setText(Utils.currentUser.getContact_name());
        tV_sign_out.setOnClickListener(v->{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
            mGoogleSignInClient.signOut();
            Utils.currentUser = null;
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        });

        tV_delete_account.setOnClickListener(v->{
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account ?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.db.collection("users")
                                    .whereEqualTo("id", Utils.currentUser.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Utils.db.collection("users")
                                                            .document(document.getId())
                                                            .delete();
                                                    if (Utils.currentUser.getType()==1) {
                                                        Utils.db.collection("requests")
                                                                .whereEqualTo("suppId", Utils.currentUser.getId())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task1) {
                                                                        if(task1.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                ObjectRequest oR = document1.toObject(ObjectRequest.class);
                                                                                if(oR.getDeliveryStatus()==0) {
                                                                                    Utils.db.collection("requests")
                                                                                            .document(document1.getId())
                                                                                            .delete();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        //TODO: setDeliveryStatus to 1 for all the request accepted by the user
                                                        Utils.db.collection("requests")
                                                                .whereEqualTo("ngoId", Utils.currentUser.getId())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task2) {
                                                                        if(task2.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                                                ObjectRequest oR = document2.toObject(ObjectRequest.class);
                                                                                if(oR.getDeliveryStatus()!=2) {
                                                                                    oR.setDeliveryStatus(0);
                                                                                    Utils.db.collection("requests")
                                                                                            .document(oR.getId())
                                                                                            .set(oR)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                                                                }
                                                                                            });


                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    Utils.currentUser = null;
                                                    startActivity(new Intent(getContext(), MainActivity.class));
                                                    getActivity().finish();
                                                }
                                            }
                                        }
                                    });
                        }
                    })

                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        return view;
    }

    private void setStatus(String status) {
        Utils.currentUser.setStatus(status);
        Utils.db.collection("users")
                .whereEqualTo("id", Utils.currentUser.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Utils.db.collection("users")
                                        .document(document.getId())
                                        .set(Utils.currentUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                            }
                                        });
                            }
                        }
                    }
                });

    }
}