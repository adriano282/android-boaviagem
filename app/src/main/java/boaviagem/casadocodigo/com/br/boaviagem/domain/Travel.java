package boaviagem.casadocodigo.com.br.boaviagem.domain;

import java.util.Date;

/**
 * Created by adriano on 29/07/15.
 */
public class Travel {
    private Long id;
    private String destiny;
    private Integer typeTravel;
    private Date dateArrive;
    private Date dateOut;
    private Double budget;
    private Integer quantityPersons;

    public Travel(){};

    public Travel(Long id, String destiny, Integer typeTravel,
                  Date dateArrive, Date dateOut, Double budget,
                  Integer quantityPersons) {
        this.id = id;
        this.destiny = destiny;
        this.typeTravel = typeTravel;
        this.dateArrive = dateArrive;
        this.dateOut = dateOut;
        this.budget = budget;
        this.quantityPersons = quantityPersons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public Integer getTypeTravel() {
        return typeTravel;
    }

    public void setTypeTravel(Integer typeTravel) {
        this.typeTravel = typeTravel;
    }

    public Date getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(Date dateArrive) {
        this.dateArrive = dateArrive;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getQuantityPersons() {
        return quantityPersons;
    }

    public void setQuantityPersons(Integer quantityPersons) {
        this.quantityPersons = quantityPersons;
    }

}
