package net.raysforge.restdb;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.raysforge.vfs.VFS;

public class StartRestDBServer {

	public static void main(String[] args) throws IOException {

		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.WARNING);
		
		VFS vfs = new VFS();

		try (ServerSocket ss = new ServerSocket(8080)) {
			// InetAddress.getLocalHost() 
			System.out.println("Ready on: " + ss.getLocalPort());
			while (true) {
				new Thread(new RestDBHttpHandler(ss.accept(), vfs)).start();
			}
		}
	}

}
