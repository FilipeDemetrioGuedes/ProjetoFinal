package service;

import java.sql.SQLException;

import dao.ClienteDAO;
import domain.Cliente;
import totalcross.ui.dialog.MessageBox;
import util.ValidadorCpfCnpj;
import util.ValidadorEmail;

public class ClienteService {

	private static ClienteService instance;

	public static ClienteService getInstance() {
		if (instance == null) {
			instance = new ClienteService();
		}
		return instance;
	}

	public boolean inserirCliente(Cliente cliente) throws Exception {
		if (!validateFields(cliente))
			throw new Exception("Algum dos Campos obrigat�rio est� Invalido!");

		if (cliente == null)
			return false;
		if (ClienteDAO.getInstance().insertCliente(cliente)) {
			return true;

		}
		return false;

	}

	public Cliente createDomain(String nome, String email, String telefone, String cpfCnpj, String tipoPessoa) {

		Cliente cliente = new Cliente();

		cliente.id = cpfCnpj;
		cliente.nome = nome;
		cliente.email = email;
		cliente.telefone = telefone;
		cliente.tipoPessoa = tipoPessoa;
		cliente.cpfCnpj = cpfCnpj;
		cliente.status = Cliente.STATUS_PENDENTE;
		return cliente;
	}

	public boolean validateUpdate(String telefone) {
		if (telefone.isEmpty()) {
			return false;
		}
		if (telefone.length() < 11) {
			new MessageBox("Aten��o", "Campo Telefone n�o pode ser vazio e deve conter 11 Digitos!").popup();
			return false;
		}
		return true;
	}

	public boolean validateFields(Cliente cliente) throws SQLException {
		if (cliente.nome.isEmpty()) {
			new MessageBox("Aten��o", "Digite um Nome!").popup();
			return false;
		}
		if (cliente.telefone.isEmpty() || cliente.telefone.length() < 11) {
			new MessageBox("Aten��o", "Campo Telefone n�o pode ser vazio e deve conter 11 Digitos!").popup();
			return false;
		}
		if (cliente.tipoPessoa.isEmpty()) {
			new MessageBox("Aten��o", "Escolha o Tipo de Pessoa").popup();
			return false;
		} 
		if(!cliente.email.isEmpty()) {
			if(!ValidadorEmail.validaEmail(cliente.email)) {
				new MessageBox("Aten��o", "Email Invalido!").popup();
				return false;
			}
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
