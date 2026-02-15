package modelo;

/**
 * Clase que representa un usuario de la red social universitaria
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String perfil; // "estudiante", "profesor", "investigador"
    private int tiempoMaximoAnuncios; // en segundos
    
    /**
     * Constructor completo de Usuario.
     */
    public Usuario(int id, String nombre, String email, String perfil, int tiempoMaximoAnuncios) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.perfil = perfil;
        this.tiempoMaximoAnuncios = tiempoMaximoAnuncios;
    }
    
    /**
     * Constructor simplificado
     */
    public Usuario(int id, String nombre, String perfil) {
        this(id, nombre, nombre.toLowerCase().replace(" ", "") + "@universidad.edu", perfil, 60);
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPerfil() {
        return perfil;
    }
    
    public int getTiempoMaximoAnuncios() {
        return tiempoMaximoAnuncios;
    }
    
    // Setters
    public void setTiempoMaximoAnuncios(int tiempoMaximoAnuncios) {
        this.tiempoMaximoAnuncios = tiempoMaximoAnuncios;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", perfil='" + perfil + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
