package pk.mohammadadnan.esgsmartapp;

import java.util.UUID;

public class Constants {
    public static final long SCAN_PERIOD = 10000;

    public static final int CONNECTING_STATUS = 0;
    public static final int CONNECTED_STATUS = 1;
    public static final int DISCONNECTED_STATUS = -1;
    public static final int CONNECTION_LOST_STATUS = -2;

    public static final int MESSAGE_READ = 2;

    public final static String ACTION_CONNECTED = "pk.mohammadadnan.esgsmartapp.ACTION_CONNECTED";
    public final static String ACTION_DISCONNECTED = "pk.mohammadadnan.esgsmartapp.ACTION_DISCONNECTED";
    public final static String ACTION_SCAN = "pk.mohammadadnan.esgsmartapp.ACTION_SCAN";
    public static final String MESSAGE_ULED1 = "1";
    public static final String MESSAGE_ULED2 = "2";
    public static final String MESSAGE_ULED3 = "3";
    public static final String MESSAGE_ULED4 = "4";
    public static final String MESSAGE_FLED = "5";
    public static final String MESSAGE_FAN1 = "6";
    public static final String MESSAGE_FAN2 = "7";
    public static final String MESSAGE_BATTERY = "8";
    public static final String MESSAGE_SPO2 = "9";
    public static final String MESSAGE_AIRQUALITY = "10";
    public static final String MESSAGE_AIRPRESSURE = "11";
    public static final String MESSAGE_CAMERA = "12";
    public static final String MESSAGE_POWER = "13";
    public static final String MESSAGE_HEART = "14";
    public static final String MESSAGE_ECG = "15";
    public static final String MESSAGE_ACCELEROMETER = "16";

    public final static String EXTRAS_MESSAGE = "pk.mohammadadnan.esgsmartapp.EXTRAS_MESSAGE";

    public static final UUID BLE_UUID_SERVICE = UUID.fromString("87b1de8d-e7cb-4ea8-a8e4-290209522c83");

    public static final UUID BLE_UUID_CHAR_WRITE = UUID.fromString("e68a5c09-aef8-4447-8f10-f3339898dee9");
//
//    public static final UUID BLE_UUID_CHAR_ULED1 = UUID.fromString("b1ea0cf1-42f9-4b71-b3f5-32a062d20fd9");
//    public static final UUID BLE_UUID_CHAR_ULED2 = UUID.fromString("5d68b94f-45aa-4c08-840e-4c855372bb40");
//    public static final UUID BLE_UUID_CHAR_ULED3 = UUID.fromString("bf25292d-b869-4cf9-b8cd-b1a2cec8a74c");
//    public static final UUID BLE_UUID_CHAR_ULED4 = UUID.fromString("aa1626e4-41b5-47c5-9b85-6cce3a23a795");
//    public static final UUID BLE_UUID_CHAR_FLED = UUID.fromString("c01e2763-8bee-4f41-ac36-ca1235e3d631");
//    public static final UUID BLE_UUID_CHAR_FAN1 = UUID.fromString("c872402a-80dc-4149-b000-81b7e6c81937");
//    public static final UUID BLE_UUID_CHAR_FAN2 = UUID.fromString("47ed86d7-8d3d-4899-84eb-66b8bbf94355");
//    public static final UUID BLE_UUID_CHAR_BATTERY = UUID.fromString("85e8c2eb-2e25-4545-99fd-817f551d4d38");
//    public static final UUID BLE_UUID_CHAR_SPO2 = UUID.fromString("da35e085-d247-40f8-a4a8-07bd76ab93ae");
//    public static final UUID BLE_UUID_CHAR_AIRQUALITY = UUID.fromString("91906f6d-bee5-4981-90b7-bdf62768f78d");
//    public static final UUID BLE_UUID_CHAR_AIRPRESSURE = UUID.fromString("997fe9cf-dc07-4347-bad0-ac29e5c17df9");
//    public static final UUID BLE_UUID_CHAR_CAMERA = UUID.fromString("7e43b9f6-b269-49ac-b44a-0aaf398eb2da");
//    public static final UUID BLE_UUID_CHAR_POWER = UUID.fromString("4e1d2bd6-ccdd-4352-b9df-bfeaa8d6d32d");
//    public static final UUID BLE_UUID_CHAR_HEART = UUID.fromString("3f8ecb6b-71b1-484b-86b3-b86ca55db719");
//    public static final UUID BLE_UUID_CHAR_ECG = UUID.fromString("d652ea07-be3e-4d68-a58e-f86d75023c22");
//    public static final UUID BLE_UUID_CHAR_ACCELEROMETER = UUID.fromString("e2ef535f-424f-47e9-ab28-28d847271018");
//
//    public static final int AUDIO = 0;
//    public static final int FAN = 1;
//    public static final int LIGHT = 2;
//    public static final int SENSOR = 3;
//    public static final int CAMERA = 4;
//    public static final int POWER = 5;
//    public static final int STATUS = 6;

}
