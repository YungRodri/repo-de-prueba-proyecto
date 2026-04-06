import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * SIA-10 — Interfaz de usuario basada en ventanas gráficas (JOptionPane).
 * Misma lógica CRUD que InterfazConsola, distinta vista.
 * Cumple la separación de la vista respecto al Controlador (modelo).
 */
public class InterfazVentanas {

    private Controlador controlador;

    public InterfazVentanas(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Inicia el bucle principal del menú con JOptionPane.
     */
    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            String[] opciones = {
                "1. Agregar alumno",
                "2. Buscar alumno y ver historial",
                "3. Actualizar datos de alumno",
                "4. Eliminar alumno",
                "5. Registrar asistencia",
                "6. Listar todos los alumnos",
                "7. Filtrar por inasistencias",
                "8. Guardar y Salir"
            };
            int seleccion = JOptionPane.showOptionDialog(
                null,
                "Curso: " + controlador.getCurso().getNombre(),
                "SIA — Menú Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

            switch (seleccion) {
                case 0: opAgregarAlumno();       break;
                case 1: opBuscarAlumno();        break;
                case 2: opActualizarAlumno();    break;
                case 3: opEliminarAlumno();      break;
                case 4: opRegistrarAsistencia(); break;
                case 5: opListarAlumnos();       break;
                case 6: opFiltrarInasistencias();break;
                case 7:
                case JOptionPane.CLOSED_OPTION:
                    continuar = false;
                    opGuardarYSalir();
                    break;
                default:
                    break;
            }
        }
    }

