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

	private static ClienteDAO instance;

	public static ClienteDAO getInstance() {
		if (instance == null) {
			instance = new ClienteDAO();
		}
		return instance;
	}

	public boolean insertCliente(Cliente cliente) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection()
				.prepareStatement("INSERT INTO CLIENTES VALUES (?,?,?,?,?,?,?)");
		try {

			ps.setString(1, cliente.id);
			ps.setString(2, cliente.nome);
			ps.setString(3, cliente.email);
			ps.setString(4, cliente.telefone);
			ps.setString(5, cliente.tipoPessoa);
			ps.setString(6, cliente.cpfCnpj);
			ps.setString(7, cliente.status);
			int inserted = ps.executeUpdate();

			return inserted > 0;

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;

		} finally {
			ps.close();
		}

	}

	public boolean atualizarCliente(Cliente cliente, Cliente clienteClicado) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection()
				.prepareStatement("UPDATE CLIENTES SET EMAIL =? , TELEFONE =? , STATUS =? WHERE ID =?");
		ps.setString(1, cliente.email);
		ps.setString(2, cliente.telefone);
		ps.setString(3, cliente.status);
		ps.setString(4, clienteClicado.id);

		int updated = ps.executeUpdate();
		ps.close();

		return updated > 0;
	}

	public boolean excluirCliente(Cliente cliente) throws SQLException {
		PreparedStatement ps = DatabaseManager.getConnection().prepareStatement("DELETE FROM  CLIENTES WHERE ID =?");
		ps.setString(1, cliente.id);

		int excluded = ps.executeUpdate();
		ps.close();

		return excluded > 0;
	}

	public List<Cliente> findAllClientes() throws SQLException {
		List<Cliente> clienteList = new ArrayList<Cliente>();
		Statement st = DatabaseManager.getConnection().createStatement();
		try {
			ResultSet rs = st.executeQuery("SELECT ID,NOME , EMAIL, TELEFONE , TIPOPESSOA , CPFCNPJ FROM CLIENTES WHERE STATUS != '"+Cliente.STATUS_PENDENTE_EXCLUSAO+"'");

			try {

				while (rs.next()) {
					Cliente cliente = new Cliente();
					cliente.id = rs.getString(1);
					cliente.nome = rs.getString(2);
					cliente.email = rs.getString(3);
					cliente.telefone = rs.getString(4);
					cliente.tipoPessoa = rs.getString(5);
					cliente.cpfCnpj = rs.getString(6);
					clienteList.add(cliente);

				}
				return clienteList;

			} finally {
				rs.close();
			}

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;
		} finally {
			st.close();
		}

	}

	public List<Cliente> findAllClientesByStatus() throws SQLException {
		List<Cliente> clienteList = new ArrayList<Cliente>();
		try {
			PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
					"SELECT ID,NOME , EMAIL, TELEFONE , TIPOPESSOA , CPFCNPJ , STATUS FROM CLIENTES WHERE STATUS = ? OR STATUS = ? OR STATUS = ?");
			ps.setString(1, Cliente.STATUS_PENDENTE);
			ps.setString(2, Cliente.STATUS_PENDENTE_ATUALIZACAO);
			ps.setString(3, Cliente.STATUS_PENDENTE_EXCLUSAO);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Cliente cliente = new Cliente();
				cliente.id = rs.getString(1);
				cliente.nome = rs.getString(2);
				cliente.email = rs.getString(3);
				cliente.telefone = rs.getString(4);
				cliente.tipoPessoa = rs.getString(5);
				cliente.cpfCnpj = rs.getString(6);
				cliente.status = rs.getString(7);
				clienteList.add(cliente);
			}
			ps.close();
			rs.close();
			return clienteList;

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;
		}
	}

	public int findClienteByCnpjCpf(String cpfCnpj) throws SQLException {

		Statement st = DatabaseManager.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT COUNT(1) FROM CLIENTES WHERE CPFCNPJ = '" + cpfCnpj + "'");
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
		Statement st = DatabaseManager.getConnection().createStatement();
		try {
			ResultSet rs = st.executeQuery("SELECT MAX(ID) FROM CLIENTES");
			try {

				while (rs.next()) {

					cliente.id = rs.getString(1);
				}

				return cliente;

			} finally {
				rs.close();
			}

		} catch (Exception e) {
			Vm.debug(e.getMessage());
			throw e;
		} finally {
			st.close();
		}
	}

}
