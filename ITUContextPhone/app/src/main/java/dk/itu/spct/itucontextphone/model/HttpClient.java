package dk.itu.spct.itucontextphone.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


//import org.glassfish.jersey.client.ClientConfig;
//import org.glassfish.jersey.client.ClientResponse;

import java.io.IOException;
import java.net.URI;

//import javax.ws.rs.client.Client;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

//import javax.ws.rs.core.MediaType;

/**
 * Created by Dan on 02-Apr-15.
 */
public class HttpClient {

    private void runHttpClient() throws IOException {
        Long id = 1L;
    ClientResponse response;
    ObjectMapper mapper = new ObjectMapper();

    ClientConfig clientConfig = new DefaultClientConfig();



    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    final Client httpClient = Client.create(clientConfig);
    final WebResource service = httpClient.resource(HttpClient.getBaseURI());

    // POST
    ContextEntity entity = new ContextEntity();
    String json = mapper.writeValueAsString(entity);
    response = service.path("context").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

    // example GET ALL http://localhost:8888/context
    response = service.path("context").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    json= response.getEntity(String.class);


    // example GET http://localhost:8888/id
    response = service.path("context").path(String.valueOf(id)).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    json = response.getEntity(String.class);
    ContextEntity entity_2 = mapper.readValue(json, ContextEntity.class);

    }

    // url of cloud goes here
    private static URI getBaseURI() {

        return UriBuilder.fromUri("http://spct-ta-context-service.appspot.com/").build();
    }
}
