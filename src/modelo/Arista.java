package modelo;

/**
 * Representa una arista (conexión) en el grafo de amistades.
 * Cada arista conecta dos usuarios y tiene un peso que representa la distancia
 * 
 * Para el problema de recomendación:
 * - Peso bajo = usuarios muy conectados (muchas interacciones)
 * - Peso alto = usuarios poco conectados (pocas interacciones)
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Arista implements Comparable<Arista> {
    private Usuario destino;
    private int peso;
    
    /**
     * Constructor de Arista
     * 
     * @param destino usuario de destino
     * @param peso distancia/costo de la conexión
     */
    public Arista(Usuario destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
    
    /**
     * Constructor alternativo: crea arista basada en interacciones
     * A más interacciones, menor el peso (más cercanos)
     * 
     * @param destino usuario de destino
     * @param interacciones número de interacciones entre usuarios
     */
    public static Arista desdeInteracciones(Usuario destino, int interacciones) {
        // Peso inversamente proporcional a las interacciones
        // Si hay 0 interacciones, peso = 100
        // Si hay 100+ interacciones, peso = 1
        int peso = Math.max(1, 100 - interacciones);
        return new Arista(destino, peso);
    }
    
    // Getters
    public Usuario getDestino() {
        return destino;
    }
    
    public int getPeso() {
        return peso;
    }
    
    // Setter
    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    /**
     * Compara aristas por peso (para ordenamiento)
     */
    @Override
    public int compareTo(Arista otra) {
        return Integer.compare(this.peso, otra.peso);
    }
    
    @Override
    public String toString() {
        return String.format("-> %s (peso: %d)", destino.getNombre(), peso);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arista arista = (Arista) o;
        return destino.equals(arista.destino);
    }
    
    @Override
    public int hashCode() {
        return destino.hashCode();
    }
}
