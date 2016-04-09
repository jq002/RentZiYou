package com.httpconnet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.entity.Para;

import android.util.Log;



public class Httptool {
	
	// doPost();
			public static String postVisitWeb(String url, List<Para> list) {
				String str = "";

				try {
					// 发出请求
					URL geturl = new URL(url);

					HttpURLConnection httpURLConnection = (HttpURLConnection) geturl
							.openConnection();
					httpURLConnection.setDoInput(true);
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setRequestMethod("POST");
					httpURLConnection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded; charset=utf-8");// 设置内容类型为表单格式
					
					httpURLConnection.connect();

					// 参数拆包
					DataOutputStream dataOutputStream = new DataOutputStream(
							httpURLConnection.getOutputStream());
					
					String strTemp = "";
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							Para p = list.get(i);
							String pname = p.pname;
							String pvalue = p.pvalue;
							strTemp = "";
							if (i == 0)
								strTemp = pname + "=" + pvalue;
							else
								strTemp = "&" + pname + "=" + pvalue;
							Log.v("strTemp",strTemp);
							dataOutputStream.write(strTemp.getBytes());
							dataOutputStream.flush();
						}
						//Log.v("strTemp",strTemp);
					}

					// 读取响应
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(httpURLConnection.getInputStream(),
									"utf-8"));
					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						line=line+"\n";
						System.out.println(line);
						str = str + line;
					}
					bufferedReader.close();
					httpURLConnection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return str;

			}


}
