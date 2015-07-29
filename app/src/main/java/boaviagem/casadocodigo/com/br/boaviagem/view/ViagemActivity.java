package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DAOTravel;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DatabaseHelper;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Travel;


public class ViagemActivity extends Activity {

    private String id;
    private int ano, mes, dia;
    private Button dateArriveButton, dateGetOutButton;
    private DatabaseHelper databaseHelper;
    private EditText destiny, quantityPersons, budget;
    private Date dataChegada, dataSaida;
    private RadioGroup radioGroup;
    private Calendar calendarArrive, calendarGetOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dateArriveButton = (Button) findViewById(R.id.dataChegada);
        dateArriveButton.setText(dia + "/" + mes + "/" + ano);
        calendarArrive = new GregorianCalendar();
        calendarArrive.set(Calendar.YEAR, ano);
        calendarArrive.set(Calendar.MONTH, mes);
        calendarArrive.set(Calendar.DAY_OF_MONTH, dia);

        dateGetOutButton = (Button) findViewById(R.id.dataSaida);
        dateGetOutButton.setText(dia + "/" + mes + "/" + ano);
        calendarGetOut = new GregorianCalendar();
        calendarGetOut.set(Calendar.YEAR, ano);
        calendarGetOut.set(Calendar.MONTH, mes);
        calendarArrive.set(Calendar.DAY_OF_MONTH, dia);

        destiny = (EditText) findViewById(R.id.destino);
        quantityPersons = (EditText) findViewById(R.id.quantidadePessoas);
        budget = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        // Prepare the access of database
        databaseHelper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        if (id != null) {
            prepareForEdit();
        }
    }

    private void prepareForEdit() {
        DAOTravel dao = new DAOTravel(this);
        Travel travel = dao.getTravelById(id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(travel.getTypeTravel() == Constantes.FUN_TRAVEL){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }
        destiny.setText(travel.getDestiny());
        dataChegada = travel.getDateArrive();
        dataSaida = travel.getDateOut();
        dateArriveButton.setText(dateFormat.format(dataChegada));
        dateGetOutButton.setText(dateFormat.format(dataSaida));
        quantityPersons.setText(travel.getQuantityPersons());
        budget.setText(travel.getBudget().toString());
    }

    public void saveTravel(View view) {

        DAOTravel dao = new DAOTravel(this);
        Travel travel = new Travel();

        travel.setDestiny(destiny.getText().toString());
        travel.setDateArrive(calendarArrive.getTime());
        travel.setDateOut(calendarGetOut.getTime());
        travel.setBudget(Double.parseDouble(budget.getText().toString()));
        travel.setQuantityPersons(Integer.parseInt(quantityPersons.getText().toString()));

        int type = radioGroup.getCheckedRadioButtonId();

        if (type == R.id.lazer) {
            travel.setTypeTravel(Constantes.FUN_TRAVEL);
        } else {
            travel.setTypeTravel(Constantes.BUSINESS_TRAVEL);
        }

        long result = dao.saveOrUpdate(travel);

        if (result != -1) {
            Toast.makeText(this, getString(R.string.success_save),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_save),Toast.LENGTH_SHORT).show();
        }
    }

    public void selecionarData2(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.dataChegada == id) {
            return new DatePickerDialog(this, listenerChegada, ano, mes, dia);
        } else if (R.id.dataSaida == id) {
            return new DatePickerDialog(this, listenerSaida, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listenerChegada = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dateArriveButton.setText(dia + "/" + mes + "/" + ano);
            calendarArrive.set(Calendar.YEAR, ano);
            calendarArrive.set(Calendar.MONTH, mes);
            calendarArrive.set(Calendar.DAY_OF_MONTH, dia);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerSaida = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dateGetOutButton.setText(dia + "/" + mes + "/" + ano);
            calendarGetOut = new GregorianCalendar();
            calendarGetOut.set(Calendar.YEAR, ano);
            calendarGetOut.set(Calendar.MONTH, mes);
            calendarGetOut.set(Calendar.DAY_OF_MONTH, dia);
        }
    };

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