    // -----------------------------------------------------------------------
    // SIA-7 — Crear: Agregar alumno
    // -----------------------------------------------------------------------
    private void opAgregarAlumno() {
        String rut = JOptionPane.showInputDialog(null, "RUT del alumno (ej: 12345678-9):", "Agregar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (rut == null) return;
        String nombre = JOptionPane.showInputDialog(null, "Nombre:", "Agregar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null) return;
        String apellidos = JOptionPane.showInputDialog(null, "Apellidos:", "Agregar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (apellidos == null) return;

        try {
            controlador.inscribirAlumno(rut, nombre, apellidos);
            JOptionPane.showMessageDialog(null, "Alumno " + nombre + " " + apellidos + " inscrito correctamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (FormatoAsistenciaInvalidoException e) {
            // SIA-12: captura de excepción personalizada
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // SIA-7 — Leer: Buscar alumno y mostrar historial polimórfico
    // -----------------------------------------------------------------------
    private void opBuscarAlumno() {
        String rut = JOptionPane.showInputDialog(null, "RUT del alumno a buscar:", "Buscar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (rut == null) return;

        try {
            Estudiante encontrado = controlador.buscarAlumno(rut);
            StringBuilder sb = new StringBuilder();
            sb.append(encontrado.toString()).append("\n\nHistorial de asistencias:\n");

            ArrayList<Asistencia> historial = encontrado.getHistorial();
            if (historial.isEmpty()) {
                sb.append("  (Sin registros)");
            } else {
                // SIA-6: impresión polimórfica de getResumen()
                for (int i = 0; i < historial.size(); i++) {
                    sb.append("  ").append(i + 1).append(". ").append(historial.get(i).getResumen()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Detalle del Alumno", JOptionPane.INFORMATION_MESSAGE);
        } catch (RutNoEncontradoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Alumno No Encontrado", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // SIA-8 — Actualizar datos del alumno
    // -----------------------------------------------------------------------
    private void opActualizarAlumno() {
        String rut = JOptionPane.showInputDialog(null, "RUT del alumno a actualizar:", "Actualizar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (rut == null) return;
        String nuevoNombre = JOptionPane.showInputDialog(null, "Nuevo nombre (dejar vacío para omitir):", "Actualizar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (nuevoNombre == null) return;
        String nuevosApellidos = JOptionPane.showInputDialog(null, "Nuevos apellidos (dejar vacío para omitir):", "Actualizar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (nuevosApellidos == null) return;

        try {
            controlador.actualizarAlumno(rut, nuevoNombre, nuevosApellidos);
            JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (RutNoEncontradoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FormatoAsistenciaInvalidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // SIA-8 — Eliminar alumno
    // -----------------------------------------------------------------------
    private void opEliminarAlumno() {
        String rut = JOptionPane.showInputDialog(null, "RUT del alumno a eliminar:", "Eliminar Alumno", JOptionPane.PLAIN_MESSAGE);
        if (rut == null) return;

        int confirmar = JOptionPane.showConfirmDialog(null,
            "¿Está seguro de eliminar al alumno con RUT: " + rut + "?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminarAlumno(rut);
                JOptionPane.showMessageDialog(null, "Alumno eliminado correctamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (RutNoEncontradoException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // -----------------------------------------------------------------------
    // SIA-5/SIA-7 — Registrar asistencia (sobrecarga según tipo)
    // -----------------------------------------------------------------------
    private void opRegistrarAsistencia() {
        String rut = JOptionPane.showInputDialog(null, "RUT del alumno:", "Registrar Asistencia", JOptionPane.PLAIN_MESSAGE);
        if (rut == null) return;
        String fecha = JOptionPane.showInputDialog(null, "Fecha (dd/MM/yyyy):", "Registrar Asistencia", JOptionPane.PLAIN_MESSAGE);
        if (fecha == null) return;

        String[] tipos = {"Asistencia Normal", "Inasistencia Extraordinaria", "Salida Anticipada"};
        int tipoSeleccionado = JOptionPane.showOptionDialog(null, "Seleccione el tipo de asistencia:",
            "Tipo de Asistencia", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

        try {
            if (tipoSeleccionado == 0) {
                int resp = JOptionPane.showConfirmDialog(null, "¿El alumno estuvo presente?", "Presente", JOptionPane.YES_NO_OPTION);
                boolean presente = (resp == JOptionPane.YES_OPTION);
                // SIA-5: Overload 1
                controlador.registrarAsistencia(rut, fecha, presente);
                JOptionPane.showMessageDialog(null, "Asistencia normal registrada.", "OK", JOptionPane.INFORMATION_MESSAGE);

            } else if (tipoSeleccionado == 1) {
                String motivo = JOptionPane.showInputDialog(null, "Motivo de la inasistencia:", "Inasistencia Extraordinaria", JOptionPane.PLAIN_MESSAGE);
                if (motivo == null) return;
                // SIA-5: Overload 2
                controlador.registrarAsistencia(rut, fecha, motivo);
                JOptionPane.showMessageDialog(null, "Inasistencia extraordinaria registrada.", "OK", JOptionPane.INFORMATION_MESSAGE);

            } else if (tipoSeleccionado == 2) {
                String hora = JOptionPane.showInputDialog(null, "Hora de salida (HH:mm):", "Salida Anticipada", JOptionPane.PLAIN_MESSAGE);
                if (hora == null) return;
                // SIA-5: Overload 3
                controlador.registrarAsistencia(rut, fecha, hora, true);
                JOptionPane.showMessageDialog(null, "Salida anticipada registrada.", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RutNoEncontradoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FormatoAsistenciaInvalidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // Listar todos los alumnos
    // -----------------------------------------------------------------------
    private void opListarAlumnos() {
        ArrayList<Estudiante> lista = controlador.listarTodosLosAlumnos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay alumnos inscritos.", "Listado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Alumnos inscritos (orden por RUT):\n\n");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(i + 1).append(". ").append(lista.get(i).toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Alumnos", JOptionPane.INFORMATION_MESSAGE);
    }

    // -----------------------------------------------------------------------
    // SIA-9 — Filtrar alumnos por inasistencias en un mes
    // -----------------------------------------------------------------------
    private void opFiltrarInasistencias() {
        String nStr = JOptionPane.showInputDialog(null, "Mínimo de inasistencias a superar (ej: 1):", "Filtro SIA-9", JOptionPane.PLAIN_MESSAGE);
        if (nStr == null) return;
        String mes = JOptionPane.showInputDialog(null, "Mes en formato MM (ej: 04 para abril):", "Filtro SIA-9", JOptionPane.PLAIN_MESSAGE);
        if (mes == null) return;

        int n;
        try {
            n = Integer.parseInt(nStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Estudiante> resultado = controlador.filtrarPorInasistencias(n, mes);
        if (resultado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ningún alumno cumple el criterio.", "Resultado del Filtro", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Alumnos con más de " + n + " inasistencia(s) en mes " + mes + ":\n\n");
            for (int i = 0; i < resultado.size(); i++) {
                sb.append("- ").append(resultado.get(i).toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Resultado del Filtro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // SIA-11 — Guardar y salir
    // -----------------------------------------------------------------------
    private void opGuardarYSalir() {
        GestorArchivos.guardarEstudiantes(controlador.getCurso(), Main.RUTA_ARCHIVO);
        JOptionPane.showMessageDialog(null, "Datos guardados. ¡Hasta luego!", "SIA — Salida", JOptionPane.INFORMATION_MESSAGE);
    }
}
