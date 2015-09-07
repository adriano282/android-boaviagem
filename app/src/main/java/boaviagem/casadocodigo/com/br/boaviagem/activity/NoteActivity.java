package boaviagem.casadocodigo.com.br.boaviagem.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import boaviagem.casadocodigo.com.br.boaviagem.NoteListener;
import boaviagem.casadocodigo.com.br.boaviagem.R;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Note;
import boaviagem.casadocodigo.com.br.boaviagem.fragment.NoteFragment;
import boaviagem.casadocodigo.com.br.boaviagem.fragment.NoteListFragment;
import boaviagem.casadocodigo.com.br.boaviagem.fragment.TravelListFragment;

/**
 * Created by adriano on 07/09/15.
 */
public class NoteActivity extends FragmentActivity implements NoteListener {
    private boolean tablet = true;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.notes);

        View view = findViewById(R.id.single_fragment);

        if (view != null) {
            tablet = false;

            TravelListFragment fragment = new TravelListFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.single_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void travelSelected(Bundle bundle) {
        FragmentManager manager = getSupportFragmentManager();
        NoteListFragment fragment;

        if (tablet) {
            fragment = (NoteListFragment) manager.findFragmentById(R.id.notes_fragment);
            fragment.listNotesByTravel(bundle);

        } else {
            fragment = new NoteListFragment();
            fragment.setArguments(bundle);

            manager.beginTransaction()
                    .replace(R.id.single_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void noteSelected(Note note) {
        FragmentManager manager = getSupportFragmentManager();
        NoteFragment fragment;

        if (tablet) {
            fragment = (NoteFragment) manager.findFragmentById(R.id.new_note_fragment);
            fragment.prepareForEdition(note);
        } else {
            fragment = new NoteFragment();
            fragment.setNote(note);

            manager.beginTransaction()
                    .replace(R.id.single_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void newNote() {}
}
