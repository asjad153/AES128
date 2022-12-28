package pk.mohammadadnan.esgsmartapp;

import xdroid.toaster.Toaster;

public class Utils {
    public static void showMessageDebug(String message){
        showMessage(message);
    }

    public static void showMessage(String message){
        Toaster.toast(message);
    }
}
