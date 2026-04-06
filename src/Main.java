import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * SIA-10 — Punto de entrada del Sistema de Información de Asistencia (SIA).
 *
 * Al arrancar:
 * 1. Crea el Curso y el Controlador central.
 * 2. SIA-11: Intenta cargar alumnos desde el archivo CSV (persistencia batch).
 * 3. SIA-3:  Si el CSV está vacío o no existe, carga datos hardcodeados de ejemplo.
 * 4. SIA-10: Pregunta al usuario qué interfaz desea usar (Consola o Ventanas).
 */
public class Main {

    /** SIA-11: Ruta del archivo CSV de persistencia. */
    public static final String RUTA_ARCHIVO = "alumnos.csv";

    public static void main(String[] args) {

        // --- Inicialización del modelo ---
        Curso curso = new Curso("3°A — Programación Avanzada", 35);
        Controlador controlador = new Controlador(curso);

        // SIA-11: Cargar datos desde el CSV al iniciar
        GestorArchivos.cargarEstudiantes(curso, RUTA_ARCHIVO);

        // SIA-3: Si tras cargar el CSV el curso está vacío, usar datos hardcodeados
        if (curso.getEstudiantes().isEmpty()) {
            controlador.cargarDatosIniciales();
        }

        // SIA-10: Preguntar al usuario qué interfaz desea usar
        int eleccion = elegirInterfaz();

        if (eleccion == 1) {
            // --- Interfaz de consola ---
            InterfazConsola consola = new InterfazConsola(controlador);
            consola.iniciar();

        } else {
            // --- Interfaz gráfica (JOptionPane) ---
            InterfazVentanas ventanas = new InterfazVentanas(controlador);
            ventanas.iniciar();
        }
    }

    /**
     * SIA-10 — Solicita al usuario que elija la interfaz de usuario.
     * Primero intenta con JOptionPane; si falla (entorno sin GUI), usa Scanner.
     *
     * @return 1 para consola, 2 para ventanas gráficas
     */
    private static int elegirInterfaz() {
        try {
            String[] opciones = {"1. Consola (Scanner)", "2. Ventanas (JOptionPane)"};
            int seleccion = JOptionPane.showOptionDialog(
                null,
                "¿Qué interfaz desea usar?",
                "SIA — Selección de Interfaz",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

            // Si el usuario cierra la ventana, se usa consola por defecto
            if (seleccion == JOptionPane.CLOSED_OPTION) {
                return 1;
            }
            return seleccion + 1; // 0→1 (consola), 1→2 (ventanas)

        } catch (Exception e) {
            // Entorno sin pantalla gráfica: usar consola
            System.out.println("[SIA] Entorno sin GUI detectado. Usando interfaz de consola.");
            Scanner sc = new Scanner(System.in);
            System.out.println("Seleccione interfaz:");
            System.out.println("  1. Consola (Scanner)");
            System.out.println("  2. Ventanas (JOptionPane)");
            System.out.print("Opción: ");
            String resp = sc.nextLine().trim();
            return "2".equals(resp) ? 2 : 1;
        }
    }
}
