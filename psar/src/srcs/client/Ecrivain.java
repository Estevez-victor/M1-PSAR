package srcs.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Ecrivain {
	private boolean working;
	private Socket s;
	private int port;
	private PrintWriter pw;
	private BufferedReader userKeyboard;
	private BufferedReader fromServer;
	public Ecrivain(int port) {
		this.port=port;
	}

	public void start() throws UnknownHostException, IOException{

		working=true;
		String command="shutdown";
		s = new Socket("localhost",port);
		userKeyboard = new BufferedReader(new InputStreamReader(System.in));
		fromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(s.getOutputStream());
		String text = "";
		while(working) {
			while(text!=null&&!text.equals("waiting_entry")) {
				text=fromServer.readLine();
				if(text!=null&&!text.equals("waiting_entry")) {
					System.out.println(text);
				}
			}
			text="";
			command=userKeyboard.readLine();
			pw.println(command);
			pw.flush();
			if(command==null||command.equals("shutdown")) {
				working=false;
			}
		}
		System.out.println("eteinte de l'écrivain");
		s.close();
	}

}