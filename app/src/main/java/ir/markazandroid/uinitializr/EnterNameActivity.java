package ir.markazandroid.uinitializr;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class EnterNameActivity extends AppCompatActivity {

    private EditText username;
    private TextInputLayout usernameLayout;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        username=findViewById(R.id.username);
        usernameLayout=findViewById(R.id.username_layout);
        submit=findViewById(R.id.submit);

        username.setHint("UWDxxxx");
        username.setText("UWD");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    try {
                        submit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        int a;
        a=3;
    }

    private boolean valid() {
        boolean valid =true;
        String name = username.getText().toString();
        usernameLayout.setErrorEnabled(false);

        if (name.isEmpty()){
            usernameLayout.setError("نام کاربری نمی تواند خالی باشد");
            valid=false;
        }
        return valid;
    }


    private void submit() throws IOException {
        getSharedPreferences("ir.markazandroid.uinitializr.SETTINGS",MODE_WORLD_READABLE).
                edit().putString("ir.markazandroid.uinitializr.EnterNameActivity.name",username.getText().toString())
                .commit();

        File policeApk=new File(Environment.getExternalStorageDirectory()+"/police/app.apk");

        FileUtils.copyInputStreamToFile(getAssets().open("Police_V1.1.41_TB.apk"),
              policeApk);

        updateTBPolice(policeApk.getPath());

    }

    public synchronized void updateTBPolice(String apkPath) throws IOException {

        Process process=Runtime.getRuntime().exec("su");
        Log.e("Up","got here2");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        Log.e("Path",apkPath);
        os.writeBytes(//"stop;"+
                "mount -o rw,remount /system;"+
                        "cp "+apkPath+" /system/app/Police.apk;"+
                        "rm "+apkPath+";"+
                        "chmod -R 644 /system/app/Police.apk;"+
                        "chown root:root /system/app/Police.apk;"+
                        "reboot\n");
        os.flush();
        Log.e("Up","got here3");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
