package com.secuencia.ordenescosecha;

public class ClassLotesSelect {
    private String codLote;
    private String nomLote;
    private String codFinca;
    private String nomFinca;
    private String codProveedor;
    private String nomProveedor;
    private String zafra;

    public String getCodLote() {
        return codLote;
    }

    public void setCodLote(String codLote) {
        this.codLote = codLote;
    }

    public String getNomLote() {
        return nomLote;
    }

    public void setNomLote(String nomLote) {
        this.nomLote = nomLote;
    }

    public String getNomFinca() {
        return nomFinca;
    }

    public void setNomFinca(String nomFinca) {
        this.nomFinca = nomFinca;
    }

    public String getCodFinca() {
        return codFinca;
    }

    public void setCodFinca(String codFinca) {
        this.codFinca = codFinca;
    }

    public String getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(String codProveedor) {
        this.codProveedor = codProveedor;
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public String getZafra() {
        return zafra;
    }

    public void setZafra(String zafra) {
        this.zafra = zafra;
    }

    public Double getAreaAfectada() {
        return areaAfectada;
    }

    public void setAreaAfectada(Double areaAfectada) {
        this.areaAfectada = areaAfectada;
    }

    private Double areaAfectada;


}