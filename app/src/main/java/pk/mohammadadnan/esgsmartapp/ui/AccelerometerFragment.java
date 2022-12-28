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

public class AccelerometerFragment extends Fragment implements View.OnClickListener {

    private TextView accelerometerTxt;
    private Button back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        accelerometerTxt = root.findViewById(R.id.accelerometer_txt);
        back = root.findViewById(R.id.back);

        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());

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
                case Constants.MESSAGE_ACCELEROMETER:
                    accelerometerTxt.setText(":" + message);
                    break;
            }
        }
    };

    private static IntentFilter BTIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONNECTED);
        intentFilter.addAction(Constants.ACTION_DISCONNECTED);
        intentFilter.addAction(Constants.MESSAGE_ACCELEROMETER);
        return intentFilter;
    }

}