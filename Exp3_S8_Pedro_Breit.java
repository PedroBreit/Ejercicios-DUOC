package ejercicios.exp3_s8_pedro_breit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
public class Exp3_S8_Pedro_Breit {
    static Scanner scan = new Scanner(System.in);
    public static class Precios {
        public static final int VIP = 10000;
        public static final int PLATEA = 7500;
        public static final int GENERAL = 5000;
    }
    public static class Descuentos {
        public static final double ESTUDIANTE = 0.10;
        public static final double TERCERA_EDAD = 0.15;
    }
    static HashMap<String, String> asientos = new HashMap<>();
    static ArrayList<String> asientosVIP = new ArrayList<>();
    static ArrayList<String> asientosPlatea = new ArrayList<>();
    static ArrayList<String> asientosGeneral = new ArrayList<>();
    static HashMap<String, Cliente> clientes = new HashMap<>();
    static ArrayList<Venta> ventas = new ArrayList<>();
    static double descuentoActual = 0;
    static int totalVentas = 0;
    static double recaudacionTotal = 0;
    static ArrayList<String> carrito = new ArrayList<>();
    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }
    static class Cliente {
        String rut;
        String nombre;
        String email;
        int edad;
        String tipo;
        public Cliente(String rut, String nombre, String email, int edad) {
            this.rut = rut;
            this.nombre = nombre;
            this.email = email;
            this.edad = edad;
            this.tipo = determinarTipoCliente(edad);
        }
        private String determinarTipoCliente(int edad) {
            if (edad <= 18) return "Estudiante";
            if (edad >= 60) return "Tercera edad";
            return "Normal";
        }
    }
    static class Venta {
        String idVenta;
        String rutCliente;
        ArrayList<String> asientos;
        double total;
        double descuento;
        String fecha;
        public Venta(String rutCliente, ArrayList<String> asientos, double total, double descuento, String fecha) {
            this.idVenta = "V" + (ventas.size() + 1);
            this.rutCliente = rutCliente;
            this.asientos = new ArrayList<>(asientos);
            this.total = total;
            this.descuento = descuento;
            this.fecha = fecha;
        }
    }
    private static void inicializarSistema() {
        for(int i=1; i<=5; i++) {
            String codigo = "A" + i;
            asientos.put(codigo, codigo);
            asientosVIP.add(codigo);
        }
        for(int i=1; i<=10; i++) {
            String codigo = "B" + i;
            asientos.put(codigo, codigo);
            asientosPlatea.add(codigo);
        }
        for(int i=1; i<=15; i++) {
            String codigo = "C" + i;
            asientos.put(codigo, codigo);
            asientosGeneral.add(codigo);
        }
    }
    private static void mostrarMenuPrincipal() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n|--------------------------|");
            System.out.println("|   TEATRO MORO - SISTEMA  |");
            System.out.println("|--------------------------|");
            System.out.println("| 1. Ver asientos          |");
            System.out.println("| 2. Registrar cliente     |");
            System.out.println("| 3. Seleccionar asientos  |");
            System.out.println("| 4. Aplicar descuentos    |");
            System.out.println("| 5. Ver carrito           |");
            System.out.println("| 6. Finalizar compra      |");
            System.out.println("| 7. Reportes de ventas    |");
            System.out.println("| 8. Salir                 |");
            System.out.println("|--------------------------|");
            System.out.print("\nSeleccione una opción: ");
            String opcion = scan.nextLine();
            switch (opcion) {
                case "1":
                    mostrarAsientosDisponibles();
                    break;
                case "2":
                    registrarNuevoCliente();
                    break;
                case "3":
                    seleccionarAsientos();
                    break;
                case "4":
                    aplicarDescuento();
                    break;
                case "5":
                    mostrarCarrito();
                    break;
                case "6":
                    finalizarCompra();
                    break;
                case "7":
                    mostrarReportes();
                    break;
                case "8":
                    continuar = false;
                    System.out.println("\n¡Gracias por usar el sistema del Teatro Moro!");
                    break;
                default:
                    System.out.println("\nOpción no válida, intente nuevamente.");
            }
        }
    }
    private static void mostrarAsientosDisponibles() {
        System.out.println("\n         ESCENARIO         ");
        System.out.println("|-------------------------|");
        System.out.println("|------- SECCIÓN VIP ------|");
        mostrarAsientosMatriz(asientosVIP, 5);
        System.out.println("|----- SECCIÓN PLATEA -----|");
        mostrarAsientosMatriz(asientosPlatea, 5);
        System.out.println("|---- SECCIÓN  GENERAL ----|");
        mostrarAsientosMatriz(asientosGeneral, 5);
        System.out.println("|-------------------------|");
    }
    private static void mostrarAsientosMatriz(ArrayList<String> seccion, int asientosPorFila) {
        int contador = 0;
        System.out.print("| ");
        for(String asiento : seccion) {
            String estado = asientos.get(asiento);
            System.out.printf("%-5s", estado);
            contador++;
            if(contador % asientosPorFila == 0 && contador < seccion.size()) {
                System.out.println(" |");
                System.out.print("| ");
            }
        }
        while(contador % asientosPorFila != 0) {
            System.out.print("      ");
            contador++;
        }
        System.out.println(" |");
    }
    private static void registrarNuevoCliente() {
        System.out.println("\n--------------------------------\nREGISTRO DE NUEVO CLIENTE\n--------------------------------\n");
        
        String rut = validarEntrada("RUT (ej: 12345678-9): ", "^\\d{7,8}-[\\dkK]$", "RUT no válido");
        if(rut == null) return;
        if(clientes.containsKey(rut)) {
            System.out.println("\nEste RUT ya está registrado.");
            return;
        }
        String nombre = validarEntrada("Nombre completo: ", "^[\\p{L} ]{5,}$", "Nombre no válido (mínimo 5 letras)");
        if(nombre == null) return;
        String email = validarEntrada("Email: ", "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", "Email no válido");
        if(email == null) return;
        int edad = validarNumero("Edad: ", 1, 120);
        if(edad == -1) return;
        clientes.put(rut, new Cliente(rut, nombre, email, edad));
        System.out.println("\nCliente registrado con éxito!");
    }
    private static String validarEntrada(String mensaje, String regex, String error) {
        while(true) {
            System.out.print(mensaje);
            String entrada = scan.nextLine();
            if(Pattern.matches(regex, entrada)) {
                return entrada;
            }
            System.out.println("\n" + error + "\nPresione Enter para reintentar o 'x' para cancelar");
            if(scan.nextLine().equalsIgnoreCase("x")) {
                return null;
            }
        }
    }
    private static int validarNumero(String mensaje, int min, int max) {
        while(true) {
            System.out.print(mensaje);
            try {
                int num = Integer.parseInt(scan.nextLine());
                if(num >= min && num <= max) return num;
                System.out.println("\nDebe ser entre " + min + " y " + max);
            } catch(NumberFormatException e) {
                System.out.println("\nDebe ingresar un número válido");
            }
        }
    }
    private static void seleccionarAsientos() {
        System.out.println("\n--------------------------\nSELECCIÓN DE ASIENTOS\n--------------------------\n");
        
        String rut = validarEntrada("Ingrese su RUT: ", "^\\d{7,8}-[\\dkK]$", "RUT no válido");
        if(rut == null || !clientes.containsKey(rut)) {
            System.out.println("\nCliente no registrado. Debe registrarse primero.");
            return;
        }
        
        mostrarAsientosDisponibles();
        
        System.out.print("\nIngrese asiento (ej: A1) o 'x' para terminar: ");
        String asiento = scan.nextLine().toUpperCase();
        
        while(!asiento.equalsIgnoreCase("x")) {
            if(validarFormatoAsiento(asiento) && asientoDisponible(asiento)) {
                reservarAsiento(asiento);
                carrito.add(asiento);
                System.out.println("Asiento " + asiento + " agregado al carrito");
            } else {
                System.out.println("Asiento no disponible o inválido");
            }
            
            System.out.print("\nIngrese otro asiento o 'x' para terminar: ");
            asiento = scan.nextLine().toUpperCase();
        }
    }
    private static boolean validarFormatoAsiento(String asiento) {
        return Pattern.matches("^[A-C][1-9]\\d?$", asiento);
    }
    private static boolean asientoDisponible(String asiento) {
        return asientos.containsKey(asiento) && !asientos.get(asiento).startsWith("[") 
               && !asientos.get(asiento).equals("XXX");
    }
    private static void reservarAsiento(String asiento) {
        asientos.put(asiento, "[" + asiento + "]");
    }
    private static void aplicarDescuento() {
        System.out.println("\n|--------------------------|");
        System.out.println("|      APLICAR DESCUENTO     |");
        System.out.println("|--------------------------|");
        if (carrito.isEmpty()) {
            System.out.println("\nNo hay asientos en el carrito. Seleccione asientos primero.");
            return;
        }
        System.out.print("Ingrese RUT del cliente (ej: 12345678-9): ");
        String rut = scan.nextLine();
        if (!clientes.containsKey(rut)) {
            System.out.println("\nCliente no registrado. Debe registrarse primero.");
            return;
        }
        Cliente cliente = clientes.get(rut);
        switch (cliente.tipo) {
            case "Estudiante":
                descuentoActual = Descuentos.ESTUDIANTE;
                System.out.printf("\nDescuento del %.0f%% aplicado (Estudiante)\n", Descuentos.ESTUDIANTE * 100);
                break;
            case "Tercera edad":
                descuentoActual = Descuentos.TERCERA_EDAD;
                System.out.printf("\nDescuento del %.0f%% aplicado (Tercera Edad)\n", Descuentos.TERCERA_EDAD * 100);
                break;
            default:
                descuentoActual = 0;
                System.out.println("\nNo aplica descuento (Cliente normal)");
        }
        System.out.println("\nResumen de descuento:");
        System.out.println("Nombre: " + cliente.nombre);
        System.out.println("Edad: " + cliente.edad);
        System.out.println("Tipo: " + cliente.tipo);
        System.out.printf("Descuento aplicado: %.0f%%\n", descuentoActual * 100);
    }
    private static void mostrarCarrito() {
        System.out.println("\n|--------------------------|");
        System.out.println("|       TU CARRITO         |");
        System.out.println("|--------------------------|");
        if (carrito.isEmpty()) {
            System.out.println("\nEl carrito está vacío");
            return;
        }
        System.out.println("\nAsientos seleccionados:\n-----------------------");
        double subtotal = 0;
        for (String asiento : carrito) {
            int precio = 0;
            char seccion = asiento.charAt(0);
            switch (seccion) {
                case 'A': precio = Precios.VIP; break;
                case 'B': precio = Precios.PLATEA; break;
                case 'C': precio = Precios.GENERAL; break;
            }
            subtotal += precio;
            System.out.printf("- %-5s %-7s $%,d\n", asiento, seccion == 'A' ? "VIP" : seccion == 'B' ? "Platea" : "General",precio);
        }
        double descuento = subtotal * descuentoActual;
        double total = subtotal - descuento;
        System.out.println("\nResumen de compra:\n-----------------------");
        System.out.printf("Subtotal:        $%,10d\n", (int) subtotal);
        System.out.printf("Descuento (%.0f%%): $%,10d\n", descuentoActual * 100, (int) descuento);
        System.out.println("---------------------");
        System.out.printf("TOTAL:          $%,10d\n", (int) total);
        System.out.println("\n[E] Eliminar asiento | [V] Volver al menú");
        boolean error = true;
        while (error){
            System.out.print("Seleccione una opción: ");
            String opcion = scan.nextLine().toUpperCase();
            switch (opcion){
                case "E":
                    eliminarAsientoDelCarrito();
                    error = false;
                case "V":
                    error = false;
                    break;
                default:
                    System.out.println("¡Error, vuelva a intentarlo!");
                    break;
            }
        }
    }
    private static void eliminarAsientoDelCarrito() {
        System.out.print("\nIngrese el asiento a eliminar (ej: A1): ");
        String asiento = scan.nextLine().toUpperCase();
        if (carrito.remove(asiento)) {
            asientos.put(asiento, asiento);
            System.out.println("\nAsiento " + asiento + " eliminado del carrito");
        } else {
            System.out.println("\nEl asiento no se encuentra en el carrito");
        }
    }
    private static void finalizarCompra() {
        System.out.println("\n|--------------------------|");
        System.out.println("|     FINALIZAR COMPRA     |");
        System.out.println("|--------------------------|");
        if (carrito.isEmpty()) {
            System.out.println("\nEl carrito está vacío. No hay nada que comprar.");
            return;
        }
        System.out.print("Ingrese RUT del cliente: ");
        String rut = scan.nextLine();
    
        if (!clientes.containsKey(rut)) {
            System.out.println("\nCliente no registrado. Debe registrarse primero.");
            return;
        }
        double subtotal = calcularSubtotal();
        double descuento = subtotal * descuentoActual;
        double total = subtotal - descuento;
        System.out.println("\n|------------------------------------|");
        System.out.println("|         RESUMEN DE COMPRA          |");
        System.out.println("|------------------------------------|");
        System.out.printf("| %-18s %15s |\n", "Cliente:", clientes.get(rut).nombre);
        System.out.printf("| %-18s %15s |\n", "RUT:", rut);
        System.out.println("|------------------------------------|");
        for (String asiento : carrito) {
            System.out.printf("| + %-16s %15s |\n", asiento, "$" + precioAsiento(asiento));
        }
        System.out.println("|------------------------------------|");
        System.out.printf("| %-18s %15s |\n", "Subtotal:", "$" + (int) subtotal);
        System.out.printf("| %-18s %15s |\n", "Descuento (" + (int)(descuentoActual * 100) + "%):", "-$" + (int) descuento);
        System.out.println("|------------------------------------|");
        System.out.printf("| %-18s %15s |\n", "TOTAL A PAGAR:", "$" + (int) total);
        System.out.println("|------------------------------------|");
        boolean error = true;
        while(error){
            System.out.print("\n¿Confirmar compra? (S/N): ");
            String confirmacion = scan.nextLine().toUpperCase();
            switch (confirmacion){
                case "S":
                    String fecha = java.time.LocalDate.now().toString();
                    Venta nuevaVenta = new Venta(rut, new ArrayList<>(carrito), total, descuentoActual, fecha);       
                    ventas.add(nuevaVenta);
                    for (String asiento : carrito) {
                    asientos.put(asiento, "XXX");
                    }
                    totalVentas++;
                    recaudacionTotal += total;
                    carrito.clear();
                    descuentoActual = 0;
                    System.out.println("\n|--------------------------|");
                    System.out.println("|  COMPRA REALIZADA CON ÉXITO |");
                    System.out.println("|--------------------------|");
                    System.out.println("\nN° de venta: " + nuevaVenta.idVenta);
                    System.out.println("Fecha: " + fecha);
                    error = false;
                    break;
                case "N":
                    System.out.println("\nCompra cancelada");
                    error = false;
                    break;
                default:
                    System.out.println("¡Error; vuelva a intentar!");
            }
        }
    }
    private static double calcularSubtotal() {
        double subtotal = 0;
        for (String asiento : carrito) {
            subtotal += precioAsiento(asiento);
        }
        return subtotal;
    }
    private static int precioAsiento(String asiento) {
        char seccion = asiento.charAt(0);
        switch (seccion) {
            case 'A': return Precios.VIP;
            case 'B': return Precios.PLATEA;
            case 'C': return Precios.GENERAL;
            default: return 0;
        }
    }
    private static void mostrarReportes() {
        System.out.println("\n|---------------------------|");
        System.out.println("|     REPORTES DE VENTAS    |");
        System.out.println("|---------------------------|");
        System.out.println("\nESTADÍSTICAS GENERALES:");
        System.out.println("----------------------------");
        System.out.printf("+ Total de ventas: %d\n", totalVentas);
        System.out.printf("+ Recaudación total: $%,d\n", (int) recaudacionTotal);
        int vipVendidos = contarAsientosVendidos(asientosVIP);
        int plateaVendidos = contarAsientosVendidos(asientosPlatea);
        int generalVendidos = contarAsientosVendidos(asientosGeneral);
        System.out.println("\nASIENTOS VENDIDOS POR SECCIÓN:");
        System.out.println("----------------------------");
        System.out.printf("+ VIP: %d/5 (%.0f%% ocupación)\n", vipVendidos, (vipVendidos / 5.0) * 100);
        System.out.printf("+ Platea: %d/10 (%.0f%% ocupación)\n", plateaVendidos, (plateaVendidos / 10.0) * 100);
        System.out.printf("+ General: %d/15 (%.0f%% ocupación)\n", generalVendidos, (generalVendidos / 15.0) * 100);
        if (!ventas.isEmpty()) {
            System.out.println("\nÚLTIMAS VENTAS:");
            System.out.println("----------------------------");
            int inicio = Math.max(0, ventas.size() - 5);
            System.out.printf("%-3s - %-11s - %-10s - %-8s\n", "ID", "RUT", "Fecha", "Total");
            for (int i = inicio; i < ventas.size(); i++) {
                Venta venta = ventas.get(i);
                System.out.printf("%-3s - %-11s - %-10s - $%,d\n", venta.idVenta, venta.rutCliente, venta.fecha, (int) venta.total);
            }
        }
        System.out.println("\nPresione Enter para continuar...");
        scan.nextLine();
    }
    private static int contarAsientosVendidos(ArrayList<String> seccion) {
        int count = 0;
        for (String asiento : seccion) {
            if (asientos.get(asiento).equals("XXX")) {
                count++;
            }
        }
        return count;
    }
}