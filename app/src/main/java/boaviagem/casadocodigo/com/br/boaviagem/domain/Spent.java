package boaviagem.casadocodigo.com.br.boaviagem.domain;

import java.util.Date;

/**
 * Created by adriano on 29/07/15.
 */
public class Spent {
    private Long id;
    private Date date;
    private String category;
    private String description;
    private Double valor;
    private String local;
    private Integer travel_id;
    
    public Spent() {};

    public Spent(Long id, Date date, String category, String description, Double valor, String local, Integer travel_id) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.valor = valor;
        this.local = local;
        this.travel_id = travel_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(Integer travel_id) {
        this.travel_id = travel_id;
    }


}
