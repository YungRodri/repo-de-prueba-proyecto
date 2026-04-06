# SIA — Sistema de Información de Asistencia

Proyecto universitario en **Java puro** (JDK 8). Controla la asistencia de alumnos de un curso usando colecciones, herencia, excepciones personalizadas y persistencia en archivo CSV.

---

## ¿Cómo probarlo?

### Requisito: tener Java instalado

Verifica con:
```bash
java -version
```
Si no tienes Java, descárgalo en https://adoptium.net (Eclipse Temurin, versión 8 o superior).

---

### Opción A — Desde terminal (CMD o PowerShell)

```bash
# 1. Clonar el repositorio
git clone https://github.com/YungRodri/repo-de-prueba-proyecto.git
cd repo-de-prueba-proyecto

# 2. Crear carpeta de compilados
mkdir bin

# 3. Compilar
javac -encoding UTF-8 -d bin src/*.java

# 4. Ejecutar
java -cp bin Main
```

Al ejecutar, el programa preguntará:
- **Opción 1 → Consola**: menú de texto en la misma terminal
- **Opción 2 → Ventanas**: menú gráfico con ventanas emergentes (JOptionPane)

> No es necesario tener datos previos. El sistema carga 3 alumnos de ejemplo automáticamente al iniciar.

---

### Opción B — Desde IntelliJ IDEA

1. `File → Open` → seleccionar la carpeta del proyecto
2. Clic derecho en la carpeta `src/` → **Mark Directory as → Sources Root**
3. `File → Project Structure → SDK` → seleccionar un JDK instalado
4. Abrir `Main.java` y pulsar el botón ▶ **Run**

---

## Estructura del proyecto

```
src/
├── Main.java                          # Punto de entrada
├── Controlador.java                   # Lógica central (CRUD, filtros, sobrecarga)
├── Curso.java                         # TreeMap<String, Estudiante>
├── Estudiante.java                    # ArrayList<Asistencia> anidado
├── Persona.java                       # Superclase
├── Asistencia.java                    # Clase abstracta (herencia)
├── AsistenciaNormal.java              # Subclase con @Override getResumen()
├── InasistenciaExtraordinaria.java    # Subclase con @Override getResumen()
├── SalidaAnticipada.java              # Subclase con @Override getResumen()
├── GestorArchivos.java                # Persistencia CSV (lectura/escritura)
├── InterfazConsola.java               # UI por Scanner
├── InterfazVentanas.java              # UI por JOptionPane
├── RutNoEncontradoException.java      # Excepción personalizada
└── FormatoAsistenciaInvalidoException.java  # Excepción personalizada
```
