package algoritmos.backtracking;

import java.util.*;
import modelo.Arista;
import modelo.Grafo;
import modelo.Usuario;

/**
 * Simulador de bloqueos en la red social usando BACKTRACKING.
 * 
 * PROBLEMA: Cuando un usuario bloquea a otro, se rompe una conexión en la red
 * El sistema debe:
 * 1. Verificar si el grafo sigue siendo conexo después del bloqueo
 * 2. Si no es conexo, encontrar el conjunto MÍNIMO de nuevas conexiones para
 *    restablecer la conectividad total
 * 
 * ENFOQUE: Backtracking con poda
 * - Explorar exhaustivamente combinaciones de nuevas conexiones
 * - Podar ramas que ya exceden el mínimo conocido
 * - Validar conectividad con DFS
 * 
 * COMPLEJIDAD:
 * - Verificación de conectividad: O(V + E) usando DFS
 * - Búsqueda de conexiones mínimas: O(2^C) donde C = conexiones candidatas
 * - Con poda efectiva: mucho mejor en la práctica
 * 
 * PARADIGMA: Backtracking
 * - Construcción incremental de solución
 * - Exploración del árbol de decisiones
 * - Poda de ramas no prometedoras
 * - Backtrack cuando se detecta que no hay solución
 *
 * @author Lucas Miño
 * * @version 1.0
 */
public class SimuladorBloqueos {
    
    // Para análisis de rendimiento
    private long operaciones = 0;
    private long tiempoEjecucion = 0;
    private int nodosExplorados = 0;
    private int nodosRecortados = 0;
    
    /**
     * Simula el bloqueo de un usuario a otro y verifica la conectividad.
     * 
     * @param grafo red social original
     * @param bloqueador usuario que bloquea
     * @param bloqueado usuario bloqueado
     * @return resultado con información de conectividad
     */
    public ResultadoBloqueo simularBloqueo(Grafo grafo, Usuario bloqueador, Usuario bloqueado) {
        operaciones = 0;
        nodosExplorados = 0;
        nodosRecortados = 0;
        long inicio = System.nanoTime();
        
        // Crear copia del grafo para no modificar el original
        Grafo grafoCopia = grafo.copiar();
        
        // Verificar que la conexión existe
        if (!grafoCopia.sonAmigos(bloqueador, bloqueado)) {
            tiempoEjecucion = System.nanoTime() - inicio;
            return new ResultadoBloqueo(
                bloqueador, bloqueado, true, 0, 
                new ArrayList<>(), "No existe conexión para bloquear"
            );
        }
        
        // Eliminar la conexión (bloqueo)
        grafoCopia.eliminarAmistad(bloqueador, bloqueado);
        operaciones++;
        
        // Verificar si el grafo sigue siendo conexo
        boolean esConexo = verificarConectividad(grafoCopia);
        
        if (esConexo) {
            // El grafo sigue conexo, no se necesitan nuevas conexiones
            tiempoEjecucion = System.nanoTime() - inicio;
            return new ResultadoBloqueo(
                bloqueador, bloqueado, true, 0,
                new ArrayList<>(), "El grafo sigue conexo después del bloqueo"
            );
        }
        
        // El grafo no es conexo, encontrar conexiones mínimas
        List<ParUsuarios> conexionesMinimas = encontrarConexionesMinimas(grafoCopia);
        
        tiempoEjecucion = System.nanoTime() - inicio;
        
        return new ResultadoBloqueo(
            bloqueador, bloqueado, false, conexionesMinimas.size(),
            conexionesMinimas, "Se requieren " + conexionesMinimas.size() + " conexiones nuevas"
        );
    }
    
