/** Para este problema se asignan los siguientes valores:
* Revisar: 1. Valor VIP = 10.000 (línea 16)
* Revisar: 2. Valor Platea = 7.500 (línea 17)
* Revisar: 3. Valor General = 5.000 (línea 18)
* Revisar: 4. Descuento estudiante 10% (línea 21)
* Revisar: 5. Descuento tercera edad 15% (línea 22)
**/

package ejercicios.exp2_s6_pedro_breit;
import java.util.ArrayList;
import java.util.Scanner;

public class Exp2_S6_Pedro_Breit {
    static Scanner scan = new Scanner(System.in);
    // Precios
    public static final int P_VIP = 10000;
    public static final int P_PLATEA = 7500;
    public static final int P_GENERAL = 5000;
    
    // Descuentos
    public static final double D_ESTUDIANTE = 0.10;
    public static final double D_TERCERA_EDAD = 0.15;
    
    // Matrices de asientos
    static String[][] aVIP = new String[1][5];
    static String[][] aPlatea = new String[2][5];
    static String[][] aGeneral = new String[3][5];
    
    static double descuentoActual = 0;

    public static void main(String[] args) {
        Carrito carrito = new Carrito();
        MatrizAsientos.inicializarMatriz(aVIP, "A");
        MatrizAsientos.inicializarMatriz(aPlatea, "B");
        MatrizAsientos.inicializarMatriz(aGeneral, "C");
        
        // Ciclo while que permite permanecer en el menú principal
        boolean continuar = true;
        
        while (continuar) {
            
            System.out.println("\n===========================\n      MENÚ PRINCIPAL      \n===========================\n");
            System.out.println("1. Ver asientos disponibles");
            System.out.println("2. Seleccionar asiento");
            System.out.println("3. Ver promociones");
            System.out.println("4. Ver carrito y modificar");
            System.out.println("5. Generar boleta");
            System.out.println("6. Salir\n");
            System.out.print(" Seleccione una opción: ");
            String opcion = scan.nextLine();
            System.out.println();
            
            switch (opcion) {
                
                // Muestra matriz de asientos y disponibilidad
                case "1":
                    System.out.println("\n|--------Escenario--------|\n");
                    MatrizAsientos.imprimirMatriz(aVIP, "VIP----");
                    MatrizAsientos.imprimirMatriz(aPlatea, "Platea-");
                    MatrizAsientos.imprimirMatriz(aGeneral, "General");
                    break;
                    
                // Seleccionar Asiento
                case "2":
                    System.out.print("\nIngrese el asiento (ej: A1): ");
                     String asiento = scan.nextLine().toUpperCase();
                     System.out.println();
                    
                    // Validacion de existencia del asiento o ver si ha sido seleccionado (ej: [A1]) o esta ocupado (ej: XXX)
                    if (MatrizAsientos.asientoDisponible(asiento) && asiento.length() <= 3 && !asiento.equals("XXX")) { //BREAKPOINT
                        MatrizAsientos.reservarAsiento(asiento);
                        carrito.agregarAsiento(asiento);
                        
                        int precio = carrito.precioAsiento(asiento);
                        String seccion = asiento.charAt(0) == 'A' ? "VIP" : (asiento.charAt(0) == 'B' ? "Platea" : "General");
                        
                        System.out.println("---------------------------------\nAsiento reservado correctamente.\nSección " + seccion +  "\nPrecio: $" + precio + "\n---------------------------------");
                    } else {
                        System.out.println("\n---------------------------------\nAsiento no disponible o inválido.\n---------------------------------");
                    }
                    break;
                
                // Promociones
                case "3":
                    
                    // Ciclo de validacion de respuesta (s o n)
                    boolean errorA = true;
                    while (errorA) {
                        
                        // Ingreso de edad para promociones
                        System.out.print("Ingrese su edad: ");
                        
                        
                        int edad = Integer.parseInt(scan.nextLine());
                        if (edad <= 0 || edad > 120) {
                            System.out.println("\n---------------------------------\nEdad inválida, vuelva a intentarlo.\n---------------------------------");
                            errorA = true;
                        } else if (edad <= 18) {
                            descuentoActual = D_ESTUDIANTE;
                            System.out.println("\n---------------------------------\nDescuento aplicado correctamente: \n10% por ser estudiante.\n---------------------------------");
                            errorA = false;
                        } else if (edad >= 60) {
                            descuentoActual = D_TERCERA_EDAD;
                            System.out.println("\n---------------------------------\nDescuento aplicado correctamente: \n15% por tercera edad.\n---------------------------------");
                            errorA = false;
                        } else {
                            descuentoActual = 0;
                            System.out.println("\n---------------------------\nNo aplica descuento.\n---------------------------");
                            errorA = false;
                        }
                    }
                    break;
                    
                // Editar Carrito
                case "4":
                    
                    // Carrito vacio
                    if (carrito.estaVacio()) {
                        System.out.println("\n---------------------------\nEl carrito está vacío.\n---------------------------");
                    } else {
                        System.out.println("---------------------------------");
                        carrito.mostrarCarrito();
                        System.out.println("---------------------------------");
                        
                        // Ciclo de validacion de respuesta (s o n)
                        boolean errorE = true;
                        while (errorE) {
                            System.out.print("\n¿Desea eliminar un asiento? (s/n): ");
                            String eliminar = scan.nextLine().toLowerCase();
                            
                            // Eliminar asiento
                            if (eliminar.equals("s")) {
                                System.out.print("\nIngrese asiento a eliminar: ");
                                String asientoEliminar = scan.nextLine().toUpperCase();
                                
                                // Verificar asiento del carrito
                                if (carrito.eliminarAsiento(asientoEliminar)) { 
                                    MatrizAsientos.liberarAsiento(asientoEliminar); //BREAKPOINT
                                    System.out.println("\n--------------------------------\nAsiento eliminado correctamente.\n--------------------------------");
                                } else {
                                    System.out.println("\n------------------------------------\nAsiento no encontrado en el carrito.\n------------------------------------");
                                }
                                errorE = false;
                                
                            // No eliminar asiento del carrito    
                            } else if (eliminar.equals("n")) {
                                errorE = false;
                            } else {
                                System.out.println("\n---------------------------\nError, vuelva a intentar.\n---------------------------");
                                errorE = true;
                            }
                        }
                    }
                    break;
                    
                // Finalizar compra
                case "5":
                    Carrito.generarBoleta(carrito); //BREAKPOINT
                    break;
                    
                // Salir de la app y mensaje de despedida    
                case "6":
                    
                    continuar = false;
                    System.out.println("\n====================================================================\nGracias por visitar la app de compras de Teatro Moro.¡Hasta pronto!\n====================================================================");
                    break;
                    
                default:
                    System.out.println("\n---------------------------\nError, vuelva a intentar.\n---------------------------");
            }
        }
    }

