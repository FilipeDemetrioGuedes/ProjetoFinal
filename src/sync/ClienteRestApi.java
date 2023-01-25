package sync;

import domain.Cliente;
import totalcross.io.ByteArrayStream;
import totalcross.io.IOException;
import totalcross.net.HttpStream;
import totalcross.net.URI;
import totalcross.sys.Vm;

public class ClienteRestApi {
	
	public String conecta(Cliente cliente) {
		
		String clienteJson = "{ \"nome\":\"" + cliente.nome + "\", \"cpfCnpj\":\"" 
		+ cliente.documento + "\" , \"telefone\":\"" + 
				cliente.telefone +"\" , \"tipoPessoa\":\""+ cliente.tipoPessoa +"\", \"email\":\"" + cliente.email +"\" }";
				

	 String msg = "";

     try {
         HttpStream.Options options = new HttpStream.Options();
         //define os tipos de conexoes , post = envio , get = receber , delete = excluir , alterar  = put
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
}
