package net.raysforge.restdb.test;

import java.io.IOException;

import net.raysforge.rest.client.GenericRestClient;
import net.raysforge.rest.client.GenericRestClient.Auth;

public class RestClientGetDoc {

	public static void main(String[] args) throws IOException {

		GenericRestClient grc = new GenericRestClient("http://localhost:8080/rest/crud/v1/", "ADMIN", "ADMIN", Auth.Basic);
		grc.debugURL = true;

		String path = "comments";

		String json = grc.getUTF8Body(path);
		System.out.println("<json>" + json + "</json>");

	}
}
