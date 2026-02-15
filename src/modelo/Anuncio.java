package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un anuncio publicitario en la red social
 * Cada anuncio tiene:
 * - Un costo (presupuesto requerido)
 * - Un alcance potencial (valor/beneficio)
 * - Una duración (tiempo de exposición)
 * - Perfiles objetivo (tipos de usuarios a los que aplica)
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Anuncio {
    private int id;
    private String titulo;
    private String contenido;
    private int costo;              // Costo del anuncio en unidades de presupuesto
    private int alcancePotencial;   // Valor de alcance (impresiones esperadas)
    private int duracion;           // Duración en segundos
    private List<String> perfilesObjetivo; // "estudiante", "profesor", "investigador"
    
    /**
     * Constructor completo de Anuncio
     */
    public Anuncio(int id, String titulo, String contenido, int costo, 
                   int alcancePotencial, int duracion, List<String> perfilesObjetivo) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.costo = costo;
        this.alcancePotencial = alcancePotencial;
        this.duracion = duracion;
        this.perfilesObjetivo = perfilesObjetivo != null ? 
                new ArrayList<>(perfilesObjetivo) : new ArrayList<>();
    }
    
    /**
     * Constructor simplificado
     */
    public Anuncio(int id, String titulo, int costo, int alcancePotencial) {
        this(id, titulo, "", costo, alcancePotencial, 30, new ArrayList<>());
    }
    
    /**
     * Constructor con perfil objetivo
     */
    public Anuncio(int id, String titulo, int costo, int alcancePotencial, String perfilObjetivo) {
        this(id, titulo, "", costo, alcancePotencial, 30, 
             perfilObjetivo != null ? List.of(perfilObjetivo) : new ArrayList<>());
    }
    
    /**
     * Calcula la eficiencia del anuncio (alcance por unidad de costo).
     * Mayor eficiencia = mejor relación costo/beneficio.
     * 
     * @return alcance / costo
     */
    public double calcularEficiencia() {
        if (costo == 0) return Double.MAX_VALUE;
        return (double) alcancePotencial / costo;
    }
    
    /**
     * Verifica si el anuncio aplica para un perfil de usuario dado.
     * 
     * @param perfilUsuario perfil del usuario
     * @return true si el anuncio es relevante para ese perfil
     */
    public boolean aplicaParaPerfil(String perfilUsuario) {
        // Si no hay perfiles específicos, aplica para todos
        if (perfilesObjetivo.isEmpty()) {
            return true;
        }
        return perfilesObjetivo.contains(perfilUsuario);
    }
    
    /**
     * Verifica si el anuncio cabe en el tiempo disponible del usuario.
     * 
     * @param tiempoDisponible tiempo disponible en segundos
     * @return true si la duración del anuncio <= tiempo disponible
     */
    public boolean cabeEnTiempo(int tiempoDisponible) {
        return duracion <= tiempoDisponible;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public int getCosto() {
        return costo;
    }
    
    public int getAlcancePotencial() {
        return alcancePotencial;
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    public List<String> getPerfilesObjetivo() {
        return new ArrayList<>(perfilesObjetivo);
    }
    
    // Setters
    public void setCosto(int costo) {
        this.costo = costo;
    }
    
    public void setAlcancePotencial(int alcancePotencial) {
        this.alcancePotencial = alcancePotencial;
    }
    
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    
    public void agregarPerfilObjetivo(String perfil) {
        if (!perfilesObjetivo.contains(perfil)) {
            perfilesObjetivo.add(perfil);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Anuncio{id=%d, titulo='%s', costo=%d, alcance=%d, eficiencia=%.2f}",
                id, titulo, costo, alcancePotencial, calcularEficiencia());
    }
    
    /**
     * Representación detallada del anuncio.
     */
    public String toStringDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Anuncio #%d: %s\n", id, titulo));
        sb.append(String.format("  Costo: %d\n", costo));
        sb.append(String.format("  Alcance potencial: %d\n", alcancePotencial));
        sb.append(String.format("  Duración: %d segundos\n", duracion));
        sb.append(String.format("  Eficiencia: %.2f alcance/costo\n", calcularEficiencia()));
        if (!perfilesObjetivo.isEmpty()) {
            sb.append(String.format("  Perfiles objetivo: %s\n", String.join(", ", perfilesObjetivo)));
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return id == anuncio.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
