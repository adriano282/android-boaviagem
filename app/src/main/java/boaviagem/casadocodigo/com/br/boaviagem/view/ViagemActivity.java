package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DatabaseHelper;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.view.GastoActivity;


public class ViagemActivity extends Activity {

    private int ano, month, day;
    private Button dateArriveButton, dateGetOutButton;
    private DatabaseHelper databaseHelper;
    private EditText destiny, quantityPersons, budget;
    private RadioGroup radioGroup;
    private Calendar calendarArrive, calendarGetOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_viagem);

        calendarArrive = new GregorianCalendar();
        dateArriveButton = (Button) findViewById(R.id.dataChegada);

        calendarArrive.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArriveButton.getText().toString().substring(0,2)));
        calendarArrive.set(Calendar.MONTH, Integer.parseInt(dateArriveButton.getText().toString().substring(3,5)));
        calendarArrive.set(Calendar.YEAR, Integer.parseInt(dateArriveButton.getText().toString().substring(6)));

        calendarGetOut = new GregorianCalendar();
        dateGetOutButton = (Button) findViewById(R.id.dataSaida);

        calendarGetOut.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateGetOutButton.getText().toString().substring(0,2)));
        calendarGetOut.set(Calendar.MONTH, Integer.parseInt(dateGetOutButton.getText().toString().substring(3,5)));
        calendarGetOut.set(Calendar.YEAR, Integer.parseInt(dateGetOutButton.getText().toString().substring(6)));

        destiny = (EditText) findViewById(R.id.description);
        quantityPersons = (EditText) findViewById(R.id.quantidadePessoas);
        budget = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        // Prepare the access of database
        databaseHelper = new DatabaseHelper(this);
    }

    public void saveTravel(View view) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destiny", destiny.getText().toString());
        values.put("date_arrive", Long.parseLong(calendarArrive.getTime().toString()));
        values.put("date_out", Long.parseLong(calendarGetOut.getTime().toString()));
        values.put("budget", budget.getText().toString());
        values.put("quantity_persons", quantityPersons.getText().toString());

        int type = radioGroup.getCheckedRadioButtonId();

        if (type == R.id.lazer) {
            values.put("type_travel", Constantes.FUN_TRAVEL);
        } else {
            values.put("type_travel", Constantes.BUSINESS_TRAVEL);
        }

        long result = db.insert("travel", null, values);

        if (result != -1) {
            Toast.makeText(this, getString(R.string.success_save),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_save),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viagem, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featuredId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case R.id.remover:
                return true;
            default:
                return super.onMenuItemSelected(featuredId, item);
        }
        return true;
    }
}
