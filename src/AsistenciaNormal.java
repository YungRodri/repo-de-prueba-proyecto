/**
 * SIA-6 — Subclase de Asistencia para asistencia normal al colegio.
 * Implementa polimorfismo puro con @Override en getResumen().
 */
public class AsistenciaNormal extends Asistencia {

    private boolean presente;

    public AsistenciaNormal(String fecha, boolean presente) {
        super(fecha);
        this.presente = presente;
    }

    public boolean isPresente() { return presente; }
    public void setPresente(boolean presente) { this.presente = presente; }

    /**
     * SIA-6 — @Override polimórfico: describe si el alumno asistió normalmente.
     */
    @Override
    public String getResumen() {
        String estadoStr = presente ? "Sí" : "No";
        return "[ASISTENCIA NORMAL]  Fecha: " + getFecha() + " | Presente: " + estadoStr;
    }
}
