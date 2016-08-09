package net.raysforge.restdb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.raysforge.commons.FileUtils;
import net.raysforge.commons.Json;
import net.raysforge.rest.server.HttpResponse;
import net.raysforge.rest.server.SimpleHttpRequestHandler;
import net.raysforge.vfs.VFS;

public class RestDBHttpHandler extends SimpleHttpRequestHandler {

	private static final String REST_CRUD_V1 = "/rest/crud/v1/";
	private VFS vfs;

	public RestDBHttpHandler(Socket client, VFS vfs) {
		super(client);
		this.vfs = vfs;
	}

	@Override
	public HttpResponse handleGET(String path, String queryString) throws IOException {
		System.out.println("GET: " + path + '?' + queryString);
		
		if (path.startsWith(REST_CRUD_V1)) {
			path = path.substring(REST_CRUD_V1.length());
			
			File f = new File(path);
			if( path.endsWith(".json")) {
				String body = vfs.readFile(f.getParent(), f.getName());
				return body == null ? new HttpResponse(404, "not found") : new HttpResponse(body);
			} else if( path.equals("comments")) {
				List<String> files = vfs.listFiles(path);
				List<Object> jsonArray = new ArrayList<>(); 
				for (String name : files) {
					String body = vfs.readFile(path, name);
					jsonArray.add(new Json().parse(new StringReader(body)));
				}
				return new HttpResponse(Json.toJsonString(jsonArray));
			} else {
				return new HttpResponse(404, "not found 2xy");
			}
			

		} else {
		
			//Files.readAllBytes(Paths.get("web/"+path))
			if (path.equals("/"))
				path = "/index.html";
			if( !path.startsWith("/"))
				path = '/'+path;
			System.out.println(path);
			StringBuffer data = FileUtils.readCompleteFile(new File("web" + path), "UTF-8"); // TODO make sure canonical
			return new HttpResponse(data.toString(), guessContentTypeFromName(path));
		}
	}

	private String guessContentTypeFromName(String path) {
		String ct = URLConnection.guessContentTypeFromName(path);
		if(ct!=null)
			return ct;
		if(path.endsWith(".css"))
			return "text/css";
		if(path.endsWith(".ico"))
			return "image/x-icon";
		return "application/octet-stream";
	}

	@Override
	public HttpResponse handleDELETE(String path, String queryString) throws IOException {
		return new HttpResponse(500, "not implemented yet");
	}

	@Override
	public HttpResponse handlePUT(String path, String queryString, String body) throws IOException {
		System.out.println("PUT: " + path);
		if (path.startsWith(REST_CRUD_V1)) {
			path = path.substring(REST_CRUD_V1.length());
		} else {
			return new HttpResponse(500, "path must start with " + REST_CRUD_V1 + ", unknown path: " + path);
		}

		File f = new File(path);
		vfs.writeFile(f.getParent(), f.getName(), body);
		return new HttpResponse("");
	}

	@Override
	public HttpResponse handlePOST(String path, String queryString, String body) throws IOException {
		System.out.println("POST: " + path + '?' + queryString);
		System.out.println("body: " + body); //  author=aaa&text=ccc&id=1470662314809
		
		if (path.startsWith(REST_CRUD_V1)) {
			path = path.substring(REST_CRUD_V1.length());
		} else {
			return new HttpResponse(500, "path must start with " + REST_CRUD_V1 + ", unknown path: " + path);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] lines = body.split("&");
		for (String tuple_ : lines) {
			String[] tuple = tuple_.split("=");
			map.put(tuple[0], tuple[1]);
		}
		
		if(!path.equals("comments"))
			return new HttpResponse(500, "path must end in 'comments', unknown path: " + path);
		
		vfs.writeFile(path, map.get("id")+".json", Json.toJsonString(map));
		return new HttpResponse("");
	}

}
