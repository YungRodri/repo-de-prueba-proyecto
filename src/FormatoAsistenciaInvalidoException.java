/**
 * SIA-12 — Excepción personalizada lanzada cuando el formato de un dato
 * de asistencia es inválido, por ejemplo: RUT vacío, fecha en formato incorrecto,
 * o campos obligatorios en blanco en el menú.
 */
public class FormatoAsistenciaInvalidoException extends Exception {

    public FormatoAsistenciaInvalidoException(String campo, String valorIngresado) {
        super("Formato inválido en el campo [" + campo + "]. Valor recibido: \""
              + valorIngresado + "\". No puede estar vacío o tener formato incorrecto.");
    }
}
