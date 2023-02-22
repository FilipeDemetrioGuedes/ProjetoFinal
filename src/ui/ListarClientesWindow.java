package ui;

import java.sql.SQLException;
import java.util.List;

import dao.ClienteDAO;
import domain.Cliente;
import service.ClienteService;
import sync.ClienteRestApi;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.ScrollContainer;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import util.Images;

public class ListarClientesWindow extends Window {

	private ScrollContainer listaClientes;
	private Button btIncluir;
	private Button btVoltar;
	private ClienteDAO clienteDAO;
	private List<Cliente> clienteList;
	private Button btSincronizar;
	private Button btBuscar;
	private ImageControl back;

	public ListarClientesWindow() throws SQLException {
		listaClientes = new ScrollContainer();
		btIncluir = new Button("Incluir Clientes");
		btVoltar = new Button("Voltar");
		btSincronizar = new Button("Sincronizar");
		btBuscar = new Button("Buscar Clientes");
		clienteDAO = new ClienteDAO();
		clienteList = clienteDAO.findAllClientes();
		back = new ImageControl(Images.background);
		back.scaleToFit = true;
		back.centerImage = true;

	}

	public void loadList() throws SQLException {
		int index = 0;
		for (Cliente cliente : clienteList) {
			String[] dados = clienteToArray(cliente);
			Container clienteContainer = new Container();
			clienteContainer.appId = index++;
			clienteContainer.setBorderStyle(BORDER_SIMPLE);
			listaClientes.add(clienteContainer, LEFT + 10, AFTER + 3, listaClientes.getWidth() - 15, 75);
			for (int dadosIndex = 0; dadosIndex < 5; dadosIndex++) {
				int horizontalPosition = dadosIndex % 2 == 0 ? LEFT + 10 : RIGHT - 10;
				int verticalPosition = dadosIndex % 2 == 0 ? AFTER : SAME;
				clienteContainer.add(new Label(dados[dadosIndex]), horizontalPosition, verticalPosition);
			}

		}

	}

	private String[] clienteToArray(Cliente cliente) {
		String[] dadosArray = new String[5];
		dadosArray[0] = ("Nome: " + cliente.nome);
		dadosArray[1] = ("Email: " + cliente.email);
		dadosArray[2] = ("Fone: " + cliente.telefone);
		dadosArray[3] = ("Pessoa: " + cliente.tipoPessoa);
		dadosArray[4] = ("CPF/CNPJ: " + cliente.cpfCnpj);

		return dadosArray;
	}

	private void reloadList() throws SQLException {
		clienteList = clienteDAO.findAllClientes();
		listaClientes = new ScrollContainer();
		removeAll();
		montaTela();
		reposition();
	}

	private void montaTela() {
		add(back, LEFT, TOP, FILL, FILL);
		add(listaClientes, LEFT, TOP, FILL, getScrollContainerSize());
		try {
			loadList();
		} catch (SQLException e) {
			Vm.debug(e.getMessage());
		}

		add(btIncluir, LEFT + 20, BOTTOM - 10);
		add(btSincronizar, AFTER + 20, BOTTOM - 10);
		add(btBuscar, AFTER + 20, BOTTOM - 10);
		add(btVoltar, AFTER + 20, BOTTOM - 10);
	}

	private int getScrollContainerSize() {
		int size = FILL;
		size = size > Settings.screenWidth ? Settings.screenHeight - 10 : size;
		return size;
	}

	@Override
	public void initUI() {
		add(back, LEFT, TOP, FILL, FILL);
		montaTela();
	}

	@Override
	public void popup() {
		setRect(0, 0, Settings.screenWidth, Settings.screenHeight);
		super.popup();
	}

	public void inserirWebClient(List<Cliente> clienteList) {
		try {
			for (Cliente cliente : clienteList) {
				cliente.status = "OK";
				try {
					if (ClienteService.getInstance().inserirCliente(cliente)) {
						ClienteRestApi clienteApi = new ClienteRestApi();
						clienteApi.atualizaWeb(cliente);
					}
				} catch (Exception e) {
					new MessageBox("Atenção", " Nao foi Possivel Cadastrar o Cliente").popup();
				}
			}
			reloadList();
		} catch (Exception e) {
			Vm.debug(e.getMessage());
		}

	}
	
	private MessageBox criaMensagemRetorno(String msg, String retorno) {
		MessageBox mb;
	    if (!"Ok".equals(retorno) && !"Excluido".equals(retorno) && !"Atualizado".equals(retorno)) {
	         mb = new MessageBox("Erro", msg);
	    } else {
	         mb = new MessageBox("Info", msg);
	         mb.setBackColor(Color.BLUE);
	    }
	    return mb;
	}
	
	@Override
	public void onEvent(Event event) {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btIncluir) {
				IncluirClientesWindow incluirClientes = new IncluirClientesWindow();
				incluirClientes.popup();
				try {
					reloadList();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				reposition();

			} else if (event.target == btVoltar) {
				this.unpop();
			} else if (event.target == btSincronizar) {
				ClienteRestApi clienteApi = new ClienteRestApi();
				List<Cliente> clienteList;
				try {
					clienteList = ClienteDAO.getInstance().findAllClientesByStatus();
					String retorno = "";

					for (Cliente cliente : clienteList) {
						String msg = "";
						if (cliente.STATUS_PENDENTE_ATUALIZACAO.equals(cliente.status)) {
							cliente.status = cliente.STATUS_ATUALIZADO;
							retorno = clienteApi.atualizaWeb(cliente);

						}
						if (cliente.STATUS_PENDENTE_EXCLUSAO.equals(cliente.status)) {
							retorno = clienteApi.deletaWeb(cliente);

						}
						if (cliente.STATUS_PENDENTE.equals(cliente.status)) {
							cliente.status = cliente.STATUS_OK;
							retorno = clienteApi.enviaWeb(cliente);
						}
						if ("Ok".equals(retorno)) {
							cliente.status = cliente.STATUS_OK;
							ClienteDAO.getInstance().atualizarCliente(cliente, cliente);
							msg = "Cliente Enviado para Web";
							
						} else if ("Atualizado".equals(retorno)) {
						cliente.status = cliente.STATUS_ATUALIZADO;
						ClienteDAO.getInstance().atualizarCliente(cliente, cliente);
							msg = "Cliente Atualizado na Web";
							
						} else if ("Excluido".equals(retorno)) {
							ClienteDAO.getInstance().excluirCliente(cliente);
							msg = "Cliente Excluido na Web";

						} else {
							msg = "Erro Conexao Servidor Web";

						}
						MessageBox mbInseridoWeb = criaMensagemRetorno( msg , retorno);
						mbInseridoWeb.popup();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			else if (event.target == btBuscar) {

				ClienteRestApi clienteApi = new ClienteRestApi();
				List<Cliente> retorno = clienteApi.buscaWeb();
				inserirWebClient(retorno);
				MessageBox mbCarregado = new MessageBox("Info", "Base Web Carregada");
				mbCarregado.setBackColor(Color.BLUE);
				mbCarregado.popup();

			}

			break;
		case PenEvent.PEN_DOWN:
			if (event.target instanceof Container && !(event.target instanceof Window)) {
				Container c = (Container) event.target;
				Cliente cliente = clienteList.get(c.appId);
				if (cliente == null)
					return;
				IncluirClientesWindow incluirClientes = new IncluirClientesWindow(cliente);
				incluirClientes.popup();
				try {
					reloadList();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
		super.onEvent(event);
	}
}
