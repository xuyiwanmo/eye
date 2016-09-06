package com.sg.eyedoctor.common.utils;

import com.sg.eyedoctor.contact.bean.ContactLetter;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 * 比较首字母排序
 */
public class PinyinComparator implements Comparator<ContactLetter> {

	public int compare(ContactLetter o1, ContactLetter o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
