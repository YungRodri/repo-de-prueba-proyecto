import java.util.ArrayList;

/**
 * SIA-3/SIA-5/SIA-7/SIA-8/SIA-9 — Controlador central del sistema SIA.
 *
 * Responsable de:
 * - SIA-3: Inicializar datos hardcodeados para pruebas
 * - SIA-5: Sobrecarga del método registrarAsistencia()
 * - SIA-7/8: CRUD completo (Crear, Leer, Actualizar, Eliminar)
 * - SIA-9: Búsqueda/filtro de alumnos por criterios múltiples
 */
public class Controlador {

    private Curso curso;

    public Controlador(Curso curso) {
        this.curso = curso;
    }

    public Curso getCurso() {
        return curso;
    }

    // =========================================================================
    // SIA-3 — Datos hardcodeados para iniciar el sistema con información de prueba
    // =========================================================================

    /**
     * Inserta 3 alumnos predefinidos y algunos registros de asistencia de ejemplo
     * para que el sistema sea funcional desde el primer arranque (sin escribir datos).
     */
    public void cargarDatosIniciales() {
        // --- Alumnos de ejemplo ---
        Estudiante e1 = new Estudiante("12345678-9", "Carlos", "Ramírez Vega");
        Estudiante e2 = new Estudiante("98765432-1", "Valentina", "López Muñoz");
        Estudiante e3 = new Estudiante("11223344-5", "Sebastián", "Torres Pino");

        // SIA-6: Uso polimórfico de las 3 subclases de Asistencia
        e1.agregarAsistencia(new AsistenciaNormal("06/04/2026", true));
        e1.agregarAsistencia(new SalidaAnticipada("05/04/2026", "14:30"));

        e2.agregarAsistencia(new AsistenciaNormal("06/04/2026", false));
        e2.agregarAsistencia(new InasistenciaExtraordinaria("04/04/2026", "Control médico"));
        e2.agregarAsistencia(new InasistenciaExtraordinaria("03/04/2026", "Viaje familiar"));

        e3.agregarAsistencia(new AsistenciaNormal("06/04/2026", true));

        // SIA-4: Inserción en el TreeMap del Curso
        curso.agregarEstudiante(e1);
        curso.agregarEstudiante(e2);
        curso.agregarEstudiante(e3);

        System.out.println("[SIA] Datos iniciales cargados: 3 alumnos de ejemplo registrados.");
    }

    // =========================================================================
    // SIA-7 — Crear: Inscribir alumno
    // =========================================================================

    /**
     * Inscribe un nuevo alumno en el TreeMap del Curso.
     * Lanza FormatoAsistenciaInvalidoException si algún campo requerido está vacío.
     */
    public void inscribirAlumno(String rut, String nombre, String apellidos)
            throws FormatoAsistenciaInvalidoException {

        // SIA-12: Validación con excepción personalizada
        if (rut == null || rut.trim().isEmpty()) {
            throw new FormatoAsistenciaInvalidoException("RUT", rut == null ? "null" : rut);
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new FormatoAsistenciaInvalidoException("nombre", nombre == null ? "null" : nombre);
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new FormatoAsistenciaInvalidoException("apellidos", apellidos == null ? "null" : apellidos);
        }

        Estudiante nuevo = new Estudiante(rut.trim(), nombre.trim(), apellidos.trim());
        curso.agregarEstudiante(nuevo); // TreeMap.put() con RUT como clave
    }

    // =========================================================================
    // SIA-7 — Leer: Buscar y mostrar alumno
    // =========================================================================

    /**
     * Busca un alumno en el TreeMap por RUT.
     * Lanza RutNoEncontradoException si el .get() del TreeMap retorna null.
     */
    public Estudiante buscarAlumno(String rut) throws RutNoEncontradoException {
        // SIA-4: búsqueda en el TreeMap
        Estudiante encontrado = curso.buscarEstudiante(rut.trim());

        // SIA-12: Lanzar excepción personalizada si no existe
        if (encontrado == null) {
            throw new RutNoEncontradoException(rut);
        }
        return encontrado;
    }

    // =========================================================================
    // SIA-8 — Actualizar: Modificar datos del alumno
    // =========================================================================

