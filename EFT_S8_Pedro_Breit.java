/**
 * 

*/
package eft_s8_pedro_breit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

class Cliente {
    private String nombre;
    private String rut;
    private String genero;
    private LocalDate fechaNacimiento;
    public Cliente(String nombre, String rut, String genero, String fechaNacimientoStr) {
        if (!validarRut(rut)) throw new IllegalArgumentException("RUT inválido");
        this.nombre = nombre;
        this.rut = rut;
        this.genero = genero;
        this.fechaNacimiento = parseFecha(fechaNacimientoStr);
    }
    private LocalDate parseFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de nacimiento inválida (dd-MM-yyyy)");
        }
    }
    private boolean validarRut(String rut) {
        return rut.matches("\\d{7,8}-[\\dkK]");
    }
    public int getEdad() {
        return (int) ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.now());
    }
    public String getNombre() {
        return nombre;
    }
    public String getRut() {
        return rut;
    }
    public String getGenero() {
        return genero;
    }
    public String getFechaNacimiento() {
        return fechaNacimiento.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    public void setGenero(String genero) {
        genero = genero.trim().toUpperCase();
        if (!genero.equals("F") && !genero.equals("M")) {
            throw new IllegalArgumentException("Género inválido. Use 'F' o 'M'.");
        }
        this.genero = genero;
    }
    public void setFechaNacimiento(String fecha) {
        this.fechaNacimiento = parseFecha(fecha);
    }
    @Override
    public String toString() {
        String datos = String.format("%-11s %-15s %-3s %-11s", getRut(), getNombre(), getGenero(), getFechaNacimiento());    
        return datos;
    }
}

class Asiento {
    String tipo;
    int precio;
    int disponibles;
    public Asiento(String tipo, int precio, int disponibles) {
        this.tipo = tipo;
        this.precio = precio;
        this.disponibles = disponibles;
    }
}

class Carrito {
    Map<String, Integer> entradas = new HashMap<>();
    Map<String, Integer> precios = new HashMap<>();
    public void agregar(String tipo, int cantidad, int precio) {
        entradas.put(tipo, entradas.getOrDefault(tipo, 0) + cantidad);
        precios.put(tipo, precio);
    }
    public void eliminar(String tipo, int cantidad) {
        if (entradas.containsKey(tipo)) {
            int restante = entradas.get(tipo) - cantidad;
            if (restante <= 0) entradas.remove(tipo);
            else entradas.put(tipo, restante);
        }
    }
    public void mostrarCarrito(Cliente cliente) {
        if (entradas.isEmpty()) {
            System.out.println("Carrito vacío.");
            return;
        }
        int total = 0;
        System.out.println("Carrito de " + cliente.getNombre());
        for (String tipo : entradas.keySet()) {
            int cant = entradas.get(tipo);
            int precio = precios.get(tipo);
            System.out.printf("\n%-16s %-16s %-1d x $%-8d",tipo, "................", cant, precio);
            total += cant * precio;
        }
        double descuento = calcularDescuento(cliente);
        System.out.printf("\n%-37s $%,7d","Total sin descuento..................", total);
        System.out.printf("\n%-41s %2d%%","Descuento aplicado...................", (int)(descuento * 100));
        System.out.printf("\n%-37s $%,7d","Total a pagar........................", (int)(total * (1 - descuento)));
    }
    public void imprimirBoleta(Cliente cliente) {
        int total = 0;
        System.out.println("\n--------------------------------------------\n                Teatro Moro\n--------------------------------------------");
        for (String tipo : entradas.keySet()) {
            int cant = entradas.get(tipo);
            int precio = precios.get(tipo);
            System.out.printf("\n%-16s %-16s %-1d x $%-8d",tipo, "................", cant, precio);
            total += cant * precio;
        }
        double descuento = calcularDescuento(cliente);
        System.out.printf("\n%-37s $%,7d","Total sin descuento..................", total);
        System.out.printf("\n%-41s %2d%%","Descuento aplicado...................", (int)(descuento * 100));
        System.out.printf("\n%-37s $%,7d","Total a pagar........................", (int)(total * (1 - descuento)));
        System.out.println("\n--------------------------------------------\n!Gracias por utilizar la app de Teatro Moro!\n--------------------------------------------");
    }
    public double calcularDescuento(Cliente c) {
        int edad = c.getEdad();
        String genero = c.getGenero().toLowerCase();
        double descuento = 0.0;
        if (edad < 7) descuento = 0.10;
        if (genero.equalsIgnoreCase("F")) {
            descuento = Math.max(descuento, 0.20);
        }
        if (edad >= 7 && edad <= 25) {
            descuento = Math.max(descuento, 0.15);
        }
        if (edad > 60) {
            descuento = Math.max(descuento, 0.25);
        }
        return descuento;
    }
    public int totalFinal(Cliente c) {
        int total = 0;
        for (String tipo : entradas.keySet()) {
            total += entradas.get(tipo) * precios.get(tipo);
        }
        return (int)(total * (1 - calcularDescuento(c)));
    }
    public void vaciar() {
        entradas.clear();
        precios.clear();
    }
}

