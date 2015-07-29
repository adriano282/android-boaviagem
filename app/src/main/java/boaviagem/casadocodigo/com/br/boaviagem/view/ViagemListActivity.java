package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DAOTravel;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DatabaseHelper;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Travel;
import boaviagem.casadocodigo.com.br.boaviagem.view.GastoActivity;
import boaviagem.casadocodigo.com.br.boaviagem.view.GastoListActivity;
import boaviagem.casadocodigo.com.br.boaviagem.view.ViagemActivity;


public class ViagemListActivity extends ListActivity
        implements OnItemClickListener, OnClickListener, ViewBinder {
    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};

        SimpleAdapter adapter =
                new SimpleAdapter(this, listarViagens(),
                        R.layout.lista_viagem, de, para);

        adapter.setViewBinder(this);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = criarAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        this.viagemSelecionada = position;
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");

        switch (item) {
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(this.viagemSelecionada);
                DAOTravel dao = new DAOTravel(this);
                dao.delete(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view.getId() == R.id.barraProgresso) {
            Double valores[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }
        return false;
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);

        return builder.create();
    }

    private AlertDialog criarAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.edit),
                getString(R.string.new_spent),
                getString(R.string.list_spent),
                getString(R.string.remove),
                };

        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle(R.string.options);
        builder.setItems(items, this);

        return builder.create();
    }

    private List<Map<String, Object>> listarViagens() {
        DAOTravel dao = new DAOTravel(this);
        List<Travel> travels = dao.listTravels();

        Map<String, Object> item;
        for (int i = 0; i < travels.size(); i++) {

            item = new HashMap<>();
            Travel current = travels.get(i);

            String periodo = dateFormat.format(current.getDateArrive()) + " a " +
                    dateFormat.format(current.getDateOut());
            double totalGasto =
                    dao.getSumSpentById(current.getId());

            double alerta = current.getBudget() * (valorLimite / 100);
            Double[] valores = new Double[] {current.getBudget(), alerta, totalGasto};

            item.put("id", current.getId());
            item.put("destino", current.getDestiny());
            item.put("data", periodo);
            item.put("total", totalGasto);
            item.put("barraProgresso", valores);

            if (current.getId() == Constantes.FUN_TRAVEL) {
                item.put("imagem", R.drawable.lazer);
            } else {
                item.put("imagem", R.drawable.negocios);
            }
            viagens.add(item);
        } // For
        return viagens;
    }


}
