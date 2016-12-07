package com.blueplanet.smartcookieteacher.ui.customactionbar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.blueplanet.smartcookieteacher.R;

import java.util.ArrayList;
import java.util.List;


public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {
	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;

	public CustomDrawerAdapter(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view
					.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
			drawerHolder.listinfo = (ListView) view
					.findViewById(R.id.drawerList);
			drawerHolder.itemLayout = (LinearLayout) view
					.findViewById(R.id.itemLayout);
			drawerHolder.spinnerLayout = (LinearLayout) view
					.findViewById(R.id.spinnerLayout);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);
		if (dItem.isList()) {

			drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);
			List<Employee> userList = new ArrayList<Employee>();
			userList.add(new Employee(UserSession.getBmp(), UserSession
					.getName()));
			CustomListAdapter adapter = new CustomListAdapter(context,
					R.layout.custom_listitem, userList);

			drawerHolder.listinfo.setAdapter(adapter);
			drawerHolder.listinfo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(context, ProfileUpdate.class);
					//context.startActivity(intent);
				}
			});
		}else if (dItem.getItemName().toString().equals("My Accounts")) {
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);
			ArrayAdapter<String> menu=new ArrayAdapter<String>(context, R.layout.custom_menu);
			drawerHolder.listinfo.setAdapter(menu);
			
		} else {

			drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);

			drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
					dItem.getImgResID()));
			drawerHolder.ItemName.setText(dItem.getItemName());
			Log.d("Getview", "Passed5");
		}

		return view;
	}

	private static class DrawerItemHolder {
		TextView ItemName;
		ImageView icon;
		LinearLayout itemLayout, spinnerLayout;
		ListView listinfo,listmenu;
		// Spinner spinner;
	}
}
