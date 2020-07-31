package com.example.playconnect;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;

public class SignUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();;
    public DateTime dateOfBirth;
    public String imageURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_up);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(View view) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    Task<Uri> downloadUrl = ref.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL = uri.toString();


                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    public void setdatePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView DateView = (TextView) findViewById(R.id.DateView);
        String date = dayOfMonth + "/" + month + "/" + year;
        DateView.setText(date);
        dateOfBirth = new DateTime(new Date(year, month, dayOfMonth));
    }

    public void validation(View view) {
        // Phone number validation
        boolean valid = true;
        EditText enterName = (EditText) findViewById(R.id.editText);
        String userName = enterName.getText().toString();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String userGender = radioButton.getText().toString();

        DateTime now = new DateTime(Calendar.getInstance().getTime());
        Integer age = Years.yearsBetween(dateOfBirth, now).getYears();
        if (age < 15) {
            Toast.makeText(this, "This app requires everyone to be at least 15 years old before they can create an account", Toast.LENGTH_LONG).show();
            valid = false;
        }

        EditText userNumber = (EditText) findViewById(R.id.getNo);
        String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        Matcher m;
        Pattern r = Pattern.compile(pattern);
        m = r.matcher(userNumber.getText().toString().trim());
        if (!m.find() || userNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please check your mobile number", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid == true) {
            String userId = CreateUserInDataBase(userNumber.getText().toString(), userName, userGender, age);
            Intent intent = new Intent(getApplicationContext(), CheckOtp.class);
            Log.i("Numberrrrr", userNumber.getText().toString());
            intent.putExtra("Mobile_no", userNumber.getText().toString());
            intent.putExtra("UserId",userId);
            startActivity(intent);
        }
    }

    final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

    public String CreateUserInDataBase(String PhoneNo, String name, String gender, Integer age) {
        User user = new User(name, gender, PhoneNo, age);
        if (imageURL != null) {
            user.setImageURL(imageURL);
        }
        String id =firebaseUser.getUid();
        databaseRef.child(id).setValue(user);
        return id;

    }
}
