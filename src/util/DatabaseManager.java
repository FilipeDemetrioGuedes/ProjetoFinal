package util;

import java.sql.SQLException;

import totalcross.db.sqlite.SQLiteUtil;
import totalcross.sql.Connection;
import totalcross.sql.Statement;
import totalcross.sys.Settings;
import totalcross.sys.Vm;

public class DatabaseManager {

	public static SQLiteUtil sqLiteUtil;

	static {
		try {
			sqLiteUtil = new SQLiteUtil(Settings.appPath, "projeto.db");
		} catch (SQLException e) {
			Vm.debug(e.getMessage());
		}

	}

	public static Connection getConnection() throws SQLException {
		return sqLiteUtil.con();
	}
	
	public static void loadTabelas() throws SQLException {
		Statement st = getConnection().createStatement();
		st.execute("CREATE TABLE IF NOT EXISTS CLIENTES (IDCLIENTE INT , NOME VARCHAR , EMAIL VARCHAR, TELEFONE VARCHAR , TIPOPESSOA VARCHAR , DOCUMENTO VARCHAR , STATUS VARCHAR ,PRIMARY KEY (IDCLIENTE))");
		st.close();
	}
	
}