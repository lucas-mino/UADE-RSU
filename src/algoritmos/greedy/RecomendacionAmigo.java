package algoritmos.greedy;

import java.util.List;
import modelo.Usuario;

/**
 * Clase que representa una recomendación de amistad
 */
public class RecomendacionAmigo {
    private Usuario usuario;
    private int distancia;
    private List<Usuario> ruta;

    public RecomendacionAmigo(Usuario usuario, int distancia, List<Usuario> ruta) {
        this.usuario = usuario;
        this.distancia = distancia;
        this.ruta = ruta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getDistancia() {
        return distancia;
    }

    public List<Usuario> getRuta() {
        return ruta;
    }

    public String getRutaTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ruta.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(ruta.get(i).getNombre());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%s (distancia: %d) - Ruta: %s",
                usuario.getNombre(), distancia, getRutaTexto());
    }
}
