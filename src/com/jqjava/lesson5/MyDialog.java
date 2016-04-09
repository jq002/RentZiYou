package com.jqjava.lesson5;

import com.example.rentziyou.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @文件名称: MyDialog.java
 * @功能描述: 自定义dialog
 * @版本信息: Copyright (c)2014
 * @�?��人员: vincent
 * @版本日志: 1.0
 * @创建时间: 2014�?�?8�?下午1:45:38
 */
public class MyDialog extends Dialog implements OnClickListener {
	private TextView leftTextView, rightTextView;
	private IDialogOnclickInterface dialogOnclickInterface;
	private Context context;

	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog);

		rightTextView = (TextView) findViewById(R.id.textview_two);
		
		rightTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dialogOnclickInterface = (IDialogOnclickInterface) context;
		switch (v.getId()) {
	
		case R.id.textview_two:
			dialogOnclickInterface.rightOnclick();
			break;
		default:
			break;
		}
	}

	public interface IDialogOnclickInterface {
		

		void rightOnclick();
	}
}
