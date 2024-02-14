package com.campusland.respository.impl.impldescuento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.campusland.respository.RepositoryDescuento;
import com.campusland.respository.models.Descuento;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;

public class RepositoryDescuentoMysqlImpl implements RepositoryDescuento{

    private Connection getConnection() throws SQLException {
        return ConexionBDMysql.getInstance();
    }

    @Override
    public List<Descuento> listar() {
        List<Descuento> listDescuentos = new ArrayList<>();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM descuento")) {
            while (rs.next()) {
                listDescuentos.add(crearDescuento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDescuentos;
    }

    @Override
    public Descuento porId(int descuentoId) {
        Descuento descuento = null;

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM descuento WHERE id_desc=?")) {
            stmt.setInt(1, descuentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    descuento = crearDescuento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return descuento;
    }

    @Override
    public void crear(Descuento descuento) {
        String sql = "INSERT INTO descuentos(tipo, condicion, valor, estado) VALUES(?,?,?,?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descuento.getTipo());
            stmt.setString(2, descuento.getCondicion());
            stmt.setFloat(3, descuento.getValor());
            stmt.setBoolean(4, descuento.isEstado());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void editar(Descuento descuento) {
        String sql = "UPDATE descuento SET tipo=?, condicion=?, valor=?, estado=? WHERE id_desc=?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descuento.getTipo());
            stmt.setString(2, descuento.getCondicion());
            stmt.setFloat(3, descuento.getValor());
            stmt.setBoolean(4, descuento.isEstado());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void eliminar(Descuento descuento) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM descuento WHERE id_desc=?")) {
            stmt.setInt(1, descuento.getId());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Descuento crearDescuento (ResultSet rs) throws SQLException {
        Descuento descuento = new Descuento();
        descuento.setId(rs.getInt("id_desc"));
        descuento.setTipo(rs.getString("tipo"));
        descuento.setCondicion(rs.getString("condicion"));
        descuento.setValor(rs.getFloat("valor"));
        descuento.setEstado(rs.getBoolean("activo"));
        return descuento;

    }

    @Override
    public List<Descuento> getActivos() {
        List<Descuento> listDescuentos = new ArrayList<>();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM descuentos WHERE activo = TRUE")) {
            while (rs.next()) {
                listDescuentos.add(crearDescuento(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDescuentos;
    }

    @Override
    public int validarCliente(int clienteId) {
        int cant = 0;

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS cant_client FROM factura WHERE cliente_id=?")) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cant = rs.getInt("cant_client");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cant;
    }
    
}
