package srcs.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;


public class TextEditor implements Runnable{
	private Socket sock;
	private BufferedReader userInput;
	private PrintWriter pw;
	private final Serveur serveur;
	public TextEditor(Socket sock, Serveur serveur) throws IOException {
		this.sock=sock;
		this.serveur=serveur;
		userInput=new BufferedReader(new InputStreamReader(sock.getInputStream()));
		pw=new PrintWriter(sock.getOutputStream());
	}

	@Override
	public void run() {
		boolean commandeInconnue;
		boolean info=false;
		System.out.println("-Thread for client start-");
		String command;
		try {
			while(!Thread.currentThread().isInterrupted()) {
				if(info) {
					pw.println("Commandes: ");
					pw.println(" \"read\": Visualiser le texte");
					pw.println(" \"edit\": Remplacer une ligne");
					pw.println(" \"insert\" Inserer une ligne");
					pw.println(" \"remove\": supprimer une ligne");
					pw.println(" \"shutdown\": Ferme l'editeur ");
					pw.flush();
				}
				pw.println("Entrez une commande:");
				pw.println("waiting_entry");
				pw.flush();
				commandeInconnue=true;
				info=false;
				command=userInput.readLine();

				if(command==null || command.equals("shutdown")) {
					ShutDown();
					commandeInconnue=false;
				}
				if(command!=null && command.equals("info")) {
					info=true;
					commandeInconnue=false;
				}
				if(command!=null && command.equals("edit")) {
					Edit();
					commandeInconnue=false;
				}
				if(command!=null && command.equals("insert")) {
					Insert();
					commandeInconnue=false;
				}
				if(command!=null && command.equals("remove")) {
					Remove();
					commandeInconnue=false;
				}
				if(command!=null && command.equals("read")) {
					Read();
					commandeInconnue=false;
				}

				if(commandeInconnue) {
					pw.println("Commande inconnue, entrez \"info\" pour voir les commandes.");
					pw.flush();
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-Thread for client end-");
	}

	private void Read() {
		int i = 1;
		LinkedList<String> doc = serveur.getDoc();
		synchronized (doc) {
			for(String lines : doc) {
				if(i<10) {
					pw.print(" ");
				}
				if(i<100) {
					pw.print(" ");
				}
				pw.print(i);
				pw.print('|');
				pw.println(lines);
				pw.flush();
				i++;
			}
		}
	}


	private void Remove() throws IOException {
		int size;
		LinkedList<String> doc = serveur.getDoc();
		synchronized (doc) {
			size=doc.size();
		}
		if(size==0) {
			pw.println("Le texte est deja vide.");
			pw.flush();
			return;
		}
		Integer at = null;
		while(at==null || at-1>=size|| at<=0) {
			pw.println("Quelle ligne?:");
			pw.println("waiting_entry");
			pw.flush();
			try {
				at = Integer.parseInt(userInput.readLine());
			}catch (NumberFormatException e) {
				pw.println("Veuillez entrer un index de ligne.");
				pw.flush();
			}
			if(at!=null && at-1>=size) {
				pw.println("Cette ligne n'existe pas.");
				pw.flush();
				synchronized (doc) {
					size=doc.size();
				}
			}
			if(at!=null && at<=0) {
				pw.println("Il nous faut un numero positif de ligne.");
				pw.flush();
				synchronized (doc) {
					size=doc.size();
				}
			}
		}
		doc = serveur.getDoc();
		synchronized (doc) {
			doc.remove(at-1);
		}
	}

	private void Insert() throws IOException {
		int at=0;
		boolean catched = false;
		while(at<=0) {
			pw.println("Quelle ligne?:");
			pw.println("waiting_entry");
			pw.flush();
			try {
				at = Integer.parseInt(userInput.readLine());
			}catch (NumberFormatException e) {
				pw.println("Veuillez entrer un index de ligne.");
				pw.flush();
				catched=true;
			}
			if(!catched && at<=0) {
				pw.println("Il nous faut un numero positif de ligne.");
				pw.flush();
			}
			catched=false;
		}
		pw.println("Votre texte:");
		pw.println("waiting_entry");
		pw.flush();
		String line= userInput.readLine();
		LinkedList<String> doc = serveur.getDoc();
		synchronized (doc) {
			int sizeNow=doc.size();
			while(at-1>sizeNow) {
				doc.addLast("");
				sizeNow++;
			}
			doc.add(at-1, line);
		}
	}

	private void Edit() throws IOException {
		int at=0;
		while(at<=0) {
			pw.println("Quelle ligne?:");
			pw.println("waiting_entry");
			pw.flush();
			try {
				at = Integer.parseInt(userInput.readLine());
			}catch (NumberFormatException e) {
				pw.println("Veuillez entrer un index de ligne.");
				pw.flush();
			}
			if(at<=0) {
				pw.println("Il nous faut un numero positif de ligne.");
				pw.flush();
			}

		}
		pw.println("Votre texte:");
		pw.println("waiting_entry");
		pw.flush();
		String line= userInput.readLine();
		LinkedList<String> doc = serveur.getDoc();
		synchronized (doc) {
			int sizeNow=doc.size();
			while(at>sizeNow) {
				doc.addLast("");
				sizeNow++;
			}
			doc.set(at-1, line);
		}
	}

	private void ShutDown() throws IOException {
		Thread.currentThread().interrupt();
		sock.close();
	}

}
