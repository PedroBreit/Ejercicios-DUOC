package com.mycompany.exp2_s4_pedro_breit;
import java.util.Scanner;
public class Exp2_S4_Pedro_Breit {
    
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido a teatro Moro,");
       
        
        //Matriz de asientos
        String [][] asientos = new String [9][5]; 
        String asiento;
        int nroAsiento = 0;
        for (int f = 0; f < 9; f++){
            for (int c = 0; c < 5; c++){
                nroAsiento++;
                    if (f < 3){
                        asiento = "A";
                    }   else if (f < 6){
                        asiento = "B";
                        } else {
                            asiento = "C";                               
                        }                           
                    if (nroAsiento > 15) {
                        nroAsiento = 1;
                    }
                    asientos[f][c] = asiento + nroAsiento;
            }
        }
        
        boolean salir = false;
        int cantEntradas = 0;
        String lugares = "";
        
        // do while: permite que e repita el codigo hasta pagar o salir
        do {
            System.out.println();
            System.out.println("¿Que acción desea realizar?");
            System.out.println();
            System.out.println("1. Comprar entrada");
            System.out.println("2. Salir");
           
            // Habilita la opcion de ir a pagar luego de haber reservado a lo menos una entrada
            String x = ")";
            if (cantEntradas > 0){
            System.out.println("3. Ir a pagar.");
            x = "/3)";
            }
            System.out.println();
            System.out.print("Seleccione una opción (1/2" + x + ": ");
            int opcion = scanner.nextInt(); 
            scanner.nextLine();
            
            // Switch: reproduce distintos codigos segun la opcion tomada
            switch (opcion) {
                
                case 1:
                    
                    // Muestra una matriz con los asientos y el escenario
                    System.out.println();
                    System.out.println("---------Escenario---------");
                    System.out.println();
                    for (int a = 0; a < 9; a++){
                        for (int b = 0; b <5; b++){
                            System.out.printf("%-6s",asientos[a][b]);
                        }
                        System.out.println();
                    }
                    
                    System.out.println();
                    System.out.print("Seleccione el asiento que desea comprar: ");
                    String seleccionado = scanner.nextLine();
                    System.out.println();
                    
                   // Si se selecciona un asiento ocupado manda error
                    if (seleccionado.equals("XXX")){
                        System.out.println();
                        System.out.println("El asiento ya esta reservado");
                        break;
                    }
                    
                    boolean esta = false;
                    
                    //Encierra entre [] el asiento seleccionado
                    for ( int f = 0; f < 9; f++){
                        for (int c = 0; c < 5; c++){
                            if (asientos[f][c].equals(seleccionado)){
                                esta = true;
                                asientos[f][c] = "[" + seleccionado + "]";   
                                
                                //Matriz de asientos
                                System.out.println();
                                System.out.println("---------Escenario---------");
                                System.out.println();
                                for (int a = 0; a < 9; a++){
                                    for (int b = 0; b <5; b++){
                                        System.out.printf("%-6s",asientos[a][b]);
                                    }
                                    System.out.println();
                                } //Muestra la matriz
                                
                                //Valida la selección de asiento y pregunta por confirmar
                                System.out.println();
                                System.out.println("El asiento " + seleccionado + " esta disponible.");
                                System.out.println();
                                System.out.print("¿Deseas confirmar tu reserva? (si/no): ");
                                String confirmacion = scanner.nextLine();    
                                
                                // Dependiendo de la confirmación arroja resultados y valida respuestas correctas
                                if (confirmacion.equals("si")){
                                    asientos[f][c] = "XXX";
                                    cantEntradas++;
                                    if (cantEntradas == 1){
                                        lugares = lugares + seleccionado;
                                    } else {
                                        lugares = lugares + ", " + seleccionado;
                                    }
                                        System.out.println();    
                                        System.out.println("Su entrada a quedado reservada.");    
                                    } else if (confirmacion.equals("no")){
                                        asientos[f][c] = seleccionado;
                                    } else {
                                        System.out.println();
                                        System.out.println("El valor ingresado no es valido");
                                        asientos[f][c] = seleccionado;
                                    
                                }
                            } 
                    
                        }
                    }            
                    if (!esta){
                        System.out.println();
                        System.out.println("El asiento no esta disponible.");
                    }
                    break;
                
                // Opción de salida
                case 2:
                    salir = true;
                    break;
                    
                // Ir a pagar: Se pregunta el valor de la entrada y la edad del cliente para aplicar descuentos
                case 3:
                    if (cantEntradas > 0){
                        System.out.print("Ingrese el precio de la entrada: ");
                        int precioBase = scanner.nextInt();
                    
                        System.out.print("Ingrese su edad para optar a un descuento: ");
                        int edad = scanner.nextInt();
                        String desc = "";
                        double descuento = 0;
                        
                        //Se aplica descuento segun la edad y se valida que sea correcta
                        if (edad <= 18 && edad > 0){
                            descuento = 0.10;
                            desc = "10%";
                        } else if (edad < 65 && edad > 18){
                            descuento = 0;
                            desc = "0%";
                        } else if (edad >= 65 && edad < 120){
                            descuento = 0.15;
                            desc = "15%";
                        } else {
                            System.out.println();
                            System.out.println("La edad no es valida");
                        }
                        //Se calcula el total a pagar
                        double totalEntradas = cantEntradas * precioBase *(1-descuento);
                        
                        //Envia los resultados
                        System.out.println();
                        System.out.println("Usted a seleccionado una cantidad de " + cantEntradas + " entradas." );
                    
                        if (cantEntradas ==1){
                            System.out.println("El asiento que selecciono fue el " + lugares);
                        } else {
                            System.out.println("Los asientos que selecciono fueron los siguientes: " + lugares);
                        }
                        System.out.println("El valor de cada entrada es de: " + precioBase);
                        System.out.println("Se le aplicara un descuento del " + desc);
                        System.out.println("El total a pagar serian $" + totalEntradas);
                        salir = true;
                        break;
                    } else {
                        System.out.println();
                        System.out.println("Ingrese un número valido");
                        break;
                    }
                default:
                    System.out.println();
                    System.out.println("Ingrese un número valido");
                    break;
            }
        
        } while (!salir);
    }
}