package algoritmos.programaciondinamica;

import java.util.*;
import modelo.Anuncio;
import modelo.Usuario;

/**
 * Asignador de publicidad usando PROGRAMACIÓN DINÁMICA (Mochila)
 * 
 * PROBLEMA: Dado un conjunto de anuncios (cada uno con costo y alcance) y un
 * presupuesto limitado, seleccionar qué anuncios mostrar para MAXIMIZAR el
 * alcance total sin exceder el presupuesto.
 * 
 * ENFOQUE: Mochila (mochila 0/1)
 * - Cada anuncio puede ser incluido (1) o no incluido (0)
 * - No se pueden incluir parcialmente
 * - Maximizar valor (alcance) sujeto a restricción de peso (costo ≤ presupuesto)
 * 
 * COMPLEJIDAD:
 * - Temporal: O(n × W) donde n = número de anuncios, W = presupuesto
 * - Espacial: O(n × W) para la tabla DP (optimizable a O(W))
 * 
 * PARADIGMA: Programación Dinámica
 * - Subestructura óptima: La solución óptima contiene soluciones óptimas de subproblemas
 * - Subproblemas superpuestos: Los mismos subproblemas se resuelven múltiples veces
 * - Memorización: Almacenar resultados para evitar recalcular
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class AsignadorPublicidad {
    
    // Tabla DP para memorización
    private int[][] dp;
    
    // Para análisis de rendimiento
    private long operaciones = 0;
    private long tiempoEjecucion = 0;
    
    /**
     * Asigna anuncios a un usuario maximizando el alcance sin exceder presupuesto.
     * Usa el algoritmo de Mochila con Programación Dinámica.
     * 
     * @param usuario usuario al que asignar anuncios
     * @param anuncios lista de anuncios disponibles
     * @param presupuesto presupuesto máximo disponible
     * @return resultado con anuncios seleccionados y alcance total
     */
    public ResultadoAsignacion asignarAnuncios(Usuario usuario, List<Anuncio> anuncios, int presupuesto) {
        operaciones = 0;
        long inicio = System.nanoTime();
        
        // Filtrar anuncios aplicables al perfil del usuario
        List<Anuncio> anunciosAplicables = filtrarAnunciosAplicables(usuario, anuncios);
        
        int n = anunciosAplicables.size();
        
        // Caso base: sin anuncios o presupuesto cero
        if (n == 0 || presupuesto <= 0) {
            tiempoEjecucion = System.nanoTime() - inicio;
            return new ResultadoAsignacion(new ArrayList<>(), 0, 0);
        }
        
        // Inicializar tabla DP
        // dp[i][w] = máximo alcance usando los primeros i anuncios con presupuesto w
        dp = new int[n + 1][presupuesto + 1];
        
        // Construir tabla DP (bottom-up)
        construirTablaDP(anunciosAplicables, presupuesto);
        
        // Reconstruir solución (qué anuncios fueron seleccionados)
        List<Anuncio> anunciosSeleccionados = reconstruirSolucion(anunciosAplicables, presupuesto);
        
        // Calcular alcance total y costo total
        int alcanceTotal = dp[n][presupuesto];
        int costoTotal = anunciosSeleccionados.stream()
                .mapToInt(Anuncio::getCosto)
                .sum();
        
        tiempoEjecucion = System.nanoTime() - inicio;
        
        return new ResultadoAsignacion(anunciosSeleccionados, alcanceTotal, costoTotal);
    }
    
    /**
     * Construye la tabla de Programación Dinámica.
     * 
     * ECUACIÓN DE RECURRENCIA:
     * dp[i][w] = max(
     *   dp[i-1][w],                              // No incluir anuncio i
     *   dp[i-1][w - costo[i]] + alcance[i]       // Incluir anuncio i
     * )
     * 
     * @param anuncios lista de anuncios
     * @param presupuesto presupuesto máximo
     */
    private void construirTablaDP(List<Anuncio> anuncios, int presupuesto) {
        int n = anuncios.size();
        
        // Inicializar primera fila (sin anuncios) = 0
        for (int w = 0; w <= presupuesto; w++) {
            dp[0][w] = 0;
            operaciones++;
        }
        
        // Llenar tabla DP
        for (int i = 1; i <= n; i++) {
            Anuncio anuncio = anuncios.get(i - 1);
            int costo = anuncio.getCosto();
            int alcance = anuncio.getAlcancePotencial();
            
            for (int w = 0; w <= presupuesto; w++) {
                operaciones++;
                
                // Opción 1: No incluir el anuncio actual
                dp[i][w] = dp[i - 1][w];
                
                // Opción 2: Incluir el anuncio actual (si cabe en presupuesto)
                if (costo <= w) {
                    int alcanceIncluyendo = dp[i - 1][w - costo] + alcance;
                    
                    // Elegir la mejor opción (DECISIÓN ÓPTIMA)
                    if (alcanceIncluyendo > dp[i][w]) {
                        dp[i][w] = alcanceIncluyendo;
                    }
                }
            }
        }
    }
    
    /**
     * Reconstruye la solución óptima (qué anuncios fueron seleccionados).
     * Recorre la tabla DP hacia atrás desde dp[n][presupuesto].
     * 
     * @param anuncios lista de anuncios
     * @param presupuesto presupuesto máximo
     * @return lista de anuncios seleccionados
     */
    private List<Anuncio> reconstruirSolucion(List<Anuncio> anuncios, int presupuesto) {
        List<Anuncio> seleccionados = new ArrayList<>();
        int n = anuncios.size();
        int w = presupuesto;
        
        // Recorrer hacia atrás desde dp[n][presupuesto]
        for (int i = n; i > 0 && w > 0; i--) {
            // Si el valor cambió respecto a la fila anterior, el anuncio fue incluido
            if (dp[i][w] != dp[i - 1][w]) {
                Anuncio anuncio = anuncios.get(i - 1);
                seleccionados.add(anuncio);
                w -= anuncio.getCosto();
            }
        }
        
        // Invertir para tener el orden original
        Collections.reverse(seleccionados);
        return seleccionados;
    }
    
    /**
     * Filtra anuncios que aplican al perfil del usuario.
     * 
     * @param usuario usuario objetivo
     * @param anuncios lista completa de anuncios
     * @return anuncios aplicables al usuario
     */
    private List<Anuncio> filtrarAnunciosAplicables(Usuario usuario, List<Anuncio> anuncios) {
        List<Anuncio> aplicables = new ArrayList<>();
        String perfilUsuario = usuario.getPerfil();
        
        for (Anuncio anuncio : anuncios) {
            if (anuncio.aplicaParaPerfil(perfilUsuario)) {
                aplicables.add(anuncio);
            }
        }
        
        return aplicables;
    }
    
    /**
     * Versión optimizada en espacio: O(W) en lugar de O(n × W).
     * Usa solo un arreglo 1D en lugar de matriz 2D.
     * 
     * @param usuario usuario objetivo
     * @param anuncios lista de anuncios
     * @param presupuesto presupuesto máximo
     * @return alcance máximo alcanzable
     */
    public int asignarAnunciosOptimizado(Usuario usuario, List<Anuncio> anuncios, int presupuesto) {
        List<Anuncio> anunciosAplicables = filtrarAnunciosAplicables(usuario, anuncios);
        int n = anunciosAplicables.size();
        
        if (n == 0 || presupuesto <= 0) {
            return 0;
        }
        
        // Usar solo un arreglo 1D
        int[] dpOptimizado = new int[presupuesto + 1];
        
        // Llenar arreglo DP (de derecha a izquierda para evitar sobrescribir)
        for (int i = 0; i < n; i++) {
            Anuncio anuncio = anunciosAplicables.get(i);
            int costo = anuncio.getCosto();
            int alcance = anuncio.getAlcancePotencial();
            
            // IMPORTANTE: Recorrer de derecha a izquierda
            for (int w = presupuesto; w >= costo; w--) {
                dpOptimizado[w] = Math.max(
                    dpOptimizado[w],
                    dpOptimizado[w - costo] + alcance
                );
            }
        }
        
        return dpOptimizado[presupuesto];
    }
    
    /**
     * Obtiene la tabla DP completa (para debugging y análisis).
     * 
     * @return tabla DP
     */
    public int[][] getTablaDP() {
        return dp;
    }
    
    /**
     * Imprime la tabla DP de forma legible.
     * Útil para entender cómo se construye la solución.
     */
    public void imprimirTablaDP(int maxFilas, int maxColumnas) {
        if (dp == null) {
            System.out.println("Tabla DP no inicializada");
            return;
        }
        
        int filas = Math.min(dp.length, maxFilas + 1);
        int columnas = Math.min(dp[0].length, maxColumnas + 1);
        
        System.out.println("\nTabla DP (primeras " + (filas-1) + " filas, " + (columnas-1) + " columnas):");
        System.out.print("     ");
        for (int w = 0; w < columnas; w++) {
            System.out.printf("%4d ", w);
        }
        System.out.println();
        
        for (int i = 0; i < filas; i++) {
            System.out.printf("%2d | ", i);
            for (int w = 0; w < columnas; w++) {
                System.out.printf("%4d ", dp[i][w]);
            }
            System.out.println();
        }
    }
    
    // Getters para análisis de rendimiento
    
    public long getOperaciones() {
        return operaciones;
    }
    
    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }
    
    public double getTiempoEjecucionMs() {
        return tiempoEjecucion / 1_000_000.0;
    }
    
    /**
     * Calcula la complejidad teórica para el problema.
     * 
     * @param n número de anuncios
     * @param W presupuesto
     * @return complejidad teórica n × W
     */
    public static long complejidadTeorica(int n, int W) {
        return (long) n * W;
    }
}
