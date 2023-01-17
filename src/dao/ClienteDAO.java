package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Cliente;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.Vm;
import util.DatabaseManager;

public class ClienteDAO {

	public boolean insertCliente(Cliente cliente) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection()
				.prepareStatement("INSERT INTO CLIENTES VALUES (?,?,?,?,?,?)");
		ps.setInt(1, cliente.idCliente);
		ps.setString(2, cliente.nome);
		ps.setString(3, cliente.email);
		ps.setString(4, cliente.telefone);
		ps.setString(5, cliente.tipoPessoa);
		ps.setString(6, cliente.documento);

		int inserted = ps.executeUpdate();
		ps.close();

		return inserted > 0;
	}
	public boolean atualizarCliente(Cliente cliente , Cliente clienteClicado) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("UPDATE CLIENTES SET EMAIL =? , TELEFONE =? WHERE IDCLIENTE =?");
		ps.setString(1, cliente.email);
		ps.setString(2, cliente.telefone);
		ps.setInt(3, clienteClicado.idCliente);
		
		int updated = ps.executeUpdate();
		ps.close();
		
		return updated > 0 ;
	}
	
	public boolean excluirCliente(Cliente cliente) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("DELETE FROM  CLIENTES WHERE IDCLIENTE =?");
		ps.setInt(1,cliente.idCliente);
		
		int excluded = ps.executeUpdate();
		ps.close();
		
		return excluded > 0 ;
	}

	public List<Cliente> findAllClientes() throws SQLException {
		List<Cliente> clienteList = new ArrayList<Cliente>();
		try {
			Statement st = DatabaseManager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDCLIENTE,NOME , EMAIL, TELEFONE , TIPOPESSOA , DOCUMENTO FROM CLIENTES");

			while (rs.next()) {
				Cliente cliente = new Cliente();
				cliente.idCliente = rs.getInt(1);
				cliente.nome = rs.getString(2);
				cliente.email = rs.getString(3);
				cliente.telefone = rs.getString(4);
				cliente.tipoPessoa = rs.getString(5);
				cliente.documento = rs.getString(6);
				clienteList.add(cliente);
			}
			st.close();
			rs.close();
			return clienteList;

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;
		}
	}

	public int findClienteByCnpjCpf(String documento) throws SQLException {

		Statement st = DatabaseManager.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT COUNT(1) FROM CLIENTES WHERE DOCUMENTO = '" + documento + "'");
		try {

			if (rs.next()) {
				int count = rs.getInt(1);
				return count;

			}
			st.close();
			rs.close();
			return 0;

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;
		} finally {
			st.close();
			rs.close();
		}
	}
		public static Cliente findMaxIdCliente() throws SQLException {
			Cliente cliente = new Cliente();
			try {
				Statement st = DatabaseManager.getConnection().createStatement();
				ResultSet rs = st.executeQuery("SELECT MAX(IDCLIENTE) FROM CLIENTES");
				while (rs.next()) {
					
					cliente.idCliente = rs.getInt(1);
				}
				st.close();
				rs.close();
				return cliente;
				
			} catch (Exception e ) {
				Vm.debug(e.getMessage());
				throw e;
			}
	}
}
