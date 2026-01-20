package com.flightontime.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DelayStat {

    @JsonProperty("op_unique_carrier")
    private Integer opUniqueCarrier;

    @JsonProperty("periodo")
    private String periodo;

    @JsonProperty("total_vuelos")
    private int totalVuelos;

    @JsonProperty("total_retrasos")
    private int totalRetrasos;

    public Integer getOpUniqueCarrier() {
        return opUniqueCarrier;
    }

    public void setOpUniqueCarrier(Integer opUniqueCarrier) {
        this.opUniqueCarrier = opUniqueCarrier;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getTotalVuelos() {
        return totalVuelos;
    }

    public void setTotalVuelos(int totalVuelos) {
        this.totalVuelos = totalVuelos;
    }

    public int getTotalRetrasos() {
        return totalRetrasos;
    }

    public void setTotalRetrasos(int totalRetrasos) {
        this.totalRetrasos = totalRetrasos;
    }
}
