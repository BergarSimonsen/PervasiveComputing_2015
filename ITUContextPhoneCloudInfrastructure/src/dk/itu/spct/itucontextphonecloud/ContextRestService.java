package dk.itu.spct.itucontextphonecloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dk.itu.spct.itucontextphonecloud.model.ContextEntity;
import dk.itu.spct.itucontextphonecloud.model.ContextEntityList;
import dk.itu.spct.itucontextphonecloud.persistence.DB;

@Path("/context")
public class ContextRestService {
	
	private DB db;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ContextEntity getEntity(@PathParam("id") long id) {
		db = DB.getInstance();
		return db.getContextEntity(id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ContextEntityList getAllEntities() {
		db = DB.getInstance();
		return db.getContextEntities();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String addEntity(ContextEntity entity) {
		String s = "";
		db = DB.getInstance();
		db.insertContextEntity(entity);
		ContextEntity e = db.getContextEntity(entity.getId());
		if(e == null)
			s = "entity is null!!!";
		else 
			s = e.toString();
		return s;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addEntities(ContextEntityList list) {
		db = DB.getInstance();
		for(ContextEntity e : list) {
			db.insertContextEntity(e);
		}
	}
}
