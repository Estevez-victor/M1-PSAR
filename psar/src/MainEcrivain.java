import java.io.IOException;

import srcs.client.Ecrivain;

public class MainEcrivain {

	public static void main(String[] args) {
		try {
			new Ecrivain (4999).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
