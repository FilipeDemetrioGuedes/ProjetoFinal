package sync;

import totalcross.io.ByteArrayStream;
import totalcross.io.IOException;
import totalcross.net.HttpStream;
import totalcross.net.URI;

public class ClienteRestApi {
	
	public void conecta() {
		
	

	 String msg = "";

     try {

         HttpStream.Options options = new HttpStream.Options();

         HttpStream httpStream = new HttpStream(new URI("http://localhost:8080/clientes"), options);
         ByteArrayStream bas = new ByteArrayStream(4096);
         bas.readFully(httpStream, 10, 2048);
         String data = new String(bas.getBuffer(), 0, bas.available());

         
         if (httpStream.responseCode == 200){
           
         }
     } catch (IOException e1) {
             msg = "erro";
     }
	}
}
