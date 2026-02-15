package algoritmos.greedy;

import java.util.*;
import modelo.Arista;
import modelo.Grafo;
import modelo.Usuario;

/**
 * Recomendador de amigos usando el algoritmo de DIJKSTRA (paradigma GREEDY)
 * 
 * Encuentra los caminos más cortos desde un usuario a todos los demás en la red,
 * y recomienda como amigos potenciales a aquellos que están más cerca pero no
 * son amigos directos.
 * 
 * COMPLEJIDAD:
 * - Temporal: O((V + E) log V) con Priority Queue (heap binario)
 * - Espacial: O(V) para almacenar distancias y predecesores
 * 
 * PARADIGMA GREEDY: En cada paso, se selecciona el nodo no visitado con la
 * distancia mínima acumulada, garantizando que se encuentra el camino óptimo.
 * 
 * @author Lucas Miño
 * @version 1.0
 */
public class RecomendadorAmigos {
    
    // Para análisis de rendimiento
    private long operaciones = 0;
    private long tiempoEjecucion = 0;
    
    /**
     * Clase interna para representar un nodo en el algoritmo de Dijkstra.
     * Incluye el usuario y su distancia acumulada desde el origen.
     */
    private static class NodoDijkstra implements Comparable<NodoDijkstra> {
        Usuario usuario;
        int distancia;
        
        public NodoDijkstra(Usuario usuario, int distancia) {
            this.usuario = usuario;
            this.distancia = distancia;
        }
        
        @Override
        public int compareTo(NodoDijkstra otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }
    
    /**
     * Ejecuta el algoritmo de Dijkstra desde un usuario origen.
     * Calcula la distancia mínima a todos los demás usuarios en la red.
     * 
     * @param grafo red social de usuarios
     * @param origen usuario desde el cual calcular distancias
     * @return mapa con distancias mínimas a cada usuario
     */
    public Map<Usuario, Integer> calcularDistancias(Grafo grafo, Usuario origen) {
        operaciones = 0;
        long inicio = System.nanoTime();
        
        // Inicializar estructuras de datos
        Map<Usuario, Integer> distancias = new HashMap<>();
        Set<Usuario> visitados = new HashSet<>();
        PriorityQueue<NodoDijkstra> cola = new PriorityQueue<>();
        
        // Inicializar todas las distancias como infinito
        for (Usuario u : grafo.getUsuarios()) {
            distancias.put(u, Integer.MAX_VALUE);
            operaciones++;
        }
        
        // La distancia al origen es 0
        distancias.put(origen, 0);
        cola.offer(new NodoDijkstra(origen, 0));
        
        // ALGORITMO DE DIJKSTRA
        while (!cola.isEmpty()) {
            operaciones++;
            
            // GREEDY: Seleccionar el nodo no visitado con menor distancia
            NodoDijkstra actual = cola.poll();
            Usuario usuarioActual = actual.usuario;
            
            // Si ya fue visitado, saltar
            if (visitados.contains(usuarioActual)) {
                continue;
            }
            
            // Marcar como visitado
            visitados.add(usuarioActual);
            
            // Relajar todas las aristas adyacentes
            for (Arista arista : grafo.getVecinos(usuarioActual)) {
                operaciones++;
                
                Usuario vecino = arista.getDestino();
                
                // Si ya fue visitado, saltar
                if (visitados.contains(vecino)) {
                    continue;
                }
                
                // Calcular nueva distancia potencial
                int distanciaActual = distancias.get(usuarioActual);
                int nuevaDistancia = distanciaActual + arista.getPeso();
                
                // Si encontramos un camino más corto, actualizar
                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    cola.offer(new NodoDijkstra(vecino, nuevaDistancia));
                }
            }
        }
        
        tiempoEjecucion = System.nanoTime() - inicio;
        return distancias;
    }
    
