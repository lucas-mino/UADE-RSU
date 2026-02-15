import java.util.Scanner;
import test.TestAsignadorPublicidad;
import test.TestGestorPublicaciones;
import test.TestRecomendadorAmigos;
import test.TestSimuladorBloqueos;

/**
 * Clase principal del proyecto Red Social Universitaria
 * 
 * Trabajo Integrador - Programación III - 2026
 *
 * Docente: María Angela León
 *
 * Problemas implementados:
 * 1. Gestión de Publicaciones (Divide y Conquista - Merge Sort) ✓
 * 2. Asignación de Publicidad (Programación Dinámica - Knapsack) ✓
 * 3. Recomendación de Amigos (Greedy - Dijkstra) ✓
 * 4. Simulación de Bloqueos (Backtracking) ✓
 *
 * @author Lucas Miño
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 5) {
            mostrarMenu();
            System.out.print("Seleccione una opción (0-5): ");

            try {
                opcion = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 0:
                    ejecutarTodos();
                    break;
                case 1:
                    ejecutarProblema1();
                    break;
                case 2:
                    ejecutarProblema2();
                    break;
                case 3:
                    ejecutarProblema3();
                    break;
                case 4:
                    ejecutarProblema4();
                    break;
                case 5:
                    System.out.println("\nSaliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }

        scanner.close();
    }
    
    private static void ejecutarProblema1() {
        System.out.println("\n=== Ejecutando Problema 1: Gestión de Publicaciones ===\n");
        TestGestorPublicaciones test = new TestGestorPublicaciones();
        test.ejecutarTodos();
    }
    
    private static void ejecutarProblema2() {
        System.out.println("\n=== Ejecutando Problema 2: Asignación de Publicidad ===\n");
        TestAsignadorPublicidad test = new TestAsignadorPublicidad();
        test.ejecutarTodos();
    }
    
    private static void ejecutarProblema3() {
        System.out.println("\n=== Ejecutando Problema 3: Recomendación de Amigos ===\n");
        TestRecomendadorAmigos test = new TestRecomendadorAmigos();
        test.ejecutarTodos();
    }
    
    private static void ejecutarProblema4() {
        System.out.println("\n=== Ejecutando Problema 4: Simulación de Bloqueos ===\n");
        TestSimuladorBloqueos test = new TestSimuladorBloqueos();
        test.ejecutarTodos();
    }

    private static void ejecutarTodos() {
        ejecutarProblema1();
        System.out.println("\n\n");
        ejecutarProblema2();
        System.out.println("\n\n");
        ejecutarProblema3();
        System.out.println("\n\n");
        ejecutarProblema4();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n----------------------------------------------------------------");
        System.out.println("--        RED SOCIAL UNIVERSITARIA - TRABAJO INTEGRADOR       --");
        System.out.println("--                 Programación III - 2026                    --");
        System.out.println("--                    Alumno: Lucas Miño                      --");
        System.out.println("----------------------------------------------------------------");
        System.out.println();
        System.out.println("  0. Ejecutar todos los problemas");
        System.out.println("  1. Gestión de Publicaciones (Divide y Conquista)");
        System.out.println("  2. Asignación de Publicidad (Prog. Dinámica)");
        System.out.println("  3. Recomendación de Amigos (Greedy - Dijkstra)");
        System.out.println("  4. Simulación de Bloqueos (Backtracking)");
        System.out.println("  5. Salir");
        System.out.println();
    }
}
