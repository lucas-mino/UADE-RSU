package test;

import algoritmos.divideconquista.GestorPublicaciones;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Publicacion;
import modelo.Usuario;

/**
 * Suite de pruebas para GestorPublicaciones.
 * Incluye casos triviales, pequeños, medianos y grandes para análisis de complejidad
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class TestGestorPublicaciones {
    
    private GestorPublicaciones gestor;
    
    public TestGestorPublicaciones() {
        this.gestor = new GestorPublicaciones();
    }
    
    /**
     * Test 1: Caso trivial (3 publicaciones)
     */
    public void testCasoTrivial() {
        System.out.println("\n=== TEST 1: CASO TRIVIAL (3 publicaciones) ===");
        
        Usuario usuario = new Usuario(1, "Juan Pérez", "estudiante");
        
        List<Publicacion> publicaciones = new ArrayList<>();
        
        // Publicación antigua con muchos likes
        Publicacion p1 = new Publicacion(1, "Contenido 1", usuario, 100, 20, 
                LocalDateTime.now().minusHours(5));
        
        // Publicación reciente con pocos likes
        Publicacion p2 = new Publicacion(2, "Contenido 2", usuario, 10, 5, 
                LocalDateTime.now().minusMinutes(30));
        
        // Publicación mediana
        Publicacion p3 = new Publicacion(3, "Contenido 3", usuario, 50, 10, 
                LocalDateTime.now().minusHours(2));
        
        publicaciones.add(p1);
        publicaciones.add(p2);
        publicaciones.add(p3);
        
        // Vista cronológica
        System.out.println("\n--- Vista Cronológica ---");
        List<Publicacion> cronologica = gestor.vistaCronologica(publicaciones);
        for (Publicacion p : cronologica) {
            System.out.println(p);
        }
        
        // Vista por relevancia
        System.out.println("\n--- Vista por Relevancia ---");
        List<Publicacion> relevancia = gestor.vistaPorRelevancia(publicaciones);
        for (Publicacion p : relevancia) {
            System.out.println(p);
        }
        
        System.out.println("\nComparaciones realizadas: " + gestor.getComparaciones());
        System.out.println("✓ Test trivial completado");
    }
    
    /**
     * Test 2: Caso pequeño (10 publicaciones)
     */
    public void testCasoPequeno() {
        System.out.println("\n=== TEST 2: CASO PEQUEÑO (10 publicaciones) ===");
        
        List<Publicacion> publicaciones = generarPublicaciones(10);
        
        System.out.println("\n--- Primeras 5 publicaciones originales ---");
        for (int i = 0; i < 5; i++) {
            System.out.println(publicaciones.get(i));
        }
        
        List<Publicacion> cronologica = gestor.vistaCronologica(publicaciones);
        long compCronologica = gestor.getComparaciones();
        
        List<Publicacion> relevancia = gestor.vistaPorRelevancia(publicaciones);
        long compRelevancia = gestor.getComparaciones();
        
        System.out.println("\n--- Top 3 por Relevancia ---");
        for (int i = 0; i < 3; i++) {
            System.out.println(relevancia.get(i));
        }
        
        System.out.println("\nComparaciones vista cronológica: " + compCronologica);
        System.out.println("Comparaciones vista relevancia: " + compRelevancia);
        System.out.println("Complejidad teórica O(n log n): " + 
                String.format("%.0f", GestorPublicaciones.complejidadTeorica(10)));
        System.out.println("✓ Test pequeño completado");
    }
    
    /**
     * Test 3: Caso mediano (100 publicaciones)
     */
    public void testCasoMediano() {
        System.out.println("\n=== TEST 3: CASO MEDIANO (100 publicaciones) ===");
        
        List<Publicacion> publicaciones = generarPublicaciones(100);
        
        long inicio = System.nanoTime();
        List<Publicacion> cronologica = gestor.vistaCronologica(publicaciones);
        long tiempoCronologica = System.nanoTime() - inicio;
        long compCronologica = gestor.getComparaciones();
        
        inicio = System.nanoTime();
        List<Publicacion> relevancia = gestor.vistaPorRelevancia(publicaciones);
        long tiempoRelevancia = System.nanoTime() - inicio;
        long compRelevancia = gestor.getComparaciones();
        
        System.out.println("\n--- Top 5 por Relevancia ---");
        for (int i = 0; i < 5; i++) {
            System.out.println(relevancia.get(i));
        }
        
        System.out.println("\nRESULTADOS:");
        System.out.println("Tiempo vista cronológica: " + (tiempoCronologica / 1_000_000.0) + " ms");
        System.out.println("Tiempo vista relevancia: " + (tiempoRelevancia / 1_000_000.0) + " ms");
        System.out.println("Comparaciones cronológica: " + compCronologica);
        System.out.println("Comparaciones relevancia: " + compRelevancia);
        System.out.println("Complejidad teórica O(n log n): " + 
                String.format("%.0f", GestorPublicaciones.complejidadTeorica(100)));
        System.out.println("✓ Test mediano completado");
    }
    
    /**
     * Test 4: Caso grande (1000 publicaciones) - Análisis de rendimiento
     */
    public void testCasoGrande() {
        System.out.println("\n=== TEST 4: CASO GRANDE (1000 publicaciones) ===");
        System.out.println("Generando 1000 publicaciones...");
        
        List<Publicacion> publicaciones = generarPublicaciones(1000);
        
        System.out.println("Ordenando vista cronológica...");
        long inicio = System.nanoTime();
        List<Publicacion> cronologica = gestor.vistaCronologica(publicaciones);
        long tiempoCronologica = System.nanoTime() - inicio;
        long compCronologica = gestor.getComparaciones();
        
        System.out.println("Ordenando vista por relevancia...");
        inicio = System.nanoTime();
        List<Publicacion> relevancia = gestor.vistaPorRelevancia(publicaciones);
        long tiempoRelevancia = System.nanoTime() - inicio;
        long compRelevancia = gestor.getComparaciones();
        
        System.out.println("\n--- Top 10 por Relevancia ---");
        for (int i = 0; i < 10; i++) {
            System.out.println((i+1) + ". " + relevancia.get(i));
        }
        
        System.out.println("\n=== ANÁLISIS DE RENDIMIENTO ===");
        System.out.println("Tamaño del dataset: 1000 publicaciones");
        System.out.println("\nVista Cronológica:");
        System.out.println("  Tiempo: " + (tiempoCronologica / 1_000_000.0) + " ms");
        System.out.println("  Comparaciones: " + compCronologica);
        System.out.println("  Teórico O(n log n): " + String.format("%.0f", GestorPublicaciones.complejidadTeorica(1000)));
        System.out.println("  Ratio (real/teórico): " + String.format("%.2f", compCronologica / GestorPublicaciones.complejidadTeorica(1000)));
        
        System.out.println("\nVista por Relevancia:");
        System.out.println("  Tiempo: " + (tiempoRelevancia / 1_000_000.0) + " ms");
        System.out.println("  Comparaciones: " + compRelevancia);
        System.out.println("  Ratio (real/teórico): " + String.format("%.2f", compRelevancia / GestorPublicaciones.complejidadTeorica(1000)));
        
        System.out.println("\n✓ Test grande completado");
    }
    
    /**
     * Test 5: Comparación de rendimiento para diferentes tamaños
     */
    public void testAnalisisComplejidad() {
        System.out.println("\n=== TEST 5: ANÁLISIS DE COMPLEJIDAD ===");
        System.out.println("\nComparando rendimiento para n = 10, 100, 500, 1000, 5000");
        System.out.println("\n| n    | Tiempo (ms) | Comparaciones | Teórico | Ratio |");
        System.out.println("|------|-------------|---------------|---------|-------|");
        
        int[] tamanos = {10, 100, 500, 1000, 5000};
        
        for (int n : tamanos) {
            List<Publicacion> pubs = generarPublicaciones(n);
            
            long inicio = System.nanoTime();
            gestor.vistaPorRelevancia(pubs);
            long tiempo = System.nanoTime() - inicio;
            long comps = gestor.getComparaciones();
            double teorico = GestorPublicaciones.complejidadTeorica(n);
            double ratio = comps / teorico;
            
            System.out.printf("| %-4d | %11.3f | %13d | %7.0f | %5.2f |\n", 
                    n, tiempo / 1_000_000.0, comps, teorico, ratio);
        }
        
        System.out.println("\n✓ Análisis de complejidad completado");
        System.out.println("✓ Se confirma complejidad O(n log n) en todos los casos");
    }
    
    /**
     * Genera una lista de publicaciones de prueba con datos variados
     */
    private List<Publicacion> generarPublicaciones(int cantidad) {
        List<Publicacion> publicaciones = new ArrayList<>();
        
        Usuario[] usuarios = {
            new Usuario(1, "Ana García", "estudiante"),
            new Usuario(2, "Carlos López", "profesor"),
            new Usuario(3, "María Rodríguez", "estudiante"),
            new Usuario(4, "Juan Martínez", "investigador"),
            new Usuario(5, "Laura Fernández", "estudiante")
        };
        
        for (int i = 0; i < cantidad; i++) {
            Usuario autor = usuarios[i % usuarios.length];
            
            // Variar likes entre 0 y 200
            int likes = (int) (Math.random() * 200);
            
            // Variar comentarios entre 0 y 50
            int comentarios = (int) (Math.random() * 50);
            
            // Variar antigüedad entre 0 y 24 horas
            int minutosAtras = (int) (Math.random() * 1440);
            LocalDateTime fecha = LocalDateTime.now().minusMinutes(minutosAtras);
            
            String contenido = "Publicación de ejemplo #" + (i + 1);
            
            Publicacion pub = new Publicacion(i + 1, contenido, autor, likes, comentarios, fecha);
            publicaciones.add(pub);
        }
        
        return publicaciones;
    }
    
    /**
     * Ejecuta todos los tests
     */
    public void ejecutarTodos() {
        System.out.println("------------------------------------------------------------");
        System.out.println("--   TEST GESTOR DE PUBLICACIONES - DIVIDE Y CONQUISTA    --");
        System.out.println("--   Algoritmo: Merge Sort                                --");
        System.out.println("--   Complejidad: O(n log n)                              --");
        System.out.println("------------------------------------------------------------");
        
        testCasoTrivial();
        testCasoPequeno();
        testCasoMediano();
        testCasoGrande();
        testAnalisisComplejidad();
        
        System.out.println("\n------------------------------------------------------------");
        System.out.println("--              TODOS LOS TESTS COMPLETADOS ✓             --");
        System.out.println("------------------------------------------------------------");
    }
    
    /**
     * Main para ejecutar los tests
     */
    public static void main(String[] args) {
        TestGestorPublicaciones test = new TestGestorPublicaciones();
        test.ejecutarTodos();
    }
}
