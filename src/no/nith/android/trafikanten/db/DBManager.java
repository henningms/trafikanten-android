package no.nith.android.trafikanten.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import no.nith.android.trafikanten.db.annotation.Entity;
import no.nith.android.trafikanten.db.annotation.Int;
import no.nith.android.trafikanten.db.annotation.PrimaryKey;
import no.nith.android.trafikanten.db.annotation.Text;
import no.nith.android.trafikanten.db.annotation.VarChar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {
	
	private Class<?> entityClass;
	
	private Context context;
	
	private DBSQLiteOpenHelper dbSqliteOpenHelper;
	
	public static enum Mode {
		WRITE, READ
	}
	
	public DBManager(Context ctx, Class<?> entityClass) throws Exception {
		
		context = ctx;
		
		Entity entityAnnotation =
				(Entity) entityClass.getAnnotation(Entity.class);
		
		// Assure that the Entity annotation exists for this class
		if (entityAnnotation == null) {
			throw new Exception("The class is not an entity!");
		}
		
		this.entityClass = entityClass;
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
	
	public void open(Mode dba) {
		dbSqliteOpenHelper = new DBSQLiteOpenHelper();
		SQLiteDatabase database = null;
		if (dba == Mode.READ) {
			Log.i("DBTEST", "test read");
			database = dbSqliteOpenHelper.getReadableDatabase();
		} else if (dba == Mode.WRITE) {
			database = dbSqliteOpenHelper.getWritableDatabase();
		}
	}
	
	public void close() {
		dbSqliteOpenHelper.close();
	}
	
	public void query() {
		
	}
	
	private String getTableName() {
		Entity entityAnnotation =
				(Entity) entityClass.getAnnotation(Entity.class);
		return entityAnnotation.table();
	}
	
	private String getDatabaseName() {
		Entity entityAnnotation =
				(Entity) entityClass.getAnnotation(Entity.class);
		return entityAnnotation.db();
	}
	
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
