package ks.app.foodilize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

    private double lat;
    private double lon;
    private int type = 0;

    EditText edt_name, edt_cont_name, edt_cont_number, edt_address;

    RoundedImageView profile;
    TextView uploadText;
    private String Document_img1="";
    GoogleSignInAccount account;
    Switch aSwitch;
    TextView tVswitchNGO, tVswitchSupp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_form);

        Intent intent = getIntent();
        account = intent.getParcelableExtra("account");
        tVswitchNGO = findViewById(R.id.tV_switch_NGO);
        tVswitchSupp = findViewById(R.id.tV_switch_supp);

        edt_address = findViewById(R.id.tV_sign_in_org_address);
        edt_name = findViewById(R.id.tV_sign_in_org_name);
        edt_cont_name = findViewById(R.id.tV_sign_in_org_contact_person);
        edt_cont_number = findViewById(R.id.tV_sign_in_org_contact);

        aSwitch = findViewById(R.id.switch_NGO_Supp);

        uploadText = findViewById(R.id.tV_sign_in_upload);
        profile = findViewById(R.id.img_profile_sign_in);

        profile.setOnClickListener(v->{
            selectImage();
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    type = 0;
                    tVswitchNGO.setTypeface(null, Typeface.BOLD);
                    tVswitchSupp.setTypeface(null, Typeface.NORMAL);
                    if(Document_img1.isEmpty()) {
                        profile.setImageResource(R.drawable.demo_ngo_build);
                    }
                } else {
                    type = 1;
                    tVswitchNGO.setTypeface(null, Typeface.NORMAL);
                    tVswitchSupp.setTypeface(null, Typeface.BOLD);
                    if(Document_img1.isEmpty()) {
                        profile.setImageResource(R.drawable.demo_user);
                    }
                }
            }
        });

        findViewById(R.id.btn_sing_in_org_next).setOnClickListener(v->{
            if(edt_name.getText().toString().isEmpty()) {
                 edt_name.setError("Cannot be left empty");
            } else if (edt_cont_number.getText().toString().isEmpty()) {
                edt_cont_number.setError("Cannot be left empty");
            } else if (edt_cont_name.getText().toString().isEmpty()) {
                edt_cont_name.setError("Cannot be left empty");
            } else if (edt_address.getText().toString().isEmpty()){
                edt_address.setError("Cannot be left empty");
            } else if (Document_img1.isEmpty()) {
                Toast.makeText(this, "Please Upload a Picture", Toast.LENGTH_SHORT).show();
            } else {
                ProgressDialog progressBar = new ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Setting up your profile");
                //progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
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
                //type = rB_ngo.isChecked()?0:1;
                user.put("type", type);
                Utils.db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressBar.setProgress(100);
                                Utils.currentUser = new ObjectNC(account.getId(), edt_name.getText().toString(), edt_address.getText().toString(), edt_cont_name.getText().toString(), edt_cont_number.getText().toString(), lat, lon, Document_img1, type, account.getEmail(), "Available");
                                startActivity(new Intent(sing_in_form.this, Ngo_Dashboard.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setProgress(100);
                                progressBar.dismiss();
                                Toast.makeText(sing_in_form.this, "Error! "+e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                                Log.e("SIGNUP", e.getMessage());
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
                    uploadText.setVisibility(View.GONE);
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
                    uploadText.setVisibility(View.GONE);
                    thumbnail=getResizedBitmap(thumbnail, 400);
                    BitMapToString(thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(!Document_img1.isEmpty()) {
                profile.setImageBitmap(Utils.StringToBitMap(Document_img1));
                profile.setAlpha(1f);
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
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