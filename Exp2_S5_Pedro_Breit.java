/** Para este problema se asignan los siguientes valores:
* 1. Valor VIP = 10.000 (linea 16)
* 2. Valir platea = 7.500 (linea 17)
* 3. Valor general = 5.000 (linea 18)
* 4. Descuento estudiante 10% (linea 21)
* 5. Descuentoi tercera edad 15% (linea 22)
**/
package com.exp2_s5_pedro_breit;
import java.util.ArrayList;
import java.util.Scanner;

public class Exp2_S5_Pedro_Breit {
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
            System.out.println("|--------Escenario--------|\n");
            MatrizAsientos.imprimirMatriz(aVIP, "VIP----");
            MatrizAsientos.imprimirMatriz(aPlatea, "Platea-");
            MatrizAsientos.imprimirMatriz(aGeneral, "General");

            System.out.println("===========================\n      MENÚ PRINCIPAL      \n===========================");
            System.out.println("1. Seleccionar asiento");
            System.out.println("2. Ver promociones");
            System.out.println("3. Ver carrito y eliminar");
            System.out.println("4. Finalizar compra");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scan.nextLine();

            switch (opcion) {
                // Seleccionar asiento
                case "1":
                    System.out.print("Ingrese el asiento (ej: A1): ");
                    String asiento = scan.nextLine().toUpperCase();

                    // Validacion de existencia del asiento o ver si ha sido seleccionado (ej: [A1]) o esta ocupado (ej: XXX)
                    if (MatrizAsientos.asientoDisponible(asiento) && asiento.length() <= 2 ) {
                        MatrizAsientos.reservarAsiento(asiento);
                        carrito.agregarAsiento(asiento);
                        System.out.println("Asiento reservado correctamente.");
                    } else {
                        System.out.println("Asiento no disponible o inválido.");
                    }
                    break;

                // Promociones
                case "2":
                    
                    //Ciclo de validacion de respuesta (s o n)
                    boolean errorA = true;
                    while (errorA){
                        
                        //Ingreso de edad para promociones
                        System.out.print("Ingrese su edad: ");
                        int edad = Integer.parseInt(scan.nextLine());
                        if (edad <= 0 || edad > 120) {
                            System.out.println("Error, vuelva a intentarlo");
                            errorA = true;
                        } else if (edad <= 18) {
                            descuentoActual = D_ESTUDIANTE;
                            System.out.println("Descuento aplicado: 10% por ser estudiante.");
                            errorA = false;
                        } else if (edad >= 60) {
                            descuentoActual = D_TERCERA_EDAD;
                            System.out.println("Descuento aplicado: 15% por tercera edad.");
                            errorA = false;
                        } else {
                            descuentoActual = 0;
                            System.out.println("No aplica descuento.");
                            errorA = false;
                        }
                    };
                    break;

                //Editar carrito
                case "3":
                    
                    //Carrito vacio
                    if (carrito.estaVacio()) {
                        System.out.println("El carrito está vacío.");
                        break;
                    } else {
                        carrito.mostrarCarrito();
                        
                        //Ciclo de validacion de respuesta (s o n)
                        boolean errorE = true;
                        while (errorE){
                            System.out.print("¿Desea eliminar un asiento? (s/n): ");
                            String eliminar = scan.nextLine().toLowerCase();
                    
                            // Eliminar asiento
                            if (eliminar.equals("s")) {
                                System.out.print("Ingrese asiento a eliminar: ");
                                String asientoEliminar = scan.nextLine().toUpperCase();
                                
                                //Verificar asiento en carrito
                                if (carrito.eliminarAsiento(asientoEliminar)) {
                                    MatrizAsientos.liberarAsiento(asientoEliminar);
                                    System.out.println("Asiento eliminado correctamente.");
                                } else {
                                    System.out.println("Asiento no encontrado en el carrito.");
                                }
                                errorE = false;
                            
                            // No eliminar carrito
                            } else if (eliminar.equals("n")){
                                errorE = false;
                                break;
                            } else {
                                System.out.println("Error, vuelva a intentar.");
                                errorE = true;
                            }
                        }
                    }
                    break;

                // Finalizar compra
                case "4":
                    
                    // Carrito vacio;
                    if (carrito.estaVacio()) {
                        System.out.println("No hay asientos en el carrito.");
                        break;
                    
                    // Muestra asientos seleccionados con su respectivo precio, total sin descuento, descuento y total con descuento
                    } else {
                        System.out.println("===========================\n   CONFIRMACIÓN DE COMPRA  \n===========================");
                        carrito.mostrarCarrito();

                        int total = carrito.calcularTotal();
                        double totalFinal = total - (total * descuentoActual);

                        System.out.println("Total sin descuento: .... $" + total);
                        System.out.println("Descuento aplicado: ..... " + (descuentoActual * 100) + "%");
                        System.out.println("Total a pagar: .......... $" + totalFinal);

                        // Validacion de respuesta (s o n)
                        boolean errorC = true;
                        while (errorC){
                            System.out.print("¿Desea confirmar la compra? (s/n): ");
                            String confirmar = scan.nextLine().toLowerCase();
                            if (confirmar.equals("s")) {
                                for (String a : carrito.asientos) {
                                    MatrizAsientos.confirmarAsiento(a);
                                }
                                carrito.vaciarCarrito();
                                System.out.println("Compra realizada con éxito. ¡Gracias por su compra!");
                                errorC = false;
                            } else if (confirmar.equals("n")) {
                                System.out.println("Compra cancelada.");
                                errorC = false;
                            } else {
                                System.out.println("Error, vuelva a intentar.");
                                errorC = true;
                            }
                        };
                    }
                    break;

                // Salir de la app y mensaje de despedida
                case "5":
                    continuar = false;
                    System.out.println("Gracias por viisitar la app de compras de Teatro Moro. ¡Hasta pronto!");
                    break;

                default:
                    System.out.println("Error, vuelva a intrentar.");
            }
        }
    }

    static class MatrizAsientos {
        
        // Creacion de matriz de asientos
        public static void inicializarMatriz(String[][] matriz, String letra) {
            int contador = 1;
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    matriz[i][j] = letra + contador++;
                }
            }
        }

        // Imprime la matriz en la consola
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

        // Boolean de buesqueda y modificacion de asientos
        private static boolean buscarYModificar(String objetivo, String nuevoValor) {
            String[][][] matrices = {aVIP, aPlatea, aGeneral};
            
            // Recorre las matrices
            for (String[][] matriz : matrices) {
                
                // Recorre fila de matices
                for (int i = 0; i < matriz.length; i++) {
                    
                    // Recorre columna de matrices
                    for (int j = 0; j < matriz[i].length; j++) {
                        
                        // devuelve true si encuentra el valor objetivo
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
            System.out.println("Asientos en el carrito:");
            
            // Recorre los asientos en la lista y obtiene la plrimera letra para asignar el precio
            for (String asiento : asientos) {
                switch (asiento.charAt(0)) {
                    case 'A': precio = P_VIP; break;
                    case 'B': precio = P_PLATEA; break;
                    case 'C': precio = P_GENERAL; break;
                }
                System.out.println("+ " + asiento + " .................... $" + precio);
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
                switch (asiento.charAt(0)) {
                    case 'A': total += P_VIP; break;
                    case 'B': total += P_PLATEA; break;
                    case 'C': total += P_GENERAL; break;
                }
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
    }
}
