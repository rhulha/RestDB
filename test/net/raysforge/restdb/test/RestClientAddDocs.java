package  net.raysforge.restdb.test;

import java.io.IOException;

import net.raysforge.rest.client.GenericRestClient;
import net.raysforge.rest.client.GenericRestClient.Auth;

public class RestClientAddDocs {

	public static void main(String[] args) throws IOException {

		GenericRestClient crc0 = new GenericRestClient("http://localhost:8080/rest/crud/v1/", "ADMIN", "ADMIN", Auth.Basic);
		crc0.debugURL = true;

		for (int i = 1000; i < 1250; i += 2) {
			crc0.postData("PUT", "test/test/test" + i + ".json", "{name: \"Iced Mocha\", size: \"Grande\", OrderNr: " + i + "}");
		}

	}
}
