package com.sg.eyedoctor.common.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 * 
 * @author zhy
 * 
 */
public class KeyBoardUtils
{

//	public static void openKeybord(Context context,EditText editText)
//	{
//		InputMethodManager imm = (InputMethodManager) context
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
////		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
////				InputMethodManager.HIDE_IMPLICIT_ONLY);
//	}
//
//	public static void closeKeybord(Activity activity,EditText editText)
//	{
//		InputMethodManager imm = (InputMethodManager) activity
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//	}

	/**
	 * 打开软键盘
	 */
	public static void showKeyboard(EditText editText) {

		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();

		InputMethodManager inputManager =
				(InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}

	/**
	 * 关闭软键盘
	 */
	public static void hideKeyboard(EditText editText) {
		((InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