    static class MatrizAsientos {
        
        // Creación matriz de asientos
        public static void inicializarMatriz(String[][] matriz, String letra) {
            int contador = 1;
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    matriz[i][j] = letra + contador++;
                }
            }
        }
        
        // Imprime la matriz de asientos
        public static void imprimirMatriz(String[][] matriz, String nombre) {
            System.out.println("-------SECCIÓN " + nombre.toUpperCase() + "----");
            for (String[] fila : matriz) {
                for (String asiento : fila) {
                    System.out.printf("%-6s", asiento);
                }
                System.out.println();
            }
        }
        
        // Busca en la matris el asiento seleccionado si esta devuelve true
        public static boolean asientoDisponible(String asiento) {
            return buscarYModificar(asiento, null);
        }
        
        // Busca en la matriz el asiento seleccionado, si esta lo reemplaza por [asiento]
        public static void reservarAsiento(String asiento) {
            buscarYModificar(asiento, "[" + asiento + "]");
        }
        
        // Busca el [asiento] y lo reemplaza por XXX, se aplica al confirmar la compra
        public static void confirmarAsiento(String asiento) {
            buscarYModificar("[" + asiento + "]", "XXX");
        }
        
        // Busca el [asiento] y lo vuelve a su valor inicial, se aplica en eliminar del carrito
        public static void liberarAsiento(String asiento) {
            buscarYModificar("[" + asiento + "]", asiento);
        }
        
        // Boolean de busqueda y modificacion de asientos
        private static boolean buscarYModificar(String objetivo, String nuevoValor) {
            String[][][] matrices = {aVIP, aPlatea, aGeneral};
            
            // Recorre las matrices
            for (String[][] matriz : matrices) {
                
                // Recorre fila de matrices
                for (int i = 0; i < matriz.length; i++) {
                    
                    // Recorre columna de matrices
                    for (int j = 0; j < matriz[i].length; j++) {
                        
                        // Devuelve true si encuentra el valor objetivo
                        if (matriz[i][j].equals(objetivo)) {
                            
                            // Si el valor no es null cambia el valor de matriz[i][j]
                            if (nuevoValor != null) matriz[i][j] = nuevoValor;
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    static class Carrito {
        
        // Lista de asientos en el carrito
        ArrayList<String> asientos = new ArrayList<>();
        
        // Agrega asientos al carrito
        public void agregarAsiento(String asiento) {
            asientos.add(asiento);
        }
        
        // Muestra el carrito con el valor de cada asiento
        public void mostrarCarrito() {
            
            int precio = 0;
            System.out.println("Asientos seleccionados:\n");
            
            // Recorre los asientos en la lista y obtiene la primera letra para asignar el precio
            for (String asiento : asientos) {
                switch (asiento.charAt(0)) {
                    case 'A': precio = P_VIP; break;
                    case 'B': precio = P_PLATEA; break;
                    case 'C': precio = P_GENERAL; break;
                }
                System.out.println("+ " + asiento + " .................. $" + precio);
            }
        }
        
        // Elimina los asients de la lista
        public boolean eliminarAsiento(String asiento) {
            return asientos.remove(asiento);
        }
        
        // Calcula el valor total de los asientos en la lista
        public int calcularTotal() {
            int total = 0;
            for (String asiento : asientos) {
                total += precioAsiento(asiento);
            }
            return total;
        }
        
        // Vacia el carrito
        public void vaciarCarrito() {
            asientos.clear();
        }
        
        // Boolean para confirmar que el carrito esta vacio
        public boolean estaVacio() {
            return asientos.isEmpty();
        }
        
        public static int precioAsiento(String asiento) {
            
            switch (asiento.charAt(0)){
                case 'A': return P_VIP;
                case 'B': return P_PLATEA;
                case 'C': return P_GENERAL;
                default: return 0;
            }  
        }
        
        public static void generarBoleta(Carrito carrito){
            
            // Carrito vacio
            if (carrito.estaVacio()) {
                System.out.println("---------------------------\nEl carrito está vacío.\n---------------------------");
                return;
                        
            // Muestra asientos seleccionados con su respectivo precio, total sin descuento, descuento y total con descuento    
            } else {
                System.out.println("================================\n             BOLETA           \n================================");
                carrito.mostrarCarrito();
                        
                int total = carrito.calcularTotal(); //BREAKPOINT
                double totalFinal = total - (total * descuentoActual);
                        
                System.out.println("\nTotal sin descuento: .. $" + total);
                System.out.println("\nDescuento aplicado: ... " + (descuentoActual * 100) + "%");
                System.out.println("\nTotal a pagar: ........ $" + totalFinal);
                System.out.println("================================");        
                // Validacion de respuesta (s o n
                boolean errorC = true;
                while (errorC) {
                    System.out.print("\n¿Desea confirmar la compra? (s/n): ");
                    String confirmar = scan.nextLine().toLowerCase();
                    if (confirmar.equals("s")) {
                                  
                        for (String a : carrito.asientos) {
                            MatrizAsientos.confirmarAsiento(a); //BREAKPOINT
                        }
                        carrito.vaciarCarrito();
                        System.out.println("\n=============================\nCompra realizada con éxito.\n\n¡Gracias por su preferencia!\n=============================");
                        errorC = false;
                    } else if (confirmar.equals("n")) {
                        System.out.println("\n===========================\nCompra cancelada.\n===========================");
                        errorC = false;
                    } else {
                        System.out.println("\n===========================\nError, vuelva a intentar.\n===========================");
                        errorC = true;
                    }
                }
            }
        }
    }
}
