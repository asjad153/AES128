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

public class FanManualFragment extends Fragment implements View.OnClickListener {

    private Button fan10, fan11, fan12, fan13, fan20, fan21, fan22, fan23, fanOff, back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fan_manual, container, false);

        fan10 = root.findViewById(R.id.fan10);
        fan11 = root.findViewById(R.id.fan11);
        fan12 = root.findViewById(R.id.fan12);
        fan13 = root.findViewById(R.id.fan13);
        fan20 = root.findViewById(R.id.fan20);
        fan21 = root.findViewById(R.id.fan21);
        fan22 = root.findViewById(R.id.fan22);
        fan23 = root.findViewById(R.id.fan23);
        fanOff = root.findViewById(R.id.fan_off);

        back = root.findViewById(R.id.back);

        fan10.setOnClickListener(this);
        fan11.setOnClickListener(this);
        fan12.setOnClickListener(this);
        fan13.setOnClickListener(this);
        fan20.setOnClickListener(this);
        fan21.setOnClickListener(this);
        fan22.setOnClickListener(this);
        fan23.setOnClickListener(this);
        fanOff.setOnClickListener(this);

        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());

        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fan10:
                bluetoothService.Write("F10");
                break;
            case R.id.fan11:
                bluetoothService.Write("F11");
                break;
            case R.id.fan12:
                bluetoothService.Write("F12");
                break;
            case R.id.fan13:
                bluetoothService.Write("F13");
                break;
            case R.id.fan20:
                bluetoothService.Write("F20");
                break;
            case R.id.fan21:
                bluetoothService.Write("F21");
                break;
            case R.id.fan22:
                bluetoothService.Write("F22");
                break;
            case R.id.fan23:
                bluetoothService.Write("F23");
                break;
            case R.id.fan_off:
                bluetoothService.Write("FOFF");
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