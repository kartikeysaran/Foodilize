package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

import static ks.app.foodilize.Utils.db;

public class CreateRequest extends AppCompatActivity {

    EditText eT_quan, eT_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        eT_quan = findViewById(R.id.eV_create_request_food_quantity);
        eT_desc = findViewById(R.id.eV_create_request_desc_opt);

        findViewById(R.id.btn_create_request).setOnClickListener(v->{
            ObjectRequest oR = new ObjectRequest("", Utils.currentUser.getId(), "", Utils.currentUser.getName(), 0,String.valueOf(System.currentTimeMillis()), Integer.parseInt(eT_quan.getText().toString()), eT_desc.getText().toString());

            Map<String, Object> request = new HashMap<>();
            request.put("ngoId", oR.getNgoId());
            request.put("suppId", oR.getSuppId());
            request.put("ngoName", oR.getNgoName());
            request.put("suppName", oR.getSuppName());
            request.put("deliveryStatus", oR.getDeliveryStatus());
            request.put("time", oR.getTime());
            request.put("quantity", oR.getQuantity());
            request.put("desc", oR.getDesc());

            //TODO: Create a check if there exists any old request than pop that up

            //Created a new request and added it to database

            db.collection("requests")
                    .add(request)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });


        });

    }
}