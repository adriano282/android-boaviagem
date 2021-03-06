package boaviagem.casadocodigo.com.br.boaviagem.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DAOTravel;
import boaviagem.casadocodigo.com.br.boaviagem.dao.DatabaseHelper;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Travel;


public class ViagemListActivity extends ListActivity
        implements OnItemClickListener, OnClickListener, ViewBinder {
    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private boolean modoSelecioanrViagem;
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

        getListView().setOnItemClickListener(this);
        this.alertDialog = criarAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();

        if(getIntent().hasExtra(Constantes.MODO_SELECIONAR_VIAGEM)) {
            modoSelecioanrViagem =
                    getIntent().getExtras().getBoolean(
                            Constantes.MODO_SELECIONAR_VIAGEM);
        }
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>> {

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            return listarViagens();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {
            String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
            int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};

            SimpleAdapter adapter =
                    new SimpleAdapter(ViagemListActivity.this, listarViagens(),
                            R.layout.lista_viagem, de, para);

            adapter.setViewBinder(ViagemListActivity.this);
            setListAdapter(adapter);
        }
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
        String destino = (String) viagens.get(viagemSelecionada).get("destino");

        switch (item) {
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, GastoListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(this.viagemSelecionada);
                DAOTravel dao = new DAOTravel(this);
                dao.delete(id.toString());
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

        viagens = new ArrayList<Map<String, Object>>();

        if (travels == null) {
            return null;
        }

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

            item.put("id", current.getId().toString());
            item.put("destino", current.getDestiny());
            item.put("data", periodo);
            item.put("total", "Gasto total R$ " + totalGasto);
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
