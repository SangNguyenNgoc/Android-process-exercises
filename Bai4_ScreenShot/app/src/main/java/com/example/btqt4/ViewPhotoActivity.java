package com.example.btqt4;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ViewPhotoActivity extends AppCompatActivity {
    String[] imageList;
    ArrayList<Bitmap> photoIds = new ArrayList<>();


    private void GetImage() {
        File picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!picturesDirectory.exists()) {
            ErrorDialog.showErrorDialog(this, "Không thấy thư mục");
            return;
        }

        // Lấy danh sách tên tất cả các tệp tin trong thư mục Pictures
        imageList = picturesDirectory.list();

        if (imageList == null) {
            ErrorDialog.showErrorDialog(this, "Thư mục rỗng");
            return;
        }

        // Tạo danh sách các ID ảnh từ tên tệp tin
        for (String imageName : imageList) {
            // Tạo đường dẫn đầy đủ đến tệp tin trong thư mục ứng dụng
//            System.out.println(imageName);
            File imagePath = new File(picturesDirectory, imageName);
            if (!imagePath.exists()) {
                ErrorDialog.showErrorDialog(this, "Lỗi lấy ảnh");
                return;
            }
            // Tạo Uri từ File bằng cách sử dụng FileProvider
            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imagePath);

            // Mở InputStream để đọc ảnh từ thư mục Pictures
            try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                // Tạo Bitmap từ InputStream và đặt ID của ảnh vào danh sách
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                photoIds.add(bitmap); // Sử dụng hashCode của tên tệp làm ID (cần kiểm tra xem có đảm bảo không)
                System.out.println(imageName.hashCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        GetImage();

        // Hiển thị danh sách ảnh đã chụp trong một GridView hoặc RecyclerView
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new PhotoAdapter(this, photoIds));

    }
}