    /**
     * Recomienda los N amigos potenciales más cercanos a un usuario.
     * 
     * Criterio: usuarios que NO son amigos directos pero están cerca en la red
     * (distancia pequeña a través de amigos mutuos).
     * 
     * @param grafo red social
     * @param usuario usuario para quien recomendar
     * @param n número de recomendaciones
     * @return lista de recomendaciones ordenadas por cercanía
     */
    public List<RecomendacionAmigo> recomendar(Grafo grafo, Usuario usuario, int n) {
        // Calcular distancias con Dijkstra
        Map<Usuario, Integer> distancias = calcularDistancias(grafo, usuario);
        
        // Obtener amigos directos para excluirlos de las recomendaciones
        Set<Usuario> amigosDirectos = new HashSet<>();
        for (Arista arista : grafo.getVecinos(usuario)) {
            amigosDirectos.add(arista.getDestino());
        }
        
        // Crear lista de candidatos (no son amigos directos, ni el mismo usuario)
        List<RecomendacionAmigo> candidatos = new ArrayList<>();
        for (Map.Entry<Usuario, Integer> entry : distancias.entrySet()) {
            Usuario candidato = entry.getKey();
            int distancia = entry.getValue();
            
            // Excluir: el mismo usuario, amigos directos, y usuarios inalcanzables
            if (!candidato.equals(usuario) && 
                !amigosDirectos.contains(candidato) &&
                distancia != Integer.MAX_VALUE) {
                
                // Reconstruir ruta
                List<Usuario> ruta = reconstruirRuta(grafo, usuario, candidato, distancias);
                
                RecomendacionAmigo rec = new RecomendacionAmigo(candidato, distancia, ruta);
                candidatos.add(rec);
            }
        }
        
        // Ordenar por distancia (menor primero)
        candidatos.sort(Comparator.comparingInt(RecomendacionAmigo::getDistancia));
        
        // Retornar top N
        return candidatos.subList(0, Math.min(n, candidatos.size()));
    }
    
    /**
     * Reconstruye la ruta más corta entre origen y destino.
     * Usa búsqueda hacia atrás desde el destino.
     * 
     * @param grafo red social
     * @param origen usuario inicial
     * @param destino usuario final
     * @param distancias mapa de distancias calculadas por Dijkstra
     * @return lista de usuarios en la ruta (desde origen a destino)
     */
    private List<Usuario> reconstruirRuta(Grafo grafo, Usuario origen, 
                                          Usuario destino, Map<Usuario, Integer> distancias) {
        List<Usuario> ruta = new ArrayList<>();
        Usuario actual = destino;
        
        // Construir ruta hacia atrás
        while (!actual.equals(origen)) {
            ruta.add(actual);
            
            // Encontrar el predecesor (vecino con distancia = actual - peso)
            Usuario predecesor = null;
            int distanciaActual = distancias.get(actual);
            
            for (Arista arista : grafo.getVecinos(actual)) {
                Usuario vecino = arista.getDestino();
                int distanciaVecino = distancias.get(vecino);
                
                if (distanciaVecino + arista.getPeso() == distanciaActual) {
                    predecesor = vecino;
                    break;
                }
            }
            
            if (predecesor == null) {
                // No debería pasar si Dijkstra se ejecutó correctamente
                break;
            }
            
            actual = predecesor;
        }
        
        ruta.add(origen);
        Collections.reverse(ruta);
        return ruta;
    }
    
    /**
     * Encuentra el usuario más cercano a un usuario dado.
     * 
     * @param grafo red social
     * @param usuario usuario de referencia
     * @return recomendación del usuario más cercano
     */
    public RecomendacionAmigo encontrarMasCercano(Grafo grafo, Usuario usuario) {
        List<RecomendacionAmigo> recomendaciones = recomendar(grafo, usuario, 1);
        return recomendaciones.isEmpty() ? null : recomendaciones.get(0);
    }
    
    /**
     * Analiza la conectividad del usuario en la red.
     * 
     * @param grafo red social
     * @param usuario usuario a analizar
     * @return estadísticas de conectividad
     */
    public EstadisticasConectividad analizarConectividad(Grafo grafo, Usuario usuario) {
        Map<Usuario, Integer> distancias = calcularDistancias(grafo, usuario);
        
        int amigosDirectos = grafo.getGrado(usuario);
        int usuariosAlcanzables = 0;
        int distanciaTotal = 0;
        int distanciaMaxima = 0;
        
        for (Map.Entry<Usuario, Integer> entry : distancias.entrySet()) {
            if (entry.getKey().equals(usuario)) continue;
            
            int dist = entry.getValue();
            if (dist != Integer.MAX_VALUE) {
                usuariosAlcanzables++;
                distanciaTotal += dist;
                distanciaMaxima = Math.max(distanciaMaxima, dist);
            }
        }
        
        double distanciaPromedio = usuariosAlcanzables > 0 ? 
                (double) distanciaTotal / usuariosAlcanzables : 0;
        
        return new EstadisticasConectividad(
                amigosDirectos,
                usuariosAlcanzables,
                distanciaPromedio,
                distanciaMaxima
        );
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
     * Calcula la complejidad teórica para un grafo dado.
     * 
     * @param V número de vértices
     * @param E número de aristas
     * @return complejidad teórica (V + E) log V
     */
    public static double complejidadTeorica(int V, int E) {
        if (V <= 0) return 0;
        return (V + E) * (Math.log(V) / Math.log(2));
    }
}
