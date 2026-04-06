/**
 * SIA-6 — Subclase de Asistencia para retiros antes del fin de la jornada.
 * Implementa polimorfismo puro con @Override en getResumen().
 */
public class SalidaAnticipada extends Asistencia {

    private String hora; // formato esperado: HH:mm

    public SalidaAnticipada(String fecha, String hora) {
        super(fecha);
        this.hora = hora;
    }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    /**
     * SIA-6 — @Override polimórfico: describe la salida anticipada y la hora de retiro.
     */
    @Override
    public String getResumen() {
        return "[SALIDA ANTICIPADA]  Fecha: " + getFecha() + " | Hora de salida: " + hora;
    }
}