    /**
     * Actualiza el nombre y apellidos de un alumno existente.
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si ambos valores están vacíos.
     */
    public void actualizarAlumno(String rut, String nuevoNombre, String nuevosApellidos)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {

        Estudiante estudiante = buscarAlumno(rut); // lanza RutNoEncontradoException si no existe

        boolean hayAlgunCambio = false;
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            estudiante.setNombre(nuevoNombre.trim());
            hayAlgunCambio = true;
        }
        if (nuevosApellidos != null && !nuevosApellidos.trim().isEmpty()) {
            estudiante.setApellidos(nuevosApellidos.trim());
            hayAlgunCambio = true;
        }
        if (!hayAlgunCambio) {
            throw new FormatoAsistenciaInvalidoException("nombre/apellidos", "(ambos vacíos)");
        }
    }

    // =========================================================================
    // SIA-8 — Eliminar alumno del mapa
    // =========================================================================

    /**
     * Elimina un alumno del TreeMap por su RUT.
     * Lanza RutNoEncontradoException si el alumno no existe en el mapa.
     */
    public void eliminarAlumno(String rut) throws RutNoEncontradoException {
        boolean eliminado = curso.eliminarEstudiante(rut.trim());
        if (!eliminado) {
            throw new RutNoEncontradoException(rut);
        }
    }

    // =========================================================================
    // SIA-5 — Sobrecarga del método registrarAsistencia()
    // =========================================================================

    /**
     * [Overload 1] Registra una AsistenciaNormal.
     * Se llama con rut + fecha + presente (boolean).
     * SIA-5: misma firma base, sobrecargada con distintos parámetros.
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si la fecha está vacía.
     */
    public void registrarAsistencia(String rut, String fecha, boolean presente)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {

        validarCampo("fecha", fecha);
        Estudiante estudiante = buscarAlumno(rut);
        estudiante.agregarAsistencia(new AsistenciaNormal(fecha.trim(), presente));
    }

    /**
     * [Overload 2] Registra una InasistenciaExtraordinaria.
     * SIA-5: sobrecarga con parámetro extra "motivo".
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si fecha o motivo están vacíos.
     */
    public void registrarAsistencia(String rut, String fecha, String motivo)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {

        validarCampo("fecha", fecha);
        validarCampo("motivo", motivo);
        Estudiante estudiante = buscarAlumno(rut);
        estudiante.agregarAsistencia(new InasistenciaExtraordinaria(fecha.trim(), motivo.trim()));
    }

    /**
     * [Overload 3] Registra una SalidaAnticipada.
     * SIA-5: sobrecarga con parámetros fecha + hora (dos Strings).
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si fecha u hora están vacíos.
     */
    public void registrarAsistencia(String rut, String fecha, String hora, boolean esSalida)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {

        validarCampo("fecha", fecha);
        validarCampo("hora", hora);
        Estudiante estudiante = buscarAlumno(rut);
        estudiante.agregarAsistencia(new SalidaAnticipada(fecha.trim(), hora.trim()));
    }

    // =========================================================================
    // SIA-9 — Búsqueda / Filtros con múltiples criterios
    // =========================================================================

    /**
     * Filtra alumnos que superen un número mínimo de inasistencias en un mes.
     * El mes debe estar en formato "MM" (ej: "04" para abril).
     * Recorre el TreeMap e inspecciona el ArrayList anidado de cada Estudiante.
     * Retorna un ArrayList con los Estudiantes que cumplen el criterio.
     */
    public ArrayList<Estudiante> filtrarPorInasistencias(int minInasistencias, String mes) {
        ArrayList<Estudiante> resultado = new ArrayList<Estudiante>();

        // Recorrido tradicional del TreeMap con for-each
        for (Estudiante estudiante : curso.getEstudiantes().values()) {
            int total = estudiante.contarInasistenciasEnMes(mes);
            if (total > minInasistencias) {
                resultado.add(estudiante);
            }
        }
        return resultado;
    }

    /**
     * Retorna todos los alumnos del TreeMap como lista (para mostrar en pantalla).
     */
    public ArrayList<Estudiante> listarTodosLosAlumnos() {
        ArrayList<Estudiante> lista = new ArrayList<Estudiante>();
        for (Estudiante estudiante : curso.getEstudiantes().values()) {
            lista.add(estudiante);
        }
        return lista;
    }

    // =========================================================================
    // Utilidad interna
    // =========================================================================

    // =========================================================================
    // SIA-7/SIA-8 — CRUD para la colección anidada ArrayList<Asistencia>
    // =========================================================================

    /**
     * SIA-8 — Elimina una asistencia del historial de un alumno por su índice.
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si el índice es inválido.
     */
    public void eliminarAsistencia(String rut, int indice)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {
        Estudiante estudiante = buscarAlumno(rut);
        boolean ok = estudiante.eliminarAsistencia(indice);
        if (!ok) {
            throw new FormatoAsistenciaInvalidoException("índice",
                    String.valueOf(indice) + " (fuera de rango 0-"
                    + (estudiante.getHistorial().size() - 1) + ")");
        }
    }

    /**
     * SIA-8 — Modifica la fecha de una asistencia existente en el historial.
     * Lanza RutNoEncontradoException si el alumno no existe.
     * Lanza FormatoAsistenciaInvalidoException si el índice o fecha son inválidos.
     */
    public void modificarFechaAsistencia(String rut, int indice, String nuevaFecha)
            throws RutNoEncontradoException, FormatoAsistenciaInvalidoException {
        validarCampo("nuevaFecha", nuevaFecha);
        Estudiante estudiante = buscarAlumno(rut);
        boolean ok = estudiante.modificarFechaAsistencia(indice, nuevaFecha.trim());
        if (!ok) {
            throw new FormatoAsistenciaInvalidoException("índice",
                    String.valueOf(indice) + " (fuera de rango 0-"
                    + (estudiante.getHistorial().size() - 1) + ")");
        }
    }

    /**
     * Valida que un campo no sea nulo ni esté vacío.
     * Lanza FormatoAsistenciaInvalidoException si falla.
     */
    private void validarCampo(String nombreCampo, String valor)
            throws FormatoAsistenciaInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new FormatoAsistenciaInvalidoException(nombreCampo,
                    valor == null ? "null" : valor);
        }
    }
}
