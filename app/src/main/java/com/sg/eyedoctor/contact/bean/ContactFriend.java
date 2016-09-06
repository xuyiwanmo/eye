package com.sg.eyedoctor.contact.bean;

public class ContactFriend extends ContactLetter{

	public String name;   //显示的数据
	public String sortLetters;  //显示数据拼音的首字母
	public String mPhone;
	public int mState;

	public ContactFriend(String name, String phone, int state) {
		this.name = name;
		mPhone = phone;
		mState = state;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
