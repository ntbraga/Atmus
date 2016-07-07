package atmus.atmus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import atmus.atmus.Controller.RealmController;
import atmus.atmus.adapters.Account;
import atmus.atmus.adapters.GcmRegID;
import atmus.atmus.model.RegResponse;
import atmus.atmus.model.UserModel;
import atmus.atmus.rest.ApiClient;
import atmus.atmus.rest.ApiInterface;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {
    TextView mail, password;
    Button confirm, cancel;
    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mail = (TextView)findViewById(R.id.mail);
        password = (TextView)findViewById(R.id.password);

        confirm = (Button)findViewById(R.id.confirm);
        cancel = (Button)findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void login(){
        Call<UserModel> call = apiService.doLogin(mail.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.body().success){
                    UserModel user = response.body();
                    Toast.makeText(Auth.this, user.full_name+" entrou", Toast.LENGTH_LONG).show();
                    reg(user);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Auth.this, "ERROOOOOOOO", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void reg(final UserModel model){
        RealmResults<GcmRegID> res = RealmController.getInstance().getRealm().allObjects(GcmRegID.class);
        if(!res.isEmpty()){
            GcmRegID regID = res.first();
            Call<RegResponse> call = apiService.reg(model.token, regID.getReg_id());
            call.enqueue(new Callback<RegResponse>() {
                @Override
                public void onResponse(Call<RegResponse> call, Response<RegResponse> response) {
                    RegResponse res = response.body();
                    if(res.success){
                        Realm realm
                                = RealmController.getInstance().getRealm();

                        Account account = new Account(model.full_name, model.email, model.token);
                        account.setId(model.id);
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(account);
                        realm.commitTransaction();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<RegResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(Auth.this, "ERROOOOOOOO 1", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
