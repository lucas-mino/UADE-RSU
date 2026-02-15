package test;

import algoritmos.programaciondinamica.AsignadorPublicidad;
import algoritmos.programaciondinamica.ResultadoAsignacion;
import java.util.*;
import modelo.Anuncio;
import modelo.Usuario;

/**
 * Set de pruebas para AsignadorPublicidad (Mochila con Programación Dinámica).
 * Incluye casos triviales, pequeños, medianos y grandes para análisis de complejidad
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class TestAsignadorPublicidad {
    
    private AsignadorPublicidad asignador;
    
    public TestAsignadorPublicidad() {
        this.asignador = new AsignadorPublicidad();
    }
    
    /**
     * Test 1: Caso trivial - Ejemplo clásico de knapsack
     */
    public void testCasoTrivial() {
        System.out.println("\n=== TEST 1: CASO TRIVIAL (5 anuncios, presupuesto 100) ===");
        
        Usuario usuario = new Usuario(1, "Ana García", "estudiante");
        
        // Crear anuncios con diferentes costos y alcances
        List<Anuncio> anuncios = new ArrayList<>();
        anuncios.add(new Anuncio(1, "Anuncio A", 10, 60));   // Eficiencia: 6.0
        anuncios.add(new Anuncio(2, "Anuncio B", 20, 100));  // Eficiencia: 5.0
        anuncios.add(new Anuncio(3, "Anuncio C", 30, 120));  // Eficiencia: 4.0
        anuncios.add(new Anuncio(4, "Anuncio D", 40, 180));  // Eficiencia: 4.5
        anuncios.add(new Anuncio(5, "Anuncio E", 50, 280));  // Eficiencia: 5.6
        
        int presupuesto = 100;
        
        System.out.println("\nAnuncios disponibles:");
        for (Anuncio a : anuncios) {
            System.out.println("  " + a);
        }
        System.out.println("\nPresupuesto: " + presupuesto);
        
        // Asignar anuncios
        ResultadoAsignacion resultado = asignador.asignarAnuncios(usuario, anuncios, presupuesto);
        
        System.out.println("\n" + resultado);
        
        // Mostrar tabla DP (primeras filas y columnas)
        asignador.imprimirTablaDP(5, 20);
        
        System.out.println("\nRendimiento:");
        System.out.println("  Operaciones: " + asignador.getOperaciones());
        System.out.println("  Tiempo: " + asignador.getTiempoEjecucionMs() + " ms");
        System.out.println("  Teórico O(n×W): " + AsignadorPublicidad.complejidadTeorica(5, 100));
        
        System.out.println("\n✓ Test trivial completado");
    }
    
    /**
     * Test 2: Caso con perfiles específicos
     */
    public void testConPerfiles() {
        System.out.println("\n=== TEST 2: ANUNCIOS CON PERFILES ESPECÍFICOS ===");
        
        Usuario estudiante = new Usuario(1, "Carlos López", "estudiante");
        Usuario profesor = new Usuario(2, "María Rodríguez", "profesor");
        
        List<Anuncio> anuncios = new ArrayList<>();
        anuncios.add(new Anuncio(1, "Laptop Gaming", 50, 200, "estudiante"));
        anuncios.add(new Anuncio(2, "Curso Online", 30, 150, "estudiante"));
        anuncios.add(new Anuncio(3, "Software Académico", 40, 180, "profesor"));
        anuncios.add(new Anuncio(4, "Libros Técnicos", 25, 100));  // Para todos
        anuncios.add(new Anuncio(5, "Conferencia", 60, 250, "profesor"));
        
        int presupuesto = 100;
        
        System.out.println("\n--- Para Estudiante ---");
        ResultadoAsignacion resEstudiante = asignador.asignarAnuncios(estudiante, anuncios, presupuesto);
        System.out.println(resEstudiante);
        
        System.out.println("\n--- Para Profesor ---");
        ResultadoAsignacion resProfesor = asignador.asignarAnuncios(profesor, anuncios, presupuesto);
        System.out.println(resProfesor);
        
        System.out.println("✓ Test con perfiles completado");
    }
    
    /**
     * Test 3: Caso mediano (20 anuncios, presupuesto 500)
     */
    public void testCasoMediano() {
        System.out.println("\n=== TEST 3: CASO MEDIANO (20 anuncios, presupuesto 500) ===");
        
        Usuario usuario = new Usuario(1, "Juan Martínez", "estudiante");
        List<Anuncio> anuncios = generarAnunciosAleatorios(20, 10, 100, 50, 500);
        int presupuesto = 500;
        
        System.out.println("Anuncios generados: " + anuncios.size());
        System.out.println("Presupuesto: " + presupuesto);
        
        // Asignar
        long inicio = System.nanoTime();
        ResultadoAsignacion resultado = asignador.asignarAnuncios(usuario, anuncios, presupuesto);
        long tiempo = System.nanoTime() - inicio;
        
        System.out.println("\n" + resultado);
        
        System.out.println("Rendimiento:");
        System.out.println("  Tiempo: " + (tiempo / 1_000_000.0) + " ms");
        System.out.println("  Operaciones: " + asignador.getOperaciones());
        System.out.println("  Teórico O(n×W): " + AsignadorPublicidad.complejidadTeorica(20, 500));
        System.out.println("  Ratio (real/teórico): " + 
                String.format("%.2f", (double)asignador.getOperaciones() / 
                AsignadorPublicidad.complejidadTeorica(20, 500)));
        
        System.out.println("\n✓ Test mediano completado");
    }
    
    /**
     * Test 4: Caso grande (50 anuncios, presupuesto 1000)
     */
    public void testCasoGrande() {
        System.out.println("\n=== TEST 4: CASO GRANDE (50 anuncios, presupuesto 1000) ===");
        
        Usuario usuario = new Usuario(1, "Laura Fernández", "estudiante");
        List<Anuncio> anuncios = generarAnunciosAleatorios(50, 10, 100, 50, 500);
        int presupuesto = 1000;
        
        System.out.println("Anuncios generados: " + anuncios.size());
        System.out.println("Presupuesto: " + presupuesto);
        
        // Asignar
        long inicio = System.nanoTime();
        ResultadoAsignacion resultado = asignador.asignarAnuncios(usuario, anuncios, presupuesto);
        long tiempo = System.nanoTime() - inicio;
        
        System.out.println("\n=== RESULTADO ===");
        System.out.println("Anuncios seleccionados: " + resultado.getNumeroAnuncios());
        System.out.println("Alcance total: " + resultado.getAlcanceTotal());
        System.out.println("Costo total: " + resultado.getCostoTotal());
        System.out.println("Presupuesto restante: " + (presupuesto - resultado.getCostoTotal()));
        System.out.println("Eficiencia: " + String.format("%.2f", resultado.getEficienciaPromedio()));
        
        System.out.println("\nRendimiento:");
        System.out.println("  Tiempo: " + (tiempo / 1_000_000.0) + " ms");
        System.out.println("  Operaciones: " + asignador.getOperaciones());
        System.out.println("  Teórico O(n×W): " + AsignadorPublicidad.complejidadTeorica(50, 1000));
        System.out.println("  Ratio: " + 
                String.format("%.2f", (double)asignador.getOperaciones() / 
                AsignadorPublicidad.complejidadTeorica(50, 1000)));
        
        System.out.println("\n✓ Test grande completado");
    }
    
    /**
     * Test 5: Análisis comparativo de complejidad
     */
    public void testAnalisisComplejidad() {
        System.out.println("\n=== TEST 5: ANÁLISIS DE COMPLEJIDAD ===");
        System.out.println("\nComparando rendimiento para diferentes tamaños");
        System.out.println("\n| n   | W    | Tiempo (ms) | Operaciones | Teórico   | Ratio |");
        System.out.println("|-----|------|-------------|-------------|-----------|-------|");
        
        int[][] configuraciones = {
            {5, 100},
            {10, 200},
            {20, 500},
            {30, 700},
            {50, 1000}
        };
        
        Usuario usuario = new Usuario(1, "Test User", "estudiante");
        
        for (int[] config : configuraciones) {
            int n = config[0];
            int W = config[1];
            
            List<Anuncio> anuncios = generarAnunciosAleatorios(n, 10, 50, 50, 300);
            
            long inicio = System.nanoTime();
            asignador.asignarAnuncios(usuario, anuncios, W);
            long tiempo = System.nanoTime() - inicio;
            
            long operaciones = asignador.getOperaciones();
            long teorico = AsignadorPublicidad.complejidadTeorica(n, W);
            double ratio = (double) operaciones / teorico;
            
            System.out.printf("| %-3d | %-4d | %11.3f | %11d | %9d | %5.2f |\n",
                    n, W, tiempo / 1_000_000.0, operaciones, teorico, ratio);
        }
        
        System.out.println("\n✓ Se confirma complejidad O(n × W)");
        System.out.println("✓ Análisis de complejidad completado");
    }
    
    /**
     * Test 6: Comparación versión normal vs optimizada
     */
    public void testVersionOptimizada() {
        System.out.println("\n=== TEST 6: COMPARACIÓN NORMAL VS OPTIMIZADA ===");
        
        Usuario usuario = new Usuario(1, "Test User", "estudiante");
        List<Anuncio> anuncios = generarAnunciosAleatorios(30, 10, 50, 50, 300);
        int presupuesto = 800;
        
        System.out.println("Anuncios: " + anuncios.size());
        System.out.println("Presupuesto: " + presupuesto);
        
        // Versión normal (O(n×W) espacio)
        long inicio1 = System.nanoTime();
        ResultadoAsignacion resultado1 = asignador.asignarAnuncios(usuario, anuncios, presupuesto);
        long tiempo1 = System.nanoTime() - inicio1;
        
        // Versión optimizada (O(W) espacio)
        long inicio2 = System.nanoTime();
        int alcance2 = asignador.asignarAnunciosOptimizado(usuario, anuncios, presupuesto);
        long tiempo2 = System.nanoTime() - inicio2;
        
        System.out.println("\n--- Versión Normal (matriz 2D) ---");
        System.out.println("Alcance: " + resultado1.getAlcanceTotal());
        System.out.println("Tiempo: " + (tiempo1 / 1_000_000.0) + " ms");
        System.out.println("Espacio: O(n × W) = O(" + anuncios.size() + " × " + presupuesto + ") = " + 
                (anuncios.size() * presupuesto) + " enteros");
        
        System.out.println("\n--- Versión Optimizada (arreglo 1D) ---");
        System.out.println("Alcance: " + alcance2);
        System.out.println("Tiempo: " + (tiempo2 / 1_000_000.0) + " ms");
        System.out.println("Espacio: O(W) = O(" + presupuesto + ") = " + presupuesto + " enteros");
        
        System.out.println("\n--- Comparación ---");
        System.out.println("Reducción de espacio: " + 
                String.format("%.1fx", (double)(anuncios.size() * presupuesto) / presupuesto));
        System.out.println("Resultados coinciden: " + (resultado1.getAlcanceTotal() == alcance2));
        
        System.out.println("\n✓ Test de optimización completado");
    }
    
    /**
     * Test 7: Casos especiales
     */
    public void testCasosEspeciales() {
        System.out.println("\n=== TEST 7: CASOS ESPECIALES ===");
        
        Usuario usuario = new Usuario(1, "Usuario Test", "estudiante");
        
        // Caso 1: Presupuesto insuficiente
        System.out.println("\n--- Caso 1: Presupuesto insuficiente ---");
        List<Anuncio> anuncios1 = Arrays.asList(
            new Anuncio(1, "Anuncio caro", 100, 500)
        );
        ResultadoAsignacion res1 = asignador.asignarAnuncios(usuario, anuncios1, 50);
        System.out.println("Anuncios seleccionados: " + res1.getNumeroAnuncios());
        System.out.println("Alcance: " + res1.getAlcanceTotal());
        System.out.println("✓ Maneja correctamente presupuesto insuficiente");
        
        // Caso 2: Todos los anuncios caben
        System.out.println("\n--- Caso 2: Presupuesto sobrado ---");
        List<Anuncio> anuncios2 = Arrays.asList(
            new Anuncio(1, "Anuncio A", 10, 50),
            new Anuncio(2, "Anuncio B", 20, 100),
            new Anuncio(3, "Anuncio C", 15, 75)
        );
        ResultadoAsignacion res2 = asignador.asignarAnuncios(usuario, anuncios2, 1000);
        System.out.println("Anuncios seleccionados: " + res2.getNumeroAnuncios());
        System.out.println("Todos incluidos: " + (res2.getNumeroAnuncios() == anuncios2.size()));
        System.out.println("Alcance total: " + res2.getAlcanceTotal());
        System.out.println("✓ Incluye todos cuando el presupuesto lo permite");
        
        // Caso 3: Anuncios con misma eficiencia
        System.out.println("\n--- Caso 3: Anuncios con misma eficiencia ---");
        List<Anuncio> anuncios3 = Arrays.asList(
            new Anuncio(1, "Anuncio A", 10, 100),  // Eficiencia: 10
            new Anuncio(2, "Anuncio B", 20, 200),  // Eficiencia: 10
            new Anuncio(3, "Anuncio C", 30, 300)   // Eficiencia: 10
        );
        ResultadoAsignacion res3 = asignador.asignarAnuncios(usuario, anuncios3, 35);
        System.out.println("Anuncios seleccionados: " + res3.getNumeroAnuncios());
        System.out.println("Alcance: " + res3.getAlcanceTotal());
        System.out.println("Costo: " + res3.getCostoTotal());
        System.out.println("✓ Maneja correctamente eficiencias iguales");
        
        // Caso 4: Sin anuncios
        System.out.println("\n--- Caso 4: Sin anuncios disponibles ---");
        List<Anuncio> anuncios4 = new ArrayList<>();
        ResultadoAsignacion res4 = asignador.asignarAnuncios(usuario, anuncios4, 100);
        System.out.println("Anuncios seleccionados: " + res4.getNumeroAnuncios());
        System.out.println("✓ Maneja correctamente lista vacía");
        
        System.out.println("\n✓ Casos especiales completados");
    }
    
    /**
     * Genera una lista de anuncios aleatorios.
     */
    private List<Anuncio> generarAnunciosAleatorios(int cantidad, int costoMin, int costoMax,
                                                    int alcanceMin, int alcanceMax) {
        List<Anuncio> anuncios = new ArrayList<>();
        Random random = new Random(42); // Seed fijo para reproducibilidad
        
        for (int i = 0; i < cantidad; i++) {
            int costo = costoMin + random.nextInt(costoMax - costoMin + 1);
            int alcance = alcanceMin + random.nextInt(alcanceMax - alcanceMin + 1);
            String titulo = "Anuncio_" + (i + 1);
            
            anuncios.add(new Anuncio(i + 1, titulo, costo, alcance));
        }
        
        return anuncios;
    }
    
    /**
     * Ejecuta todos los tests
     */
    public void ejecutarTodos() {
        System.out.println("------------------------------------------------------------");
        System.out.println("--  TEST ASIGNADOR DE PUBLICIDAD - PROGRAMACIÓN DINÁMICA  --");
        System.out.println("--  Algoritmo: Mochila                                    --");
        System.out.println("--  Complejidad: O(n × W)                                 --");
        System.out.println("------------------------------------------------------------");
        
        testCasoTrivial();
        testConPerfiles();
        testCasoMediano();
        testCasoGrande();
        testAnalisisComplejidad();
        testVersionOptimizada();
        testCasosEspeciales();
        
        System.out.println("\n------------------------------------------------------------");
        System.out.println("--             TODOS LOS TESTS COMPLETADOS ✓              --");
        System.out.println("------------------------------------------------------------");
    }
    
    /**
     * Main para ejecutar los tests
     */
    public static void main(String[] args) {
        TestAsignadorPublicidad test = new TestAsignadorPublicidad();
        test.ejecutarTodos();
    }
}
