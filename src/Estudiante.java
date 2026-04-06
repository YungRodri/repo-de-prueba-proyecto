import java.util.ArrayList;

/**
 * SIA-4/SIA-6 — Clase Estudiante que hereda de Persona.
 * Colección anidada: ArrayList<Asistencia> para el historial de asistencias.
 */
public class Estudiante extends Persona {

    // SIA-4: Colección anidada dentro de Estudiante
    private ArrayList<Asistencia> historial;

    public Estudiante(String rut, String nombre, String apellidos) {
        super(rut, nombre, apellidos);
        this.historial = new ArrayList<Asistencia>();
    }

    /**
     * Agrega un registro de asistencia al historial del alumno.
     * El objeto puede ser cualquier subclase de Asistencia (polimorfismo).
     */
    public void agregarAsistencia(Asistencia asistencia) {
        historial.add(asistencia);
    }

    public ArrayList<Asistencia> getHistorial() {
        return historial;
    }

    /**
     * SIA-8 — Elimina una asistencia del historial por su posición (índice).
     * Retorna true si el índice es válido y se eliminó, false si está fuera de rango.
     */
    public boolean eliminarAsistencia(int indice) {
        if (indice >= 0 && indice < historial.size()) {
            historial.remove(indice);
            return true;
        }
        return false;
    }

    /**
     * SIA-8 — Modifica la fecha de una asistencia existente en el historial.
     * Retorna true si el índice es válido y se modificó.
     */
    public boolean modificarFechaAsistencia(int indice, String nuevaFecha) {
        if (indice >= 0 && indice < historial.size()) {
            historial.get(indice).setFecha(nuevaFecha);
            return true;
        }
        return false;
    }

    /**
     * Retorna la asistencia en la posición indicada, o null si el índice es inválido.
     */
    public Asistencia getAsistencia(int indice) {
        if (indice >= 0 && indice < historial.size()) {
            return historial.get(indice);
        }
        return null;
    }

    /**
     * Cuenta solo las InasistenciaExtraordinaria en un mes dado.
     * El mes debe estar en formato "MM" (ej: "03" para marzo).
     * Retorna la cantidad de inasistencias extraordinarias encontradas.
     * Usado en SIA-9 (filtros).
     */
    public int contarInasistenciasEnMes(String mes) {
        int contador = 0;
        for (int i = 0; i < historial.size(); i++) {
            Asistencia a = historial.get(i);
            if (a instanceof InasistenciaExtraordinaria) {
                // fecha tiene formato dd/MM/yyyy, mes es posición 3-4
                if (a.getFecha().length() >= 5 && a.getFecha().substring(3, 5).equals(mes)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    @Override
    public String toString() {
        return super.toString() + " | Registros de asistencia: " + historial.size();
    }
}
