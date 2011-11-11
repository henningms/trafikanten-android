/**
 * DBManager
 * 
 * Creates and deletes tables based on classes (models)
 * 
 * Can insert, delete and update rows by passing an instance of the class used
 * 
 * Written by Arild Wanvik Tvergrov 2011
 */
package no.nith.android.trafikanten.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import no.nith.android.trafikanten.db.annotation.Entity;
import no.nith.android.trafikanten.db.annotation.Int;
import no.nith.android.trafikanten.db.annotation.PrimaryKey;
import no.nith.android.trafikanten.db.annotation.Text;
import no.nith.android.trafikanten.db.annotation.VarChar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {

	/**
	 * The activity context.
	 */
	private Context context;
	
	/**
	 * Used to connect to the database, or create one if it doesn't exist.
	 */
	private DBSQLiteOpenHelper helper;
	
	/**
	 * Used to map cursors to the right entities.
	 */
	private Class<?> entityClass;
	
	/**
	 * The database access mode.
	 */
	public static enum Mode {
		WRITE, READ
	}
	
	public DBManager(Context ctx)  {
		context = ctx;
	}
	
	public DBManager(Context ctx, Class<?> entityClass) throws Exception {
		context = ctx;
		setEntity(entityClass);
	}
	
	public <T> DBManager setEntity(Class<T> entityClass) throws Exception {	
		// Assure that the Entity annotation exists for this class
		if (!isEntity(entityClass)) {
			throw new Exception("The class is not an entity!");
		}
		
		this.entityClass = entityClass;
		
		return this;
	}
	
	private class DBSQLiteOpenHelper extends SQLiteOpenHelper {

		private SQLiteDatabase database;
		
		public DBSQLiteOpenHelper() {
			super(DBManager.this.getContext(),
					DBManager.this.getDatabaseName(), null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(generateCreateTableQuery());
			this.database = db;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
		}
		
	}
	
	public DBManager open(Mode dba) {
		helper = new DBSQLiteOpenHelper();
		SQLiteDatabase db = null;
		if (dba == Mode.READ) {
			db = helper.getReadableDatabase();
		} else if (dba == Mode.WRITE) {
			db = helper.getWritableDatabase();
		}
		helper.database = db;
		return this;
	}
	
	public DBManager close() {
		helper.close();
		return this;
	}
	
	/**
	 * Inserts an entity into the database.
	 * @param entityObject The entity to be inserted in the database.
	 * @return The ID that the entity is assigned after it has been inserted.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public long insert(Object entityObject) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			InvocationTargetException {
		
		/* Make sure that this object is a class of the entity specified in the
		 * constructor. */
		if (!entityClass.isAssignableFrom(entityObject.getClass())) {
			throw new IllegalArgumentException("entityObject must be of the " +
					"same type as the entity class.");
		}
		
		return helper.database.insert(getTableName(), null,
				entityObjectToContentValues(entityObject));
	}
	
	/**
	 * Updates an entity object in the database.
	 * @param entityObject The Entity object to be updated in the database.
	 * @return True if the entity was updated in the database, otherwise false.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public boolean update(Object entityObject) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			InvocationTargetException {
		
		/* Make sure that this object is a class of the entity specified in the
		 * constructor. */
		if (!entityClass.isAssignableFrom(entityObject.getClass())) {
			throw new IllegalArgumentException("entityObject must be of the " +
					"same type as the entity class.");
		}
		
		String[] primaryKeys = getPrimaryKeys();
		
		String[] primaryKeyValues = new String[primaryKeys.length];
		
		StringBuilder whereClause = new StringBuilder();
		
		int i = 0;
		for (String primaryKey : primaryKeys) {
			if (whereClause.length() != 0) {
				whereClause.append(" AND ");
			}
			whereClause.append(primaryKey + " = ?");
			Method m = entityObject.getClass().getMethod("get" +
					primaryKey.toUpperCase().charAt(0) +
					primaryKey.substring(1));
			primaryKeyValues[i++] = m.invoke(entityObject) + "";
		}
		
		return helper.database.update(getTableName(),
				entityObjectToContentValues(entityObject),
				whereClause.toString(), primaryKeyValues) == 1;
	}
	
	public boolean delete(Object entityObject) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		
		/* Make sure that this object is a class of the entity specified in the
		 * constructor. */
		if (!entityClass.isAssignableFrom(entityObject.getClass())) {
			throw new IllegalArgumentException("entityObject must be of the " +
					"same type as the entity class.");
		}
		
		String[] primaryKeys = getPrimaryKeys();
		
		String[] primaryKeyValues = new String[primaryKeys.length];
		
		StringBuilder whereClause = new StringBuilder();
		
		int i = 0;
		for (String primaryKey : primaryKeys) {
			if (whereClause.length() != 0) {
				whereClause.append(" AND ");
			}
			whereClause.append(primaryKey + " = ?");
			Method m = entityObject.getClass().getMethod("get" +
					primaryKey.toUpperCase().charAt(0) +
					primaryKey.substring(1));
			primaryKeyValues[i++] = m.invoke(entityObject) + "";
		}
		
		return helper.database.delete(getTableName(),
				whereClause.toString(), primaryKeyValues) == 1;
	}
	
	public <T> ArrayList<T> select() throws SecurityException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, NoSuchMethodException,
			InvocationTargetException {
		return select(null, null, null, null);
	}
	
	/**
	 * Selects entities from the database.
	 * @param whereClause The conditions that the entities must meet.
	 * @param whereArgs The arguments for the conditions.
	 * @param orderBy The order of the entities.
	 * @param limit The number of entities to select.
	 * @return A list of the entities selected from the database.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> select(String whereClause, String[] whereArgs,
			String orderBy, Integer limit)
			throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchFieldException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		
		String limitString = limit == null ? "" : limit + "";
		
		Cursor c = helper.database.query(getTableName(), null,
				whereClause, whereArgs, null, null, orderBy, limitString);
		
		return (ArrayList<T>) mapToEntities(c);
	}
	
	/**
	 * Maps a cursor to a list of entities.
	 * @param c The cursor from which to map the entities.
	 * @return A list of the entities.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private <T> ArrayList<T> mapToEntities(Cursor c)
			throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchFieldException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {

		ArrayList<T> entities = new ArrayList<T>();
		
		String[] columnNames = c.getColumnNames();
		
		if (c.moveToFirst()) {
			do {
				@SuppressWarnings("unchecked")
				T inst = (T) entityClass.newInstance();
				entities.add(inst);
				for (String columnName : columnNames) {
					int index = c.getColumnIndex(columnName); 
					
					Field f = entityClass.getDeclaredField(columnName);
					Class<?> fieldType = f.getType();
					Object value = null;
					Method m = null;
					Class<?>[] paramTypes = null;
					
					if (String.class.isAssignableFrom(fieldType)) {
						value = c.getString(index);
						paramTypes = new Class[] {String.class};
					} else if (Integer.class.isAssignableFrom(fieldType)) {
						value = c.getInt(index);
						paramTypes = new Class[] {Integer.class};
					}
					m = entityClass.getMethod(
							getSetMethodNameByField(f),
							paramTypes);

					m.invoke(inst, new Object[] { value });
				}
			} while(c.moveToNext());
			
		}
		
		return entities;
	}
	
	/**
	 * Gets the table name for the current entity.
	 * @return The table name.
	 */
	private String getTableName() {
		Entity entityAnnotation =
				(Entity) entityClass.getAnnotation(Entity.class);
		return entityAnnotation.table();
	}
	
	/**
	 * Gets the database name used in this manager.
	 * @return The database name.
	 */
	private String getDatabaseName() {
//		Entity entityAnnotation =
//				(Entity) entityClass.getAnnotation(Entity.class);
//		return entityAnnotation.db();
		return "favorite_stations_db";
	}
	
	private String getGetMethodNameByField(Field f) {
		return "get" + f.getName().toUpperCase().charAt(0) +
				f.getName().substring(1);
	}
	
	private String getSetMethodNameByField(Field f) {
		return "set" + f.getName().toUpperCase().charAt(0) +
				f.getName().substring(1);
	}
	
	/**
	 * Gets the primary keys for the current entity.
	 * @return The primary keys.
	 */
	private String[] getPrimaryKeys() {
		
		// Need to determine which columns this entity class has.
		Field[] fields = entityClass.getDeclaredFields();
		
		StringBuilder primaryKeys = new StringBuilder();
		
		for (Field field : fields) {
			
			Annotation[] annotations = field.getAnnotations();
			
			// If the field has no annotations, it's not a column in the db.
			if (annotations.length == 0) {
				// Continue to next field.
				continue;
			}
			
			for (Annotation annotation : annotations) {
				if (annotation instanceof PrimaryKey) {
					if (primaryKeys.length() != 0) {
						primaryKeys.append(", ");
					}
					primaryKeys.append(field.getName());
				}
			}
		}

		return primaryKeys.toString().split(", ");
	}
	
	/**
	 * Checks if the entity has an Entity annotation attached to it.
	 * @param entityClass The class to check.
	 * @return True if it has an Entity annotation attached to it, otherwise
	 * false.
	 */
	private boolean isEntity(Class<?> entityClass) {
		Entity entityAnnotation =
				(Entity) entityClass.getAnnotation(Entity.class);
		return entityAnnotation != null;
	}
	
	/**
	 * Creates a ContentValues object based on the value of the fields of the
	 * specified entity object.
	 * @param entityObject The entity object from which to create a Content
	 * Values object.
	 * @return 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private ContentValues entityObjectToContentValues(Object entityObject)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		ContentValues cv = new ContentValues();
		
		Field[] fields = entityObject.getClass().getDeclaredFields();
		
		// Loop through fields to create content values.
		for (Field field : fields) {
			String key = field.getName();
			
			// Construct method name
			String methodName = getGetMethodNameByField(field);
			
			Method m = entityClass.getMethod(methodName);
			
			Object value = m.invoke(entityObject);
			
			if (value == null) {
				cv.putNull(key);
			} else if (value instanceof Integer) {
				cv.put(key, (Integer) value);
			} else if (value instanceof String) {
				cv.put(key, (String) value);
			} else if (value instanceof Boolean) {
				cv.put(key, (Boolean) value);
			}
		}
		
		return cv;
	}
	
	/**
	 * Creates the SQL statement for creating the table, corresponding to the
	 * current entity, in the database.
	 * @return The SQL statement.
	 */
	private String generateCreateTableQuery() {
		// Need to determine which columns this entity class has.
		Field[] fields = entityClass.getDeclaredFields();
		
		StringBuilder columnDefinitions = new StringBuilder();
		StringBuilder primaryKeys = new StringBuilder();
		
		for (Field field : fields) {
			
			Annotation[] annotations = field.getAnnotations();
			
			// If the field has no annotations, it's not a column in the db.
			if (annotations.length == 0) {
				// Continue to next field.
				continue;
			}
			
			columnDefinitions.append(field.getName());
			
			for (Annotation annotation : annotations) {
				boolean fieldIsPrimaryKey = false;
				if (annotation instanceof PrimaryKey) {
					if (primaryKeys.length() != 0) {
						primaryKeys.append(", ");
					}
					primaryKeys.append(field.getName());
					fieldIsPrimaryKey = true;
				}
				if (annotation instanceof Int) {
					Int vc = (Int) annotation;
					columnDefinitions.append(" INTEGER");
					if (vc.notNull() && !fieldIsPrimaryKey) {
						columnDefinitions.append(" NOT NULL");
					}
					if (vc.unique() && !fieldIsPrimaryKey) {
						columnDefinitions.append(" UNIQUE");
					}
				} else if (annotation instanceof VarChar) {
					VarChar vc = (VarChar) annotation;
					columnDefinitions.append(String.format(" VARCHAR(%d)", vc.length()));
					if (vc.notNull() && !fieldIsPrimaryKey) {
						columnDefinitions.append(" NOT NULL");
					}
					if (vc.unique() && !fieldIsPrimaryKey) {
						columnDefinitions.append(" UNIQUE");
					}
				} else if (annotation instanceof Text) {
					Text vc = (Text) annotation;
					columnDefinitions.append(" TEXT");
					if (vc.notNull() && !fieldIsPrimaryKey) {
						columnDefinitions.append(" NOT NULL");
					}
				}
				
			}
			
			columnDefinitions.append(", ");
		}

		String queryFormat = "CREATE TABLE %s (%sPRIMARY KEY (%s));";
		
		return String.format(queryFormat, getTableName(),
				columnDefinitions.toString(), primaryKeys.toString());
	}
	
	private Context getContext() {
		return context;
	}
}
