package dk.itu.spct.itucontextphonecloud.persistence;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import dk.itu.spct.itucontextphonecloud.model.ContextEntity;
import dk.itu.spct.itucontextphonecloud.model.ContextEntityList;
import dk.itu.spct.itucontextphonecloud.util.Constant;

public class DB {

	private static DB instance;

	public static DB getInstance() {
		if(instance == null)
			instance = new DB();
		return instance;
	}

	private DatastoreService dataStore;

	private DB() {
		dataStore = DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * Generates a key from the kind and id.
	 */
	private Key generateKey(String kind, long id) {
		return KeyFactory.createKey(kind, id);
	}

	public ContextEntityList getContextEntities() {
		ContextEntityList list = new ContextEntityList();

		Query query = new Query(Constant.CONTEXT_ENTITY);
		PreparedQuery pq = dataStore.prepare(query);

		for(Entity e : pq.asIterable()) {
			ContextEntity ce = new ContextEntity();
			ce.setId(Long.valueOf(e.getProperty(Constant.ID).toString()));
			ce.setSensor(e.getProperty(Constant.SENSOR).toString());
			ce.setTimeStamp(Long.valueOf(e.getProperty(Constant.TIMESTAMP).toString()));
			ce.setType(e.getProperty(Constant.TYPE).toString());
			ce.setValue(e.getProperty(Constant.VALUE).toString());

			list.add(ce);
		}

		return list;
	}

	public ContextEntity getContextEntity(long id) {
		ContextEntity r = null;
		Entity e = getEntity(id);
		if(e != null) {
			r = new ContextEntity();
			r.setId(Long.valueOf(e.getProperty(Constant.ID).toString()));
			r.setSensor(e.getProperty(Constant.SENSOR).toString());
			r.setValue(e.getProperty(Constant.VALUE).toString());
			r.setType(e.getProperty(Constant.TYPE).toString());
			r.setTimeStamp(Long.valueOf(e.getProperty(Constant.TIMESTAMP).toString()));
		}
		return r;
	}

	public int insertContextEntity(ContextEntity entity) {
		//		Key k = generateKey(Constant.CONTEXT_ENTITY, entity.getId());

		Entity e = new Entity(Constant.CONTEXT_ENTITY, entity.getId());

		e.setProperty(Constant.ID, entity.getId());
		e.setProperty(Constant.SENSOR, entity.getSensor());
		e.setProperty(Constant.VALUE, entity.getValue());
		e.setProperty(Constant.TYPE, entity.getType());
		e.setProperty(Constant.TIMESTAMP, entity.getTimeStamp());

		return insertEntity(e);
	}
	
	public int deleteContextEntity(long id) {
		Key k = generateKey(Constant.CONTEXT_ENTITY, id);
		return deleteEntity(k);
	}

	// Private "raw" db crud methods
	private Entity getEntity(long id) {
		Entity retval = null;
		try {
			Key key = generateKey(Constant.CONTEXT_ENTITY, id);
			retval = dataStore.get(key);
		} catch (EntityNotFoundException e) {
			System.out.println("DB: Entity not found. ID: " + id);
			e.printStackTrace();
		}
		return retval;
	}
	
	private int insertEntity(Entity entity) {
		try {
			dataStore.put(entity);
			return Constant.RESPONSE_SUCCESS;
		} catch (Exception e) {
			return Constant.RESPONSE_ERROR;
		}
	}

	private int deleteEntity(Key key) {
		try {
			dataStore.delete(key);
			return Constant.RESPONSE_SUCCESS;
		} catch (Exception e) {
			return Constant.RESPONSE_ERROR;
		}
	}
}
