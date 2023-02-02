package sync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domain.Cliente;
import totalcross.io.ByteArrayStream;
import totalcross.io.IOException;
import totalcross.json.JSONArray;
import totalcross.json.JSONFactory;
import totalcross.json.JSONObject;
import totalcross.net.HttpStream;
import totalcross.net.URI;
import totalcross.sys.Vm;

public class ClienteRestApi {

	public String enviaWeb(Cliente cliente) {

		String clienteJson = "{ \"nome\":\"" + cliente.nome + "\", \"cpfCnpj\":\"" + cliente.cpfCnpj
				+ "\" , \"telefone\":\"" + cliente.telefone + "\" , \"tipoPessoa\":\"" + cliente.tipoPessoa
				+ "\", \"id\":\"" + cliente.id + "\", \"status\":\"" + cliente.status + "\", \"email\":\"" + cliente.email + "\" }";

		String msg = "";

		try {
			HttpStream.Options options = new HttpStream.Options();
			// define os tipos de conexoes , post = envio , get = receber , delete = excluir
			// , alterar = put
			options.httpType = totalcross.net.HttpStream.POST;
			options.doPost = true;
			options.doGet = false;
			options.postData = clienteJson;
			options.postHeaders.put("Content-Type", "application/json;charset=utf-8");

			HttpStream httpStream = new HttpStream(new URI("http://localhost:8080/clientes"), options);

			Vm.debug("");
			ByteArrayStream bas = new ByteArrayStream(4096);
			bas.readFully(httpStream, 10, 2048);
			String data = new String(bas.getBuffer(), 0, bas.available());
			return data;

//         if (httpStream.responseCode == 200){
//           
//         }
		} catch (IOException e1) {
			msg = "erro";
			return msg;
		}
	}
	
	public String atualizaWeb(Cliente cliente) {

		String clienteJson = "{ \"nome\":\"" + cliente.nome + "\", \"cpfCnpj\":\"" + cliente.cpfCnpj
				+ "\" , \"telefone\":\"" + cliente.telefone + "\" , \"tipoPessoa\":\"" + cliente.tipoPessoa
				+ "\", \"status\":\"" + cliente.status + "\", \"email\":\"" + cliente.email + "\"  }";

		String msg = "";

		try {
			HttpStream.Options options = new HttpStream.Options();
			// define os tipos de conexoes , post = envio , get = receber , delete = excluir
			// , alterar = put
			options.httpType = totalcross.net.HttpStream.POST;
			options.doPost = true;
			options.doGet = false;
			options.postData = clienteJson;
			options.postHeaders.put("Content-Type", "application/json;charset=utf-8");

			HttpStream httpStream = new HttpStream(new URI("http://localhost:8080/clientes/"+cliente.cpfCnpj), options);

			Vm.debug("");
			ByteArrayStream bas = new ByteArrayStream(4096);
			bas.readFully(httpStream, 10, 2048);
			String data = new String(bas.getBuffer(), 0, bas.available());
			return data;

//         if (httpStream.responseCode == 200){
//           
//         }
		} catch (IOException e1) {
			msg = "erro";
			return msg;
		}
	}

	public List<Cliente> buscaWeb() {

		String msg = "";

		try {
			HttpStream.Options options = new HttpStream.Options();
			// define os tipos de conexoes , post = envio , get = receber , delete = excluir
			// , alterar = put
			options.httpType = totalcross.net.HttpStream.GET;
			options.doPost = false;
			options.doGet = true;
			options.postHeaders.put("Content-Type", "application/json;charset=utf-8");

			HttpStream httpStream = new HttpStream(new URI("http://localhost:8080/clientes/pendentes"), options);

			Vm.debug("");
			ByteArrayStream bas = new ByteArrayStream(4096);
			bas.readFully(httpStream, 10, 2048);
			String data = new String(bas.getBuffer(), 0, bas.available());
			JSONArray clientesArray = new JSONArray(data);
			List<Cliente> clienteList = new ArrayList();
			for (int i = 0; i < clientesArray.length(); i++) {
				JSONObject clienteJson = clientesArray.getJSONObject(i);
				Cliente cliente = JSONFactory.parse(clienteJson, Cliente.class);
				clienteList.add(cliente);
			}
			return clienteList;

//         if (httpStream.responseCode == 200){
//           
//         }
		} catch (IOException e1) {
			msg = "erro";
			return null;
		} catch (Exception e) {
			return null;
		}

	}
}
