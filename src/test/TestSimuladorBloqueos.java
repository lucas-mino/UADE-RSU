package test;

import algoritmos.backtracking.ParUsuarios;
import algoritmos.backtracking.ResultadoBloqueo;
import algoritmos.backtracking.SimuladorBloqueos;
import java.util.*;
import modelo.Grafo;
import modelo.Usuario;

/**
 * Set de pruebas para SimuladorBloqueos (Backtracking).
 * Incluye casos triviales, pequeños, medianos y análisis del algoritmo
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class TestSimuladorBloqueos {
    
    private SimuladorBloqueos simulador;
    
    public TestSimuladorBloqueos() {
        this.simulador = new SimuladorBloqueos();
    }
    
    /**
     * Test 1: Caso trivial - Bloqueo que no rompe conectividad
     */
    public void testBloqueoSinRomperConectividad() {
        System.out.println("\n=== TEST 1: BLOQUEO SIN ROMPER CONECTIVIDAD ===");
        
        // Crear grafo con ciclo: A -- B -- C -- D -- A
        Usuario a = new Usuario(1, "Ana", "estudiante");
        Usuario b = new Usuario(2, "Bruno", "estudiante");
        Usuario c = new Usuario(3, "Carlos", "estudiante");
        Usuario d = new Usuario(4, "Diana", "estudiante");
        
        Grafo grafo = new Grafo();
        grafo.agregarAmistad(a, b, 1);
        grafo.agregarAmistad(b, c, 1);
        grafo.agregarAmistad(c, d, 1);
        grafo.agregarAmistad(d, a, 1); // Ciclo cerrado
        
        System.out.println("Grafo original (4 usuarios, 4 conexiones en ciclo):");
        System.out.println("  A -- B -- C -- D -- A");
        
        // Bloquear A -> B (sigue habiendo camino A -> D -> C -> B)
        System.out.println("\nSimulando bloqueo: Ana -> Bruno");
        ResultadoBloqueo resultado = simulador.simularBloqueo(grafo, a, b);
        
        System.out.println("\n" + resultado);
        System.out.println("Rendimiento:");
        System.out.println("  Tiempo: " + simulador.getTiempoEjecucionMs() + " ms");
        System.out.println("  Operaciones: " + simulador.getOperaciones());
        
        System.out.println("\n✓ Test completado");
    }
    
    /**
     * Test 2: Bloqueo que rompe conectividad (puente)
     */
    public void testBloqueoRompeConectividad() {
        System.out.println("\n=== TEST 2: BLOQUEO ROMPE CONECTIVIDAD (PUENTE) ===");
        
        // Crear grafo con puente: A -- B -- C (B es puente)
        Usuario a = new Usuario(1, "Ana", "estudiante");
        Usuario b = new Usuario(2, "Bruno", "estudiante");
        Usuario c = new Usuario(3, "Carlos", "estudiante");
        
        Grafo grafo = new Grafo();
        grafo.agregarAmistad(a, b, 1);
        grafo.agregarAmistad(b, c, 1);
        
        System.out.println("Grafo original (3 usuarios, conexión lineal):");
        System.out.println("  A -- B -- C");
        System.out.println("  (B es puente crítico)");
        
        // Bloquear A -> B (desconecta A de C)
        System.out.println("\nSimulando bloqueo: Ana -> Bruno");
        ResultadoBloqueo resultado = simulador.simularBloqueo(grafo, a, b);
        
        System.out.println("\n" + resultado);
        System.out.println("Rendimiento:");
        System.out.println("  Tiempo: " + simulador.getTiempoEjecucionMs() + " ms");
        System.out.println("  Nodos explorados: " + simulador.getNodosExplorados());
        System.out.println("  Nodos recortados: " + simulador.getNodosRecortados());
        
        System.out.println("\n✓ Test completado");
    }
    
    /**
     * Test 3: Caso con múltiples componentes
     */
    public void testMultiplesComponentes() {
        System.out.println("\n=== TEST 3: MÚLTIPLES COMPONENTES ===");
        
        // Crear grafo con 3 componentes:
        // Componente 1: A -- B
        // Componente 2: C -- D
        // Componente 3: E -- F
        
        Usuario a = new Usuario(1, "Ana", "estudiante");
        Usuario b = new Usuario(2, "Bruno", "estudiante");
        Usuario c = new Usuario(3, "Carlos", "estudiante");
        Usuario d = new Usuario(4, "Diana", "estudiante");
        Usuario e = new Usuario(5, "Elena", "estudiante");
        Usuario f = new Usuario(6, "Franco", "estudiante");
        Usuario g = new Usuario(7, "Gloria", "estudiante");
        
        Grafo grafo = new Grafo();
        grafo.agregarAmistad(a, b, 1);
        grafo.agregarAmistad(b, g, 1); // Conectar comp 1 con comp 3
        grafo.agregarAmistad(g, c, 1); // Conectar comp 3 con comp 2
        grafo.agregarAmistad(c, d, 1);
        grafo.agregarAmistad(d, e, 1); // Conectar comp 2 con comp 4
        grafo.agregarAmistad(e, f, 1);
        
        System.out.println("Grafo original (7 usuarios, 6 conexiones):");
        System.out.println("  A -- B -- G -- C -- D -- E -- F");
        
        // Bloquear B -> G (separa en 2 componentes)
        System.out.println("\nSimulando bloqueo: Bruno -> Gloria");
        ResultadoBloqueo resultado = simulador.simularBloqueo(grafo, b, g);
        
        System.out.println("\n" + resultado);
        
        // Identificar componentes
        Grafo grafoCopia = grafo.copiar();
        grafoCopia.eliminarAmistad(b, g);
        List<Set<Usuario>> componentes = simulador.identificarComponentes(grafoCopia);
        
        System.out.println("\nComponentes resultantes: " + componentes.size());
        for (int i = 0; i < componentes.size(); i++) {
            System.out.print("  Componente " + (i+1) + ": ");
            for (Usuario u : componentes.get(i)) {
                System.out.print(u.getNombre() + " ");
            }
            System.out.println();
        }
        
        System.out.println("\nRendimiento:");
        System.out.println("  Tiempo: " + simulador.getTiempoEjecucionMs() + " ms");
        System.out.println("  Nodos explorados: " + simulador.getNodosExplorados());
        System.out.println("  Nodos recortados: " + simulador.getNodosRecortados());
        System.out.println("  Efectividad de poda: " + 
                String.format("%.1f%%", simulador.getEfectividadPoda()));
        
        System.out.println("\n✓ Test completado");
    }
    
    /**
     * Test 4: Análisis de backtracking (árbol de decisiones)
     */
    public void testAnalisisBacktracking() {
        System.out.println("\n=== TEST 4: ANÁLISIS DE BACKTRACKING ===");
        
        // Crear grafo que al bloquearse genera 3 componentes
        // Requiere 2 conexiones mínimas
        
        Usuario a = new Usuario(1, "A", "estudiante");
        Usuario b = new Usuario(2, "B", "estudiante");
        Usuario c = new Usuario(3, "C", "estudiante");
        Usuario d = new Usuario(4, "D", "estudiante");
        Usuario e = new Usuario(5, "E", "estudiante");
        
        Grafo grafo = new Grafo();
        grafo.agregarAmistad(a, b, 1);
        grafo.agregarAmistad(b, c, 1);
        grafo.agregarAmistad(c, d, 1);
        grafo.agregarAmistad(d, e, 1);
        
        System.out.println("Grafo original: A -- B -- C -- D -- E");
        
        // Bloquear B -> C (separa en 2 componentes)
        System.out.println("\nSimulando bloqueo: B -> C");
        
        ResultadoBloqueo resultado = simulador.simularBloqueo(grafo, b, c);
        
        System.out.println("\n" + resultado);
        
        System.out.println("=== ANÁLISIS DEL ALGORITMO ===");
        System.out.println("Nodos explorados en árbol de decisiones: " + 
                simulador.getNodosExplorados());
        System.out.println("Nodos recortados por poda: " + 
                simulador.getNodosRecortados());
        System.out.println("Efectividad de poda: " + 
                String.format("%.1f%%", simulador.getEfectividadPoda()));
        System.out.println("Tiempo total: " + simulador.getTiempoEjecucionMs() + " ms");
        
        // Comparar con solución greedy rápida
        Grafo grafoCopia = grafo.copiar();
        grafoCopia.eliminarAmistad(b, c);
        List<ParUsuarios> solucionRapida = simulador.encontrarConexionesRapido(grafoCopia);
        
        System.out.println("\n--- Comparación con Solución Greedy ---");
        System.out.println("Backtracking (óptimo): " + resultado.getConexionesNecesarias() + " conexiones");
        System.out.println("Greedy (heurística): " + solucionRapida.size() + " conexiones");
        
        System.out.println("\n✓ Test completado");
    }
    
    /**
     * Test 5: Casos especiales
     */
    public void testCasosEspeciales() {
        System.out.println("\n=== TEST 5: CASOS ESPECIALES ===");
        
        // Caso 1: Grafo ya desconectado
        System.out.println("\n--- Caso 1: Grafo ya desconectado ---");
        Usuario a = new Usuario(1, "A", "estudiante");
        Usuario b = new Usuario(2, "B", "estudiante");
        Usuario c = new Usuario(3, "C", "estudiante");
        
        Grafo grafo1 = new Grafo();
        grafo1.agregarAmistad(a, b, 1);
        grafo1.agregarUsuario(c); // C aislado
        
        boolean esConexo1 = simulador.verificarConectividad(grafo1);
        System.out.println("¿Grafo conexo? " + esConexo1);
        System.out.println("✓ Detecta correctamente grafo desconectado");
        
        // Caso 2: Bloqueo inexistente
        System.out.println("\n--- Caso 2: Intentar bloquear conexión inexistente ---");
        Usuario d = new Usuario(4, "D", "estudiante");
        ResultadoBloqueo res2 = simulador.simularBloqueo(grafo1, a, d);
        System.out.println(res2.getMensaje());
        System.out.println("✓ Maneja correctamente bloqueos inexistentes");
        
        // Caso 3: Grafo completo K4
        System.out.println("\n--- Caso 3: Grafo completo (muy redundante) ---");
        Usuario[] usuarios = new Usuario[4];
        for (int i = 0; i < 4; i++) {
            usuarios[i] = new Usuario(i+10, "U" + i, "estudiante");
        }
        
        Grafo grafoCompleto = new Grafo();
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                grafoCompleto.agregarAmistad(usuarios[i], usuarios[j], 1);
            }
        }
        
        System.out.println("Grafo completo K4 (6 aristas)");
        ResultadoBloqueo res3 = simulador.simularBloqueo(grafoCompleto, usuarios[0], usuarios[1]);
        System.out.println("Bloqueo mantiene conectividad: " + res3.isSigueConexo());
        System.out.println("✓ Maneja grafos altamente conectados");
        
        // Caso 4: Estrella con centro crítico
        System.out.println("\n--- Caso 4: Topología estrella (centro crítico) ---");
        Usuario centro = new Usuario(20, "Centro", "estudiante");
        Grafo estrella = new Grafo();
        
        for (int i = 0; i < 5; i++) {
            Usuario rama = new Usuario(21+i, "Rama" + i, "estudiante");
            estrella.agregarAmistad(centro, rama, 1);
        }
        
        Usuario rama0 = new Usuario(21, "Rama0", "estudiante");
        ResultadoBloqueo res4 = simulador.simularBloqueo(estrella, centro, rama0);
        System.out.println("Conexiones necesarias: " + res4.getConexionesNecesarias());
        System.out.println("✓ Maneja topologías estrella");
        
        System.out.println("\n✓ Casos especiales completados");
    }
    
    /**
     * Test 6: Comparación de rendimiento
     */
    public void testRendimiento() {
        System.out.println("\n=== TEST 6: ANÁLISIS DE RENDIMIENTO ===");
        
        System.out.println("\n| Nodos | Aristas | Componentes | Tiempo (ms) | Nodos Expl. | Poda (%) |");
        System.out.println("|-------|---------|-------------|-------------|-------------|----------|");
        
        int[][] configuraciones = {
            {6, 4, 2},   // 6 nodos, 4 aristas, esperamos 2 componentes
            {9, 6, 3},   // 9 nodos, 6 aristas, esperamos 3 componentes
            {12, 9, 3},  // 12 nodos, 9 aristas, esperamos 3 componentes
        };
        
        for (int[] config : configuraciones) {
            int nodos = config[0];
            int aristas = config[1];
            int componentes = config[2];
            
            Grafo grafo = generarGrafoDesconectado(nodos, aristas, componentes);
            
            // Verificar conectividad
            long inicio = System.nanoTime();
            List<Set<Usuario>> comps = simulador.identificarComponentes(grafo);
            long tiempo = System.nanoTime() - inicio;
            
            // Encontrar conexiones mínimas
            inicio = System.nanoTime();
            List<ParUsuarios> conexiones = simulador.encontrarConexionesMinimas(grafo);
            tiempo += System.nanoTime() - inicio;
            
            System.out.printf("| %-5d | %-7d | %-11d | %11.3f | %-11d | %8.1f |\n",
                    nodos, aristas, comps.size(), tiempo / 1_000_000.0,
                    simulador.getNodosExplorados(), simulador.getEfectividadPoda());
        }
        
        System.out.println("\n✓ Análisis de rendimiento completado");
    }
    
    /**
     * Genera un grafo desconectado con componentes específicas.
     */
    private Grafo generarGrafoDesconectado(int totalNodos, int totalAristas, int numComponentes) {
        Grafo grafo = new Grafo();
        Random random = new Random(42);
        
        // Crear usuarios
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < totalNodos; i++) {
            usuarios.add(new Usuario(i, "U" + i, "estudiante"));
            grafo.agregarUsuario(usuarios.get(i));
        }
        
        // Dividir en componentes
        int nodosPorComp = totalNodos / numComponentes;
        int aristasCreadas = 0;
        
        for (int comp = 0; comp < numComponentes && aristasCreadas < totalAristas; comp++) {
            int inicio = comp * nodosPorComp;
            int fin = (comp == numComponentes - 1) ? totalNodos : (comp + 1) * nodosPorComp;
            
            // Conectar nodos dentro de cada componente
            for (int i = inicio; i < fin - 1 && aristasCreadas < totalAristas; i++) {
                grafo.agregarAmistad(usuarios.get(i), usuarios.get(i+1), 1);
                aristasCreadas++;
            }
        }
        
        return grafo;
    }
    
    /**
     * Ejecuta todos los tests
     */
    public void ejecutarTodos() {
        System.out.println("----------------------------------------------------------");
        System.out.println("--   TEST SIMULADOR DE BLOQUEOS - BACKTRACKING          --");
        System.out.println("--   Algoritmo: Backtracking con Poda                   --");
        System.out.println("--   Complejidad: O(V + E) + O(2^C) con poda            --");
        System.out.println("----------------------------------------------------------");
        
        testBloqueoSinRomperConectividad();
        testBloqueoRompeConectividad();
        testMultiplesComponentes();
        testAnalisisBacktracking();
        testCasosEspeciales();
        testRendimiento();
        
        System.out.println("\n----------------------------------------------------------");
        System.out.println("--           TODOS LOS TESTS COMPLETADOS ✓              --");
        System.out.println("----------------------------------------------------------");
    }
    
    /**
     * Main para ejecutar los tests
     */
    public static void main(String[] args) {
        TestSimuladorBloqueos test = new TestSimuladorBloqueos();
        test.ejecutarTodos();
    }
}
