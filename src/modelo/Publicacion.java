package modelo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Clase que representa una publicación en la red social
 * El score de relevancia se calcula considerando likes, comentarios y antigüedad
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Publicacion {
    private int id;
    private String contenido;
    private Usuario autor;
    private int likes;
    private int comentarios;
    private LocalDateTime fecha;
    
    /**
     * Constructor completo de Publicacion
     */
    public Publicacion(int id, String contenido, Usuario autor, int likes, int comentarios, LocalDateTime fecha) {
        this.id = id;
        this.contenido = contenido;
        this.autor = autor;
        this.likes = likes;
        this.comentarios = comentarios;
        this.fecha = fecha;
    }
    
    /**
     * Constructor simplificado (publicación nueva)
     */
    public Publicacion(int id, String contenido, Usuario autor) {
        this(id, contenido, autor, 0, 0, LocalDateTime.now());
    }
    
    /**
     * Calcula el score de relevancia de la publicación.
     * Formula: (likes * 2 + comentarios) * factorTiempo
     * 
     * El factor tiempo decae exponencialmente:
     * - Publicación reciente (< 1 hora): factor cercano a 1.0
     * - Publicación antigua (> 24 horas): factor cercano a 0
     * 
     * @return score de relevancia (mayor = más relevante)
     */
    public double calcularScore() {
        // Calcular minutos desde la publicación
        long minutosDesdePublicacion = ChronoUnit.MINUTES.between(fecha, LocalDateTime.now());
        
        // Factor de decaimiento temporal (exponencial)
        // Cada hora reduce el factor a la mitad aproximadamente
        double factorTiempo = 1.0 / (1.0 + minutosDesdePublicacion / 60.0);
        
        // Score final: engagement (likes y comentarios) ponderado por tiempo
        // Los likes pesan el doble que los comentarios
        return (likes * 2.0 + comentarios) * factorTiempo;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public Usuario getAutor() {
        return autor;
    }
    
    public int getLikes() {
        return likes;
    }
    
    public int getComentarios() {
        return comentarios;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    // Setters (para simular interacción)
    public void agregarLike() {
        this.likes++;
    }
    
    public void agregarComentario() {
        this.comentarios++;
    }
    
    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public void setComentarios(int comentarios) {
        this.comentarios = comentarios;
    }
    
    @Override
    public String toString() {
        return String.format("Publicacion{id=%d, autor=%s, likes=%d, comentarios=%d, fecha=%s, score=%.2f}",
                id, autor.getNombre(), likes, comentarios, 
                fecha.toString().substring(0, 16), calcularScore());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacion that = (Publicacion) o;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
