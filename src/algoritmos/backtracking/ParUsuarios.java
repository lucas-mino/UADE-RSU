package algoritmos.backtracking;

import modelo.Usuario;

/**
 * Representa un par de usuarios para una posible conexión
 */
public class ParUsuarios {
    private Usuario usuario1;
    private Usuario usuario2;

    public ParUsuarios(Usuario usuario1, Usuario usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    @Override
    public String toString() {
        return String.format("%s <-> %s", usuario1.getNombre(), usuario2.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParUsuarios that = (ParUsuarios) o;
        return (usuario1.equals(that.usuario1) && usuario2.equals(that.usuario2)) ||
               (usuario1.equals(that.usuario2) && usuario2.equals(that.usuario1));
    }

    @Override
    public int hashCode() {
        return usuario1.hashCode() + usuario2.hashCode();
    }
}
