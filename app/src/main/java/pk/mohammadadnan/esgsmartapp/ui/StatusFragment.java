package pk.mohammadadnan.esgsmartapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import pk.mohammadadnan.esgsmartapp.BluetoothService;
import pk.mohammadadnan.esgsmartapp.Constants;
import pk.mohammadadnan.esgsmartapp.R;

import static pk.mohammadadnan.esgsmartapp.Utils.showMessageDebug;

public class StatusFragment extends Fragment implements View.OnClickListener {

    private TextView uled1Txt, uled2Txt, uled3Txt, uled4Txt, fledTxt, fan1Txt, fan2Txt, batteryTxt, spo2Txt, qualityTxt, pressureTxt, cameraTxt, powerTxt;
    private Button back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_status, container, false);

        uled1Txt = root.findViewById(R.id.uled1_txt);
        uled2Txt = root.findViewById(R.id.uled2_txt);
        uled3Txt = root.findViewById(R.id.uled3_txt);
        uled4Txt = root.findViewById(R.id.uled4_txt);
        fledTxt = root.findViewById(R.id.fled_txt);
        fan1Txt = root.findViewById(R.id.fan1_txt);
        fan2Txt = root.findViewById(R.id.fan2_txt);
        batteryTxt = root.findViewById(R.id.battery_txt);
        spo2Txt = root.findViewById(R.id.spo2_txt);
        qualityTxt = root.findViewById(R.id.quality_txt);
        pressureTxt = root.findViewById(R.id.pressure_txt);
        cameraTxt = root.findViewById(R.id.camera_txt);
        powerTxt = root.findViewById(R.id.power_txt);
        back = root.findViewById(R.id.back);

        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());
        bluetoothService.RediscoverServices();

        return root;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            bluetoothService.Disconnect();
            Navigation.findNavController(view).popBackStack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(BTStateChangeReceiver, BTIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(BTStateChangeReceiver);
    }

    private final BroadcastReceiver BTStateChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final String message = intent.getStringExtra(Constants.EXTRAS_MESSAGE);
            switch (action) {
                case Constants.ACTION_CONNECTED:
                    showMessageDebug("Device connection successful.");
                    break;
                case Constants.ACTION_DISCONNECTED:
                    showMessageDebug("Device connection unsuccessful.");
                    Navigation.findNavController(getView()).popBackStack();
                    break;
                case Constants.MESSAGE_ULED1:
                    uled1Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_ULED2:
                    uled2Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_ULED3:
                    uled3Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_ULED4:
                    uled4Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_FLED:
                    fledTxt.setText(":" + message);
                    break;
                case Constants.MESSAGE_FAN1:
                    fan1Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_FAN2:
                    fan2Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_BATTERY:
                    batteryTxt.setText(":" + message);
                    break;
                case Constants.MESSAGE_SPO2:
                    spo2Txt.setText(":" + message);
                    break;
                case Constants.MESSAGE_AIRQUALITY:
                    qualityTxt.setText(":" + message);
                    break;
                case Constants.MESSAGE_AIRPRESSURE:
                    pressureTxt.setText(":" + message);
                    break;
                case Constants.MESSAGE_CAMERA:
                    cameraTxt.setText(":" + message);
                    break;
                case Constants.MESSAGE_POWER:
                    powerTxt.setText(":" + message);
                    break;
            }
        }
    };

    private static IntentFilter BTIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONNECTED);
        intentFilter.addAction(Constants.ACTION_DISCONNECTED);
        intentFilter.addAction(Constants.MESSAGE_AIRPRESSURE);
        intentFilter.addAction(Constants.MESSAGE_AIRQUALITY);
        intentFilter.addAction(Constants.MESSAGE_BATTERY);
        intentFilter.addAction(Constants.MESSAGE_CAMERA);
        intentFilter.addAction(Constants.MESSAGE_FAN1);
        intentFilter.addAction(Constants.MESSAGE_FAN2);
        intentFilter.addAction(Constants.MESSAGE_FLED);
        intentFilter.addAction(Constants.MESSAGE_ULED1);
        intentFilter.addAction(Constants.MESSAGE_ULED2);
        intentFilter.addAction(Constants.MESSAGE_ULED3);
        intentFilter.addAction(Constants.MESSAGE_ULED4);
        intentFilter.addAction(Constants.MESSAGE_SPO2);
        intentFilter.addAction(Constants.MESSAGE_POWER);
        return intentFilter;
    }

}