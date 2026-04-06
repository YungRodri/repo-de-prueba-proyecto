import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SIA-11 — Clase responsable de la persistencia batch del sistema.
 * Importa estudiantes desde un archivo CSV al iniciar el programa,
 * y los exporta al salir (sobrescribiendo el archivo).
 *
 * Formato del CSV:
 *   rut,nombre,apellidos
 * Ejemplo:
 *   12345678-9,Juan,Pérez González
 *
 * Nota: El historial de asistencias no persiste en esta versión
 * (solo los datos del alumno se guardan para simplicidad académica).
 */
public class GestorArchivos {

    private static final String SEPARADOR = ",";

    /**
     * Lee el archivo CSV e inscribe los estudiantes encontrados en el Curso.
     * Si el archivo no existe, se ignora silenciosamente (primer uso).
     */
    public static void cargarEstudiantes(Curso curso, String rutaArchivo) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            int numeroLinea = 0;
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue; // saltar líneas vacías
                }
                String[] partes = linea.split(SEPARADOR, 3);
                if (partes.length < 3) {
                    System.out.println("[GestorArchivos] Línea " + numeroLinea
                            + " ignorada (formato incorrecto): " + linea);
                    continue;
                }
                String rut = partes[0].trim();
                String nombre = partes[1].trim();
                String apellidos = partes[2].trim();

                Estudiante estudiante = new Estudiante(rut, nombre, apellidos);
                curso.agregarEstudiante(estudiante);
            }
            System.out.println("[GestorArchivos] Carga completada desde: " + rutaArchivo);
        } catch (IOException e) {
            // Si el archivo no existe, es el primer uso; no es un error crítico
            System.out.println("[GestorArchivos] Archivo no encontrado (" + rutaArchivo
                    + "). Se iniciará con datos vacíos o hardcodeados.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("[GestorArchivos] Error al cerrar el archivo.");
                }
            }
        }
    }

    /**
     * Sobrescribe el archivo CSV con el estado actual del TreeMap del Curso.
     * Se llama al salir del programa para guardar los cambios.
     */
    public static void guardarEstudiantes(Curso curso, String rutaArchivo) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(rutaArchivo, false)); // false = sobrescribir
            for (Estudiante estudiante : curso.getEstudiantes().values()) {
                String linea = estudiante.getRut() + SEPARADOR
                             + estudiante.getNombre() + SEPARADOR
                             + estudiante.getApellidos();
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("[GestorArchivos] Datos guardados en: " + rutaArchivo
                    + " (" + curso.getEstudiantes().size() + " alumnos exportados).");
        } catch (IOException e) {
            System.out.println("[GestorArchivos] Error al guardar el archivo: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("[GestorArchivos] Error al cerrar el escritor.");
                }
            }
        }
    }
}
