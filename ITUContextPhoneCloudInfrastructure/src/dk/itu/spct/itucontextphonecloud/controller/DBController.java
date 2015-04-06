package dk.itu.spct.itucontextphonecloud.controller;

import dk.itu.spct.itucontextphonecloud.io.Response;
import dk.itu.spct.itucontextphonecloud.model.ContextEntity;
import dk.itu.spct.itucontextphonecloud.model.ContextEntityList;
import dk.itu.spct.itucontextphonecloud.persistence.DB;
import dk.itu.spct.itucontextphonecloud.util.Constant;

public class DBController {
	
	private DB db;
	
	public DBController() { db = DB.getInstance(); }
	
	private int setGetResponseCode(Response r) {
		if(r.getDataCount() > 0)
			return Constant.RESPONSE_SUCCESS;
		else 
			return Constant.RESPONSE_NOT_FOUND;
	}
	
	public void deleteAll() {
		db.deleteAll();
	}
	
	public Response getContextEntities() {
		Response r = new Response();
		r.setData(db.getContextEntities());
		r.setResponseCode(setGetResponseCode(r));
		return r;
	}
	
	public Response getContextEntity(long id) {
		Response r = new Response();
		r.addToData(db.getContextEntity(id));
		r.setResponseCode(setGetResponseCode(r));
		return r;
	}
	
	public Response insertContextEntity(ContextEntity entity) {
		Response r = new Response();
		r.setResponseCode(db.insertContextEntity(entity));
		return r;
	}
	
	public Response deleteContextEntity(long id) {
		Response r = new Response();
		r.setResponseCode(db.deleteContextEntity(id));
		return r;
	}
	
	public Response insertContextEntities(ContextEntityList list) {
		int code = -1;
		Response r = new Response();
		for(ContextEntity e : list) {
			code = db.insertContextEntity(e);
		}
		r.setResponseCode(code);
		return r;
	}
}
