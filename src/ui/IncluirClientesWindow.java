package ui;

import java.sql.SQLException;

import dao.ClienteDAO;
import domain.Cliente;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import util.ValidadorCpfCnpj;

public class IncluirClientesWindow extends Window {

	private Edit editNome;
	private Edit editTelefone;
	private Edit editEmail;
	private Edit editCpf;
	private Edit editCnpj;

	private ComboBox cbTipoPessoa;

	private Button btInserir;
	private Button btVoltar;
	private Button btAtualizar;
	private Button btExcluir;
	private ClienteDAO clienteDAO;
	private Cliente clienteClicado;

	private boolean atualizando;

	public IncluirClientesWindow() {
		this.atualizando = false;
		editNome = new Edit();
		editTelefone = new Edit("(99)9999-9999");
		editTelefone.setMode(Edit.NORMAL, true);
		editTelefone.setValidChars("0123456789");
		editEmail = new Edit();
		editEmail.caption = "exemplo@email.com";
		cbTipoPessoa = new ComboBox();
		cbTipoPessoa.add("Física");
		cbTipoPessoa.add("Jurídica");

		editCpf = new Edit("999.999.999-99");
		editCpf.setMode(Edit.NORMAL, true);
		editCpf.setValidChars("0123456789");
		editCnpj = new Edit("99.999.999/9999-99");
		editCnpj.setMode(Edit.NORMAL, true);
		editCnpj.setValidChars("0123456789");

		btInserir = new Button("Inserir");
		btVoltar = new Button("Voltar");
		btAtualizar = new Button("Atualizar");
		btExcluir = new Button("Excluir");

		clienteDAO = new ClienteDAO();
	}

	public IncluirClientesWindow(Cliente cliente) {
		this();
		this.atualizando = true;
		clienteClicado = cliente;
		editNome.setText(cliente.nome);
		editNome.setEditable(false);
		editEmail.setText(cliente.email);
		editTelefone.setText(cliente.telefone);
		cbTipoPessoa.setSelectedItem(cliente.tipoPessoa);
		cbTipoPessoa.setEnabled(false);
		if ("Jurídica".equals(cliente.tipoPessoa)) {
			editCnpj.setText(cliente.documento);
			editCnpj.setEditable(false);
		} else {
			editCpf.setText(cliente.documento);
			editCpf.setEditable(false);
		}
	
		
	}

	@Override
	public void initUI() {

		add(new Label("Nome"), LEFT + 10, TOP + 10);
		add(editNome, LEFT + 10, AFTER + 5, FILL - 10, PREFERRED);
		add(new Label("Telefone"), LEFT + 10, AFTER + 10);
		add(editTelefone, LEFT + 10, AFTER + 5);
		add(new Label("Email"), LEFT + 10, AFTER + 10);
		add(editEmail, LEFT + 10, AFTER + 5, FILL - 10, PREFERRED);
		add(new Label("Escolha o Tipo de Pessoa"), LEFT + 10, AFTER + 10);
		add(cbTipoPessoa, LEFT + 10, AFTER + 5);

		add(new Label("Documento"), LEFT + 10, AFTER + 10);
		add(editCpf, LEFT + 10, AFTER + 5);
		add(editCnpj, SAME, SAME);

		if (atualizando) {
			add(btAtualizar, CENTER - 20, AFTER + 20);
			add(btExcluir, AFTER + 20, SAME);
		} else {
			add(btInserir, CENTER, AFTER + 20);

		}
		add(btVoltar, RIGHT - 20, BOTTOM - 5);

		visibilidade();

	}

	@Override
	public void popup() {
		setRect(0, 0, Settings.screenWidth, Settings.screenHeight);
		super.popup();
	}

	@Override
	public void onEvent(Event event) {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btVoltar) {
				this.unpop();
			} else if (event.target == btInserir) {
				insertCliente();
				

			} else if (event.target == cbTipoPessoa) {
				visibilidade();

			} else if (event.target == btAtualizar) {
				atualizarCliente();
			
			} else if (event.target == btExcluir) {
				excluirCliente();
				
			}
			break;

