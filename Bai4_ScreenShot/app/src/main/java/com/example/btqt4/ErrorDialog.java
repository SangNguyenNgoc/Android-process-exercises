package com.example.btqt4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorDialog {
    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Điều này sẽ được gọi khi người dùng nhấp vào nút "OK"
                        dialog.dismiss(); // Đóng dialog
                    }
                })
                .show();
    }
}
