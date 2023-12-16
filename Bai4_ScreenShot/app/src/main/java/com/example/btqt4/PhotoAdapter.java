package com.example.btqt4;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Bitmap> photoIds; // Danh sách ID ảnh

    public PhotoAdapter(Context context, ArrayList<Bitmap> photoIds) {
        mContext = context;
        this.photoIds = photoIds;
    }

    @Override
    public int getCount() {
        // Trả về số lượng ảnh
        return photoIds.size();
    }

    @Override
    public Object getItem(int position) {
        // Trả về ảnh tại vị trí position
        return photoIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Trả về ID của ảnh tại vị trí position
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo và trả về View để hiển thị ảnh tại vị trí position
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Đặt chiều rộng là match_parent hoặc giá trị cụ thể
                    500)); // Đặt chiều cao là giá trị cụ thể, có thể thay đổi tùy ý
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        // Đặt ảnh từ danh sách vào ImageView
        Bitmap photoBitmap = (Bitmap) getItem(position);
        imageView.setImageBitmap(photoBitmap);

        return imageView;
    }

}
