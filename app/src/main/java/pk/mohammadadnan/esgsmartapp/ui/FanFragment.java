package pk.mohammadadnan.esgsmartapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pk.mohammadadnan.esgsmartapp.BluetoothService;
import pk.mohammadadnan.esgsmartapp.Constants;
import pk.mohammadadnan.esgsmartapp.R;

import static pk.mohammadadnan.esgsmartapp.Utils.showMessageDebug;

public class FanFragment extends Fragment implements View.OnClickListener {

    private Button autoBtn, manualBtn, back;

    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fan, container, false);

        autoBtn = root.findViewById(R.id.auto_fan_btn);
        manualBtn = root.findViewById(R.id.manual_fan_btn);
        back = root.findViewById(R.id.back);

        autoBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
        back.setOnClickListener(this);

        bluetoothService = BluetoothService.getInstance(getActivity());

        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auto_fan_btn:
                bluetoothService.Write("FA");
                break;
            case R.id.manual_fan_btn:
                bluetoothService.Write("FM");
                Navigation.findNavController(view).navigate(R.id.fan_to_manual);
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