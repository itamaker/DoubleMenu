package cn.doublemenu.com.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.doublemenu.com.activity.R;
import cn.doublemenu.com.bean.Category;

public class GenericDAO extends SQLiteOpenHelper {
	private static Object lock = new Object();

	private static SQLiteDatabase db;
	public static final String KEY_ID = "id";
	public static final int DATABASE_VERSION = 7;
	private Context context;
	private final static String DATABASE_NAME = "category";
	private static volatile GenericDAO instance;

	private GenericDAO(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		context = ctx;
	}

	/*
	 * public static GenericDAO getInstance() { return
	 * getInstance(ActivityManager.getCurrent()); }
	 */

	public static GenericDAO getInstance(Context ctx) {
		try {
			if (instance == null) {
				synchronized (lock) {
					if (instance == null) {
						instance = new GenericDAO(ctx);
						db = instance.getWritableDatabase();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private void onDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			Log.e("test", "onCreate execute");
			load(db, R.raw.category);
			db.setTransactionSuccessful();
		} catch (Exception ee) {
			ee.printStackTrace();
			Log.e("test", ee.toString());
		} finally {
			if (db != null) {
				db.endTransaction();
			}
		}
	}

	private void load(SQLiteDatabase db, int fileId)
			throws UnsupportedEncodingException, IOException {
		BufferedReader bufferedReader = null;
		try {
			String temp;
			InputStream in = context.getResources().openRawResource(fileId);
			bufferedReader = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			while ((temp = bufferedReader.readLine()) != null) {
				db.execSQL(temp);
				temp = null;
			}
		} catch (Exception e) {

		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {

				}
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.beginTransaction();
			db.delete("category", null, null);

			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public long insert(String table, ContentValues values) {
		long ret = db.insert(table, null, values);
		return ret;
	}

	public Category getCategory(int id) {
		return Category.get(db, id);
	}

	public List<Category> listCategories() {
		try {
			List<Category> ret = Category.list(db);
			if (ret == null || ret.isEmpty()) {
				onCreate(db);
				return Category.list(db);
			}

			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			onCreate(db);
			return Category.list(db);
		}
	}

	public List<Category> listCategories(Category category) {
		return Category.list(db, category);
	}

}
