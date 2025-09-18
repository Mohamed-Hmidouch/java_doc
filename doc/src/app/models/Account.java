package app.models;

import java.util.UUID;
import java.math.BigDecimal;

public class Account {
    private UUID id;
    private String type;
    private BigDecimal solde;
    private String status = "active";

    public Account(UUID id, String type, BigDecimal solde) {
        this.id = id;
        this.type = type;
        this.solde = solde != null ? solde : BigDecimal.ZERO;
        this.status = "active";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde != null ? solde : BigDecimal.ZERO;
    }
    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}