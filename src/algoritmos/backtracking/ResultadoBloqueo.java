package algoritmos.backtracking;

import java.util.List;
import modelo.Usuario;

/**
 * Resultado de la simulación de bloqueo
 */
public class ResultadoBloqueo {
    private Usuario bloqueador;
    private Usuario bloqueado;
    private boolean sigueConexo;
    private int conexionesNecesarias;
    private List<ParUsuarios> conexionesMinimas;
    private String mensaje;

    public ResultadoBloqueo(Usuario bloqueador, Usuario bloqueado, boolean sigueConexo,
                           int conexionesNecesarias, List<ParUsuarios> conexionesMinimas,
                           String mensaje) {
        this.bloqueador = bloqueador;
        this.bloqueado = bloqueado;
        this.sigueConexo = sigueConexo;
        this.conexionesNecesarias = conexionesNecesarias;
        this.conexionesMinimas = conexionesMinimas;
        this.mensaje = mensaje;
    }

    public Usuario getBloqueador() {
        return bloqueador;
    }

    public Usuario getBloqueado() {
        return bloqueado;
    }

    public boolean isSigueConexo() {
        return sigueConexo;
    }

    public int getConexionesNecesarias() {
        return conexionesNecesarias;
    }

    public List<ParUsuarios> getConexionesMinimas() {
        return conexionesMinimas;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE SIMULACIÓN DE BLOQUEO ===\n");
        sb.append(String.format("Bloqueador: %s\n", bloqueador.getNombre()));
        sb.append(String.format("Bloqueado: %s\n", bloqueado.getNombre()));
        sb.append(String.format("Grafo sigue conexo: %s\n", sigueConexo ? "SÍ" : "NO"));

        if (!sigueConexo && conexionesNecesarias > 0) {
            sb.append(String.format("\nConexiones necesarias: %d\n", conexionesNecesarias));
            sb.append("Conexiones mínimas sugeridas:\n");
            for (int i = 0; i < conexionesMinimas.size(); i++) {
                sb.append(String.format("  %d. %s\n", i + 1, conexionesMinimas.get(i)));
            }
        }

        sb.append(String.format("\n%s\n", mensaje));

        return sb.toString();
    }
}
