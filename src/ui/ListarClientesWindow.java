package ui;

import java.sql.SQLException;
import java.util.List;

import dao.ClienteDAO;
import domain.Cliente;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Label;
import totalcross.ui.ScrollContainer;
import totalcross.ui.Window;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;

public class ListarClientesWindow extends Window {

	private ScrollContainer listaClientes;
	private Button  btIncluir;
	private Button btVoltar;
	private ClienteDAO clienteDAO;
	private List<Cliente> clienteList;
	
	public ListarClientesWindow() throws SQLException {
		listaClientes = new ScrollContainer();
		btIncluir = new Button("Incluir Clientes");
		btVoltar = new Button("Voltar");
		clienteDAO = new ClienteDAO();
		clienteList = clienteDAO.findAllClientes();
		
	}
	
	
	public void loadList() throws  SQLException {
		int index =0;
		for (Cliente cliente : clienteList) {
			String[] dados = clienteToArray(cliente);
			Container clienteContainer = new Container();
			clienteContainer.appId = index++;
			clienteContainer.setBorderStyle(BORDER_SIMPLE);
			listaClientes.add(clienteContainer, LEFT + 10, AFTER + 3, listaClientes.getWidth() -15, 75);
			for (int dadosIndex = 0; dadosIndex < 5; dadosIndex++) {
				int horizontalPosition = dadosIndex % 2 == 0 ? LEFT + 10 : RIGHT - 10;
				int verticalPosition = dadosIndex % 2 == 0 ? AFTER  : SAME;
				clienteContainer.add(new Label(dados[dadosIndex]), horizontalPosition, verticalPosition);
			}

		}

	}

	private String[] clienteToArray(Cliente cliente) {
		String[] dadosArray = new String[5];
		dadosArray[0] = ("Nome: " + cliente.nome);
		dadosArray[1] = ("Email: " + cliente.email);
		dadosArray[2] = ("Fone: " + cliente.telefone);
		dadosArray[3] = ("Pessoa: " +cliente.tipoPessoa);
		dadosArray[4] = ("CPF/CNPJ: " +cliente.documento);
		

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
			add(listaClientes , LEFT , TOP , FILL , getScrollContainerSize());
			try {
				loadList();
			} catch (SQLException e) {
				Vm.debug(e.getMessage());
			}
			add(btIncluir, CENTER -20 , BOTTOM -10);
			add(btVoltar, AFTER +20, BOTTOM -10);
		}
	private int getScrollContainerSize() {
		int size = (clienteList.size() * 50) + (clienteList.size() * 3) + 10;
		size = size > Settings.screenWidth ? Settings.screenHeight - 10 : size;
		return size;
	}
	@Override
	public void initUI() {
		montaTela();
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
			if(event.target == btIncluir) {
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
			}
			
			break;
		case PenEvent.PEN_DOWN:
			if(event.target instanceof Container && !(event.target instanceof Window)) {
				Container c = (Container) event.target;
				Cliente cliente = clienteList.get(c.appId);
				if(cliente == null) return;
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
