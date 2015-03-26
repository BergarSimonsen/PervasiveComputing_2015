package dk.itu.spct.itucontextphonecloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dk.itu.spct.itucontextphonecloud.model.ContextEntity;

@Path("/context")
public class ContextRestService {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getEntity(@PathParam("id") long id) {
		return String.valueOf(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllEntities() {
		return "all entities TEST";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addEntity(ContextEntity entity) {
		// TODO: Insert into db
	}
}
