package com.example.taskmaster;
/*
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthCredential;
import java.util.Objects;
 */
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SesionActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    /*
        GoogleSignInClient googleSignInClient;
     */

    Button btnInicio;
    TextView registro;

    EditText txtEmailIs, txtContraIs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        btnInicio = findViewById(R.id.btnInicio);
        registro = findViewById(R.id.txtRegistroIs);
        txtEmailIs = findViewById(R.id.txtEmailIs);
        txtContraIs = findViewById(R.id.txtContraIs);


        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Si el usuario está autenticado, cargar sus datos
            startActivity(new Intent(SesionActivity.this, MainActivity.class));
        }


        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmailIs.getText().toString().trim();
                String contrasena = txtContraIs.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!contrasena.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, contrasena).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(SesionActivity.this, "Bienvenido!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SesionActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SesionActivity.this, "Algo ha salido mal...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(SesionActivity.this, "El campo contraseña no puede estar vacio", Toast.LENGTH_SHORT).show();
                    }
                } else if (email.isEmpty()) {
                    Toast.makeText(SesionActivity.this, "El campo email no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SesionActivity.this, "Introduzca un email valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SesionActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

    }
}
        /*
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(SesionActivity.this, options);

        auth = FirebaseAuth.getInstance();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

         */
    /*
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {
                            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SesionActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SesionActivity.this, SesionActivity.class));
                                }
                            });
                        }
                    }
                });
                FirebaseAuth.getInstance().signOut();
            }
        });
        */

// btnSignOut = findViewById(R.id.btnSignOut);
// btnSignIn = findViewById(R.id.btnSignIn);

    /*
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

            if (o.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SesionActivity.this, MainActivity.class));
                                Toast.makeText(SesionActivity.this, "Has iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("SesionActivity", "no: " + task.getException());
                                Toast.makeText(SesionActivity.this, "Fallo al iniciar sesión: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                    Log.e("SesionActivity", "Exception status code: " + e.getStatusCode());
                    Log.e("SesionActivity", "Exception message: " + e.getMessage());
                }
            }
        }
    });

     */