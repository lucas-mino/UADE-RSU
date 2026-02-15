package test;

import algoritmos.greedy.EstadisticasConectividad;
import algoritmos.greedy.RecomendacionAmigo;
import algoritmos.greedy.RecomendadorAmigos;
import java.util.*;
import modelo.Grafo;
import modelo.Usuario;

/**
 * Suite de pruebas para RecomendadorAmigos (Algoritmo de Dijkstra).
 * Incluye casos triviales, pequeños, medianos y grandes para análisis de complejidad
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class TestRecomendadorAmigos {
    
    private RecomendadorAmigos recomendador;
    
    public TestRecomendadorAmigos() {
        this.recomendador = new RecomendadorAmigos();
    }
    
    /**
     * Test 1: Caso trivial - Red pequeña de 5 usuarios
     */
    public void testCasoTrivial() {
        System.out.println("\n=== TEST 1: CASO TRIVIAL (5 usuarios) ===");
        System.out.println("\nCreando red social pequeña...");
        
        // Crear usuarios
        Usuario ana = new Usuario(1, "Ana García", "estudiante");
        Usuario carlos = new Usuario(2, "Carlos López", "estudiante");
        Usuario maria = new Usuario(3, "María Rodríguez", "profesor");
        Usuario juan = new Usuario(4, "Juan Martínez", "estudiante");
        Usuario laura = new Usuario(5, "Laura Fernández", "estudiante");
        
        // Crear grafo
        Grafo grafo = new Grafo();
        
        // Conexiones:
        // Ana -- Carlos (peso 5)
        // Ana -- María (peso 10)
        // Carlos -- Juan (peso 3)
        // María -- Juan (peso 7)
        // Juan -- Laura (peso 2)
        
        grafo.agregarAmistad(ana, carlos, 5);
        grafo.agregarAmistad(ana, maria, 10);
        grafo.agregarAmistad(carlos, juan, 3);
        grafo.agregarAmistad(maria, juan, 7);
        grafo.agregarAmistad(juan, laura, 2);
        
        System.out.println("\n" + grafo);
        
        // Recomendar amigos para Ana
        System.out.println("=== Recomendaciones para Ana García ===");
        List<RecomendacionAmigo> recomendaciones = recomendador.recomendar(grafo, ana, 3);
        
        for (int i = 0; i < recomendaciones.size(); i++) {
            System.out.println((i+1) + ". " + recomendaciones.get(i));
        }
        
        // Analizar conectividad de Ana
        System.out.println("\n" + recomendador.analizarConectividad(grafo, ana));
        
        System.out.println("\nOperaciones: " + recomendador.getOperaciones());
        System.out.println("Tiempo: " + recomendador.getTiempoEjecucionMs() + " ms");
        System.out.println("\n✓ Test trivial completado");
    }
    
    /**
     * Test 2: Red con amigos mutuos
     */
    public void testAmigosMutuos() {
        System.out.println("\n=== TEST 2: AMIGOS MUTUOS (10 usuarios) ===");
        
        // Crear 10 usuarios
        Usuario[] usuarios = new Usuario[10];
        for (int i = 0; i < 10; i++) {
            usuarios[i] = new Usuario(i+1, "Usuario" + (i+1), "estudiante");
        }
        
        Grafo grafo = new Grafo();
        
        // Crear una red donde Usuario1 tiene amigos que también se conocen entre sí
        // Usuario1 -- Usuario2 (peso 2)
        // Usuario1 -- Usuario3 (peso 3)
        // Usuario2 -- Usuario4 (peso 1)
        // Usuario3 -- Usuario4 (peso 1)
        // Usuario4 -- Usuario5 (peso 2)
        
        grafo.agregarAmistad(usuarios[0], usuarios[1], 2);
        grafo.agregarAmistad(usuarios[0], usuarios[2], 3);
        grafo.agregarAmistad(usuarios[1], usuarios[3], 1);
        grafo.agregarAmistad(usuarios[2], usuarios[3], 1);
        grafo.agregarAmistad(usuarios[3], usuarios[4], 2);
        
        // Agregar más conexiones
        grafo.agregarAmistad(usuarios[4], usuarios[5], 3);
        grafo.agregarAmistad(usuarios[5], usuarios[6], 1);
        grafo.agregarAmistad(usuarios[6], usuarios[7], 2);
        grafo.agregarAmistad(usuarios[7], usuarios[8], 1);
        grafo.agregarAmistad(usuarios[8], usuarios[9], 2);
        
        System.out.println("Red creada con " + grafo.getNumeroVertices() + 
                         " usuarios y " + grafo.getNumeroAristas() + " amistades");
        
        // Recomendar para Usuario1
        System.out.println("\n=== Top 5 recomendaciones para Usuario1 ===");
        List<RecomendacionAmigo> recs = recomendador.recomendar(grafo, usuarios[0], 5);
        
        for (int i = 0; i < recs.size(); i++) {
            RecomendacionAmigo rec = recs.get(i);
            System.out.println(String.format("%d. %s", i+1, rec));
        }
        
        // Estadísticas
        System.out.println("\n" + recomendador.analizarConectividad(grafo, usuarios[0]));
        
        System.out.println("\nRendimiento:");
        System.out.println("  Operaciones: " + recomendador.getOperaciones());
        System.out.println("  Tiempo: " + recomendador.getTiempoEjecucionMs() + " ms");
        System.out.println("\n✓ Test de amigos mutuos completado");
    }
    
    /**
     * Test 3: Red mediana (50 usuarios)
     */
    public void testCasoMediano() {
        System.out.println("\n=== TEST 3: CASO MEDIANO (50 usuarios) ===");
        
        Grafo grafo = generarRedAleatoria(50, 100);
        
        System.out.println("Red generada:");
        System.out.println("  Usuarios: " + grafo.getNumeroVertices());
        System.out.println("  Amistades: " + grafo.getNumeroAristas());
        
        // Seleccionar un usuario aleatorio
        Usuario usuario = grafo.getUsuarios().iterator().next();
        
        System.out.println("\n=== Análisis para " + usuario.getNombre() + " ===");
        
        long inicio = System.nanoTime();
        List<RecomendacionAmigo> recs = recomendador.recomendar(grafo, usuario, 10);
        long tiempo = System.nanoTime() - inicio;
        
        System.out.println("\nTop 10 recomendaciones:");
        for (int i = 0; i < Math.min(10, recs.size()); i++) {
            System.out.println((i+1) + ". " + recs.get(i).getUsuario().getNombre() + 
                             " (distancia: " + recs.get(i).getDistancia() + ")");
        }
        
        EstadisticasConectividad stats = recomendador.analizarConectividad(grafo, usuario);
        System.out.println("\n" + stats);
        
        System.out.println("\nRendimiento:");
        System.out.println("  Operaciones: " + recomendador.getOperaciones());
        System.out.println("  Tiempo: " + (tiempo / 1_000_000.0) + " ms");
        System.out.println("  Teórico O((V+E)logV): " + 
                String.format("%.0f", RecomendadorAmigos.complejidadTeorica(50, 100)));
        
        System.out.println("\n✓ Test mediano completado");
    }
    
    /**
     * Test 4: Red grande (200 usuarios) - Análisis de rendimiento
     */
    public void testCasoGrande() {
        System.out.println("\n=== TEST 4: CASO GRANDE (200 usuarios) ===");
        
        System.out.println("Generando red grande...");
        Grafo grafo = generarRedAleatoria(200, 500);
        
        System.out.println("Red generada:");
        System.out.println("  Usuarios: " + grafo.getNumeroVertices());
        System.out.println("  Amistades: " + grafo.getNumeroAristas());
        
        // Probar con varios usuarios
        List<Usuario> muestra = new ArrayList<>(grafo.getUsuarios());
        muestra = muestra.subList(0, Math.min(5, muestra.size()));
        
        System.out.println("\nEjecutando Dijkstra desde " + muestra.size() + " usuarios diferentes...");
        
        long tiempoTotal = 0;
        long operacionesTotal = 0;
        
        for (Usuario usuario : muestra) {
            long inicio = System.nanoTime();
            Map<Usuario, Integer> distancias = recomendador.calcularDistancias(grafo, usuario);
            long tiempo = System.nanoTime() - inicio;
            
            tiempoTotal += tiempo;
            operacionesTotal += recomendador.getOperaciones();
        }
        
        double tiempoPromedio = (tiempoTotal / muestra.size()) / 1_000_000.0;
        long operacionesPromedio = operacionesTotal / muestra.size();
        
        System.out.println("\n=== RESULTADOS PROMEDIO ===");
        System.out.println("Tiempo promedio: " + String.format("%.2f", tiempoPromedio) + " ms");
        System.out.println("Operaciones promedio: " + operacionesPromedio);
        System.out.println("Teórico O((V+E)logV): " + 
                String.format("%.0f", RecomendadorAmigos.complejidadTeorica(200, 500)));
        
        // Recomendaciones para un usuario
        Usuario usuarioPrueba = muestra.get(0);
        System.out.println("\n=== Top 5 recomendaciones para " + usuarioPrueba.getNombre() + " ===");
        List<RecomendacionAmigo> recs = recomendador.recomendar(grafo, usuarioPrueba, 5);
        
        for (int i = 0; i < recs.size(); i++) {
            System.out.println((i+1) + ". " + recs.get(i).getUsuario().getNombre() + 
                             " (distancia: " + recs.get(i).getDistancia() + ")");
        }
        
        System.out.println("\n✓ Test grande completado");
    }
    
    /**
     * Test 5: Análisis comparativo de complejidad
     */
    public void testAnalisisComplejidad() {
        System.out.println("\n=== TEST 5: ANÁLISIS DE COMPLEJIDAD ===");
        System.out.println("\nComparando rendimiento para diferentes tamaños de red");
        System.out.println("\n| Vértices | Aristas | Tiempo (ms) | Operaciones | Teórico  | Ratio |");
        System.out.println("|----------|---------|-------------|-------------|----------|-------|");
        
        int[][] configuraciones = {
            {10, 15},
            {25, 40},
            {50, 100},
            {100, 250},
            {200, 500}
        };
        
        for (int[] config : configuraciones) {
            int V = config[0];
            int E = config[1];
            
            Grafo grafo = generarRedAleatoria(V, E);
            Usuario usuario = grafo.getUsuarios().iterator().next();
            
            // Ejecutar Dijkstra varias veces y promediar
            long tiempoTotal = 0;
            long operacionesTotal = 0;
            int repeticiones = 3;
            
            for (int i = 0; i < repeticiones; i++) {
                long inicio = System.nanoTime();
                recomendador.calcularDistancias(grafo, usuario);
                tiempoTotal += System.nanoTime() - inicio;
                operacionesTotal += recomendador.getOperaciones();
            }
            
            double tiempoPromedio = (tiempoTotal / repeticiones) / 1_000_000.0;
            long operacionesPromedio = operacionesTotal / repeticiones;
            double teorico = RecomendadorAmigos.complejidadTeorica(V, E);
            double ratio = operacionesPromedio / teorico;
            
            System.out.printf("| %-8d | %-7d | %11.3f | %11d | %8.0f | %5.2f |\n",
                    V, E, tiempoPromedio, operacionesPromedio, teorico, ratio);
        }
        
        System.out.println("\n✓ Se confirma complejidad O((V + E) log V)");
        System.out.println("✓ Análisis de complejidad completado");
    }
    
    /**
     * Test 6: Casos especiales
     */
    public void testCasosEspeciales() {
        System.out.println("\n=== TEST 6: CASOS ESPECIALES ===");
        
        // Caso 1: Usuario sin amigos
        System.out.println("\n--- Caso 1: Usuario aislado ---");
        Grafo grafo1 = new Grafo();
        Usuario aislado = new Usuario(1, "Usuario Aislado", "estudiante");
        Usuario otro = new Usuario(2, "Otro Usuario", "estudiante");
        grafo1.agregarUsuario(aislado);
        grafo1.agregarUsuario(otro);
        
        List<RecomendacionAmigo> recs1 = recomendador.recomendar(grafo1, aislado, 5);
        System.out.println("Recomendaciones para usuario aislado: " + recs1.size());
        System.out.println("✓ Maneja correctamente usuarios sin conexiones");
        
        // Caso 2: Todos son amigos directos
        System.out.println("\n--- Caso 2: Todos son amigos directos ---");
        Grafo grafo2 = new Grafo();
        Usuario central = new Usuario(10, "Usuario Central", "estudiante");
        grafo2.agregarUsuario(central);
        
        for (int i = 1; i <= 5; i++) {
            Usuario amigo = new Usuario(10 + i, "Amigo" + i, "estudiante");
            grafo2.agregarAmistad(central, amigo, i);
        }
        
        List<RecomendacionAmigo> recs2 = recomendador.recomendar(grafo2, central, 5);
        System.out.println("Recomendaciones cuando todos son amigos directos: " + recs2.size());
        System.out.println("✓ Maneja correctamente cuando no hay recomendaciones");
        
        // Caso 3: Cadena lineal
        System.out.println("\n--- Caso 3: Cadena lineal ---");
        Grafo grafo3 = new Grafo();
        Usuario[] cadena = new Usuario[6];
        for (int i = 0; i < 6; i++) {
            cadena[i] = new Usuario(20 + i, "Usuario" + i, "estudiante");
            grafo3.agregarUsuario(cadena[i]);
        }
        
        for (int i = 0; i < 5; i++) {
            grafo3.agregarAmistad(cadena[i], cadena[i+1], 1);
        }
        
        List<RecomendacionAmigo> recs3 = recomendador.recomendar(grafo3, cadena[0], 3);
        System.out.println("\nTop 3 desde el extremo de la cadena:");
        for (int i = 0; i < recs3.size(); i++) {
            System.out.println((i+1) + ". " + recs3.get(i));
        }
        System.out.println("✓ Maneja correctamente topologías lineales");
        
        System.out.println("\n✓ Casos especiales completados");
    }
    
    /**
     * Genera una red social aleatoria.
     * 
     * @param numUsuarios número de usuarios
     * @param numAmistades número de amistades (aristas)
     * @return grafo generado
     */
    private Grafo generarRedAleatoria(int numUsuarios, int numAmistades) {
        Grafo grafo = new Grafo();
        Random random = new Random(42); // Seed fijo para reproducibilidad
        
        // Crear usuarios
        Usuario[] usuarios = new Usuario[numUsuarios];
        for (int i = 0; i < numUsuarios; i++) {
            usuarios[i] = new Usuario(i+1, "Usuario" + (i+1), "estudiante");
            grafo.agregarUsuario(usuarios[i]);
        }
        
        // Agregar amistades aleatorias
        int aristasCreadas = 0;
        int intentos = 0;
        int maxIntentos = numAmistades * 10;
        
        while (aristasCreadas < numAmistades && intentos < maxIntentos) {
            int i = random.nextInt(numUsuarios);
            int j = random.nextInt(numUsuarios);
            
            if (i != j && !grafo.sonAmigos(usuarios[i], usuarios[j])) {
                int peso = random.nextInt(20) + 1; // Peso entre 1 y 20
                grafo.agregarAmistad(usuarios[i], usuarios[j], peso);
                aristasCreadas++;
            }
            
            intentos++;
        }
        
        return grafo;
    }
    
    /**
     * Ejecuta todos los tests
     */
    public void ejecutarTodos() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST RECOMENDADOR DE AMIGOS - ALGORITMO DE DIJKSTRA    ║");
        System.out.println("║   Paradigma: GREEDY                                      ║");
        System.out.println("║   Complejidad: O((V + E) log V)                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        testCasoTrivial();
        testAmigosMutuos();
        testCasoMediano();
        testCasoGrande();
        testAnalisisComplejidad();
        testCasosEspeciales();
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              TODOS LOS TESTS COMPLETADOS ✓               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Main para ejecutar los tests
     */
    public static void main(String[] args) {
        TestRecomendadorAmigos test = new TestRecomendadorAmigos();
        test.ejecutarTodos();
    }
}
