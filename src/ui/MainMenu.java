package ui;

import java.sql.SQLException;

import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.ImageControl;
import totalcross.ui.MainWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import util.DatabaseManager;
import util.Images;

public class MainMenu extends MainWindow {

	private Button btClientes;
	private Button btSair;
	
	private ImageControl back;
	
	
	public MainMenu() throws SQLException {
		DatabaseManager.loadTabelas();
		Images.loadImages();
		btClientes = new Button("Meus Clientes");
		btSair = new Button("Sair");
		back = new ImageControl(Images.background);
		back.scaleToFit = true;
		back.centerImage = true;
		
	}

	@Override
	public void initUI() {
		
		add(back,LEFT,TOP,FILL,FILL);
		add(btClientes, LEFT + 10, TOP + 10, FILL - 10, PREFERRED);
		add(btSair, CENTER, BOTTOM -10, 50, PREFERRED);
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
			
			}else if (event.target == btSair) {
				exit(AFTER);
			}
			break;

		default:
			
			break;
		}
		super.onEvent(event);
	}

}
