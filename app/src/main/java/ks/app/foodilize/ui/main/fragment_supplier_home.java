package ks.app.foodilize.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import ks.app.foodilize.CreateRequest;
import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;

public class fragment_supplier_home extends Fragment {

    ArrayList<ObjectRequest> arrayList = new ArrayList<>();
    ActivityCardAdapter activityCardAdapter;
    RecyclerView rvSelfReqeust;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tV;
    RelativeLayout noResults;
    public fragment_supplier_home() {
    }

    public static fragment_supplier_home newInstance() {
        fragment_supplier_home fragment = new fragment_supplier_home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_home, container, false);
        view.findViewById(R.id.btn_supplier_home_create_request).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), CreateRequest.class));
        });
        rvSelfReqeust = view.findViewById(R.id.rV_self_activity_supplier_home);
        swipeRefreshLayout = view.findViewById(R.id.sRL_supplier_home);
        tV = view.findViewById(R.id.tV_supplier_home_request_history);
        noResults = view.findViewById(R.id.supplier_home_no_result_found);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void callData() {
        /*
        for(int i = 0; i < arrayList.size(); i++) {
            arrayList.remove(i);
            activityCardAdapter.notifyItemRemoved(i);
        }*/
        arrayList = new ArrayList<>();
        Utils.db.collection("requests")
                .whereEqualTo("suppId", Utils.currentUser.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ObjectRequest oR = document.toObject(ObjectRequest.class);
                                oR.setId(document.getId());
                                arrayList.add(oR);
                            }
                            if(!arrayList.isEmpty()||arrayList.size()>0) {
                                activityCardAdapter = new ActivityCardAdapter(arrayList, getContext());
                                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
                                rvSelfReqeust.setLayoutManager(linearLayoutManager3);
                                rvSelfReqeust.setAdapter(activityCardAdapter);
                            } else {
                                noResults.setVisibility(View.VISIBLE);
                            }

                        } else {
                            noResults.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

}