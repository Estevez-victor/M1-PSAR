package srcs;

import java.io.IOException;
import java.net.UnknownHostException;

import srcs.client.Ecrivain;
import srcs.serveur.Serveur;


public class MainDeTest {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {		
		try {
			new Thread(new Serveur(4999)).start();
			Ecrivain e = new Ecrivain (4999);
			//Ecrivain e2= new Ecrivain(4999);
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.start();
			//e2.start();

			//PrintWriter writer=new PrintWriter(new FileWriter("/home/vicman/Documents/monDoc2.txt"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
