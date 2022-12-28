package pk.mohammadadnan.esgsmartapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import static pk.mohammadadnan.esgsmartapp.Constants.ACTION_CONNECTED;
import static pk.mohammadadnan.esgsmartapp.Constants.ACTION_DISCONNECTED;
import static pk.mohammadadnan.esgsmartapp.Constants.ACTION_SCAN;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ACCELEROMETER;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_AIRPRESSURE;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_AIRQUALITY;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_BATTERY;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_CAMERA;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ECG;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_FAN1;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_FAN2;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_FLED;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_HEART;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_POWER;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_SPO2;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ULED1;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ULED2;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ULED3;
//import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_ULED4;
import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_CHAR_WRITE;
import static pk.mohammadadnan.esgsmartapp.Constants.BLE_UUID_SERVICE;
import static pk.mohammadadnan.esgsmartapp.Constants.EXTRAS_MESSAGE;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ACCELEROMETER;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_AIRPRESSURE;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_AIRQUALITY;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_BATTERY;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_CAMERA;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ECG;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_FAN1;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_FAN2;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_FLED;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_HEART;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_POWER;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_SPO2;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ULED1;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ULED2;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ULED3;
import static pk.mohammadadnan.esgsmartapp.Constants.MESSAGE_ULED4;
import static pk.mohammadadnan.esgsmartapp.Constants.SCAN_PERIOD;
import static pk.mohammadadnan.esgsmartapp.Utils.showMessage;
import static pk.mohammadadnan.esgsmartapp.Utils.showMessageDebug;

public class BluetoothService {

    public static BluetoothService INSTANCE;
    public static BluetoothAdapter bluetoothAdapter;

    public static Context context;
    private Handler handler = new Handler();