    /**
     * Verifica si el grafo es conexo usando DFS (Depth-First Search).
     * Un grafo es conexo si existe un camino entre cualquier par de nodos.
     * 
     * @param grafo grafo a verificar
     * @return true si el grafo es conexo
     */
    public boolean verificarConectividad(Grafo grafo) {
        if (grafo.estaVacio()) {
            return true;
        }
        
        Set<Usuario> usuarios = grafo.getUsuarios();
        if (usuarios.isEmpty()) {
            return true;
        }
        
        // Iniciar DFS desde el primer usuario
        Usuario inicio = usuarios.iterator().next();
        Set<Usuario> visitados = new HashSet<>();
        
        dfs(grafo, inicio, visitados);
        operaciones += visitados.size();
        
        // Si visitamos todos los usuarios, el grafo es conexo
        return visitados.size() == usuarios.size();
    }
    
    /**
     * DFS recursivo para marcar todos los nodos alcanzables.
     * 
     * @param grafo grafo a explorar
     * @param actual nodo actual
     * @param visitados conjunto de nodos ya visitados
     */
    private void dfs(Grafo grafo, Usuario actual, Set<Usuario> visitados) {
        visitados.add(actual);
        
        for (Arista arista : grafo.getVecinos(actual)) {
            Usuario vecino = arista.getDestino();
            if (!visitados.contains(vecino)) {
                dfs(grafo, vecino, visitados);
            }
        }
    }
    
    /**
     * Identifica las componentes conexas del grafo.
     * Una componente conexa es un subgrafo maximal donde todos los nodos están conectados.
     * 
     * @param grafo grafo desconectado
     * @return lista de componentes conexas
     */
    public List<Set<Usuario>> identificarComponentes(Grafo grafo) {
        List<Set<Usuario>> componentes = new ArrayList<>();
        Set<Usuario> visitados = new HashSet<>();
        
        for (Usuario usuario : grafo.getUsuarios()) {
            if (!visitados.contains(usuario)) {
                Set<Usuario> componente = new HashSet<>();
                dfs(grafo, usuario, componente);
                componentes.add(componente);
                visitados.addAll(componente);
            }
        }
        
        return componentes;
    }
    
    /**
     * Encuentra el conjunto MÍNIMO de conexiones necesarias para hacer el grafo conexo.
     * Usa BACKTRACKING con poda para explorar combinaciones de conexiones.
     * 
     * @param grafo grafo desconectado
     * @return lista de pares de usuarios que deben conectarse
     */
    public List<ParUsuarios> encontrarConexionesMinimas(Grafo grafo) {
        // Identificar componentes desconectadas
        List<Set<Usuario>> componentes = identificarComponentes(grafo);
        
        if (componentes.size() <= 1) {
            // Ya es conexo
            return new ArrayList<>();
        }
        
        // Generar todas las conexiones candidatas (entre componentes diferentes)
        List<ParUsuarios> candidatas = generarConexionesCandidatas(componentes);
        
        // Búsqueda con backtracking
        List<ParUsuarios> mejorSolucion = new ArrayList<>();
        List<ParUsuarios> solucionActual = new ArrayList<>();
        
        // El mínimo teórico es (número de componentes - 1)
        int minimoTeorico = componentes.size() - 1;
        
        backtrack(grafo, candidatas, 0, solucionActual, mejorSolucion, minimoTeorico);
        
        return mejorSolucion;
    }
    
