package com.arup.yahmoney;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageCompressor {
    private static class Size {
        int height;
        int width;
        public Size(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    private static void compress(Size size) {
        int h = size.getHeight();
        int w = size.getWidth();
        if(h + w == 1200) {
            if(h > w) {
                size.setWidth(w * (600 / h));
                size.setHeight(600);
            }
            else {
                size.setHeight(h * (600 / h));
                size.setWidth(600);
            }
        }
    }

    public static Bitmap compress(Bitmap image) {
        Size size = new Size(image.getHeight(), image.getWidth());
        compress(size);
        image = Bitmap.createScaledBitmap(image, size.getHeight(),  size.getWidth(), false);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.WEBP, 4, out);

        image = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return image;
    }
}
