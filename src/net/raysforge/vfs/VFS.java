package net.raysforge.vfs;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class VFS {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	HashMap<String, Map<String, Record>> indexes = new HashMap<String, Map<String, Record>>();

	public List<String> listFiles(String folder) throws IOException {
		folder = sanitize(folder);
		File offsetFile = new File(folder + ".offsets.txt");
		
		List<String> fileNames = new ArrayList<>();
		
		if (!offsetFile.exists())
			offsetFile.createNewFile();
		BufferedReader br = new BufferedReader(new FileReader(offsetFile));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] split = line.split(":");
			fileNames.add(split[0]);
		}
		br.close();

		return fileNames;
	}

	public synchronized void writeFile(String folder, String name, String contents) throws IOException {
		if(name.contains(":")) {
			throw new IOException("name.contains(\":\")");
		}

		folder = sanitize(folder);

		File dataFile = new File(folder + ".dat");
		File offsetFile = new File(folder + ".offsets.txt");
		FileOutputStream offsetFileOS = new FileOutputStream(offsetFile, true);
		long offset = dataFile.length();

		FileOutputStream dataFileOS = new FileOutputStream(dataFile, true);
		dataFileOS.write(new byte[] { 0x1E, 0x1E, 0x1E, 0x1E }); // record separator
		byte[] bytes = contents.getBytes(UTF8);
		dataFileOS.write(bytes);
		dataFileOS.close();

		offsetFileOS.write((name + ":" + offset + ":" + bytes.length + "\r\n").getBytes(UTF8));
		offsetFileOS.close();

		if (indexes.containsKey(folder)) {
			Map<String, Record> map = indexes.get(folder);
			map.put(name, new Record(offset, Math.toIntExact(bytes.length)));
		}

	}

	private String sanitize(String folder) {
		if(folder==null)
			folder="null";
		folder = folder.replace('/', '~').replace('\\', '~');
		return folder;
	}

	public String readFile(String folder, String name) throws IOException {
		
		if(name.contains(":")) {
			throw new IOException("name.contains(\":\")");
		}
		
		folder = sanitize(folder);

		Record record;

		Map<String, Record> map = null;

		synchronized (indexes) {
			if (indexes.containsKey(folder)) {
				map = indexes.get(folder);
			} else {
				map = loadOffsetFile(folder);
			}
			
		}
		
		record = map.get(name);

		if (record != null) {

			File dataFile = new File(folder + ".dat");
			if (!dataFile.exists())
				dataFile.createNewFile();
			try (FileInputStream dataFileIS = new FileInputStream(dataFile)) {
				if (record.offset > 0)
					dataFileIS.skip(record.offset);

				for (int i = 0; i < 4; i++)
					if (dataFileIS.read() != 0x1E)
						throw new RuntimeException("error in data file.");

				byte buffer[] = new byte[record.length];
				int read = dataFileIS.read(buffer);
				if (read != record.length)
					throw new RuntimeException("read!=record.length." + read + " x " + record.length);
				return new String(buffer, UTF8);
			}
		} else {
			return null;
		}

	}

	private Map<String, Record> loadOffsetFile(String folder) throws IOException, FileNotFoundException {
		Map<String, Record> map;
		map = new TreeMap<String, Record>();

		File offsetFile = new File(folder + ".offsets.txt");
		if (!offsetFile.exists())
			offsetFile.createNewFile();
		BufferedReader br = new BufferedReader(new FileReader(offsetFile));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] split = line.split(":");
			map.put(split[0], new Record(Long.parseLong(split[1]), Integer.parseInt(split[2])));
		}
		br.close();
		indexes.put(folder, map);
		return map;
	}
}
