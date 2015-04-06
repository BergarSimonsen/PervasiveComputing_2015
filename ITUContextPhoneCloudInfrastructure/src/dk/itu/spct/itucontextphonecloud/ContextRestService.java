package dk.itu.spct.itucontextphonecloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dk.itu.spct.itucontextphonecloud.controller.DBController;
import dk.itu.spct.itucontextphonecloud.io.Response;
import dk.itu.spct.itucontextphonecloud.model.ContextEntity;
import dk.itu.spct.itucontextphonecloud.model.ContextEntityList;

@Path("/context")
public class ContextRestService {
	
	private DBController dbc;
	
	private void init() { dbc = new DBController(); }
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntity(@PathParam("id") long id) {
		init();
		return dbc.getContextEntity(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEntities() {
		init();
		return dbc.getContextEntities();
	}
	
	@POST
	@Path("/single")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEntity(ContextEntity entity) {
		init();
		dbc.insertContextEntity(entity);
		return dbc.getContextEntity(entity.getId());
	}
	
	@GET
	@Path("/removeAllEntries")
	public String removeAllEntries() {
		init();
		dbc.deleteAll();
		return "Delete Finished";
	}
	
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEntities(ContextEntityList list) {
		init();
		return dbc.insertContextEntities(list);
	}
}
