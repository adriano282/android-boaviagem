package boaviagem.casadocodigo.com.br.boaviagem.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DAOSpent;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Spent;

/**
 * Created by adriano on 21/07/15.
 */
public class GastoActivity extends Activity {
    private int ano, mes, dia;
    private Button dataGasto;
    private Spinner categoria;
    private TextView destino;
    private EditText descricao;
    private EditText local;
    private EditText valor;
    private String travelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGasto = (Button) findViewById(R.id.data);
        dataGasto.setText(dia + "/" +( mes + 1) + "/" + ano);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.category_spent,
                android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.category);
        categoria.setAdapter(adapter);
        String viagemDestino = getIntent().getExtras().getString(Constantes.VIAGEM_DESTINO);
        destino = (TextView) findViewById(R.id.destino);
        destino.setText(viagemDestino);

        travelId = getIntent().getExtras().getString(Constantes.VIAGEM_ID);
        descricao = (EditText) findViewById(R.id.description);
        local = (EditText) findViewById(R.id.local);
        valor = (EditText) findViewById(R.id.valor);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gasto, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featuredId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.remover:
                return true;
            default:
                return super.onMenuItemSelected(featuredId, item);
        }
    }

    public void selecionarData(View view) {
        showDialog(view.getId());
    }


    public void registerSpent(View view) {
        DAOSpent dao = new DAOSpent(this);
        Spent spent = new Spent();


        String category = categoria.getSelectedItem().toString();
        String description = descricao.getText().toString();
        String value = valor.getText().toString();
        String place = local.getText().toString();

        Calendar data = new GregorianCalendar();
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.MONTH, mes);
        data.set(Calendar.DAY_OF_MONTH, dia);

        spent.setDate(data.getTime());
        spent.setCategory(category);
        spent.setDescription(description);
        spent.setValor(Double.parseDouble(value));
        spent.setLocal(place);
        spent.setTravel_id(Integer.parseInt(travelId));

        if (dao.saveOrUpdate(spent) == -1) {
            Toast.makeText(this, getString(R.string.error_save),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.success_save),
                    Toast.LENGTH_SHORT).show();
        };
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data == id) {
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataGasto.setText(dia + "/" + (mes +1) + "/" + ano);
        }
    };
}
