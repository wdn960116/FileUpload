package com.wdn.fileupload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wdn.loopj.android.http.AsyncHttpClient;
import com.wdn.loopj.android.http.AsyncHttpResponseHandler;
import com.wdn.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText et_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_path = findViewById(R.id.et_path);
        Button btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        File file = new File(getCacheDir(), "upload.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
          writer.write("upload");
          writer.newLine();
          writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void upload(){
       String path=et_path.getText().toString().trim();
        File file = new File(path);
        if (TextUtils.isEmpty(path)){
            Toast.makeText(this, "路径不能为空", Toast.LENGTH_SHORT).show();
        }
        if (file.exists()&file.length()>0){
            try {
                AsyncHttpClient client=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put("profile_picture",file);
                client.post("http://192.168.1.6:8081/servlet/UploadServlet", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "文件不存在或者内容为空", Toast.LENGTH_SHORT).show();
        }
    }

}
