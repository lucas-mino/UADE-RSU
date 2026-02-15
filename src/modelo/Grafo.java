package modelo;

import java.util.*;

/**
 * Representa un grafo no dirigido de usuarios conectados por relaciones de amistad.
 * Implementado usando lista de adyacencias para eficiencia en grafos dispersos
 * 
 * Soporta:
 * - Agregar usuarios (vértices)
 * - Agregar amistades (aristas)
 * - Consultar vecinos
 * - Verificar existencia de conexiones
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Grafo {
    // Lista de adyacencias: Usuario -> Lista de Aristas
    private Map<Usuario, List<Arista>> adyacencias;
    
    // Conjunto de todos los usuarios en el grafo
    private Set<Usuario> usuarios;
    
    // Contador de aristas
    private int numeroAristas;
    
    /**
     * Constructor: crea un grafo vacío
     */
    public Grafo() {
        this.adyacencias = new HashMap<>();
        this.usuarios = new HashSet<>();
        this.numeroAristas = 0;
    }
    
    /**
     * Agrega un usuario al grafo.
     * Si el usuario ya existe, no hace nada.
     * 
     * @param usuario usuario a agregar
     */
    public void agregarUsuario(Usuario usuario) {
        if (!usuarios.contains(usuario)) {
            usuarios.add(usuario);
            adyacencias.put(usuario, new ArrayList<>());
        }
    }
    
    /**
     * Agrega una amistad (arista no dirigida) entre dos usuarios.
     * Si los usuarios no existen, los agrega automáticamente.
     * 
     * @param u1 primer usuario
     * @param u2 segundo usuario
     * @param peso distancia/costo de la conexión
     */
    public void agregarAmistad(Usuario u1, Usuario u2, int peso) {
        // Asegurar que ambos usuarios existan
        agregarUsuario(u1);
        agregarUsuario(u2);
        
        // Crear aristas en ambas direcciones (grafo no dirigido)
        Arista arista1 = new Arista(u2, peso);
        Arista arista2 = new Arista(u1, peso);
        
        // Verificar que no exista ya la arista
        if (!adyacencias.get(u1).contains(arista1)) {
            adyacencias.get(u1).add(arista1);
            adyacencias.get(u2).add(arista2);
            numeroAristas++;
        }
    }
    
    /**
     * Agrega una amistad basada en número de interacciones.
     * A más interacciones, menor distancia.
     * 
     * @param u1 primer usuario
     * @param u2 segundo usuario
     * @param interacciones número de interacciones entre ellos
     */
    public void agregarAmistadPorInteracciones(Usuario u1, Usuario u2, int interacciones) {
        int peso = Math.max(1, 100 - interacciones);
        agregarAmistad(u1, u2, peso);
    }
    
    /**
     * Obtiene los vecinos (amigos directos) de un usuario.
     * 
     * @param usuario usuario a consultar
     * @return lista de aristas a sus vecinos
     */
    public List<Arista> getVecinos(Usuario usuario) {
        return adyacencias.getOrDefault(usuario, new ArrayList<>());
    }
    
    /**
     * Verifica si dos usuarios son amigos directos.
     * 
     * @param u1 primer usuario
     * @param u2 segundo usuario
     * @return true si son amigos directos
     */
    public boolean sonAmigos(Usuario u1, Usuario u2) {
        if (!usuarios.contains(u1) || !usuarios.contains(u2)) {
            return false;
        }
        
        List<Arista> vecinos = adyacencias.get(u1);
        for (Arista arista : vecinos) {
            if (arista.getDestino().equals(u2)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene el peso de la arista entre dos usuarios.
     * 
     * @param u1 primer usuario
     * @param u2 segundo usuario
     * @return peso de la arista, o -1 si no existe
     */
    public int getPesoArista(Usuario u1, Usuario u2) {
        if (!sonAmigos(u1, u2)) {
            return -1;
        }
        
        for (Arista arista : adyacencias.get(u1)) {
            if (arista.getDestino().equals(u2)) {
                return arista.getPeso();
            }
        }
        return -1;
    }
    
    /**
     * Elimina la amistad entre dos usuarios.
     * Útil para el problema de simulación de bloqueos.
     * 
     * @param u1 primer usuario
     * @param u2 segundo usuario
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminarAmistad(Usuario u1, Usuario u2) {
        if (!sonAmigos(u1, u2)) {
            return false;
        }
        
        // Remover de ambas listas de adyacencia
        adyacencias.get(u1).removeIf(a -> a.getDestino().equals(u2));
        adyacencias.get(u2).removeIf(a -> a.getDestino().equals(u1));
        numeroAristas--;
        return true;
    }
    
    /**
     * Obtiene todos los usuarios del grafo.
     * 
     * @return conjunto de usuarios
     */
    public Set<Usuario> getUsuarios() {
        return new HashSet<>(usuarios);
    }
    
    /**
     * Retorna el número de usuarios (vértices).
     * 
     * @return cantidad de usuarios
     */
    public int getNumeroVertices() {
        return usuarios.size();
    }
    
    /**
     * Retorna el número de amistades (aristas).
     * 
     * @return cantidad de aristas
     */
    public int getNumeroAristas() {
        return numeroAristas;
    }
    
    /**
     * Verifica si el grafo está vacío.
     * 
     * @return true si no hay usuarios
     */
    public boolean estaVacio() {
        return usuarios.isEmpty();
    }
    
    /**
     * Obtiene el grado (número de amigos) de un usuario.
     * 
     * @param usuario usuario a consultar
     * @return número de amigos directos
     */
    public int getGrado(Usuario usuario) {
        return adyacencias.getOrDefault(usuario, new ArrayList<>()).size();
    }
    
    /**
     * Genera una representación en texto del grafo.
     * Útil para debugging.
     * 
     * @return representación del grafo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo de Red Social:\n");
        sb.append(String.format("  Usuarios: %d\n", getNumeroVertices()));
        sb.append(String.format("  Amistades: %d\n\n", getNumeroAristas()));
        
        for (Usuario usuario : usuarios) {
            sb.append(String.format("%s -> ", usuario.getNombre()));
            List<Arista> vecinos = adyacencias.get(usuario);
            if (vecinos.isEmpty()) {
                sb.append("(sin amigos)");
            } else {
                for (int i = 0; i < vecinos.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(vecinos.get(i).getDestino().getNombre());
                }
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Crea una copia del grafo.
     * Útil para algoritmos que modifican el grafo.
     * 
     * @return copia del grafo
     */
    public Grafo copiar() {
        Grafo copia = new Grafo();
        
        // Copiar todos los usuarios
        for (Usuario u : usuarios) {
            copia.agregarUsuario(u);
        }
        
        // Copiar todas las aristas
        Set<String> aristasAgregadas = new HashSet<>();
        for (Usuario u : usuarios) {
            for (Arista arista : adyacencias.get(u)) {
                String key = u.getId() + "-" + arista.getDestino().getId();
                String keyInversa = arista.getDestino().getId() + "-" + u.getId();
                
                if (!aristasAgregadas.contains(key) && !aristasAgregadas.contains(keyInversa)) {
                    copia.agregarAmistad(u, arista.getDestino(), arista.getPeso());
                    aristasAgregadas.add(key);
                }
            }
        }
        
        return copia;
    }
}
