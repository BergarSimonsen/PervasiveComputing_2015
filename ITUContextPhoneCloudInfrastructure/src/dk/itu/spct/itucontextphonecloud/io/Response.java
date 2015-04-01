package dk.itu.spct.itucontextphonecloud.io;

import dk.itu.spct.itucontextphonecloud.model.ContextEntity;
import dk.itu.spct.itucontextphonecloud.model.ContextEntityList;

public class Response {
	private ContextEntityList data;
	private int responseCode;
	private int dataCount;
	
	public Response() { data = new ContextEntityList(); }
	
	public int getResponseCode() { return responseCode; }
	
	public void setResponseCode(int code) {	this.responseCode = code; }
	
	public ContextEntityList getData() { return data; }
	
	public void setData(ContextEntityList data) { this.data = data; }
	
	public void addToData(ContextEntity entity) { data.add(entity);	}
	
	public int getDataCount() { return data.size(); }

	public void setDataCount(int dataCount) { return; }
}
