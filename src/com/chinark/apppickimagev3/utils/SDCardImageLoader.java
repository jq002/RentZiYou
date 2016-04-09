package com.chinark.apppickimagev3.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.rentziyou.R;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

//import com.chinark.apppickimagev3.R;

/**
 * 从SDCard异步加载图片
 *
 * @author hanj 2013-8-22 19:25:46
 */
public class SDCardImageLoader {
    //缓存
    private LruCache<String, Bitmap> imageCache;
    //固定2个线程来执行任务
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Handler handler = new Handler();

    private int screenW, screenH;

    public SDCardImageLoader(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // 设置图片缓存大小为程序最大可用内存的1/8
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    private Bitmap loadDrawable(final int smallRate, final String filePath,
                                final ImageCallback callback) {
        //如果缓存过就从缓存中取出数据
//        if (imageCache.get(filePath) != null) {
//            return imageCache.get(filePath);
//        }

        // 如果缓存没有则读取SD卡
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, opt);

                    // 閼惧嘲褰囬崚鎷岀箹娑擃亜娴橀悧鍥╂畱閸樼喎顬婄�硅棄瀹抽崪宀勭彯鎼达拷
                    int picWidth = opt.outWidth;
                    int picHeight = opt.outHeight;

                    //鐠囪褰囬崶鍓у婢惰精瑙﹂弮鍓佹纯閹恒儴绻戦崶锟�
                    if (picWidth == 0 || picHeight == 0) {
                        return;
                    }

                    //閸掓繂顬婇崢瀣級濮ｆ柧绶�
                    opt.inSampleSize = smallRate;
                    // 閺嶈宓佺仦蹇曟畱婢堆冪毈閸滃苯娴橀悧鍥с亣鐏忓繗顓哥粻妤�鍤紓鈺傛杹濮ｆ柧绶�
                    if (picWidth > picHeight) {
                        if (picWidth > screenW)
                        	Log.v("screenw...",String.valueOf(screenW));
                            opt.inSampleSize *= picWidth / screenW;
                        //Log.v("screenw...",String.valueOf(screenW));
                    } else {
                        if (picHeight > screenH)
                        	Log.v("screenH....",String.valueOf(screenH));
                            opt.inSampleSize *= picHeight / screenH;
                        //Log.v("screenH....",String.valueOf(screenH));
                    }

                    //鏉╂瑦顐奸崘宥囨埂濮濓絽婀撮悽鐔稿灇娑擄拷閲滈張澶婂剼缁辩姷娈戦敍宀�绮℃潻鍥╃級閺�鍙ョ啊閻ㄥ垺itmap
                    opt.inJustDecodeBounds = false;
                    final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);
                    //鐎涙ê鍙唌ap
                    imageCache.put(filePath, bmp);

                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(bmp);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }

    /**
     * 瀵倹顒炵拠璇插絿SD閸椻�虫禈閻楀浄绱濋獮鑸靛瘻閹稿洤鐣鹃惃鍕槷娓氬绻樼悰灞藉竾缂傗晪绱欓張锟姐亣娑撳秷绉存潻鍥х潌楠炴洖鍎氱槐鐘虫殶閿涳拷
     *
     * @param smallRate 閸樺缂夊В鏂剧伐閿涘奔绗夐崢瀣級閺冩儼绶崗锟介敍灞绢劃閺冭泛鐨㈤幐澶婄潌楠炴洖鍎氱槐鐘虫殶鏉╂稖顢戞潏鎾冲毉
     * @param filePath  閸ュ墽澧栭崷鈯緿閸楋紕娈戦崗銊ㄧ熅瀵帮拷
     * @param imageView 缂佸嫪娆�
     */
    public void loadImage(int smallRate, final String filePath, final ImageView imageView) {

        Bitmap bmp = loadDrawable(smallRate, filePath, new ImageCallback() {

            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.empty_photo);
                    }
                }
            }
        });

        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }

    }


    // 鐎电懓顧囬悾灞界磻閺�鍓ф畱閸ョ偠鐨熼幒銉ュ經
    public interface ImageCallback {
        // 濞夈劍鍓� 濮濄倖鏌熷▔鏇熸Ц閻€劍娼电拋鍓х枂閻╊喗鐖ｇ�电钖勯惃鍕禈閸嶅繗绁┃锟�
        public void imageLoaded(Bitmap imageDrawable);
    }
}
