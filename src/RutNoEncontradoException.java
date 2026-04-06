/**
 * SIA-12 — Excepción personalizada lanzada cuando se busca un RUT
 * en el TreeMap del Curso y el resultado es null (alumno no existe).
 */
public class RutNoEncontradoException extends Exception {

    public RutNoEncontradoException(String rut) {
        super("No se encontró ningún alumno con el RUT: " + rut);
    }
}
