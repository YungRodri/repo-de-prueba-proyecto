/**
 * SIA-4/SIA-6 — Superclase de la jerarquía de personas.
 * Encapsulamiento estricto: todos los atributos son private.
 */
public class Persona {

    private String rut;
    private String nombre;
    private String apellidos;

    public Persona(String rut, String nombre, String apellidos) {
        this.rut = rut;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    // --- Getters ---
    public String getRut() { return rut; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }

    // --- Setters ---
    public void setRut(String rut) { this.rut = rut; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    @Override
    public String toString() {
        return "RUT: " + rut + " | Nombre: " + nombre + " " + apellidos;
    }
}
