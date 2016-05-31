package es.pulimento.logswitch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnEnable).setOnClickListener(this);
        findViewById(R.id.btnDisable).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Boolean result;
        if(v.getId() == R.id.btnEnable) {
            result = execCommands("mv /system/logd /system/logd.bak");
            Toast.makeText(MainActivity.this, "" + result, Toast.LENGTH_LONG).show();
        } else if(v.getId() == R.id.btnDisable) {
            result = execCommands("mv /system/logd.bak /system/logd");
            Toast.makeText(MainActivity.this, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    public Boolean execCommands(String... command) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            for(int i = 0; i < command.length; i++) {
                os.writeBytes(command[i] + "\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
