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

public class CameraFragment extends Fragment implements View.OnClickListener {

    private Button onPwr, offPwr, onRec, offRec, back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        onPwr = root.findViewById(R.id.power_on);
        offPwr = root.findViewById(R.id.power_off);
        onRec = root.findViewById(R.id.rec_on);
        offRec = root.findViewById(R.id.rec_off);
        back = root.findViewById(R.id.back);

        onPwr.setOnClickListener(this);
        offPwr.setOnClickListener(this);
        onRec.setOnClickListener(this);
        offRec.setOnClickListener(this);
        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());

        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.power_on:
                bluetoothService.Write("CPWRN");
                break;
            case R.id.power_off:
                bluetoothService.Write("CPWRF");
                break;
            case R.id.rec_on:
                bluetoothService.Write("CRECN");
                break;
            case R.id.rec_off:
                bluetoothService.Write("CRECF");
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