package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import boaviagem.casadocodigo.com.br.boaviagem.R;


public class BoaViagemActivity extends Activity {
    private static final String KEEP_CONNECTED = "keep_connected";
    private EditText usuario;
    private EditText senha;
    private CheckBox keepConnected;


    public void entrarOnClick(View view) {
        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();

        if ("leitor".equals(usuarioInformado) && "123".equals(senhaInformada)) {

            SharedPreferences preferences = getPreferences(MODE_PRIVATE);

            Editor editor = preferences.edit();
            editor.putBoolean(KEEP_CONNECTED, keepConnected.isChecked());
            editor.commit();

            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            String mensagemErro = getString(R.string.authentication_error);
            Toast toast = Toast.makeText(this, mensagemErro,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        keepConnected = (CheckBox) findViewById(R.id.keepConnected);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean connected =
                preferences.getBoolean(KEEP_CONNECTED, false);

        if (connected) {
            startActivity(new Intent(this, DashboardActivity.class));
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
}
