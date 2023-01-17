package ui;

import java.sql.SQLException;

import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.MainWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import util.DatabaseManager;

public class MainMenu extends MainWindow {

	private Button btClientes;

	public MainMenu() throws SQLException {
		DatabaseManager.loadTabelas();
		btClientes = new Button("Clientes");

	}

	@Override
	public void initUI() {
		add(btClientes, LEFT + 10, TOP + 10, FILL - 10, PREFERRED);

	}

	@Override
	public void onEvent(Event event) {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btClientes) {
				ListarClientesWindow clienteWindow = null;
				try {
					clienteWindow = new ListarClientesWindow();
				} catch (SQLException e) {
					Vm.debug(e.getMessage());
				}
				clienteWindow.popup();
			}
			break;

		default:
			break;
		}
		super.onEvent(event);
	}

}
