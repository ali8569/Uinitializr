package ir.markazandroid.uinitializr.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        FileUtils.copyInputStreamToFile(getAssets().open("Police_V1.2.4_DS.apk"),
                policeApk);
        installInSystem(policeApk.getPath(), "Police", "Police.apk", () -> {
            try {
                installLauncher();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void installLauncher() throws IOException {
        File launcherApk = new File(Environment.getExternalStorageDirectory() + "/police/Launcher.apk");

        FileUtils.copyInputStreamToFile(getAssets().open("Launcher_V1.2.0_DS.apk"),
                launcherApk);
        installInSystem(launcherApk.getPath(), "Launcher", "Launcher.apk", () -> {
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
        installInSystem(launcherApk.getPath(), "Advertiser", "Advertiser.apk", () -> {

            runOnUiThread(() -> Toast.makeText(getApplication(), "OK", Toast.LENGTH_LONG).show());
            /*try {
                reboot();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        });
    }


    public synchronized void installInSystem(String apkPath, String folderName, String fileName, Runnable listener) {
        new Thread(() -> {
            Process process = null;
            try {

                process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(//"stop;"+
                        "mount -o rw,remount /system;" +
                                "mkdir -p /system/priv-app/" + folderName + ";" +
                                "mv " + apkPath + " /system/priv-app/" + folderName + "/" + fileName + ";" +
                                "chmod 755 /system/priv-app/" + folderName + ";" +
                                "chmod -R 644 /system/priv-app/" + folderName + ";" +
                                "chmod 755 /system/priv-app/" + folderName + ";" +
                                "chown root:root /system/priv-app/" + folderName + ";" +
                                "chown root:root /system/priv-app/" + folderName + "/" + fileName + ";exit\n" +
                                "reboot\n");
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
