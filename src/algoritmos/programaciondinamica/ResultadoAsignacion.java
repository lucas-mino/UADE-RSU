package algoritmos.programaciondinamica;

import java.util.List;
import modelo.Anuncio;

/**
 * Clase que encapsula el resultado de la asignación de anuncios
 */
public class ResultadoAsignacion {
    private List<Anuncio> anunciosSeleccionados;
    private int alcanceTotal;
    private int costoTotal;

    public ResultadoAsignacion(List<Anuncio> anunciosSeleccionados, int alcanceTotal, int costoTotal) {
        this.anunciosSeleccionados = anunciosSeleccionados;
        this.alcanceTotal = alcanceTotal;
        this.costoTotal = costoTotal;
    }

    public List<Anuncio> getAnunciosSeleccionados() {
        return anunciosSeleccionados;
    }

    public int getAlcanceTotal() {
        return alcanceTotal;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public int getNumeroAnuncios() {
        return anunciosSeleccionados.size();
    }

    public double getEficienciaPromedio() {
        if (costoTotal == 0) return 0;
        return (double) alcanceTotal / costoTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE ASIGNACIÓN ===\n");
        sb.append(String.format("Anuncios seleccionados: %d\n", getNumeroAnuncios()));
        sb.append(String.format("Alcance total: %d\n", alcanceTotal));
        sb.append(String.format("Costo total: %d\n", costoTotal));
        sb.append(String.format("Eficiencia promedio: %.2f alcance/costo\n", getEficienciaPromedio()));

        if (!anunciosSeleccionados.isEmpty()) {
            sb.append("\nAnuncios incluidos:\n");
            for (int i = 0; i < anunciosSeleccionados.size(); i++) {
                Anuncio a = anunciosSeleccionados.get(i);
                sb.append(String.format("  %d. %s (costo: %d, alcance: %d)\n",
                        i + 1, a.getTitulo(), a.getCosto(), a.getAlcancePotencial()));
            }
        }

        return sb.toString();
    }
}
