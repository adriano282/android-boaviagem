package boaviagem.casadocodigo.com.br.boaviagem.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void selecionarOpcao(View view) {
        switch(view.getId()) {
            case R.id.nova_viagem:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case R.id.novo_gasto:
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(this);
                boolean modoViagem = preferences.getBoolean(Constantes.MODO_VIAGEM, false);

                if (modoViagem) {
                    String destino = "São Paulo";
                    Intent intent = new Intent(this, GastoActivity.class);
                    intent.putExtra(Constantes.VIAGEM_ID, "1");
                    intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ViagemListActivity.class);
                    intent.putExtra(Constantes.MODO_SELECIONAR_VIAGEM, true);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.minhas_viagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
            case R.id.configuracoes:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.list_notes:
                startActivity(new Intent(this, NoteActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            String id = data.getExtras().getString(Constantes.VIAGEM_ID);
            String destino = data.getExtras().getString(Constantes.VIAGEM_DESTINO);

            Intent intent = new Intent(this, GastoActivity.class);
            intent.putExtra(Constantes.VIAGEM_ID, id);
            intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, getString(R.string.erro_selecionar_viagem),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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

        if (id == R.id.action_getout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