    /**
     * Algoritmo de BACKTRACKING recursivo.
     * Explora el árbol de decisiones de forma exhaustiva con poda.
     * 
     * En cada nivel del árbol:
     * - INCLUIR la conexión candidata
     * - NO INCLUIR la conexión candidata
     * 
     * PODA:
     * - Si la solución actual ya es >= mejor conocida, podar
     * - Si encontramos solución óptima (tamaño = mínimo teórico), podar resto
     * 
     * @param grafo grafo original
     * @param candidatas lista de conexiones candidatas
     * @param index índice actual en la lista de candidatas
     * @param actual solución en construcción
     * @param mejor mejor solución encontrada hasta ahora
     * @param minimoTeorico cota inferior teórica
     */
    private void backtrack(Grafo grafo, List<ParUsuarios> candidatas, int index,
                          List<ParUsuarios> actual, List<ParUsuarios> mejor, int minimoTeorico) {
        
        nodosExplorados++;
        
        // CASO BASE: Verificar si la solución actual hace el grafo conexo
        if (!actual.isEmpty()) {
            Grafo grafoPrueba = grafo.copiar();
            for (ParUsuarios par : actual) {
                grafoPrueba.agregarAmistad(par.getUsuario1(), par.getUsuario2(), 1);
            }
            
            if (verificarConectividad(grafoPrueba)) {
                // Encontramos una solución válida
                if (mejor.isEmpty() || actual.size() < mejor.size()) {
                    mejor.clear();
                    mejor.addAll(actual);
                    
                    // PODA ÓPTIMA: Si alcanzamos el mínimo teórico, no hay mejor solución
                    if (mejor.size() == minimoTeorico) {
                        return;
                    }
                }
                return; // No seguir explorando esta rama
            }
        }
        
        // CASO BASE: Exploradas todas las candidatas
        if (index >= candidatas.size()) {
            return;
        }
        
        // PODA: Si la solución actual ya es >= mejor conocida, no seguir
        if (!mejor.isEmpty() && actual.size() >= mejor.size()) {
            nodosRecortados++;
            return;
        }
        
        // DECISIÓN 1: INCLUIR la conexión actual
        actual.add(candidatas.get(index));
        backtrack(grafo, candidatas, index + 1, actual, mejor, minimoTeorico);
        actual.remove(actual.size() - 1); // BACKTRACK
        
        // DECISIÓN 2: NO INCLUIR la conexión actual
        backtrack(grafo, candidatas, index + 1, actual, mejor, minimoTeorico);
    }
    
    /**
     * Genera todas las posibles conexiones entre componentes diferentes.
     * Solo consideramos conexiones que unan componentes distintas.
     * 
     * @param componentes lista de componentes conexas
     * @return lista de pares de usuarios candidatos a conectar
     */
    private List<ParUsuarios> generarConexionesCandidatas(List<Set<Usuario>> componentes) {
        List<ParUsuarios> candidatas = new ArrayList<>();
        
        // Para cada par de componentes diferentes
        for (int i = 0; i < componentes.size(); i++) {
            for (int j = i + 1; j < componentes.size(); j++) {
                Set<Usuario> comp1 = componentes.get(i);
                Set<Usuario> comp2 = componentes.get(j);
                
                // Tomar un representante de cada componente
                Usuario u1 = comp1.iterator().next();
                Usuario u2 = comp2.iterator().next();
                
                candidatas.add(new ParUsuarios(u1, u2));
            }
        }
        
        return candidatas;
    }
    
    /**
     * Variante optimizada: encuentra una solución (no necesariamente óptima) rápidamente.
     * Usa heurística greedy: conectar las componentes más pequeñas primero.
     * 
     * @param grafo grafo desconectado
     * @return lista de conexiones (solución aproximada)
     */
    public List<ParUsuarios> encontrarConexionesRapido(Grafo grafo) {
        List<Set<Usuario>> componentes = identificarComponentes(grafo);
        List<ParUsuarios> conexiones = new ArrayList<>();
        
        if (componentes.size() <= 1) {
            return conexiones;
        }
        
        // Ordenar componentes por tamaño (más pequeñas primero)
        componentes.sort(Comparator.comparingInt(Set::size));
        
        // Conectar cada componente con la primera (estrategia estrella)
        Set<Usuario> principal = componentes.get(0);
        Usuario hub = principal.iterator().next();
        
        for (int i = 1; i < componentes.size(); i++) {
            Usuario representante = componentes.get(i).iterator().next();
            conexiones.add(new ParUsuarios(hub, representante));
        }
        
        return conexiones;
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
    
    public int getNodosExplorados() {
        return nodosExplorados;
    }
    
    public int getNodosRecortados() {
        return nodosRecortados;
    }
    
    public double getEfectividadPoda() {
        if (nodosExplorados == 0) return 0;
        return 100.0 * nodosRecortados / (nodosExplorados + nodosRecortados);
    }
}
