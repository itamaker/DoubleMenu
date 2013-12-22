package cn.doublemenu.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.doublemenu.com.bean.Category;
import cn.doublemenu.com.db.GenericDAO;
import cn.doublemenu.com.view.CategoryList;

public class MainActivity extends Activity {
	protected Category category;
	private CategoryList categoryList;
	private Button categoryButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		GenericDAO.getInstance(this).getWritableDatabase();

		categoryButton = (Button) findViewById(R.id.show);
		categoryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean visible = (categoryList.getVisibility() == View.VISIBLE);
				hideList(visible);

			}
		});
		initCategoryList(255);
		

	}

	protected void initCategoryList(int cat) {

		category = GenericDAO.getInstance(this).getCategory(cat);
		categoryList = (CategoryList) findViewById(R.id.categorylist);
		categoryList.init(new CategoryList.Listener() {
			public void onSelected(Category c, Category parentCategory) {
				categoryList.setVisibility(View.GONE);

			}

			public void onScroll(Category category) {
			}
		}, category);

	}

	protected void hideList(boolean vis) {
		if (categoryList != null) {
			if (vis) {
				categoryList.setVisibility(View.GONE);
			} else {
				categoryList.setVisibility(View.VISIBLE);
			}

		}
	}
}