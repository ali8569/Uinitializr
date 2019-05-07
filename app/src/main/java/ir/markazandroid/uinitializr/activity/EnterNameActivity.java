package ir.markazandroid.uinitializr.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import ir.markazandroid.uinitializr.R;

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

        username.setHint("نام دستگاه");

        submit.setOnClickListener(v -> {
            if (valid()) {
                try {
                    submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
                edit().putString("ir.markazandroid.uinitializr.EnterNameActivity.name", username.getText().toString())
                .commit();

        installPolice();

    }

    public synchronized void installPolice() throws IOException {

        File policeApk = new File(Environment.getExternalStorageDirectory() + "/police/Police.apk");

        FileUtils.copyInputStreamToFile(getAssets().open("Police_V1.2.2_IOT20A.apk"),
                policeApk);
        installInSystem(policeApk, () -> {
            try {
                installLauncher();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void installLauncher() throws IOException {
        File launcherApk = new File(Environment.getExternalStorageDirectory() + "/police/Launcher.apk");

        FileUtils.copyInputStreamToFile(getAssets().open("Launcher_V1.1.0_IOT20A.apk"),
                launcherApk);
        installInSystem(launcherApk, () -> {
            try {
                installAdvertiser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void installAdvertiser() throws IOException {
        File launcherApk = new File(Environment.getExternalStorageDirectory() + "/police/Advertiser.apk");

        FileUtils.copyInputStreamToFile(getAssets().open("Advertiser_V7.0.2_PG.apk"),
                launcherApk);
        installInSystem(launcherApk, () -> {
            try {
                reboot();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized void installInSystem(File apkFile, Runnable listener) {

        new Thread(() -> {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(//"stop;"+
                        "mount -o rw,remount /system;" +
                                "cp " + apkFile.getPath() + " /system/app/" + apkFile.getName() + ";" +
                                "rm " + apkFile.getPath() + ";" +
                                "chmod -R 644 /system/app/" + apkFile.getName() + ";" +
                                "chown root:root /system/app/" + apkFile.getName() + ";exit\n");
                os.flush();
                process.waitFor();
                listener.run();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void reboot() throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        os.writeBytes("reboot\n");
        os.flush();
        process.waitFor();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