		default:
			break;
		}
		super.onEvent(event);
	}

	private void insertCliente() {
		try {
			Cliente cliente = screenToDomain();
			if (!validateFields(cliente))
				throw new Exception("Campos Invalidos!");

			if (cliente == null)
				return;
				if (clienteDAO.insertCliente(cliente)) {
					new MessageBox("Info", "Cliente Inserido!").popup();
					;
				}
				unpop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void atualizarCliente() {
		try {
			Cliente cliente = screenToDomain();
			if (!validateUpdate(cliente.telefone))
				throw new Exception("Campos Invalidos!");

			if (clienteDAO.atualizarCliente(cliente, clienteClicado)) {
				new MessageBox("Info", "Cliente Atualizado!").popup();
			}
			unpop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void excluirCliente() {
		try {
			if (clienteClicado == null)
				return;
			if (clienteDAO.excluirCliente(clienteClicado)) {
				new MessageBox("info", "Cliente Excluido").popup();
			}
			unpop();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private Cliente screenToDomain() throws Exception {
		String nome = editNome.getText();
		String email = editEmail.getText();
		String telefone = editTelefone.getText();

		String tipoPessoa = (String) cbTipoPessoa.getSelectedItem();
		String documento = "";
		if ("Jurídica".equals(tipoPessoa)) {
			documento = editCnpj.getTextWithoutMask();
		} else {
			documento = editCpf.getTextWithoutMask();

		}
		
		Cliente cliente = createDomain(nome, email, telefone, documento, tipoPessoa);
		return cliente;
	}

	private Cliente createDomain(String nome, String email, String telefone, String documento, String tipoPessoa) throws SQLException {

		Cliente cliente = new Cliente();
		Cliente ultimoCliente = ClienteDAO.findMaxIdCliente();
		if (ultimoCliente.idCliente > 0) {
			cliente.idCliente = ++ultimoCliente.idCliente;
		} else {
			cliente.idCliente = 1;
		}
		cliente.nome = nome;
		cliente.email = email;
		cliente.telefone = telefone;
		cliente.tipoPessoa = tipoPessoa;
		cliente.documento = documento;
		
		return cliente;
	}
	
	private boolean validateUpdate(String telefone) {
		if (telefone.isEmpty()) {
			new MessageBox("Atenção", "Digite um Telefone!").popup();
			return false;
		}
		return true;
	}
	private boolean validateFields(Cliente cliente) throws SQLException {
		if (cliente.nome.isEmpty()) {
			new MessageBox("Atenção", "Digite um Nome!").popup();
			return false;
		}
		if (cliente.telefone.isEmpty()) {
			new MessageBox("Atenção", "Digite um Telefone!").popup();
			return false;
		}
		if (cbTipoPessoa.getSelectedItem() == null) {
			new MessageBox("Atenção", "Escolha o Tipo de Pessoa").popup();
			return false;
		}
		if (cliente.documento.isEmpty()) {

			new MessageBox("Atenção", "Digite o Numero do Seu Documento!").popup();
			return false;
		}
		if ("Jurídica".equals(cbTipoPessoa.getSelectedItem())) {
			if (!ValidadorCpfCnpj.isCNPJ(cliente.documento)) {
				new MessageBox("Atenção", "Cnpj Invalido!").popup();
				return false;
			}

		} else {
			if (!ValidadorCpfCnpj.isCPF(cliente.documento)) {
				new MessageBox("Atenção", "Cpf Invalido!").popup();
				return false;
			}

		}
		int count = clienteDAO.findClienteByCnpjCpf(cliente.documento);
		if (count > 0) {
			new MessageBox("Atenção", "Cliente ja Cadastrado!").popup();
			return false;
		}
		return true;
	}

	public void visibilidade() {
		String tipoPessoa = (String) cbTipoPessoa.getSelectedItem();

		if ("Jurídica".equals(tipoPessoa)) {
			editCnpj.setVisible(true);
			editCpf.setVisible(false);
		} else {
			editCnpj.setVisible(false);
			editCpf.setVisible(true);

		}
		repaintNow();
	}

}
