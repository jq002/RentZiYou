package com.jqjava.lesson5;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

public class MainApplication extends Application { 

private ArrayList<Activity>activities=new ArrayList<Activity>();
private static MainApplication instance; 

private MainApplication() 
{ 
} 
//����ģʽ�л�ȡΨһ��MyApplicationʵ�� 
public static MainApplication getInstance() 
{ 
if(null == instance) 
{ 
instance = new MainApplication(); 
} 
return instance; 
} 
//���Activity�������� 
public void addActivity(Activity activity) 
{ 
	activities.add(activity);
} 
public void deleteActivity(Activity activity){
	activities.remove(activity);
}
//finish 
public void exit() 
{ 
  for(Activity activity:activities){
	  activity.finish();
  }
  activities.clear();
 
} 
} 