    private boolean isConnected = false;
    private BluetoothGatt bluetoothGatt;
    private ArrayList<BluetoothDevice> bondedDevices = new ArrayList<>();
    private BluetoothLeScanner bluetoothLeScanner;

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(result.getDevice().getName()!=null && !result.getDevice().getName().isEmpty()) {
                for(BluetoothDevice bluetoothDevice:bondedDevices){
                    if(bluetoothDevice.getAddress().equals(result.getDevice().getAddress())){
                        return;
                    }
                }
                bondedDevices.add(result.getDevice());
                broadcastUpdate(ACTION_SCAN,result.getDevice().getAddress() +"space"+result.getDevice().getName());

//                ArrayList<String> deviceList = new ArrayList<String>();
//                Log.i("Names::::",result.getDevice().getName());
//                deviceList.add(result.getDevice().getName());
//                ArrayAdapter adapter = new ArrayAdapter<String>(R.id.listView,deviceList);
//                ListView listview = (ListView)findViewById(R.id.listView);
//                listview.setAdapter(adapter);
                if(result.getDevice().getName().equals("ESP32")){
                    result.getDevice().createBond();
                    bluetoothGatt = result.getDevice().connectGatt(context,false,gattCallback);
                }
            }
        }
    };

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String deviceAddress = gatt.getDevice().getAddress();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    isConnected = true;
                    bluetoothGatt = gatt;
                    showMessage("Connection to bluetooth device "+deviceAddress+ " successful!");
                    broadcastUpdate(ACTION_CONNECTED,"");
                    bluetoothGatt.discoverServices();
                }else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    isConnected = false;
                    showMessage("Disconnected from bluetooth device "+deviceAddress);
                    broadcastUpdate(ACTION_DISCONNECTED,"");
                    gatt.close();
                }
            }else{
                isConnected = false;
                showMessage("Error "+status+" encountered for "+deviceAddress);
                broadcastUpdate(ACTION_DISCONNECTED,"");
                gatt.close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

//            BluetoothGattCharacteristic uled1 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ULED1);
//            BluetoothGattCharacteristic uled2 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ULED2);
//            BluetoothGattCharacteristic uled3 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ULED3);
//            BluetoothGattCharacteristic uled4 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ULED4);
//            BluetoothGattCharacteristic fled = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_FLED);
//            BluetoothGattCharacteristic fan1 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_FAN1);
//            BluetoothGattCharacteristic fan2 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_FAN2);
//            BluetoothGattCharacteristic battery = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_BATTERY);
//            BluetoothGattCharacteristic spo2 = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_SPO2);
//            BluetoothGattCharacteristic airQuality = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_AIRQUALITY);
//            BluetoothGattCharacteristic airPressure = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_AIRPRESSURE);
//            BluetoothGattCharacteristic camera = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_CAMERA);
//            BluetoothGattCharacteristic power = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_POWER);
//            BluetoothGattCharacteristic heart = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_HEART);
//            BluetoothGattCharacteristic ecg = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ECG);
//            BluetoothGattCharacteristic accelerometer = bluetoothGatt.getService(BLE_UUID_SERVICE).getCharacteristic(BLE_UUID_CHAR_ACCELEROMETER);
//            if(uled1 != null){
//                bluetoothGatt.readCharacteristic(uled1);
//            }
//            if(uled2 != null){
//                bluetoothGatt.readCharacteristic(uled2);
//            }
//            if(uled3 != null){
//                bluetoothGatt.readCharacteristic(uled3);
//            }
//            if(uled4 != null){
//                bluetoothGatt.readCharacteristic(uled4);
//            }
//            if(fled != null){
//                bluetoothGatt.readCharacteristic(fled);
//            }
//            if(fan1 != null){
//                bluetoothGatt.readCharacteristic(fan1);
//            }
//            if(fan2 != null){
//                bluetoothGatt.readCharacteristic(fan2);
//            }
//            if(battery != null){
//                bluetoothGatt.readCharacteristic(battery);
//            }
//            if(spo2 != null){
//                bluetoothGatt.readCharacteristic(spo2);
//            }
//            if(airQuality != null){
//                bluetoothGatt.readCharacteristic(airQuality);
//            }
//            if(airPressure != null){
//                bluetoothGatt.readCharacteristic(airPressure);
//            }
//            if(camera != null){
//                bluetoothGatt.readCharacteristic(camera);
//            }
//            if(power != null){
//                bluetoothGatt.readCharacteristic(power);
//            }
//            if(heart != null){
//                bluetoothGatt.readCharacteristic(heart);
//            }
//            if(ecg != null){
//                bluetoothGatt.readCharacteristic(ecg);
//            }
//            if(accelerometer != null){
//                bluetoothGatt.readCharacteristic(accelerometer);
//            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            if(status == BluetoothGatt.GATT_SUCCESS){

                byte[] bytes = characteristic.getValue();
                String str = new String(bytes, StandardCharsets.UTF_8);

//                if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED1)){
//                    broadcastUpdate(MESSAGE_ULED1,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED2)){
//                    broadcastUpdate(MESSAGE_ULED2,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED3)){
//                    broadcastUpdate(MESSAGE_ULED3,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED4)){
//                    broadcastUpdate(MESSAGE_ULED4,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FLED)){
//                    broadcastUpdate(MESSAGE_FLED,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FAN1)){
//                    broadcastUpdate(MESSAGE_FAN1,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FAN2)){
//                    broadcastUpdate(MESSAGE_FAN2,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_BATTERY)){
//                    broadcastUpdate(MESSAGE_BATTERY,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_SPO2)){
//                    broadcastUpdate(MESSAGE_SPO2,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_AIRQUALITY)){
//                    broadcastUpdate(MESSAGE_AIRQUALITY,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_AIRPRESSURE)){
//                    broadcastUpdate(MESSAGE_AIRPRESSURE,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_CAMERA)){
//                    broadcastUpdate(MESSAGE_CAMERA,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_POWER)){
//                    broadcastUpdate(MESSAGE_POWER,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_HEART)){
//                    broadcastUpdate(MESSAGE_HEART,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ECG)){
//                    broadcastUpdate(MESSAGE_ECG,str);
//                }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ACCELEROMETER)){
//                    broadcastUpdate(MESSAGE_ACCELEROMETER,str);
//                }

                if (!bluetoothGatt.setCharacteristicNotification(characteristic, true)) {
                    showMessage("Failed to subscribe to sensor data!");
                }

            }else if(status == BluetoothGatt.GATT_READ_NOT_PERMITTED){
                showMessage("Read not permitted!");
            }else {
                showMessage("Error "+status+" encountered in reading");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            String str = new String(bytes, StandardCharsets.UTF_8);

//            if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED1)){
//                broadcastUpdate(MESSAGE_ULED1,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED2)){
//                broadcastUpdate(MESSAGE_ULED2,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED3)){
//                broadcastUpdate(MESSAGE_ULED3,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ULED4)){
//                broadcastUpdate(MESSAGE_ULED4,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FLED)){
//                broadcastUpdate(MESSAGE_FLED,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FAN1)){
//                broadcastUpdate(MESSAGE_FAN1,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_FAN2)){
//                broadcastUpdate(MESSAGE_FAN2,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_BATTERY)){
//                broadcastUpdate(MESSAGE_BATTERY,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_SPO2)){
//                broadcastUpdate(MESSAGE_SPO2,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_AIRQUALITY)){
//                broadcastUpdate(MESSAGE_AIRQUALITY,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_AIRPRESSURE)){
//                broadcastUpdate(MESSAGE_AIRPRESSURE,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_CAMERA)){
//                broadcastUpdate(MESSAGE_CAMERA,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_POWER)){
//                broadcastUpdate(MESSAGE_POWER,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_HEART)){
//                broadcastUpdate(MESSAGE_HEART,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ECG)){
//                broadcastUpdate(MESSAGE_ECG,str);
//            }else if(characteristic.getUuid().equals(BLE_UUID_CHAR_ACCELEROMETER)){
//                broadcastUpdate(MESSAGE_ACCELEROMETER,str);
//            }
        }
    };

    public BluetoothService(){}

    public static BluetoothService getInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = new BluetoothService();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothService.context = context;
        }
        return INSTANCE;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermissions() {
        // Android M Permission check 
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else {
            return true;
        }
    }

    private void broadcastUpdate(final String action,
                                 final String message) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRAS_MESSAGE, message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void Scan() {
        bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if(!checkPermissions()){
            showMessage("Enable all permissions first!");
            return;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showMessage("Device doesn't support BLE!");
            return;
        }
        if(bluetoothLeScanner == null){
            showMessage("Enable Bluetooth first!");
            return;
        }
        if(!bondedDevices.isEmpty()){
            bondedDevices.clear();
        }
        // Stops scanning after a pre-defined scan period.
        handler.postDelayed(() -> {
            if(bondedDevices.isEmpty()){
                showMessage("No Devices Available!");
            }
            bluetoothLeScanner.stopScan(leScanCallback);
        }, SCAN_PERIOD);

        bluetoothLeScanner.startScan(leScanCallback);
    }


    public void Connect(String macAddress){

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!checkPermissions()){
            showMessage("Enable all permissions first!");
            return;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showMessage("Device doesn't support BLE!");
            return;
        }
        if(bluetoothAdapter == null){
            showMessage("Enable Bluetooth first!");
            return;
        }
        if(macAddress.equals("")){
            showMessage("Go to settings to add a device");
            return;
        }

        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);

        if(bluetoothDevice == null){
            showMessage("Device not found!");
            return;
        }
        Disconnect();
        bluetoothGatt = bluetoothDevice.connectGatt(context,false,gattCallback);
    }

    public void Disconnect () {
        if ( bluetoothGatt != null ) {
            bluetoothGatt.close();
        }
    }

    public void Write(String message){
        if(!isConnected){
            showMessage("Connect to a device first!");
            return;
        }
        Log.i("Characteristic", BLE_UUID_SERVICE.toString());
        BluetoothGattService bluetoothGattService = bluetoothGatt.getService(BLE_UUID_SERVICE);
        System.out.println(bluetoothGattService);
        if (bluetoothGattService == null) {
            showMessage("Service not found!");
            return;
        }
        BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(BLE_UUID_CHAR_WRITE);
        if (bluetoothGattCharacteristic == null) {
            showMessage("Characteristic not found!");
            return;
        }

        try {
            Log.i("TRYING", "TRYING");
            bluetoothGattCharacteristic.setValue(message.getBytes("UTF-8")); // call this BEFORE(!) you 'write' any stuff to the server
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    public void RediscoverServices(){
        if(bluetoothGatt != null){
            bluetoothGatt.discoverServices();
        }
    }


}
