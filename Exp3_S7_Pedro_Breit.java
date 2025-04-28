package ejercicios.exp3_s7_pedro_breit;
import java.util.*;

public class Exp3_S7_Pedro_Breit {
    static final String NOMBRE_TEATRO = "Teatro Moro";
    static final int CAPACIDAD_TOTAL = 50;
    static int entradasDisponibles = CAPACIDAD_TOTAL;
    static double totalIngresos = 0;
    static int totalEntradasVendidas = 0;
    List<Venta> ventas = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Exp3_S7_Pedro_Breit teatro = new Exp3_S7_Pedro_Breit();
        teatro.menu();
    }

    public void menu() {
        String opcion;
        do {
            System.out.println("\n--- " + NOMBRE_TEATRO + " ---");
            System.out.println("1. Venta de entradas");
            System.out.println("2. Ver resumen de venta");
            System.out.println("3. Generar boleta");
            System.out.println("4. Calcular ingresos totales");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextLine();
            switch (opcion) {
                case "1" -> venderEntrada();
                case "2" -> mostrarResumen();
                case "3" -> generarBoletas();
                case "4" -> calcularIngresos();
                case "5" -> System.out.println("Gracias por su compra");
                default -> System.out.println("Opción no válida.");
            }
        } while (!opcion.equals("5"));
    }

    public void venderEntrada() {
        if (entradasDisponibles <= 0) {
            System.out.println("No hay entradas disponibles.");
            return;
        }
        sc.nextLine(); // Limpiar buffer
        System.out.print("Ingrese ubicación (VIP, Platea, Balcón): ");
        String ubicacion = sc.nextLine().toUpperCase();
        double precioBase = 0;
        
        // Cambio de Switch clásico
        if (ubicacion.equals("VIP")) {
            precioBase = 10000;
        } else if (ubicacion.equals("PLATEA")) {
            precioBase = 7500;
        } else if (ubicacion.equals("BALCÓN")) {
            precioBase = 5000;
        } else {
            System.out.println("Ubicación no válida.");
            return;
        }
        
        
        System.out.print("Usted es: \n1. Estudiante\n2. Tercera edad\n3. Ninguno");
        double descuento =0;
        boolean error = true;
        while (error){
            System.out.print("\nSeleccione una opción: ");
            String opcion = sc.nextLine();
            switch (opcion){
                case "1":
                    descuento = 0.1;
                    error = false;
                    break;
                case "2":
                    descuento = 0.15;
                    error = false;
                    break;
                case "3":
                    descuento = 0;
                    error = false;
                    break;
                default:
                    System.out.println("Error, vuelva a intentar");
                    error = true;
                    break;
            }
        }
        double precioFinal = precioBase * (1 - descuento);
        
        Venta venta = new Venta(ubicacion, precioBase, descuento, precioFinal);
        ventas.add(venta);
        entradasDisponibles--;
        totalEntradasVendidas++;
        totalIngresos += precioFinal;
        
        System.out.println("Entrada vendida con éxito.");
    }

    public void mostrarResumen() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("\nResumen de ventas:");
        for (Venta venta : ventas) {
            System.out.println(venta);
        }
    }

    public void generarBoletas() {
        int total = 0;
        if (ventas.isEmpty()) {
            System.out.println("No hay boletas para generar.");
            return;
        }
        System.out.println("\n--- Boletas Detalladas ---");
        System.out.println("\n-----------------\n" + NOMBRE_TEATRO + " \n-----------------");
        for (Venta venta : ventas) {
            
            System.out.println("\nUbicación: " + venta.getUbicacion());
            System.out.println("Costo Base: $" + venta.getPrecioBase());
            System.out.println("Descuento: " + (int)(venta.getDescuento() * 100) + "%");
            System.out.println("Costo con descuento: $" + venta.getPrecioFinal());
            total += venta.getPrecioFinal();
        }
        System.out.println("\n Costo final: $" + total);
        System.out.println("\n-----------------\n¡Gracias por su compra en " + NOMBRE_TEATRO + "!\n-----------------\n");
        ventas.clear();
    }

    public void calcularIngresos() {
        System.out.println("\nTotal de ingresos: $" + totalIngresos);
        System.out.println("Total de entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Entradas disponibles: " + entradasDisponibles);
    }

    static class Venta {
        private final String ubicacion;
        private final double precioBase;
        private final double descuento;
        private final double precioFinal;

        public Venta(String ubicacion, double precioBase, double descuento, double precioFinal) {
            this.ubicacion = ubicacion;
            this.precioBase = precioBase;
            this.descuento = descuento;
            this.precioFinal = precioFinal;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public double getPrecioBase() {
            return precioBase;
        }

        public double getDescuento() {
            return descuento;
        }

        public double getPrecioFinal() {
            return precioFinal;
        }

        @Override
        public String toString() {
            return "Ubicación: " + ubicacion + " | Precio Final: $" + precioFinal + " | Descuento: " + (int)(descuento * 100) + "%";
        }
    }
}
