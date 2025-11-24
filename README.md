# 📝 Bloc de Notas en Java (Swing)

Aplicación de escritorio desarrollada en **Java** con **Swing**, que permite crear, abrir, editar y guardar archivos de texto plano.  
El proyecto se ha desarrollado como práctica del ciclo **DAM (Desarrollo de Aplicaciones Multiplataforma)** utilizando **NetBeans**.

---

## 📸 Captura de pantalla

<img src="screenshot.png" width="500"/>


---

## 🚀 Funcionalidades

- **Nuevo archivo**  
  Limpia completamente el área de texto y reinicia el editor.

- **Abrir archivo**  
  Permite seleccionar un archivo mediante `JFileChooser` y muestra su contenido.

- **Guardar**  
  - Si el archivo es nuevo → pide nombre y ruta.  
  - Si ya existe → guarda directamente sobre él.

- **Guardar como**  
  Solicita siempre nueva ubicación y nombre.  
  Pregunta antes de sobrescribir si el archivo ya existe.

---

## 🛠 Tecnologías utilizadas

- **Java 17**
- **Swing** (Interfaz gráfica)
- **NetBeans GUI Builder**
- Clases estándar de Java:
  - `File`
  - `BufferedReader`, `BufferedWriter`
  - `FileReader`, `FileWriter`
  - `JFileChooser`, `JOptionPane`

---

## 📁 Estructura del proyecto


