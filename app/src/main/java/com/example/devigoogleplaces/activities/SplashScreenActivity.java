package com.example.devigoogleplaces.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.devigoogleplaces.R;
import com.example.devigoogleplaces.utils.SnackBar;
import com.google.android.material.snackbar.Snackbar;


public class SplashScreenActivity extends AppCompatActivity {
    public Typeface NIRMALA;
    public Typeface NIRMALAB;
    public Typeface NIRMALAS;
    /*@BindView(R.id.vT_as_app)
    TextView vTAsApp;*/
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 88;
    Handler mHandlerTime;
    Runnable mRunnableTimeOut;

    private int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setfullscreen();
        setTypeFace();
        setfullscreen();
        setvalues();
    }


    private void setfullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void setTypeFace() {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                //vTAsApp.setTypeface(NIRMALAB);


            }
        };
        r.run();
    }

    private void setvalues() {
        if (isTaskRoot() || !getIntent().hasCategory("android.intent.category.LAUNCHER") || getIntent().getAction() == null || !getIntent().getAction().equals("android.intent.action.MAIN")) {
            if (mayRequestReadWriteSd()) {
                if (checkLocationPermission()){
                   /* if (checkAccountsPermission()){*/
                        splashScreenCall();
                    /*}*/
                }

                return;
            } else {
                Log.e(" Permission", "enteredwrong");
                return;
            }
        }
        finish();
    }

    private void splashScreenCall() {
        mHandlerTime = new Handler();
        mRunnableTimeOut = new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        };
    }

    private boolean mayRequestReadWriteSd() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(SplashScreenActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(
                new String[]{Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_FOR_STORAGE_PERMISSION);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FOR_STORAGE_PERMISSION:
                Log.e("Permission", "entered");
                if (grantResults.length > 0) {
                    Log.e("Permission", "entered1");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        if (checkLocationPermission()){
                            splashScreenCall();
                        }
                        Log.e("Permission", "entered2");
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale
                                        (SplashScreenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showPermissionRationaleSnackBar();
                        } else {
                            Toast.makeText(SplashScreenActivity.this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        /*if (checkAccountsPermission()){*/
                            splashScreenCall();
                      /*  }*/



                    }

                } else {

                    SnackBar.makeText(SplashScreenActivity.this, "Please accept location permission", SnackBar.LENGTH_SHORT).setAction("Accept", new LocationListener()).show();


                    return;
                }
                break;
            /*case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.GET_ACCOUNTS)
                            == PackageManager.PERMISSION_GRANTED) {



                        splashScreenCall();

                    }

                } else {

                    SnackBar.makeText(SplashScreenActivity.this, "Please accept location permission", SnackBar.LENGTH_SHORT).setAction("Accept", new LocationListener()).show();


                    return;
                }
                break;*/
        }
    }

    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(android.R.id.content),
                "Please Grant Permissions",
                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_FOR_STORAGE_PERMISSION);
                    }
                }).show();
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_permission)
                        //.setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean checkAccountsPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_permission)
                        //.setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                        new String[]{Manifest.permission.GET_ACCOUNTS},
                                        MY_PERMISSIONS_REQUEST_ACCOUNTS);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_ACCOUNTS);
            }
            return false;
        } else {
            return true;
        }
    }

    public class LocationListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (checkLocationPermission()){
                splashScreenCall();
            }
        }
    }

    protected void onPause() {
        if (this.mHandlerTime != null) {
            this.mHandlerTime.removeCallbacks(this.mRunnableTimeOut);
        }
        super.onPause();
    }

    protected void onResume() {
        if (this.mHandlerTime != null) {
            this.mHandlerTime.postDelayed(this.mRunnableTimeOut, (long) this.SPLASH_TIME_OUT);
        }
        super.onResume();
    }

}
