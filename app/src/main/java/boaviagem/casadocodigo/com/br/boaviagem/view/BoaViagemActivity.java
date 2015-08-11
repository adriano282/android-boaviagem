package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;

import static boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes.*;

import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

import java.io.IOException;


public class BoaViagemActivity extends Activity {
    private EditText usuario;
    private EditText senha;
    private CheckBox keepConnected;
    private SharedPreferences preferences;
    private GoogleAccountManager accountManager;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountManager = new GoogleAccountManager(this);

        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        keepConnected = (CheckBox) findViewById(R.id.keepConnected);

        preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean connected =
                preferences.getBoolean(KEEP_CONNECTED, false);

        if (connected) {
            requestAuthorization();
        }
    }

    private void initDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void entrarOnClick(View view) {
        String userInformed = usuario.getText().toString();
        String passwordInformed = senha.getText().toString();

        authenticate(userInformed, passwordInformed);
    }

    private void authenticate(final String nameAccount, String password) {
        account = accountManager.getAccountByName(nameAccount);

        if (account == null) {
            Toast.makeText(this, R.string.account_doesnt_exist,
                     Toast.LENGTH_LONG).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, nameAccount);
        bundle.putString(AccountManager.KEY_PASSWORD, password);

        accountManager.getAccountManager().confirmCredentials(account, bundle, this,
                new AuthenticatorCallback(), null);

    }

    private void requestAuthorization() {
        String accessToken = preferences.getString(ACCESS_TOKEN, null);
        String accountName = preferences.getString(ACCOUNT_NAME, null);

        if (accessToken != null) {
            accountManager.invalidateAuthToken(accessToken);
            account = accountManager.getAccountByName(accountName);
        }

        accountManager.getAccountManager()
                .getAuthToken(account,
                        Constantes.AUTH_TOKEN_TYPE,
                        null,
                        this,
                        new AuthenticatorCallback2(),
                        null);
    }

    private class AuthenticatorCallback2 implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = future.getResult();
                String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                String accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                writeTokenAccess(accountName, accessToken);
                initDashboard();
            } catch (OperationCanceledException e) {
                //usuário cancelou a operação
            } catch (AuthenticatorException e) {
                //possível problema no autenticador
            } catch (IOException e) {
                //possível problema de comunicação
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeTokenAccess(String nameAccount, String tokenAccess) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ACCOUNT_NAME, nameAccount);
        editor.putString(ACCESS_TOKEN, tokenAccess);
        editor.putBoolean(KEEP_CONNECTED, keepConnected.isChecked());
        editor.commit();
    }

    private class AuthenticatorCallback implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = future.getResult();

                if (bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
                    requestAuthorization();
                    initDashboard();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.authentication_error),
                            Toast.LENGTH_LONG).show();
                }
            } catch (OperationCanceledException e) {
                // the user have been canceled the operation
            } catch (AuthenticatorException e) {
                // possible fail on the authenticator
            } catch (IOException e) {
                // possible fail of communication
            }
        }
    }
}
