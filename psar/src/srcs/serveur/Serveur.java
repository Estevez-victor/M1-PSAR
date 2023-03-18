package srcs.serveur;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
public class Serveur implements Runnable{
	private final int port;
	private final LinkedList<String> doc;
	//private String name;
	PrintWriter writer;

	public Serveur(int port) {
		this.port=port;
		doc= new LinkedList<String>();
	}
	@Override
	public void run() {
		try(ServerSocket ss= new ServerSocket(port)){
			ss.setSoTimeout(5000);
			System.out.println("(S) waiting for client");
			while(!Thread.currentThread().isInterrupted()) {
				try {
					Socket s=ss.accept();
					System.out.println("(S) Nouvelle connection.");
					new Thread(new TextEditor(s,this)).start();
				}catch (SocketTimeoutException e) {
					Save();
					System.out.println("(S) Save");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Save();
		System.out.println("(S) fin.");
	}

	public LinkedList<String> getDoc() {
		return doc;
	}
	private void Save() {
		synchronized(doc) {
			try {
				writer = new PrintWriter(new FileWriter("/home/vicman/Documents/monDoc.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(String s: doc) {
				writer.println(s);
			}
			writer.flush();
		}
	}

	public void remove() {

	}
	public void add() {

	}

}