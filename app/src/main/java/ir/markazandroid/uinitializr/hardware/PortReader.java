package ir.markazandroid.uinitializr.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import ir.markazandroid.uinitializr.hardware.serial.SerialPort;
import ir.markazandroid.uinitializr.util.Roozh;

/**
 * Coded by Ali on 01/03/2018.
 */

public class PortReader extends Thread {

    public void setListener(PortReadDataListener listener) {
        this.listener = listener;
    }

    public interface PortReadDataListener{
        void onPortDataReceive(String data);
    }

    private Context context;
    private Handler handler;
    private boolean isBlocked=false;
    private InputParser inputParser;
    private SerialPort serialPort;
    boolean isCancelled =false;
    public static String lastData="NA";
    private InputStream inputStream;
    private Timer timer;
    private int blockAcc,unBlockAcc;
    private PortReadDataListener listener;
    private String portName;

    public PortReader(Context context, String portName) {
        this.context = context;
        this.portName = portName;
        blockAcc =0;
        unBlockAcc=0;


        /*for (SerialPort port:ports){
            Log.e("port", "PortReader: "+port.getDescriptivePortName()+" : "+port.getSystemPortName() );
        }*/



        handler=new Handler(context.getMainLooper());
        inputParser=new InputParser('\n', cmd -> {
            try {
                Log.e("command",cmd);
                if (listener!=null) listener.onPortDataReceive(cmd);
            /*if(cmd.equals("OFF")){
                isBlocked=true;
                sendBlockViewSignal();
            }*/
                cmd=cmd.replace('$','&')
                        .replaceAll("&","")
                        .replaceAll("#","")
                        .replaceAll("\r","")
                        .replaceAll("\n","");
                lastData=cmd;


                /*String[] dataArray = cmd.split(";");
                Map<String,String> dataMap = new HashMap<>();
                for(String data:dataArray){
                    String[] d = data.split(":");
                    dataMap.put(d[0],d[1]);
                }*/
            }catch (Exception ignored){

            }

        });
        inputParser.init();


        /*PortInfo[] list = Serial.listPorts();

        for(PortInfo info:list){
            Log.e("port", "PortReader: "+info.description+" : "+info.hardwareId+" : "+info.port);
        }*/

        init();
    }


    public void init(){
        try {
            serialPort = new SerialPort(new File(portName), 9600, 0);
            inputStream=serialPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {


        //port.open(connection);
        //port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        byte buffer[] = new byte[1024];

        //isCancelled
        while (!isCancelled) {
            try {
                String read = readChar(inputStream,buffer);
                Log.e("read",read);
                inputParser.addInput(read);
            } catch (Exception e) {
                e.printStackTrace();
                init();
                //handler.post(()-> Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show());
            }


            //SystemClock.sleep(2000);
                /*if (Integer.parseInt(in.charAt(2)+"")==1)
                    if (!isBlocked) {
                        isBlocked=true;
                        sendBlockViewSignal();
                    }
                else if (isBlocked) {
                        isBlocked=false;
                        sendUnBlockViewSignal();
                    }*/

        }


        /*serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        try {
            while (true)
            {
                byte[] readBuffer = new byte[1024];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                Log.e("Read",numRead+"");
            }
        } catch (Exception e) { e.printStackTrace(); }
        serialPort.closePort();*/
    }

    public String readChar(InputStream inputStream,byte[] buffer) throws IOException {
        int numRead = inputStream.read(buffer);
        while (numRead<1){
            try {
                if (isCancelled) break;
                Thread.sleep(1);
                numRead = inputStream.read(buffer);
            } catch (Exception e) {
                handler.post(()-> Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show());
                e.printStackTrace();
                break;
            }
        }
        return new String(buffer,0,numRead);
    }

    /*private void showToast(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
            }
        });
    }
*/

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            /*PortReader portReader = new PortReader(context);
                            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                            threadPoolExecutor.execute(portReader);*/
                        }
                    }
                    else {
                        Log.d("tag", "permission denied for device " + device);
                    }
                }
            }
        }
    };

    public void close(){
        try {
            isCancelled=true;
            if (timer!=null)
                timer.cancel();
            if(serialPort!=null) {
                serialPort.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean write(String toWrite){
        try {
            serialPort.getOutputStream().write(toWrite.getBytes());
            Log.e("wrote:",toWrite);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void save(String finalBitmap) {

        //getFilesDir()
        //openFileInput()
        //openFileOutput()
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/uinitializr/logs");
        myDir.mkdirs();
        String fname = Roozh.getCurrentTimeNo()+".txt";
        File file = new File (myDir, fname);
        try {
            FileWriter out =new FileWriter(file,true);
            out.append(Roozh.getTime(System.currentTimeMillis()))
                    .append("  --  ")
                    .append(finalBitmap)
                    .append("\r\n");
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
