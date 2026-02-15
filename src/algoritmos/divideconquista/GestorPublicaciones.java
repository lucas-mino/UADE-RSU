package algoritmos.divideconquista;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelo.Publicacion;

/**
 * Gestor de publicaciones que implementa el paradigma DIVIDE Y CONQUISTA
 * mediante el algoritmo Merge Sort para ordenar publicaciones
 * 
 * Permite dos vistas:
 * 1. Cronológica: ordenadas por fecha (más reciente primero)
 * 2. Por Relevancia: ordenadas por score (más relevante primero)
 * 
 * COMPLEJIDAD TEMPORAL: O(n log n) en todos los casos
 * COMPLEJIDAD ESPACIAL: O(n)
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class GestorPublicaciones {
    
    private long comparaciones = 0; // Para análisis de rendimiento
    
    /**
     * Retorna las publicaciones ordenadas cronológicamente (más reciente primero).
     * 
     * @param publicaciones lista original de publicaciones
     * @return lista ordenada por fecha descendente
     */
    public List<Publicacion> vistaCronologica(List<Publicacion> publicaciones) {
        List<Publicacion> copia = new ArrayList<>(publicaciones);
        comparaciones = 0;
        
        // Comparador: fecha más reciente primero
        Comparator<Publicacion> comparadorFecha = (p1, p2) -> {
            comparaciones++;
            return p2.getFecha().compareTo(p1.getFecha()); // Descendente
        };
        
        mergeSort(copia, 0, copia.size() - 1, comparadorFecha);
        return copia;
    }
    
    /**
     * Retorna las publicaciones ordenadas por relevancia (score más alto primero).
     * 
     * @param publicaciones lista original de publicaciones
     * @return lista ordenada por score descendente
     */
    public List<Publicacion> vistaPorRelevancia(List<Publicacion> publicaciones) {
        List<Publicacion> copia = new ArrayList<>(publicaciones);
        comparaciones = 0;
        
        // Comparador: score más alto primero
        Comparator<Publicacion> comparadorRelevancia = (p1, p2) -> {
            comparaciones++;
            double diff = p2.calcularScore() - p1.calcularScore(); // Descendente
            if (diff > 0) return 1;
            if (diff < 0) return -1;
            return 0;
        };
        
        mergeSort(copia, 0, copia.size() - 1, comparadorRelevancia);
        return copia;
    }
    
    /**
     * Algoritmo Merge Sort - DIVIDE Y CONQUISTA
     * 
     * Divide recursivamente el arreglo en mitades hasta tener subarreglos de 1 elemento,
     * luego los combina (merge) de forma ordenada.
     * 
     * @param lista lista a ordenar
     * @param inicio índice inicial
     * @param fin índice final
     * @param comparador criterio de ordenamiento
     */
    private void mergeSort(List<Publicacion> lista, int inicio, int fin, Comparator<Publicacion> comparador) {
        // Caso base: subarreglo de 1 elemento o vacío
        if (inicio >= fin) {
            return;
        }
        
        // DIVIDE: calcular punto medio
        int medio = inicio + (fin - inicio) / 2;
        
        // CONQUISTA: ordenar recursivamente ambas mitades
        mergeSort(lista, inicio, medio, comparador);      // Mitad izquierda
        mergeSort(lista, medio + 1, fin, comparador);     // Mitad derecha
        
        // COMBINA: fusionar las dos mitades ordenadas
        merge(lista, inicio, medio, fin, comparador);
    }
    
    /**
     * Combina dos subarreglos ordenados en uno solo ordenado.
     * 
     * @param lista lista completa
     * @param inicio índice inicial del primer subarreglo
     * @param medio índice final del primer subarreglo
     * @param fin índice final del segundo subarreglo
     * @param comparador criterio de ordenamiento
     */
    private void merge(List<Publicacion> lista, int inicio, int medio, int fin, Comparator<Publicacion> comparador) {
        // Crear copias temporales de los subarreglos
        List<Publicacion> izquierda = new ArrayList<>();
        List<Publicacion> derecha = new ArrayList<>();
        
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(lista.get(i));
        }
        
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(lista.get(i));
        }
        
        // Índices para recorrer los subarreglos y el arreglo principal
        int i = 0;              // Índice para izquierda
        int j = 0;              // Índice para derecha
        int k = inicio;         // Índice para lista principal
        
        // Combinar elementos en orden
        while (i < izquierda.size() && j < derecha.size()) {
            if (comparador.compare(izquierda.get(i), derecha.get(j)) <= 0) {
                lista.set(k, izquierda.get(i));
                i++;
            } else {
                lista.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        
        // Copiar elementos restantes de izquierda (si hay)
        while (i < izquierda.size()) {
            lista.set(k, izquierda.get(i));
            i++;
            k++;
        }
        
        // Copiar elementos restantes de derecha (si hay)
        while (j < derecha.size()) {
            lista.set(k, derecha.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * Retorna el número de comparaciones realizadas en la última operación.
     * Útil para análisis de complejidad experimental.
     * 
     * @return número de comparaciones
     */
    public long getComparaciones() {
        return comparaciones;
    }
    
    /**
     * Análisis teórico de complejidad para un arreglo de tamaño n.
     * 
     * @param n tamaño del arreglo
     * @return complejidad teórica O(n log n)
     */
    public static double complejidadTeorica(int n) {
        if (n <= 0) return 0;
        return n * (Math.log(n) / Math.log(2));
    }
}
