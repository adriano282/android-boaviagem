package boaviagem.casadocodigo.com.br.boaviagem.calendar;

import android.content.Context;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import boaviagem.casadocodigo.com.br.boaviagem.domain.Constantes;
import boaviagem.casadocodigo.com.br.boaviagem.domain.Travel;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by adriano on 10/08/15.
 */
public class CalendarService {
    private Calendar calendar;
    private String nameAccount;

    public CalendarService(String nameAccount, String accessToken) {
        this.nameAccount = nameAccount;
        GoogleCredential credential = new GoogleCredential();
        credential.setAccessToken(accessToken);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = new GsonFactory();

        calendar = Calendar.builder(transport, jsonFactory)
                .setApplicationName(Constantes.APP_NAME)
                .setHttpRequestInitializer(credential)
                .setJsonHttpRequestInitializer(
                        new GoogleKeyInitializer(Constantes.API_KEY))
                .build();
    }

    public String createEvent(Travel travel) {
        Event event = new Event();
        event.setSummary(travel.getDestiny());

        List<EventAttendee> attendees = Arrays.asList(new EventAttendee().setEmail(nameAccount));
        event.setAttendees(attendees);
        DateTime begin = new DateTime(travel.getDateArrive(),
                TimeZone.getDefault());

        DateTime end = new DateTime(travel.getDateOut(),
                TimeZone.getDefault());

        event.setStart(new EventDateTime().setDateTime(begin));
        event.setEnd(new EventDateTime().setDateTime(end));
        try {

          Event eventCreated = calendar.events()
                  .insert(nameAccount, event)
                  .setCalendarId(nameAccount)
                  .execute();
            return eventCreated.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
