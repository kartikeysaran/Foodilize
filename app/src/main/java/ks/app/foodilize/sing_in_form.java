package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class sing_in_form extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    private int id;
    private String name;
    private String address;
    private String contact_name;
    private String contact_number;
    private double lat;
    private double lon;
    private String imgUrl;
    private int type;

    EditText edt_name, edt_cont_name, edt_cont_number, edt_address;
    RadioButton rB_ngo, rB_supplier;
    RadioGroup rG;
    RoundedImageView profile;
    ImageView add_Profile;
    private String Document_img1="";
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_form);

        Intent intent = getIntent();
        account = intent.getParcelableExtra("account");
        Utils.db = FirebaseFirestore.getInstance();

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition willexecute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(sing_in_form.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            lat = gps.getLatitude();
            lon = gps.getLongitude();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        edt_address = findViewById(R.id.tV_sign_in_org_address);
        edt_name = findViewById(R.id.tV_sign_in_org_name);
        edt_cont_name = findViewById(R.id.tV_sign_in_org_contact_person);
        edt_cont_number = findViewById(R.id.tV_sign_in_org_contact);
        rB_ngo = findViewById(R.id.ngo);
        rB_supplier = findViewById(R.id.supplier);
        rG = findViewById(R.id.tV_sign_in_radio);
        add_Profile = findViewById(R.id.img_sign_in_icon_add);
        profile = findViewById(R.id.img_profile_sign_in);

        profile.setOnClickListener(v->{
            selectImage();
        });

        findViewById(R.id.btn_sing_in_org_next).setOnClickListener(v->{
            if(edt_name.getText().toString().isEmpty() || edt_cont_number.getText().toString().isEmpty() || edt_cont_name.getText().toString().isEmpty()|| edt_address.getText().toString().isEmpty() || rG.getCheckedRadioButtonId() == -1 || Document_img1.isEmpty()) {
            } else {
                //TODO: Create user and save into database and forward it to dashboard
                Map<String, Object> user = new HashMap<>();
                user.put("id", account.getId());
                user.put("emailId", account.getEmail());
                user.put("name", edt_name.getText().toString());
                user.put("address", edt_address.getText().toString());
                user.put("contact_name", edt_cont_name.getText().toString());
                user.put("contact_number", edt_cont_number.getText().toString());
                user.put("lat", lat);
                user.put("lon", lon);
                user.put("imgUrl", Document_img1);
                user.put("status", "available");
                type = rB_ngo.isChecked()?0:1;
                user.put("type", type);
                Utils.db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Utils.currentUser = new ObjectNC(account.getId(), edt_name.getText().toString(), edt_address.getText().toString(), edt_cont_name.getText().toString(), edt_cont_number.getText().toString(), lat, lon, Document_img1, type, account.getEmail(), "Available");
                                startActivity(new Intent(sing_in_form.this, Ngo_Dashboard.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        
        
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(sing_in_form.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    add_Profile.setVisibility(View.GONE);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    add_Profile.setVisibility(View.GONE);
                    thumbnail=getResizedBitmap(thumbnail, 400);
                    BitMapToString(thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        profile.setImageBitmap(Utils.StringToBitMap(Document_img1));
        profile.setAlpha(1);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}