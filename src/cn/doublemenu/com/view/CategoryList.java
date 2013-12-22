package cn.doublemenu.com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.doublemenu.com.activity.R;
import cn.doublemenu.com.bean.Category;
import cn.doublemenu.com.db.GenericDAO;

public class CategoryList extends LinearLayout {
	private List<Category> topCategories;
	private Map<Integer, List<Category>> categoriesMap;
	private GenericDAO dao;
	private Context context;
	private Listener listener;
	private ListView groupListView;
	private ListView childListView;
	private ChildAdapter adapter;
	private int positionOut;

	public interface Listener {
		public void onSelected(Category category, Category parentCategory);

		public void onScroll(Category category);
	}

	public CategoryList(Context c, AttributeSet attrs) {
		super(c, attrs);
		dao = GenericDAO.getInstance(c);
		context = c;
	}

	public void init(final Listener listener, Category category) {
		topCategories = dao.listCategories();
		categoriesMap = new HashMap<Integer, List<Category>>();
		this.listener = listener;
		adapter = new ChildAdapter(new ArrayList<Category>());
		groupListView = (ListView) findViewById(R.id.list_group);
		childListView = (ListView) findViewById(R.id.list_child);
		groupListView.setCacheColorHint(0);
		groupListView.setAdapter(new GroupAdapter());
		childListView.setAdapter(adapter);
		List<Category> childCategoryList = getCategories(0);
		adapter.setCategoryList(childCategoryList);
		adapter.notifyDataSetChanged();
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				positionOut = position;
				List<Category> childList = getCategories(position);
				adapter.setCategoryList(childList);
				adapter.notifyDataSetChanged();
				GroupAdapter adapt = (GroupAdapter) parent.getAdapter();
				adapt.setmCurSelectPosition(position);
				adapt.notifyDataSetChanged();

			}
		});
		childListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.onSelected(adapter.getCategoryList().get(position),
						topCategories.get(positionOut));
			}
		});

	}

	private List<Category> getCategories(int groupPosition) {
		List<Category> categories = categoriesMap.get(groupPosition);
		if (categories == null) {
			Category parent = topCategories.get(groupPosition);
			categories = dao.listCategories(parent);
			categoriesMap.put(groupPosition, categories);
		}

		return categories;
	}

	private class ViewHolder {
		public ImageView imgView;
		public TextView textView;
	}

	class GroupAdapter extends BaseAdapter {
		private ViewHolder mSelectHolder;
		private int mCurSelectPosition = 0;

		public ViewHolder getmSelectHolder() {
			return mSelectHolder;
		}

		public void setmSelectHolder(ViewHolder mSelectHolder) {
			this.mSelectHolder = mSelectHolder;
		}

		public int getmCurSelectPosition() {
			return mCurSelectPosition;
		}

		public void setmCurSelectPosition(int mCurSelectPosition) {
			this.mCurSelectPosition = mCurSelectPosition;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (topCategories != null) {
				return topCategories.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (topCategories != null && topCategories.size() > position - 1) {
				return topCategories.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = layoutInflater.inflate(
						R.layout.address_item_layout, null);
				viewHolder = new ViewHolder();

				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.city_item_title);
				viewHolder.imgView = (ImageView) convertView
						.findViewById(R.id.img_tag);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position == mCurSelectPosition) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.address_child));
				upDateItemUi(viewHolder.imgView, true);
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.white));
				upDateItemUi(viewHolder.imgView, false);
			}
			Category category = topCategories.get(position);
			viewHolder.textView.setText(category.name);
			return convertView;
		}
	}

	private void upDateItemUi(ImageView imgView, boolean b) {
		if (b) {
			imgView.setVisibility(View.VISIBLE);
		} else {
			imgView.setVisibility(View.INVISIBLE);
		}
	}

	class ChildAdapter extends BaseAdapter {
		private List<Category> categoryList;

		public List<Category> getCategoryList() {
			return categoryList;
		}

		public void setCategoryList(List<Category> categoryList) {
			this.categoryList = categoryList;
		}

		public ChildAdapter(List<Category> childList) {
			super();
			this.categoryList = childList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (categoryList != null) {
				return categoryList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (categoryList != null && categoryList.size() > position - 1) {
				return categoryList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.address_textview_layout,
						null);
			}
			TextView cityChildTextView = (TextView) view
					.findViewById(R.id.single_textview);
			cityChildTextView.setText(categoryList.get(position).name);
			return view;
		}
	}
}