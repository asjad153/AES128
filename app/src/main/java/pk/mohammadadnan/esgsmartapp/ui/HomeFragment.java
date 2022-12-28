package pk.mohammadadnan.esgsmartapp.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pk.mohammadadnan.esgsmartapp.BluetoothService;
import pk.mohammadadnan.esgsmartapp.Constants;
import pk.mohammadadnan.esgsmartapp.R;

import static pk.mohammadadnan.esgsmartapp.Utils.showMessageDebug;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button writeBtn,fanBtn,lightBtn,sensorBtn,cameraBtn,powerBtn,statusBtn;

//    private int chosenOption = Constants.AUDIO;

    private ArrayList<BluetoothDevice> bondedDevices = new ArrayList<>();

    private BluetoothService bluetoothService;
    private BluetoothAdapter bluetoothAdapter;

//    ArrayList<String> deviceList = new ArrayList<String>();
//String[] deviceList ={"asjad","sarosh","asjad","sarosh","asjad","sarosh","asjad","sarosh"};
List<String> deviceList = new ArrayList<String>();
    List<String> macAddressList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        audioBtn = root.findViewById(R.id.audioBtn);
//        fanBtn = root.findViewById(R.id.fanBtn);
//        lightBtn = root.findViewById(R.id.lightBtn);
//        sensorBtn = root.findViewById(R.id.sensorBtn);
//        cameraBtn = root.findViewById(R.id.cameraBtn);
//        powerBtn = root.findViewById(R.id.powerBtn);
        statusBtn = root.findViewById(R.id.statusBtn);
        writeBtn = root.findViewById(R.id.Write);
        ListView listview = root.findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                System.out.println(position);
                String macAddress = macAddressList.get(position);
                System.out.println(macAddress);
                bluetoothService.Connect(macAddress);
                //intent
            }
        });
        arrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_view, R.id.itemTextView, deviceList);

                listview.setAdapter(arrayAdapter);
        arrayAdapter.setNotifyOnChange(true);

//        audioBtn.setOnClickListener(this);
//        fanBtn.setOnClickListener(this);
//        lightBtn.setOnClickListener(this);
//        sensorBtn.setOnClickListener(this);
//        cameraBtn.setOnClickListener(this);
//        powerBtn.setOnClickListener(this);
        statusBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothService.Write("570000001A00000000000002026073");

            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothService = BluetoothService.getInstance(getActivity());

        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.audioBtn:
//                chosenOption = Constants.AUDIO;
//                break;
//            case R.id.fanBtn:
//                chosenOption = Constants.FAN;
//                break;
//            case R.id.lightBtn:
//                chosenOption = Constants.LIGHT;
//                break;
//            case R.id.sensorBtn:
//                chosenOption = Constants.SENSOR;
//                break;
//            case R.id.cameraBtn:
//                chosenOption = Constants.CAMERA;
//                break;
//            case R.id.powerBtn:
//                chosenOption = Constants.POWER;
//                break;
            case R.id.statusBtn:
//                chosenOption = Constants.STATUS;
                break;
        }
        bluetoothService.Scan();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(BTStateChangeReceiver, BTIntentFilter());
        bluetoothService.Disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(BTStateChangeReceiver);
    }

    private final BroadcastReceiver BTStateChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Constants.ACTION_CONNECTED)) {
                showMessageDebug("Device connection successful.");
                bluetoothService.Write("570000001A00000000000002026073");
//                switch (chosenOption){
//                    case Constants.AUDIO:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_audio);
//                        break;
//                    case Constants.FAN:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_fan);
//                        break;
//                    case Constants.LIGHT:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_light);
//                        break;
//                    case Constants.SENSOR:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_sensor);
//                        break;
//                    case Constants.CAMERA:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_camera);
//                        break;
//                    case Constants.POWER:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_power);
//                        break;
//                    case Constants.STATUS:
//                        Navigation.findNavController(getView()).navigate(R.id.home_to_status);
//                        break;
//                }
            }else if(action.equals(Constants.ACTION_DISCONNECTED)){
                showMessageDebug("Service connection unsuccessful.");
            }else if(action.equals(Constants.ACTION_SCAN)){
//                Log.e("ACtion::::",action);

              String message =  intent.getStringExtra(Constants.EXTRAS_MESSAGE);
                Log.e("data::::",message);

                String[] separated = message.split("space");
//                List<String> data=new ArrayList<>();
//                data.add(separated[1]);
//                List<String> deduped = data.stream().distinct().collect(Collectors.toList());
//
//
//                for(int i=0;i<deduped.size();i++)
//                {
//                    deviceList.add(deduped.get(i));
//                }
//                System.out.println(deviceList);
                macAddressList.add(separated[0]);
                deviceList.add(separated[1]);

                arrayAdapter.notifyDataSetChanged();


            }
        }
    };

    private static IntentFilter BTIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONNECTED);
        intentFilter.addAction(Constants.ACTION_DISCONNECTED);
        intentFilter.addAction(Constants.ACTION_SCAN);
        return intentFilter;
    }

}