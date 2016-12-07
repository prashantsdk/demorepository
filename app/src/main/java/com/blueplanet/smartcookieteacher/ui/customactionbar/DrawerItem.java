package com.blueplanet.smartcookieteacher.ui.customactionbar;

public class DrawerItem {
	String ItemName,MenuName;
	
	int imgResID,i;
	boolean isList,isMenu1,isMenu2;
	public boolean isList() {
		return isList;
	}
	public boolean isMenu1() {
		return isMenu1;
	}
	public boolean isMenu2() {
		return isMenu2;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	public void setList(boolean isMenu1,int i) {
		this.isMenu1 = isMenu1;
	}
	public DrawerItem(String itemName, int imgResID) {
		this.ItemName = itemName;
		this.imgResID = imgResID;
	}
	public DrawerItem(String itemName) {
		this.ItemName = itemName;
		
	}

	public DrawerItem(boolean isList) {
		this(null, 0);
		this.isList = isList;
	}
	public DrawerItem(boolean isMenu1,int i) {
		
		this.isMenu1 = isMenu1;
		this.i=i;
	}
	
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public int getImgResID() {
		return imgResID;
	}
	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}
	
	
}
