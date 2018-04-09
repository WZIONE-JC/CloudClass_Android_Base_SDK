package com.kaola.qrcodescanner.qrcode.decode;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.Result;
import com.kaola.qrcodescanner.qrcode.utils.QrUtils;

/**
 * Created by xingli on 1/4/16.
 *
 * 解析图像二维码线程
 */
public class DecodeImageThread implements Runnable {
    private static final int MAX_PICTURE_PIXEL = 256;
    private byte[] mData;
    private int[] mIntDatas;

    private int mWidth;
    private int mHeight;
    private String mImgPath;
    private DecodeImageCallback mCallback;

    public DecodeImageThread(String imgPath, DecodeImageCallback callback) {
        this.mImgPath = imgPath;
        this.mCallback = callback;
    }

    @Override
    public void run() {
        if (null == mData) {
            if (!TextUtils.isEmpty(mImgPath)) {
//                Bitmap bitmap = BitmapFactory.decodeFile(mImgPath);
                Bitmap bitmap = QrUtils.decodeSampledBitmapFromFile(mImgPath, MAX_PICTURE_PIXEL, MAX_PICTURE_PIXEL);
                this.mData = QrUtils.getYUV420sp(bitmap.getWidth(), bitmap.getHeight(), bitmap);
                this.mWidth = bitmap.getWidth();
                this.mHeight = bitmap.getHeight();
//                mIntDatas = new int[mWidth * mHeight];
//                bitmap.getPixels(mIntDatas, 0, mWidth, 0, 0, mWidth, mHeight);
            }
        }

        if (mData == null || mData.length == 0 || mWidth == 0 || mHeight == 0) {
            if (null != mCallback) {
                mCallback.decodeFail(0, "No image string");
            }
            return;
        }

        final Result result = QrUtils.decodeImage(mData, mWidth, mHeight);
//        final Result result = QrUtils.decodeImage(mIntDatas, mWidth, mHeight);

        if (null != mCallback) {
            if (null != result) {
                mCallback.decodeSucceed(result);
            } else {
                mCallback.decodeFail(0, "Decode image failed.");
            }
        }
    }
}