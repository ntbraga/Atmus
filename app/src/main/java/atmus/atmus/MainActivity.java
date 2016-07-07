package atmus.atmus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import atmus.atmus.Controller.RealmController;
import atmus.atmus.adapters.Account;
import atmus.atmus.adapters.DividerItemDecoration;
import atmus.atmus.adapters.GcmRegID;
import atmus.atmus.adapters.TokenAdapter;
import atmus.atmus.token.TokenAndroidController;
import atmus.atmus.token.TokenManager;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    List<Account> accountList = new ArrayList<>();
    RecyclerView recyclerView;
    TokenAdapter tokenAdapter;
    FloatingActionButton fab;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.addAcc);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Auth.class);
                startActivityForResult(intent, 1234);
            }
        });
        Realm realm = RealmController.getInstance().getRealm();
        if(realm.allObjects(GcmRegID.class).isEmpty()) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        Log.i("TOKEN", getString(R.string.gcm_send_message));
                        Toast.makeText(MainActivity.this, getString(R.string.gcm_send_message), Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("TOKEN", getString(R.string.token_error_message));
                        Toast.makeText(MainActivity.this, getString(R.string.token_error_message), Toast.LENGTH_LONG).show();
                    }
                }
            };

            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                Log.v(TAG, "TESTE ENTROU");
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }

        recyclerView = (RecyclerView)findViewById(R.id.rec_token);

        tokenAdapter = new TokenAdapter(accountList, (TextView)findViewById(R.id.remaing), (ProgressBar)findViewById(R.id.pbar));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(tokenAdapter);
        accountList.addAll(realm.allObjects(Account.class));

        tokenAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1234:{
                    accountList.clear();
                    accountList.addAll(RealmController.getInstance().getRealm().allObjects(Account.class));
                    tokenAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
