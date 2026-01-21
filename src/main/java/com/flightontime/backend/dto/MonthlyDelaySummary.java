package com.flightontime.backend.dto;

public class MonthlyDelaySummary {
    private String periodo;
    private int totalVuelos;
    private int totalRetrasos;

    public MonthlyDelaySummary() {
    }

    public MonthlyDelaySummary(String periodo, int totalVuelos, int totalRetrasos) {
        this.periodo = periodo;
        this.totalVuelos = totalVuelos;
        this.totalRetrasos = totalRetrasos;
        this.porcentaje = (totalVuelos == 0) ? 0 : (double) totalRetrasos / totalVuelos * 100;
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

    private double porcentaje;

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
