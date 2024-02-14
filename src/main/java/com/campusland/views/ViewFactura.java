package com.campusland.views;

import java.sql.SQLException;
import java.time.LocalDateTime;
import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.Impuesto;
import com.campusland.respository.models.ItemFactura;
import com.campusland.respository.models.Producto;

public class ViewFactura extends ViewMain {

    public static void startMenu() {

        int op = 0;

        do {
            clear();
            op = mostrarMenu();
            switch (op) {
                case 1:
                    crearFactura();
                    leer.next();
                    break;
                case 2:
                    listarFactura();
                    leer.next();
                    break;
                case 3:
                    informeDian();
                    leer.next();
                    break;
                case 4:
                    informeVentas();
                    leer.next();
                    break;
                case 5:
                    listarClientesPorCompras();
                    leer.next();
                    break;
                case 6:
                    listarProductosPorVentas();
                    leer.next();
                    break;
                case 7:
                    modificarDescuento();
                    leer.next();
                    break;
                case 8:
                    eliminarDescuento();
                    leer.next();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }

        } while (op >= 1);

    }

    public static int mostrarMenu() {
        System.out.println("----Menu--Factura----");
        System.out.println(" 1. Crear factura.");
        System.out.println(" 2. Listar factura.");
        System.out.println(" 3. Generar archivo DIAN por año.");
        System.out.println(" 4. Informe total de ventas, descuentos e impuestos.");
        System.out.println(" 5. Listado descendente clientes por compras.");
        System.out.println(" 6. Listado descendente producto  mas vendido.");
        System.out.println(" 7. Modificar regla de descuento.");
        System.out.println(" 8. Eliminar regla de descuento.");
        System.out.println(" 0. Salir ");
        return leer.nextInt();
    }

    public static void listarFactura() {
        System.out.println("Lista de Facturas");
        for (Factura factura : serviceFactura.listar()) {
            factura.display();
            System.out.println();
        }
    }

    public static void crearFactura() {
        System.out.println("-- Creación de Factura ---");

        Cliente cliente;
        do {
            cliente = ViewCliente.buscarGetCliente();
        } while (cliente == null);

        Factura factura = new Factura(LocalDateTime.now(), cliente);
        System.out.println("-- Se creó la factura -----------------");
        System.out.println("-- Seleccione los productos a comprar por código");
     

        do {
            Producto producto = ViewProducto.buscarGetProducto();

            if (producto != null) {
                System.out.print("Cantidad: ");
                int cantidad = leer.nextInt();
                ItemFactura item = new ItemFactura(cantidad, producto);
                factura.agregarItem(item);

                System.out.println("Agregar otro producto: si o no");
                String otroItem = leer.next();
                if (!otroItem.equalsIgnoreCase("si")) {
                    break;
                }
            }

        } while (true);
        

        try {
            factura.calcularDescuentoTotal(serviceDescuento.getAplicables(factura));
            factura.calcularImpuesto(serviceFactura.obtenerImpuesto(factura.getFecha().getYear()));
            factura.displayFinal(serviceDescuento.getAplicables(factura));
            serviceFactura.crear(factura);
            System.out.println("Se creó la factura");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void informeDian() {
        System.out.println("Generar Archivo DIAN del Año: ");
        int year = leer.nextInt();
        for (Factura factura : serviceFactura.listarporAnino(year)){
            serviceImpuesto.crear(new Impuesto(factura));
        }
    }

    private static void informeVentas() {
        serviceFactura.informeVentas();
    }

    private static void listarClientesPorCompras() {
        serviceFactura.listarClientesPorCompras();
    }

 private static void listarProductosPorVentas() {
        serviceFactura.listarProductosPorVentas();
    }

    private static void modificarDescuento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarDescuento'");
    }

    private static void eliminarDescuento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarDescuento'");
    }
    
}
