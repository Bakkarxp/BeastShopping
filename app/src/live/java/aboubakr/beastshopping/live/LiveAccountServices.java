package aboubakr.beastshopping.live;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.aboubakr.beastshopping.activities.RegisterActivity;
import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;
import com.aboubakr.beastshopping.infrastructure.Utils;
import com.aboubakr.beastshopping.services.AccountServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.Executor;


public class LiveAccountServices extends BaseLiveService {
    public LiveAccountServices(BeastShoppingApplication application) {
        super(application);
    }


    @Subscribe
    public void RegisterUser(final AccountServices.RegisterUserRequest request){
        AccountServices.RegisterUserResponse response = new AccountServices.RegisterUserResponse();
        if(request.userEmail.isEmpty()){
            response.setPropertyErrors("email","Please, put in your email");
        }
        if(request.userName.isEmpty()){
            response.setPropertyErrors("userName","Please, put in your name");
        }
        if(response.didSucceed()){
            request.progressDialog.show();

            SecureRandom random = new SecureRandom();
            final String randomPassWord = new BigInteger(32, random).toString();

            auth.createUserWithEmailAndPassword(request.userEmail,randomPassWord)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isComplete()){
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }else{

                                auth.sendPasswordResetEmail(request.userEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    request.progressDialog.dismiss();
                                                    Toast.makeText(application.getApplicationContext(),
                                                            task.getException().getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                } else {
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference reference = database.getReference(Utils.FIREBASE_USER_REFERENCE + Utils.encodeEmail(request.userEmail));
                                                    HashMap<String,Object> timeJoined = new HashMap<>();
                                                    timeJoined.put("dateJoined", ServerValue.TIMESTAMP);

                                                    reference.child("email").setValue(request.userEmail);
                                                    reference.child("name").setValue(request.userName);
                                                    reference.child("hasLoggedInWithPassword").setValue(false);
                                                    reference.child("dateJoined").setValue(timeJoined);

                                                    request.progressDialog.dismiss();

                                                    Toast.makeText(application.getApplicationContext(),
                                                            "Please, check your email",
                                                            Toast.LENGTH_LONG).show();





                                                }
                                            }

                                        });
                            }
                        }
                    });

        }
        bus.post(response);

    }


    @Subscribe
    public void LogInUser(final AccountServices.LogInUserRequest request){

        AccountServices.LogInUserResponse response = new AccountServices.LogInUserResponse();
        if(request.userEmail.isEmpty()){
            response.setPropertyErrors("email","Please, put in your email");
        }
        if(request.userPassword.isEmpty()){
            response.setPropertyErrors("password","Please, put in your password");
        }
        if(response.didSucceed()){
            request.progressDialog.show();
            auth.signInWithEmailAndPassword(request.userEmail,request.userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }else{
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference(Utils.FIREBASE_USER_REFERENCE +Utils.encodeEmail(request.userEmail));
                                reference.child("hasLoggedInWithPassword").setValue(true);
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),
                                        "User has logged in",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
        bus.post(response);
    }

    @Subscribe
    public void FacebookLogin(final AccountServices.LogUserInFacebookRequest request){
        request.progressDialog.show();
        AuthCredential authCredential = FacebookAuthProvider.getCredential(request.accessToken.getToken());
        auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    request.progressDialog.dismiss();
                    Toast.makeText(application.getApplicationContext(),
                            task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference reference = database.getReference(Utils.FIREBASE_USER_REFERENCE + Utils.encodeEmail(request.userEmail));
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()==null){
                                HashMap<String,Object> timeJoined = new HashMap<>();
                                timeJoined.put("dateJoined", ServerValue.TIMESTAMP);
                                reference.child("email").setValue(request.userEmail);
                                reference.child("name").setValue(request.userName);
                                reference.child("hasLoggedInWithPassword").setValue(true);
                                reference.child("dateJoined").setValue(timeJoined);
                                Log.d("Test", "onDataChange: request.userEmail");


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            request.progressDialog.dismiss();
                            Toast.makeText(application.getApplicationContext(),
                                    databaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    request.progressDialog.dismiss();
                    Toast.makeText(application.getApplicationContext(),
                            "User has loged in.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}
