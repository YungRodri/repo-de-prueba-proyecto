/**
 * SIA-6 — Clase abstracta base de la jerarquía de asistencias.
 * Declara el método abstracto getResumen() que cada subclase debe implementar
 * con @Override para lograr polimorfismo puro.
 */
public abstract class Asistencia {

    private String fecha; // formato esperado: dd/MM/yyyy

    public Asistencia(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    /**
     * Devuelve un resumen textual del tipo de asistencia.
     * Implementado de forma distinta en cada subclase (polimorfismo).
     */
    public abstract String getResumen();
}
