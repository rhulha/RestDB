package  net.raysforge.restdb.test;

import java.io.IOException;

import net.raysforge.rest.client.GenericRestClient;
import net.raysforge.rest.client.GenericRestClient.Auth;

public class RestClientDeleteDocs {

	public static void main(String[] args) throws IOException {

		GenericRestClient grc = new GenericRestClient("http://localhost:10000/rest/crud/v1/", "ADMIN", "ADMIN", Auth.Basic);
		grc.debugURL = true;

		for (int i = 1000; i < 1500; i++) {
			grc.postData("DELETE", "test/test/test" + i + ".json", "");
		}

	}
}
