package com.tools;

import java.util.Random;  

import android.graphics.Bitmap;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Bitmap.Config;  

public class Code {  
    
  private static final char[] CHARS = {  
      '2', '3', '4', '5', '6', '7', '8', '9',   
  };  
  private static Code bmpCode; 
  public static Code getInstance() { 
	  if(bmpCode == null)  
          bmpCode = new Code();  
      return bmpCode;  
  }  
    
  //default settings  
  private static final int DEFAULT_CODE_LENGTH = 4;  
  private static final int DEFAULT_FONT_SIZE = 25;  
  private static final int DEFAULT_LINE_NUMBER = 2;  
  private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 15, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 20;  
  private static final int DEFAULT_WIDTH = 75, DEFAULT_HEIGHT = 40;  
    
  //settings decided by the layout xml  
  //canvas width and height  
  private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;   
    
  //random word space and pading_top  
  private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT,   
          base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;  
    
  //number of chars, lines; font size  
  private int codeLength = DEFAULT_CODE_LENGTH, line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;  
    
  //variables  
  private static String code="";  
  private int padding_left, padding_top;  
  private Random random = new Random();  
  //验证码图片  
  public Bitmap createBitmap() { 
	  code = createCode();
      padding_left = 0;  
        
      Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);   
      Canvas c = new Canvas(bp);  

  
      c.drawColor(Color.WHITE);  
      Paint paint = new Paint();  
      paint.setTextSize(font_size);  
        
      for (int i = 0; i < code.length(); i++) {  
    	  randomPadding();  
          c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);  
      }  
        
      c.save( Canvas.ALL_SAVE_FLAG );//保存    
      c.restore();//  
      return bp;  
  }  
    
  public static String getCode() {  
      return code;  
  }  
    
  //验证码  
  private String createCode() {  
      StringBuilder buffer = new StringBuilder();  
      for (int i = 0; i < codeLength; i++) {  
          buffer.append(CHARS[random.nextInt(CHARS.length)]); 
          
      }  
      return buffer.toString();  
  }  
  private void randomPadding() {  
      padding_left += base_padding_left + random.nextInt(range_padding_left);  
      padding_top = base_padding_top + random.nextInt(range_padding_top);  
  }  
}  
