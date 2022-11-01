package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ks.app.foodilize.ui.main.ActivityCardAdapter;
import ks.app.foodilize.ui.main.CardAdapter;

public class ViewNGO extends AppCompatActivity {
    TextView name, cont_name, status;
    ActivityCardAdapter cardAdapter;
    RecyclerView recyclerView;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ngo);
        Intent intent = getIntent();
        name = findViewById(R.id.tV_view_ngo_name);
        cont_name = findViewById(R.id.tV_view_ngo_cont_name);
        status = findViewById(R.id.tV_view_ngo_status);
        recyclerView = findViewById(R.id.rV_view_ngo);
        profile = findViewById(R.id.iV_view_ngo_profile);
        if(intent.getSerializableExtra("USER")!=null) {
            ObjectNC mUser = (ObjectNC) getIntent().getSerializableExtra("USER");
            loadData(mUser);

        } else {
            finish();
            Toast.makeText(this, "Error! Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(ObjectNC user) {
        name.setText(user.getName());
        status.setText(user.getStatus());
        cont_name.setText(user.getContact_name());
        profile.setImageBitmap(Utils.StringToBitMap(user.getImgUrl()));
        findViewById(R.id.iV_view_ngo_map).setOnClickListener(v->{
            String strUri = "http://maps.google.com/maps?q=loc:" + 28.644800 + "," + 77.216721 + " (" + user.getName() + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        });
        findViewById(R.id.iV_view_ngo_call).setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + user.getContact_number()));
                startActivity(intent);
            }
        });
        findViewById(R.id.iV_view_ngo_chat).setOnClickListener(v->{
            Toast.makeText(this, "Ye next version me aaega", Toast.LENGTH_SHORT).show();
        });
        ArrayList<ObjectRequest> arrayList = new ArrayList<>();
        if(user.getType() == 1) {
            Utils.db.collection("requests")
                    .whereEqualTo("suppId", user.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.e("VIEWNGO", "1 Task Successful");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ObjectRequest oR = document.toObject(ObjectRequest.class);
                                    oR.setId(document.getId());
                                    arrayList.add(oR);
                                }
                                Log.e("VIEWNGO", "ArrayList Size: "+arrayList.size());
                                cardAdapter = new ActivityCardAdapter(arrayList, ViewNGO.this);
                                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(ViewNGO.this);
                                recyclerView.setLayoutManager(linearLayoutManager3);
                                recyclerView.setAdapter(cardAdapter);
                            } else {
                            }
                        }
                    });

        } else {
            Utils.db.collection("requests")
                    .whereEqualTo("ngoId", user.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.e("VIEWNGO", "0 Task Successful");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ObjectRequest oR = document.toObject(ObjectRequest.class);
                                    oR.setId(document.getId());
                                    arrayList.add(oR);
                                }
                                Log.e("VIEWNGO", "ArrayList Size: "+arrayList.size());
                                cardAdapter = new ActivityCardAdapter(arrayList, ViewNGO.this);
                                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(ViewNGO.this);
                                recyclerView.setLayoutManager(linearLayoutManager3);
                                recyclerView.setAdapter(cardAdapter);
                            } else {
                            }
                        }
                    });
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}