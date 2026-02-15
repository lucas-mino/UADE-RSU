package algoritmos.greedy;

/**
 * Estadísticas de conectividad de un usuario en la red
 */
public class EstadisticasConectividad {
    private int amigosDirectos;
    private int usuariosAlcanzables;
    private double distanciaPromedio;
    private int distanciaMaxima;

    public EstadisticasConectividad(int amigosDirectos, int usuariosAlcanzables,
                                   double distanciaPromedio, int distanciaMaxima) {
        this.amigosDirectos = amigosDirectos;
        this.usuariosAlcanzables = usuariosAlcanzables;
        this.distanciaPromedio = distanciaPromedio;
        this.distanciaMaxima = distanciaMaxima;
    }

    public int getAmigosDirectos() {
        return amigosDirectos;
    }

    public int getUsuariosAlcanzables() {
        return usuariosAlcanzables;
    }

    public double getDistanciaPromedio() {
        return distanciaPromedio;
    }

    public int getDistanciaMaxima() {
        return distanciaMaxima;
    }

    @Override
    public String toString() {
        return String.format(
                "Conectividad:\n" +
                "  - Amigos directos: %d\n" +
                "  - Usuarios alcanzables: %d\n" +
                "  - Distancia promedio: %.2f\n" +
                "  - Distancia máxima: %d",
                amigosDirectos, usuariosAlcanzables, distanciaPromedio, distanciaMaxima
        );
    }
}
