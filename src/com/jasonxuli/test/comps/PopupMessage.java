package com.jasonxuli.test.comps;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jasonxuli.test.R;

public class PopupMessage {

	private Activity _activity;
	private PopupWindow _popup;
	private View _view;
	
	private String _title;
	private String _message;
	private String _okLabel;
	
	
	/**
	 * without cancel button; press ok button will dismiss this popup.
	 * @param activity
	 * @param title
	 * @param message
	 * @param okLabel
	 */
	public PopupMessage(Activity activity, String title, String message, String okLabel)
	{
		_activity = activity;
		_title = title;
		_message = message;
		_okLabel = okLabel;
		
		initPopup();
	}
	
	/**
	 * okLabel = "OK";
	 * without cancel button; press ok button will dismiss this popup.
	 * @param activity
	 * @param title
	 * @param message
	 */
	public PopupMessage(Activity activity, String title, String message)
	{
		_activity = activity;
		_title = title;
		_message = message;
		_okLabel = activity.getString(R.string.okLabel);
		
		initPopup();
	}
	
	
	private void initPopup()
	{
		LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_view = inflater.inflate(R.layout.popup_common, null);
		_popup = new PopupWindow(_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		_popup.setOutsideTouchable(true);
		_popup.setFocusable(true);
		setTitle(_title);
		setMessage(_message);
		setOKButton(_okLabel);
		((Button) _view.findViewById(R.id.popup_common_cancel)).setVisibility(View.GONE);
		
		((Button) _view.findViewById(R.id.popup_common_ok)).setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				_popup.dismiss();
			}
		});
	}
	
	
	public void show()
	{
		_popup.showAtLocation(_activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
	}
	
	public void dismiss()
	{
		_popup.dismiss();
	}
	
	
	public void setTitle(String title)
	{
		((TextView) _view.findViewById(R.id.popup_common_title)).setText(title);
	}
	
	public void setMessage(String message)
	{
		((TextView) _view.findViewById(R.id.popup_common_message)).setText(message);
	}
	
	public void setOKButton(String okLabel)
	{
		((Button) _view.findViewById(R.id.popup_common_ok)).setText(okLabel);
	}
	
	
}
