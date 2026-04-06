import java.util.ArrayList;
import java.util.Scanner;

/**
 * SIA-10 — Interfaz de usuario basada en consola (Scanner).
 * Separa la vista del controlador central, cumpliendo la separación
 * de responsabilidades (Vista vs. Modelo/Controlador).
 */
public class InterfazConsola {

    private Controlador controlador;
    private Scanner scanner;

    public InterfazConsola(Controlador controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia el bucle principal del menú de consola.
     * Al elegir "Guardar y Salir" se persisten los datos y se cierra.
     */
    public void iniciar() {
        System.out.println("\n===================================================");
        System.out.println("   SIA — Sistema de Información de Asistencia");
        System.out.println("   Curso: " + controlador.getCurso().getNombre());
        System.out.println("===================================================");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": opAgregarAlumno();       break;
                case "2": opBuscarAlumno();        break;
                case "3": opActualizarAlumno();    break;
                case "4": opEliminarAlumno();      break;
                case "5": opRegistrarAsistencia(); break;
                case "6": opListarAlumnos();       break;
                case "7": opFiltrarInasistencias();break;
                case "8": continuar = false; opGuardarYSalir(); break;
                default:
                    System.out.println("[!] Opción no válida. Intente de nuevo.");
            }
        }
    }

    // -----------------------------------------------------------------------
    // Menú
    // -----------------------------------------------------------------------
    private void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Agregar alumno");
        System.out.println("2. Buscar alumno y ver historial");
        System.out.println("3. Actualizar datos de alumno");
        System.out.println("4. Eliminar alumno");
        System.out.println("5. Registrar asistencia");
        System.out.println("6. Listar todos los alumnos");
        System.out.println("7. Filtrar alumnos por inasistencias (SIA-9)");
        System.out.println("8. Guardar y Salir");
        System.out.print("Seleccione una opción: ");
    }

    // -----------------------------------------------------------------------
    // SIA-7 — Crear: Inscribir alumno
    // -----------------------------------------------------------------------
    private void opAgregarAlumno() {
        System.out.println("\n-- Agregar Alumno --");
        System.out.print("RUT (ej: 12345678-9): ");
        String rut = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = scanner.nextLine();

        try {
            controlador.inscribirAlumno(rut, nombre, apellidos);
            System.out.println("[OK] Alumno " + nombre + " " + apellidos + " inscrito correctamente.");
        } catch (FormatoAsistenciaInvalidoException e) {
            // SIA-12: captura de excepción personalizada
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // SIA-7 — Leer: Buscar alumno y recorrer ArrayList de asistencias
    // -----------------------------------------------------------------------
    private void opBuscarAlumno() {
        System.out.println("\n-- Buscar Alumno --");
        System.out.print("RUT del alumno: ");
        String rut = scanner.nextLine();

        try {
            Estudiante encontrado = controlador.buscarAlumno(rut);
            System.out.println("\n" + encontrado.toString());
            System.out.println("Historial de asistencias:");

            ArrayList<Asistencia> historial = encontrado.getHistorial();
            if (historial.isEmpty()) {
                System.out.println("  (Sin registros de asistencia)");
            } else {
                // SIA-6: recorre el ArrayList e imprime getResumen() polimórfico
                for (int i = 0; i < historial.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + historial.get(i).getResumen());
                }
            }
        } catch (RutNoEncontradoException e) {
            // SIA-12: captura de excepción personalizada
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // SIA-8 — Actualizar datos del alumno
    // -----------------------------------------------------------------------
    private void opActualizarAlumno() {
        System.out.println("\n-- Actualizar Alumno --");
        System.out.print("RUT del alumno a modificar: ");
        String rut = scanner.nextLine();
        System.out.print("Nuevo nombre (Enter para omitir): ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nuevos apellidos (Enter para omitir): ");
        String nuevosApellidos = scanner.nextLine();

        try {
            controlador.actualizarAlumno(rut, nuevoNombre, nuevosApellidos);
            System.out.println("[OK] Datos actualizados correctamente.");
        } catch (RutNoEncontradoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (FormatoAsistenciaInvalidoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // SIA-8 — Eliminar alumno del TreeMap
    // -----------------------------------------------------------------------
    private void opEliminarAlumno() {
        System.out.println("\n-- Eliminar Alumno --");
        System.out.print("RUT del alumno a eliminar: ");
        String rut = scanner.nextLine();

        try {
            controlador.eliminarAlumno(rut);
            System.out.println("[OK] Alumno con RUT " + rut + " eliminado del sistema.");
        } catch (RutNoEncontradoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // SIA-5/SIA-7 — Registrar asistencia con sobrecarga
    // -----------------------------------------------------------------------
    private void opRegistrarAsistencia() {
        System.out.println("\n-- Registrar Asistencia --");
        System.out.print("RUT del alumno: ");
        String rut = scanner.nextLine();
        System.out.print("Fecha (dd/MM/yyyy): ");
        String fecha = scanner.nextLine();
        System.out.println("Tipo de asistencia:");
        System.out.println("  1. Asistencia Normal");
        System.out.println("  2. Inasistencia Extraordinaria");
        System.out.println("  3. Salida Anticipada");
        System.out.print("Seleccione: ");
        String tipo = scanner.nextLine().trim();

        try {
            if ("1".equals(tipo)) {
                System.out.print("¿Estuvo presente? (s/n): ");
                String resp = scanner.nextLine().trim().toLowerCase();
                boolean presente = resp.equals("s") || resp.equals("si") || resp.equals("sí");
                // SIA-5: Overload 1 — registrar(rut, fecha, boolean)
                controlador.registrarAsistencia(rut, fecha, presente);
                System.out.println("[OK] Asistencia normal registrada.");

            } else if ("2".equals(tipo)) {
                System.out.print("Motivo de la inasistencia: ");
                String motivo = scanner.nextLine();
                // SIA-5: Overload 2 — registrar(rut, fecha, String motivo)
                controlador.registrarAsistencia(rut, fecha, motivo);
                System.out.println("[OK] Inasistencia extraordinaria registrada.");

            } else if ("3".equals(tipo)) {
                System.out.print("Hora de salida (HH:mm): ");
                String hora = scanner.nextLine();
                // SIA-5: Overload 3 — registrar(rut, fecha, String hora, true)
                controlador.registrarAsistencia(rut, fecha, hora, true);
                System.out.println("[OK] Salida anticipada registrada.");

            } else {
                System.out.println("[!] Tipo de asistencia no válido.");
            }
        } catch (RutNoEncontradoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (FormatoAsistenciaInvalidoException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Listar todos los alumnos del TreeMap
    // -----------------------------------------------------------------------
    private void opListarAlumnos() {
        System.out.println("\n-- Listado de Alumnos (ordenado por RUT) --");
        ArrayList<Estudiante> lista = controlador.listarTodosLosAlumnos();
        if (lista.isEmpty()) {
            System.out.println("  No hay alumnos inscritos.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + lista.get(i).toString());
            }
        }
    }

    // -----------------------------------------------------------------------
    // SIA-9 — Filtrar alumnos por inasistencias en un mes
    // -----------------------------------------------------------------------
    private void opFiltrarInasistencias() {
        System.out.println("\n-- Filtro: Alumnos con más de N inasistencias en un mes --");
        System.out.print("Número mínimo de inasistencias (ej: 1): ");
        String nStr = scanner.nextLine().trim();
        System.out.print("Mes a consultar en formato MM (ej: 04 para abril): ");
        String mes = scanner.nextLine().trim();

        int n;
        try {
            n = Integer.parseInt(nStr);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Ingrese un número entero válido.");
            return;
        }

        ArrayList<Estudiante> resultado = controlador.filtrarPorInasistencias(n, mes);
        if (resultado.isEmpty()) {
            System.out.println("  Ningún alumno cumple el criterio.");
        } else {
            System.out.println("  Alumnos con más de " + n + " inasistencia(s) en mes " + mes + ":");
            for (int i = 0; i < resultado.size(); i++) {
                System.out.println("  - " + resultado.get(i).toString());
            }
        }
    }

    // -----------------------------------------------------------------------
    // SIA-11 — Guardar y salir
    // -----------------------------------------------------------------------
    private void opGuardarYSalir() {
        GestorArchivos.guardarEstudiantes(controlador.getCurso(), Main.RUTA_ARCHIVO);
        System.out.println("\n¡Hasta luego! Datos guardados correctamente.");
        scanner.close();
    }
}