class Venta {
    static int contador = 1;
    int id;
    Cliente cliente;
    Map<String, Integer> entradas;
    int total;
    public Venta(Cliente c, Map<String, Integer> entradas, int total) {
        this.id = contador++;
        this.cliente = c;
        this.entradas = new HashMap<>(entradas);
        this.total = total;
    }
    public String resumen() {
        
        String encabezado = String.format("%-4s %-12s %-15s %-10s\n", "ID","Rut", "Cliente", "Venta");
        String datos = String.format("%-3s %-12s %-15s $%-9s", id, cliente.getRut(), cliente.getNombre(), total);
        return encabezado + datos;        
    }
    public boolean esDeCliente(String rut) {
        return cliente.getRut().equals(rut);
    }    
}

public class EFT_S8_Pedro_Breit {
    static Map<String, Cliente> clientes = new HashMap<>();
    static Map<String, Asiento> asientos = new LinkedHashMap<>();
    static Carrito carrito = new Carrito();
    static List<Venta> ventas = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        inicializarAsientos();
        while (true) {
            System.out.print("\n========================================\n               TEATRO  MORO\n========================================\n1. Agregar o modificar cliente\n2. Comprar entrada\n3. Carrito de compra\n4. Registro de ventas\n5. Salir\nSeleccione una opción: ");
            String opc = sc.nextLine();
            switch (opc) {
                case "1": menuCliente(); break;
                case "2": comprarEntradas(); break;
                case "3": menuCarrito(); break;
                case "4": registroVentas(); break;
                case "5": return;
            }
        }
    }
    static void inicializarAsientos() {
        asientos.put("vip", new Asiento("VIP", 80000, 10));
        asientos.put("palco", new Asiento("Palco", 60000, 15));
        asientos.put("platea baja", new Asiento("Platea Baja", 40000, 20));
        asientos.put("platea alta", new Asiento("Platea Alta", 35000, 25));
        asientos.put("galeria", new Asiento("Galería", 25000, 30));
    }
    static void menuCliente() {
        OUTER:
        while (true) {
            System.out.print("\n----------------------------------------\n                Clientes\n----------------------------------------\n1. Agregar Cliente\n2. Modificar Cliente\n3. Ver Clientes\n4. Volver\nSeleccione una opción: ");
            String o = sc.nextLine();
            switch (o) {
                case "1":
                    agregarCliente();
                    break;
                case "2":
                    modificarCliente();
                    break;
                case "3":
                    verClientes();
                default:
                    break OUTER;
            }
        }
    }
    static void agregarCliente() {
        try {
            System.out.print("\nNombre: "); String nombre = sc.nextLine();
            System.out.print("RUT (formato 12345678-9): ");
            String rut = sc.nextLine();
            if(clientes.containsKey(rut)) {
                System.out.println("Error: Ya existe un cliente con ese RUT.");
                return;
            }
            System.out.print("Género (F/M): ");
            String genero = sc.nextLine();
            if(!genero.equalsIgnoreCase("F") && !genero.equalsIgnoreCase("M")) {
                System.out.println("Error: Género inválido. Debe ser 'F' o 'M'.");
                return;
            }
            System.out.print("Fecha de nacimiento (dd-MM-yyyy): "); String fn = sc.nextLine();
            clientes.put(rut, new Cliente(nombre, rut, genero, fn));
            System.out.println("Cliente agregado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    static void modificarCliente() {
        System.out.print("\nIngrese RUT del cliente: ");
        String rut = sc.nextLine();
        Cliente c = clientes.get(rut);
        if (c == null) {
            System.out.println("Cliente no encontrado."); return;
        }
        while (true) {
            System.out.print("\n1. Cambiar género\n2. Cambiar fecha nacimiento\n3. Eliminar cliente\n4. Volver\nSeleccione una opción: ");
            String o = sc.nextLine();
            if (o.equals("1")) {
                System.out.print("Nuevo género: "); c.setGenero(sc.nextLine());
            } else if (o.equals("2")) {
                System.out.print("Nueva fecha (dd-MM-yyyy): "); c.setFechaNacimiento(sc.nextLine());
            } else if (o.equals("3")) {
                clientes.remove(rut); break;
            } else break;
        }
    }
    static void verClientes(){
        if (clientes.isEmpty()){
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("\n--------------------------------------------\n          Clientes registrados\n--------------------------------------------");
        System.out.printf("%-11s %-12s %-6s %-11s","Rut", "Nombre", "Genero", "F. Nacimientos\n");
        for (Cliente c : clientes.values()) {
            System.out.println(c);
        }
    }
    static void comprarEntradas() {
        System.out.println("\n--------------------------------------------\n            COMPRAR ENTRADAS\n--------------------------------------------");
        System.out.printf("%-12s %-10s %-15s\n", "Ubicación", "Precio", "Disponibles");
        System.out.println("--------------------------------------------");
        for (String tipo : asientos.keySet()) {
            Asiento a = asientos.get(tipo);
            System.out.printf("%-12s $%-9d %-15d\n", a.tipo, a.precio, a.disponibles);
        }
        System.out.print("Seleccione una ubicación: ");
        String tipo = sc.nextLine().toLowerCase();
        Asiento a = asientos.get(tipo);
        if (a == null) {
            System.out.println("Ubicación inválido"); return;
        }
        System.out.print("Cantidad de asientos: ");
        int cant = Integer.parseInt(sc.nextLine());
        if (cant > a.disponibles) {
            System.out.println("No hay suficientes asientos"); return;
        }
        a.disponibles -= cant;
        carrito.agregar(tipo, cant, a.precio);
        System.out.println("Asientos agregados al carrito.");
    }
    static void menuCarrito() {
        if (carrito.entradas.isEmpty()) {
            System.out.println("No hay asientos reservados.");
            return;
        }
        System.out.print("RUT del cliente: ");
        String rut = sc.nextLine();
        Cliente c = clientes.get(rut);
        if (c == null) { System.out.println("Cliente no encontrado"); return; }
        System.out.println("\n--------------------------------------------\n                 CARRITO\n--------------------------------------------");
        carrito.mostrarCarrito(c);
        System.out.println("1. Eliminar asientos\n2. Pagar y generar boleta\n3. Volver");
        String o = sc.nextLine();
        if (o.equals("1")) {
            System.out.print("Tipo: "); String tipo = sc.nextLine().toLowerCase();
            System.out.print("Cantidad: "); int cant = Integer.parseInt(sc.nextLine());
            carrito.eliminar(tipo, cant);
        } else if (o.equals("2")) {
            
            int total = carrito.totalFinal(c);
            ventas.add(new Venta(c, carrito.entradas, total));
            System.out.println("\nPago realizado.");
            carrito.imprimirBoleta(c);
            carrito.vaciar();
        }
    }
    static void registroVentas() {
        while (true) {
            System.out.println("\n--------------------------------------------\n           REGISTRO DE VENTAS\n--------------------------------------------");
            System.out.print("\n1. Ver historial\n2. Buscar por RUT\n3. Buscar por ID de venta\n4. Volver\nSeleccione una opción: ");
            String o = sc.nextLine();
            if (o.equals("1")) {
                System.out.println("\n--------------------------------------------\n            Historial de ventas\n--------------------------------------------");
                ventas.forEach(v -> System.out.println(v.resumen()));
            } else if (o.equals("2")) {
                System.out.print("RUT: ");
                String rut = sc.nextLine();
                System.out.println("\n--------------------------------------------\n            Historial de ventas\n--------------------------------------------");
                ventas.stream().filter(v -> v.esDeCliente(rut)).forEach(v -> System.out.println(v.resumen()));
            } else if (o.equals("3")) {
                System.out.print("ID de venta: ");
                int id = Integer.parseInt(sc.nextLine());
                System.out.println("\n--------------------------------------------\n            Historial de ventas\n--------------------------------------------");
                ventas.stream().filter(v -> v.id == id).forEach(v -> System.out.println(v.resumen()));
            } else break;
        }
    }
}
