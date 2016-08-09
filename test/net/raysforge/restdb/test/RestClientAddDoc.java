package net.raysforge.restdb.test;

import java.io.IOException;

import net.raysforge.rest.client.GenericRestClient;
import net.raysforge.rest.client.GenericRestClient.Auth;

public class RestClientAddDoc {

	public static void main(String[] args) throws IOException {
		
		GenericRestClient grc = new GenericRestClient("http://localhost:8080/rest/crud/v1/", "ADMIN", "ADMIN", Auth.Basic);
		grc.debugURL = true;

		grc.postData("PUT", "comments/comment.json", "{\"author\":\"aaa\",\"text\":\"vvv\",\"id\":\"1470662733554\"}");

	}
}
