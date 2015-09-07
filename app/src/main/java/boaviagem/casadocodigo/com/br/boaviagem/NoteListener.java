package boaviagem.casadocodigo.com.br.boaviagem;

import android.os.Bundle;

import boaviagem.casadocodigo.com.br.boaviagem.domain.Note;

/**
 * Created by adriano on 07/09/15.
 */
public interface NoteListener {
    void travelSelected(Bundle bundle);
    void noteSelected(Note note);
    void newNote();
}
