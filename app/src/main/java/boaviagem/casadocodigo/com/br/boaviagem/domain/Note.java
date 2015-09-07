package boaviagem.casadocodigo.com.br.boaviagem.domain;

/**
 * Created by adriano on 07/09/15.
 */
public class Note {
    private Long id;
    private Integer day;
    private String title;
    private String description;

    @Override
    public String toString() {
        return "Day " + day + " - " + title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
