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

public class PopupConfirm {

	private Activity _activity;
	private PopupWindow _popup;
	private View _view;
	
	private String _title;
	private String _message;
	private String _okLabel;
	private String _cancelLabel;
	
//	private OnClickListener onOKButtonClickListener;
//	private OnClickListener onCancelButtonClickListener;
	
	
	/**
	 * okLabel = "OK", cancelLabel = "Cancel"
	 * default "cancelHandler" just dismisses this popup.
	 * @param activity
	 * @param title
	 * @param message
	 */
	public PopupConfirm(Activity activity, String title, String message)
	{
		_activity = activity;
		_title = title;
		_message = message;
		_okLabel = activity.getString(R.string.okLabel);
		_cancelLabel = activity.getString(R.string.cancelLabel);
		
		initPopup();
	}
	
	/**
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 * @param okLabel
	 * @param cancelLabel
	 */
	public PopupConfirm(Activity activity, String title, String message, String okLabel, String cancelLabel)
	{
		_activity = activity;
		_title = title;
		_message = message;
		_okLabel = okLabel;
		_cancelLabel = cancelLabel;
		
		initPopup();
	}
	
	
	
	private void initPopup()
	{
		LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_view = inflater.inflate(R.layout.popup_common, null);
		_popup = new PopupWindow(_view, 400, LayoutParams.WRAP_CONTENT);
		_popup.setOutsideTouchable(true);
		_popup.setFocusable(true);
		setTitle(_title);
		setMessage(_message);
		setOKButton(_okLabel, null);
		setCancelButton(_cancelLabel, null);
		
		((Button) _view.findViewById(R.id.popup_common_cancel)).setOnClickListener(defaultCancelListener);
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
	
	public void setOKButton(String okLabel, OnClickListener clickListener)
	{
		Button okButton = (Button) _view.findViewById(R.id.popup_common_ok);
		okButton.setText(okLabel);
		okButton.setOnClickListener(clickListener);
	}
	
	public void setOKButton(OnClickListener clickListener)
	{
		Button okButton = (Button) _view.findViewById(R.id.popup_common_ok);
		okButton.setOnClickListener(clickListener);
	}
	
	public void setCancelButton(String cancelLabel, OnClickListener clickListener)
	{
		Button cancelButton = (Button) _view.findViewById(R.id.popup_common_cancel);
		cancelButton.setText(cancelLabel);
		cancelButton.setOnClickListener(clickListener);
	}
	
	public void setCancelButton(OnClickListener clickListener)
	{
		Button cancelButton = (Button) _view.findViewById(R.id.popup_common_cancel);
		cancelButton.setOnClickListener(clickListener);
	}
	
	
	final OnClickListener defaultCancelListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) {
			_popup.dismiss();
		}
	};
	
}
