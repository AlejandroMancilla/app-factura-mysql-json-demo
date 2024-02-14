package com.campusland.respository.impl.implfactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.respository.models.Producto;
import com.campusland.utils.Formato;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;


import java.sql.Statement;

public class RepositoryFacturaMysqlImpl implements RepositoryFactura {

    private static final String SQL_GET_LIST_FACTURAS = "SELECT f.numeroFactura, f.fecha, c.id, c.nombre, c.apellido, c.email, c.direccion, c.celular, c.documento, f.descuento, f.impuesto FROM factura f JOIN cliente c ON c.id=f.cliente_id ORDER BY f.numeroFactura asc";
    private static final String SQL_GET_LIST_ITEMS_FACTURAS = "SELECT i.id,i.cantidad,i.importe,p.codigo,p.nombre,p.descripcion,p.precioVenta,p.precioCompra  FROM item_factura i join producto p on i.producto_codigo=p.codigo WHERE i.factura_numeroFactura =?";
    private static final String INSERT_FACTURA = "INSERT INTO factura (fecha, cliente_id, descuento, impuesto) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ITEM_FACTURA = "INSERT INTO item_factura (factura_numeroFactura, producto_codigo, cantidad, importe) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_LIST_CLIENTES_COMPRAS = "SELECT c.nombre, c.apellido, c.documento, SUM(i.importe) AS suma_compras FROM factura f JOIN item_factura i ON f.numeroFactura = i.factura_numeroFactura JOIN cliente  c ON c.id = f.cliente_id GROUP BY c.id ORDER BY suma_compras DESC";
    private static final String SQL_GET_LIST_PRODUCTO_VENDIDOS = "SELECT p.nombre, SUM(i.cantidad) AS ventas_producto FROM item_factura i JOIN producto p ON i.producto_codigo = p.codigo GROUP BY i.producto_codigo ORDER BY ventas_producto DESC";
    private static final String SQL_GET_FACTURA_IMPUESTO = "SELECT valor FROM impuestos WHERE year = ?";

    private Connection getConnection() throws SQLException {
        return ConexionBDMysql.getInstance();
    }

    @Override
    public List<Factura> listar() {
        List<Factura> listFacturas = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_LIST_FACTURAS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Factura factura = creaFactura(rs);
                obtenerItemsFactura(conn,factura);
                listFacturas.add(factura);
            }

        } catch (SQLException e) {        
                e.printStackTrace();           
        }
       
        return listFacturas;
    }   

    private void obtenerItemsFactura(Connection connection,Factura factura) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LIST_ITEMS_FACTURAS)) {
            preparedStatement.setInt(1, factura.getNumeroFactura());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    factura.agregarItem(crearItems(rs));
                }
            }
        }
    }

    @Override
    public void crear(Factura factura) throws FacturaExceptionInsertDataBase {
        Connection conn=null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psFactura = conn.prepareStatement(INSERT_FACTURA,
                    Statement.RETURN_GENERATED_KEYS)) {
                psFactura.setDate(1, Formato.convertirLocalDateTimeSqlDate(factura.getFecha()));
                psFactura.setInt(2, factura.getCliente().getId());
                psFactura.setDouble(3, factura.getDescuento());
                psFactura.setDouble(4, factura.getImpuesto());
                psFactura.executeUpdate();
                try (ResultSet generatedKeys = psFactura.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        factura.setNumeroFactura(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el valor autoincremental para numeroFactura");
                    }
                }
            }

            try (PreparedStatement psItem = conn.prepareStatement(INSERT_ITEM_FACTURA)) {
                for (ItemFactura item : factura.getItems()) {
                    psItem.setInt(1, factura.getNumeroFactura());
                    psItem.setInt(2, item.getProducto().getCodigo());
                    psItem.setInt(3, item.getCantidad());
                    psItem.setDouble(4, item.getImporte());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();                    
                }
            }
            e.printStackTrace();
            throw new FacturaExceptionInsertDataBase("No se pudo hacer el insert en la base de datos de factura");
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();                    
                }
            }
        }
    }

   

    private Factura creaFactura(ResultSet rs) throws SQLException {
        final int clienteId = rs.getInt("id");
        final String documento = rs.getString("documento");
        final String nombre = rs.getString("nombre");
        final String apellido = rs.getString("apellido");
        final String email = rs.getString("email");
        final String direccion = rs.getString("direccion");
        final String celular = rs.getString("celular");
        final Cliente cliente = new Cliente(clienteId, documento, nombre, apellido, email, direccion, celular);
        final int numeroFactura = rs.getInt("numeroFactura");
        final LocalDateTime fecha = Formato.convertirTimestampFecha(rs.getTimestamp("fecha"));
        final double descuento = rs.getDouble("descuento");
        final double impuesto = rs.getDouble("impuesto");
        return new Factura(numeroFactura, fecha, cliente, descuento, impuesto);

    }

    private ItemFactura crearItems(ResultSet rs) throws SQLException {
        final int codigo = rs.getInt("codigo");
        final String nombre = rs.getString("nombre");
        final String descripcion = rs.getString("descripcion");
        final double precioVenta = rs.getDouble("precioVenta");
        final double precioCompra = rs.getDouble("precioCompra");
        final Producto producto = new Producto(codigo, nombre, descripcion, precioVenta, precioCompra);
        return new ItemFactura(rs.getInt("cantidad"), producto);

    }

    @Override
    public void listarProductosPorVentas() {
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_LIST_PRODUCTO_VENDIDOS);
             ResultSet rs = preparedStatement.executeQuery()) {
             System.out.printf("|%-15s|%-10s|%n", "PRODUCTO", "VENDIDOS");
             System.out.println("+" + "-".repeat(26) + "+");
            while (rs.next()) {
                System.out.printf("|%15s|%10s|%n", rs.getString("nombre"), rs.getInt("ventas_producto"));
            }
            System.out.println("+" + "-".repeat(26) + "+");
        } catch (SQLException e) {        
                e.printStackTrace();           
        }
    }

    public void listarClientesPorCompras() {
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_GET_LIST_CLIENTES_COMPRAS);
             ResultSet rs = preparedStatement.executeQuery()) {
             System.out.printf("|%8s|%-20s|%15s|%n", "CC", "CLIENTE", "CANTIDAD");
             System.out.println("+" + "-".repeat(45) + "+");
            while (rs.next()) {
                System.out.printf("|%8s|%-20s|%-15s|%n", rs.getString("documento"), rs.getString("nombre") + rs.getString("apellido"), Formato.formatoMonedaPesos(rs.getInt("suma_compras")));
            }
            System.out.println("+" + "-".repeat(45) + "+");
        } catch (SQLException e) {        
                e.printStackTrace();           
        }
    }

    @Override
    public void informeVentas() {
        double ventasTotal = 0;
        double descuentoTotal = 0;
        double impuestoTotal = 0;
        for(Factura factura : listar()) {
            ventasTotal += factura.getTotalFactura();
            descuentoTotal += factura.getDescuento();
            impuestoTotal += factura.getImpuesto();
        }
        System.out.println("INFORME TOTAL DE VENTAS");
        System.out.println("     Ventas Totales: " + Formato.formatoMonedaPesos(ventasTotal));
        System.out.println(" Descuentos Totales: " + Formato.formatoMonedaPesos(descuentoTotal));
        System.out.println("  Impuestos Totales: " + Formato.formatoMonedaPesos(impuestoTotal));

    }

    public double obtenerImpuesto(int year) {
        double imp = 0;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT valor FROM impuestos WHERE year=?")) {
            stmt.setInt(1, year);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imp = rs.getDouble("valor");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imp;
    }

 
    

}
