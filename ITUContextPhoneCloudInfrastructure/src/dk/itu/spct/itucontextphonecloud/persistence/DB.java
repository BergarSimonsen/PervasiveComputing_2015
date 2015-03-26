package dk.itu.spct.itucontextphonecloud.persistence;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import dk.itu.spct.itucontextphonecloud.model.ContextEntity;

public class DB {
	
	private final static String ID 			= "id";
	private final static String SENSOR 		= "sensor";
	private final static String VALUE 		= "value";
	private final static String TYPE 		= "type";
	private final static String TIMESTAMP 	= "timeStamp";
	
	public static DB instance;
	
	public static DB getInstance() {
		if(instance == null)
			instance = new DB();
		return instance;
	}
	
	private DatastoreService dataStore;
	
	private DB() {
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public void insertEntity(ContextEntity entity) {
		Entity e = new Entity(String.valueOf(entity.getId()));
		e.setProperty(ID, entity.getId());
		e.setProperty(SENSOR, entity.getSensor());
		e.setProperty(VALUE, entity.getValue());
		e.setProperty(TYPE, entity.getType());
		e.setProperty(TIMESTAMP, entity.getTimeStamp());
		
		insert(e);
	}
	
	public ContextEntity getEntity(long id) {
		ContextEntity r = null;
		Entity e = get(id);
		if(e != null) {
			r = new ContextEntity();
			r.setId(Long.valueOf(e.getProperty(ID).toString()));
			r.setSensor(e.getProperty(SENSOR).toString());
			r.setValue(e.getProperty(VALUE).toString());
			r.setType(e.getProperty(TYPE).toString());
			r.setTimeStamp(Long.valueOf(e.getProperty(TIMESTAMP).toString()));
		}
		return r;
	}
	
	private Entity get(long id) {
		Entity retval = null;
		try {
			String idString = String.valueOf(id);
			Key key = KeyFactory.stringToKey(idString);
			retval = dataStore.get(key);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	private void insert(Entity entity) {
		dataStore.put(entity);
	}

}