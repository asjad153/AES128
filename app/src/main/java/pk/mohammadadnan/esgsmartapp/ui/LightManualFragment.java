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

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import pk.mohammadadnan.esgsmartapp.BluetoothService;
import pk.mohammadadnan.esgsmartapp.Constants;
import pk.mohammadadnan.esgsmartapp.R;

import static pk.mohammadadnan.esgsmartapp.Utils.showMessageDebug;

public class LightManualFragment extends Fragment implements View.OnClickListener {

    private Button onLED1, offLED1, onLED2, offLED2, onLED3, offLED3, onLED4, offLED4, onFLED, offFLED, back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_light_manual, container, false);

        onLED1 = root.findViewById(R.id.led_1_on);
        onLED2 = root.findViewById(R.id.led_2_on);
        onLED3 = root.findViewById(R.id.led_3_on);
        onLED4 = root.findViewById(R.id.led_4_on);
        onFLED = root.findViewById(R.id.fled_on);
        offLED1 = root.findViewById(R.id.led_1_off);
        offLED2 = root.findViewById(R.id.led_2_off);
        offLED3 = root.findViewById(R.id.led_3_off);
        offLED4 = root.findViewById(R.id.led_4_off);
        offFLED = root.findViewById(R.id.fled_off);
        back = root.findViewById(R.id.back);

        onLED1.setOnClickListener(this);
        onLED2.setOnClickListener(this);
        onLED3.setOnClickListener(this);
        onLED4.setOnClickListener(this);
        onFLED.setOnClickListener(this);
        offLED1.setOnClickListener(this);
        offLED2.setOnClickListener(this);
        offLED3.setOnClickListener(this);
        offLED4.setOnClickListener(this);
        offFLED.setOnClickListener(this);
        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());

        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.led_1_on:
                bluetoothService.Write("U1N");
                break;
            case R.id.led_2_on:
                bluetoothService.Write("U2N");
                break;
            case R.id.led_3_on:
                bluetoothService.Write("U3N");
                break;
            case R.id.led_4_on:
                bluetoothService.Write("U4N");
                break;
            case R.id.fled_on:
                bluetoothService.Write("FN");
                break;
            case R.id.led_1_off:
                bluetoothService.Write("U1F");
                break;
            case R.id.led_2_off:
                bluetoothService.Write("U2F");
                break;
            case R.id.led_3_off:
                bluetoothService.Write("U3F");
                break;
            case R.id.led_4_off:
                bluetoothService.Write("U4F");
                break;
            case R.id.fled_off:
                bluetoothService.Write("FO");
                break;
            case R.id.back:
                bluetoothService.Disconnect();
                Navigation.findNavController(view).popBackStack();
                break;
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
            if (action.equals(Constants.ACTION_CONNECTED)) {
                showMessageDebug("Device connection successful.");
            }else if(action.equals(Constants.ACTION_DISCONNECTED)){
                showMessageDebug("Device connection unsuccessful.");
                Navigation.findNavController(getView()).popBackStack();
            }
        }
    };

    private static IntentFilter BTIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONNECTED);
        intentFilter.addAction(Constants.ACTION_DISCONNECTED);
        return intentFilter;
    }

}