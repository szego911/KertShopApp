package hu.alkfejl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameEditText;
    EditText userEmailEditText;
    EditText userAddressEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;


    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Bundle bundle = getIntent().getExtras();
        // int secret_key = bundle.getInt("SECRET_KEY");
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        userNameEditText = findViewById(R.id.usernameEditText);
        userEmailEditText = findViewById(R.id.emailEditText);
        userAddressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String email = preferences.getString("email", "");
        String address = preferences.getString("address", "");
        String password = preferences.getString("password", "");

        userNameEditText.setText(userName);
        userEmailEditText.setText(email);
        userAddressEditText.setText(address);
        passwordEditText.setText(password);
        passwordConfirmEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    public void register(View view) {

        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String address = userAddressEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "A két jelszó nem egyezik!", Toast.LENGTH_LONG).show();

            return;
        }

        HashMap<String, Object> userFirebase = new HashMap<>();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.i(LOG_TAG, "Regisztrált: " + userName + ", e-mail: " + email);
                Log.d(LOG_TAG, "Felhasználó létrehozva sikeresen!");
                Toast.makeText(this, "Felhasználó létrehozva sikeresen!", Toast.LENGTH_LONG).show();

                userFirebase.put("username",userName);
                userFirebase.put("email",email);
                userFirebase.put("address",address);
                userFirebase.put("password",password);

                FirebaseFirestore.getInstance().collection("User")
                                .document(email)
                                        .set(userFirebase)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RegistrationActivity.this,"Felhasználó adatai mentve!",Toast.LENGTH_SHORT).show();
                                                        Log.d(LOG_TAG, "Felhasználó adatok elmentve!");
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(LOG_TAG, "Felhasználó adatok mentése sikertelen!");
                                                                Toast.makeText(RegistrationActivity.this,"Nem sikerült a mentés: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                startShopping(email);
            } else {
                Log.d(LOG_TAG, "Nem tudtuk létrehozni a felhasználót");
                Toast.makeText(RegistrationActivity.this, "Nem tudtuk létrehozni a felhasználót: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    private void startShopping(String userEmail) {
        Intent intent = new Intent(this, KertshopActivity.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
