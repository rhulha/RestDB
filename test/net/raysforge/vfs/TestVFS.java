package net.raysforge.vfs;

import java.io.IOException;
import java.util.List;

public class TestVFS {
	
	public static void main(String[] args) throws IOException {
		VFS vfs = new VFS();
		
		for (int i = 0; i < 1000; i++) {
			vfs.writeFile("test/test", "name"+i+".txt", "contents"+i);
		}

		for (int i = 0; i < 1000; i++) {
			String c = vfs.readFile("test/test", "name"+i+".txt");
			System.out.println(c);
		}
		
		List<String> files = vfs.listFiles("test/test");
		for (String name : files) {
			String body = vfs.readFile("test/test", name);
			System.out.println(body);
		}
	}

}
