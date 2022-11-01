package ks.app.foodilize.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ks.app.foodilize.AcceptedCardAdapter;
import ks.app.foodilize.DotsIndicatorDecoration;
import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.RequestAdapter;
import ks.app.foodilize.Utils;

public class fragment_ngo_home extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rV_request, rV_accepted;
    RequestAdapter requestAdapter;
    ArrayList<ObjectRequest> arrayList, arrayList1;
    AcceptedCardAdapter acceptedCardAdapter;

    public fragment_ngo_home() {

    }


    public static fragment_ngo_home newInstance() {
        fragment_ngo_home fragment = new fragment_ngo_home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO:  If Utils.currentUser == null then show error page
        View view = inflater.inflate(R.layout.fragment_ngo_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.sRL_ngo_home);
        rV_request = view.findViewById(R.id.rV_ngo_home_requests);
        rV_accepted = view.findViewById(R.id.rV_accepted_request);

        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        rV_request.setOnClickListener(v->{
            callData();
        });

        return view;
    }

    private void callData() {

        for(int i = 0; i < arrayList.size(); i++) {
            arrayList.remove(i);
            requestAdapter.notifyItemRemoved(i);
        }
        for(int i=0; i<arrayList1.size(); i++) {
            arrayList1.remove(i);
            acceptedCardAdapter.notifyItemRemoved(i);
        }
        Utils.db.collection("requests")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ObjectRequest oR = document.toObject(ObjectRequest.class);
                                oR.setId(document.getId());
                                if(oR.getDeliveryStatus()==0) {
                                    arrayList.add(oR);
                                }
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            rV_request.setLayoutManager(linearLayoutManager);;
                            requestAdapter = new RequestAdapter(arrayList,getContext());
                            rV_request.setAdapter(requestAdapter);
                        }
                    }
                });


        Utils.db.collection("requests")
                .whereEqualTo("ngoId", Utils.currentUser.getId().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ObjectRequest oR = document.toObject(ObjectRequest.class);
                                oR.setId(document.getId());
                                if (oR.getDeliveryStatus() == 1) {
                                    arrayList1.add(oR);
                                }
                            }
                            if(!arrayList1.isEmpty()) {
                                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                rV_accepted.setLayoutManager(linearLayoutManager2);
                                acceptedCardAdapter = new AcceptedCardAdapter(arrayList1, getContext());
                                rV_accepted.setAdapter(acceptedCardAdapter);
                                final int radius = getResources().getDimensionPixelSize(R.dimen.radius);
                                final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.dots_height);
                                final int color = ContextCompat.getColor(getContext(), R.color.pinkdusty);
                                rV_accepted.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, color, color));
                                new PagerSnapHelper().attachToRecyclerView(rV_accepted);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arrayList.clear();
    }
}