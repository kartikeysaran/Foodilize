package ks.app.foodilize.ui.main;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ks.app.foodilize.ObjectNC;
import ks.app.foodilize.ObjectRequest;
import ks.app.foodilize.R;
import ks.app.foodilize.Utils;

public class fragment_ngo_activity extends Fragment {

    RecyclerView rV_top_supplier, rV_top_ngo, rV_activity;
    CardAdapter cardAdapter,cardAdapter2;
    ActivityCardAdapter activityCardAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<ObjectRequest> arrayList = new ArrayList<>();
    ArrayList<ObjectNC> topSupplier = new ArrayList<>();
    ArrayList<ObjectNC> topNgo = new ArrayList<>();

    HashMap<String, Integer> topSupplierHashMap = new HashMap<>();
    HashMap<String, Integer> topNgoHashMap = new HashMap<>();

    public fragment_ngo_activity() {
    }

    public static fragment_ngo_activity newInstance() {
        fragment_ngo_activity fragment = new fragment_ngo_activity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ngo_activity, container, false);

        rV_top_ngo = view.findViewById(R.id.rV_ngo_activity_top_ngo);
        rV_top_supplier = view.findViewById(R.id.rV_ngo_activity_top_supplier);
        rV_activity = view.findViewById(R.id.rV_ngo_activity_activity);
        swipeRefreshLayout = view.findViewById(R.id.sRL_ngo_activity);

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

        topNgo = new ArrayList<>();
        topSupplier = new ArrayList<>();
        arrayList = new ArrayList<>();
        Utils.db.collection("requests")
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
                            Log.e("ACTIVITY", "Array List Request Size: "+arrayList.size());
                            updateViews();
                        } else {
                        }
                    }
                });
    }

    private void updateViews() {
        for (ObjectRequest oR: arrayList) {
            if(!topSupplierHashMap.containsKey(oR.getSuppId())) {
                topSupplierHashMap.put(oR.getSuppId(), 1);
            } else {
                int a = topSupplierHashMap.get(oR.getSuppId());
                topSupplierHashMap.put(oR.getSuppId(), a+1);
            }

            if(!topNgoHashMap.containsKey(oR.getNgoId())) {
                topNgoHashMap.put(oR.getNgoId(), 1);
            } else {
                int a = topNgoHashMap.get(oR.getNgoId());
                topNgoHashMap.put(oR.getNgoId(), a+1);
            }

        }

        ArrayList<String> topSuppIds = new ArrayList<String>(sortByValue(topSupplierHashMap).keySet());
        Collections.reverse(topSuppIds);
        ArrayList<String> topNgoIds = new ArrayList<String>(sortByValue(topNgoHashMap).keySet());
        Collections.reverse(topNgoIds);


        for(int i = 0; i < topNgoIds.size(); i++) {
            int k = i;
            Utils.db.collection("users")
                    .whereEqualTo("id", topNgoIds.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ObjectNC oM = document.toObject(ObjectNC.class);
                                    topNgo.add(oM);
                                    cardAdapter.notifyItemInserted(k);

                                }
                            } else {

                            }
                        }
                    });
        }

        for(int i = 0; i < topSuppIds.size(); i++) {
            int k = i;
            Utils.db.collection("users")
                    .whereEqualTo("id", topSuppIds.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ObjectNC oM = document.toObject(ObjectNC.class);
                                    topSupplier.add(oM);
                                    cardAdapter2.notifyItemInserted(k);
                                }
                            } else {

                            }
                        }
                    });
        }

        cardAdapter = new CardAdapter(getContext(), topNgo);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rV_top_ngo.setLayoutManager(linearLayoutManager2);
        rV_top_ngo.setAdapter(cardAdapter);

        cardAdapter2 = new CardAdapter(getContext(), topSupplier);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rV_top_supplier.setLayoutManager(linearLayoutManager);
        rV_top_supplier.setAdapter(cardAdapter2);

        activityCardAdapter = new ActivityCardAdapter(arrayList, getContext());
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        rV_activity.setLayoutManager(linearLayoutManager3);
        rV_activity.setAdapter(activityCardAdapter);

    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


}