package ui;

import dao.ClienteDAO;
import domain.Cliente;
import service.ClienteService;
import sync.ClienteRestApi;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import util.Images;

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
	private ImageControl back;
	private boolean atualizando;

	public IncluirClientesWindow() {
		this.atualizando = false;
		editNome = new Edit();
		editTelefone = new Edit("(99)99999-9999");
		editTelefone.setMode(Edit.NORMAL, true);
		editTelefone.setValidChars("0123456789");
		editEmail = new Edit();
		editEmail.caption = "exemplo@email.com";
		cbTipoPessoa = new ComboBox();
		cbTipoPessoa.add("FISICA");
		cbTipoPessoa.add("JURIDICA");

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
		back = new ImageControl(Images.background);
		back.scaleToFit = true;
		back.centerImage = true;
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
		if ("JURIDICA".equals(cliente.tipoPessoa)) {
			editCnpj.setText(cliente.cpfCnpj);
			editCnpj.setEditable(false);
		} else {
			editCpf.setText(cliente.cpfCnpj);
			editCpf.setEditable(false);
		}

	}

	@Override
	public void initUI() {
		add(back,LEFT,TOP,FILL,FILL);
		add(new Label("Nome"), LEFT + 10, TOP + 10);
		add(editNome, LEFT + 10, AFTER + 5, FILL - 10, PREFERRED);
		add(new Label("Telefone"), LEFT + 10, AFTER + 10);
		add(editTelefone, LEFT + 10, AFTER + 5);
		add(new Label("Email"), LEFT + 10, AFTER + 10);
		add(editEmail, LEFT + 10, AFTER + 5, FILL - 10, PREFERRED);
		add(new Label("Tipo de Pessoa"), LEFT + 10, AFTER + 10);
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
			if (ClienteService.getInstance().inserirCliente(cliente)) {
				MessageBox mbInserido = new MessageBox("Info", "Cliente Inserido!");
				mbInserido.setBackColor(Color.BLUE);
				mbInserido.popup();
				unpop();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void atualizarCliente() {
		try {
			Cliente cliente = screenToDomain();
			if (!ClienteService.getInstance().validateUpdate(cliente.telefone)) {
				new MessageBox("Aten??o", "Campo Telefone n?o pode ser vazio e deve conter 11 Digitos!").popup();
				throw new Exception("Campos Invalidos!");
			}
			
			cliente.status = cliente.STATUS_PENDENTE_ATUALIZACAO;
			if (clienteDAO.atualizarCliente(cliente, clienteClicado)) {
				MessageBox mbAtualizar = new MessageBox("Info", "Cliente Atualizado!" );
				mbAtualizar.setBackColor(Color.BLUE);
				mbAtualizar.popup();
			}
			unpop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void excluirCliente() {
		try {
			Cliente cliente = screenToDomain();
			if (clienteClicado == null)
				return;
			MessageBox mb = new MessageBox("Info!", "Confirma Exclus?o?", new String[] { "Sim!", "N?o!" });
			mb.setBackColor(Color.BLUE);
			mb.popup();
			
			if (mb.getPressedButtonIndex() == 0 ) {
				clienteClicado.status = Cliente.STATUS_PENDENTE_EXCLUSAO;
				
				if (clienteDAO.atualizarCliente(clienteClicado,clienteClicado)) {
					MessageBox mbExcluir = new MessageBox("info", "Cliente Excluido");
					mbExcluir.setBackColor(Color.BLUE);
					mbExcluir.popup();
				} 
			}
			unpop();
		
			
//			if (mb.getPressedButtonIndex() == 0) {
//				ClienteRestApi clienteApi = new ClienteRestApi();
//				if (clienteApi.deletaWeb(clienteClicado)) {
//					if (clienteDAO.excluirCliente(clienteClicado)) {
//						new MessageBox("info", "Cliente Excluido").popup();
//					}
//					unpop();
//				} else {
//					MessageBox mb2 = new MessageBox("Info!",
//							"N?o foi Possivel Excluir na Web.Confirma Exclus?o no App?",
//							new String[] { "Sim!", "N?o!" });
//					mb2.popup();
//					if (mb2.getPressedButtonIndex() == 0) {
//						if (clienteDAO.excluirCliente(clienteClicado)) {
//							new MessageBox("info", "Cliente Excluido").popup();
//							
//						}
//						unpop();
//					}
				

			

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private Cliente screenToDomain() throws Exception {
		String nome = editNome.getText();
		String email = editEmail.getText();
		String telefone = editTelefone.getTextWithoutMask();

		String tipoPessoa = (String) cbTipoPessoa.getSelectedItem();
		String documento = "";
		if ("JURIDICA".equals(tipoPessoa)) {
			documento = editCnpj.getTextWithoutMask();
		} else {
			documento = editCpf.getTextWithoutMask();

		}

		Cliente cliente = ClienteService.getInstance().createDomain(nome, email, telefone, documento, tipoPessoa);
		return cliente;
	}

	public void visibilidade() {
		String tipoPessoa = (String) cbTipoPessoa.getSelectedItem();

		if ("JURIDICA".equals(tipoPessoa)) {
			editCnpj.setVisible(true);
			editCpf.setVisible(false);
		} else {
			editCnpj.setVisible(false);
			editCpf.setVisible(true);

		}
		repaintNow();
	}

}
