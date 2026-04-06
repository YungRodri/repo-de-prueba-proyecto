import java.util.TreeMap;

/**
 * SIA-4 — Clase Curso que contiene el TreeMap principal del sistema.
 * Clave: RUT del estudiante (String). Valor: objeto Estudiante.
 * El TreeMap mantiene los elementos ordenados alfabéticamente por RUT.
 */
public class Curso {

    private String nombre;
    private int cantidadAlumnos; // capacidad máxima (referencial)

    // SIA-4: Colección principal — TreeMap con clave = RUT
    private TreeMap<String, Estudiante> estudiantes;

    public Curso(String nombre, int cantidadAlumnos) {
        this.nombre = nombre;
        this.cantidadAlumnos = cantidadAlumnos;
        this.estudiantes = new TreeMap<String, Estudiante>();
    }

    // --- Getters/Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCantidadAlumnos() { return cantidadAlumnos; }
    public void setCantidadAlumnos(int cantidadAlumnos) { this.cantidadAlumnos = cantidadAlumnos; }
    public TreeMap<String, Estudiante> getEstudiantes() { return estudiantes; }

    /**
     * Agrega un Estudiante al TreeMap usando su RUT como clave.
     */
    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.put(estudiante.getRut(), estudiante);
    }

    /**
     * Busca un Estudiante en el TreeMap por su RUT.
     * Retorna null si el RUT no existe.
     */
    public Estudiante buscarEstudiante(String rut) {
        return estudiantes.get(rut);
    }

    /**
     * Elimina un Estudiante del TreeMap por su RUT.
     * Retorna true si fue eliminado, false si no existía.
     */
    public boolean eliminarEstudiante(String rut) {
        if (estudiantes.containsKey(rut)) {
            estudiantes.remove(rut);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Curso: " + nombre + " | Alumnos inscritos: " + estudiantes.size()
               + " | Capacidad: " + cantidadAlumnos;
    }
}
