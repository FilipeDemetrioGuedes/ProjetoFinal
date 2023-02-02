package service;

import java.sql.SQLException;

import dao.ClienteDAO;
import domain.Cliente;
import totalcross.ui.dialog.MessageBox;
import util.ValidadorCpfCnpj;

public class ClienteService {

	private static ClienteService instance;
	
	
	public static ClienteService getInstance() {
		if( instance == null) {
			instance = new ClienteService();
		}
		return instance;
	}
	
	public boolean inserirCliente(Cliente cliente) throws Exception {
		if (!validateFields(cliente))
			throw new Exception("Campos Invalidos!");

		if (cliente == null)
			return false;
			if (ClienteDAO.getInstance().insertCliente(cliente)) {
				return true;
		
			} return false;
			
			
	}
	
	private boolean validateUpdate(String telefone) {
		if (telefone.isEmpty()) {
			new MessageBox("Aten��o", "Digite um Telefone!").popup();
			return false;
		}
		return true;
	}
	private boolean validateFields(Cliente cliente) throws SQLException {
		if (cliente.nome.isEmpty()) {
			new MessageBox("Aten��o", "Digite um Nome!").popup();
			return false;
		}
		if (cliente.telefone.isEmpty()) {
			new MessageBox("Aten��o", "Digite um Telefone!").popup();
			return false;
		}
		if (cliente.getTipoPessoa() == null) {
			new MessageBox("Aten��o", "Escolha o Tipo de Pessoa").popup();
			return false;
		}
		if (cliente.cpfCnpj.isEmpty()) {

			new MessageBox("Aten��o", "Digite o Numero do Seu Documento!").popup();
			return false;
		}
		if ("JURIDICA".equals(cliente.getTipoPessoa())) {
			if (!ValidadorCpfCnpj.isCNPJ(cliente.cpfCnpj)) {
				new MessageBox("Aten��o", "Cnpj Invalido!").popup();
				return false;
			}

		} else {
			if (!ValidadorCpfCnpj.isCPF(cliente.cpfCnpj)) {
				new MessageBox("Aten��o", "Cpf Invalido!").popup();
				return false;
			}

		}
		int count = ClienteDAO.getInstance().findClienteByCnpjCpf(cliente.cpfCnpj);
		if (count > 0) {
			new MessageBox("Aten��o", "Cliente ja Cadastrado!").popup();
			return false;
		}
		return true;
	}
}
