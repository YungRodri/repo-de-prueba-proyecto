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
     * @param asistencia objeto polimórfico (puede ser cualquier subclase de Asistencia)
     */
    public void agregarAsistencia(Asistencia asistencia) {
        historial.add(asistencia);
    }

    public ArrayList<Asistencia> getHistorial() {
        return historial;
    }

    /**
     * Cuenta solo las InasistenciaExtraordinaria en un mes dado.
     * Usado en SIA-9 (filtros).
     * @param mes número de mes en formato "MM" (ej: "03" para marzo)
     * @return cantidad de inasistencias extraordinarias en ese mes
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
