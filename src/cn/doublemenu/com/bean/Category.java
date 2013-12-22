package cn.doublemenu.com.bean;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Category {
	public int id;
	public Integer pid;
	public String name;

	public static Category get(SQLiteDatabase db, int id) {
		if (id == 255) {
			Category category = new Category();
			category.pid = 0;
			category.id = 255;
			category.name = "全部分类";
			return category;
		}

		Cursor cursor = db.rawQuery("SELECT * FROM category WHERE id = " + id,
				null);
		cursor.moveToFirst();

		Category ret = toCategory(cursor);
		cursor.close();

		return ret;
	}

	public static List<Category> list(SQLiteDatabase db) {
		List<Category> ret = new ArrayList<Category>();
		Cursor cursor = null;
		try {
			ret.add(get(db, 255));

			cursor = db.rawQuery("SELECT * FROM category WHERE pid IS NULL",
					null);

			if (cursor.moveToFirst()) {
				do {
					ret.add(toCategory(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}

		return ret;
	}

	public static List<Category> list(SQLiteDatabase db, Category parent) {
		List<Category> ret = new ArrayList<Category>();
		ret.add(parent);

		Cursor cursor = db.rawQuery(
				"SELECT id,pid,name FROM category WHERE pid = " + parent.id,
				null);

		if (cursor.moveToFirst()) {
			do {
				ret.add(toCategory(cursor));
			} while (cursor.moveToNext());
		}

		cursor.close();
		return ret;
	}

	private static Category toCategory(Cursor cursor) {
		Category category = new Category();
		category.id = cursor.getInt(0);
		category.pid = cursor.getInt(1);
		category.name = cursor.getString(2);

		return category;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Category && ((Category) o).id == id;
	}
}
