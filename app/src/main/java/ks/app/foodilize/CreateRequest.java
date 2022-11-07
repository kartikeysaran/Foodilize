package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static ks.app.foodilize.Utils.db;

public class CreateRequest extends AppCompatActivity {

    EditText eT_quan, eT_desc, eT_date_expiry, eT_time_expiry;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        eT_quan = findViewById(R.id.eV_create_request_food_quantity);
        eT_desc = findViewById(R.id.eV_create_request_desc_opt);
        eT_time_expiry = findViewById(R.id.eV_create_request_exp_time);
        eT_date_expiry = findViewById(R.id.eV_create_request_exp_date);

        eT_time_expiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Log.e("TAG", "Click on et_time");
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(CreateRequest.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            eT_time_expiry.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Expiry Time");
                    mTimePicker.show();
                }
            }
        });

        eT_date_expiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Log.e("TAG", "Click on et_date");
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(CreateRequest.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            selectedmonth = selectedmonth + 1;
                            eT_date_expiry.setText("" + selectedday + "-" + selectedmonth + "-" + selectedyear);
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select Expiry Date");
                    mDatePicker.show();
                }
            }
        });




        findViewById(R.id.btn_create_request).setOnClickListener(v->{
            ObjectRequest oR = new ObjectRequest("", Utils.currentUser.getId(), "", Utils.currentUser.getName(), 0,String.valueOf(LocalDateTime.now()), Integer.parseInt(eT_quan.getText().toString()), eT_desc.getText().toString());

            String str_exp_time = eT_time_expiry.getText().toString() + " "+eT_date_expiry.getText().toString()+":00";

            Map<String, Object> request = new HashMap<>();
            request.put("ngoId", oR.getNgoId());
            request.put("suppId", oR.getSuppId());
            request.put("ngoName", oR.getNgoName());
            request.put("suppName", oR.getSuppName());
            request.put("deliveryStatus", oR.getDeliveryStatus());
            request.put("time", oR.getTime());
            request.put("quantity", oR.getQuantity());
            request.put("desc", oR.getDesc());
            request.put("expTime", str_exp_time);

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