/**
 * SIA-6 — Subclase de Asistencia para inasistencias con justificación especial.
 * Implementa polimorfismo puro con @Override en getResumen().
 */
public class InasistenciaExtraordinaria extends Asistencia {

    private String motivo;

    public InasistenciaExtraordinaria(String fecha, String motivo) {
        super(fecha);
        this.motivo = motivo;
    }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    /**
     * SIA-6 — @Override polimórfico: describe la inasistencia y su motivo.
     */
    @Override
    public String getResumen() {
        return "[INASISTENCIA EXTRAORDINARIA]  Fecha: " + getFecha() + " | Motivo: " + motivo;
    }
}
