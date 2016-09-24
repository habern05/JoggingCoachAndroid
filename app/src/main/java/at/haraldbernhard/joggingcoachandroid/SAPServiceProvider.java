package at.haraldbernhard.joggingcoachandroid;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Harald Bernhard on 15.07.2016.
 */
public class SAPServiceProvider extends SAAgent{

    public final static String TAG = "SAPServiceProvider";
    public final static int SAP_SERVICE_CHANNEL_ID = 321;
    private final IBinder mIBinder = new LocalBinder();
    HashMap<Integer, SAPServiceProviderConnection> connectionMap = null;

    public SAPServiceProvider() {
        super(TAG, SAPServiceProviderConnection.class);
    }


    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent peerAgent, int result) {
        switch(result){
            case PEER_AGENT_FOUND:
                //Peer Agent is found
                requestServiceConnection(peerAgent);
                Log.e(TAG,  "PeerAgent is found");
                break;
            case FINDPEER_DEVICE_NOT_CONNECTED:
                //Peer Agent are not found, no accessory device connected
                Log.e(TAG,  "PeerAgent not found");
                break;
            case FINDPEER_SERVICE_NOT_FOUND:
                //No matching service on connected accessory
                Log.e(TAG,  "No matching");
                break;
            default:
                Log.e(TAG,  "Default");
        }
    }


    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket thisConnection, int result) {
        switch(result){
            case CONNECTION_SUCCESS:
                if(thisConnection != null){
                    SAPServiceProviderConnection myConnection = (SAPServiceProviderConnection) thisConnection;
                    if(connectionMap == null){
                        connectionMap = new HashMap<Integer, SAPServiceProviderConnection>();
                    }
                    myConnection.connectionID = (int) (System.currentTimeMillis() & 255);
                    Log.d(TAG,  "onServiceConnection connectionID = " + myConnection.connectionID);
                    connectionMap.put(myConnection.connectionID, myConnection);
                    Toast.makeText(getBaseContext(),  "Connection Established",  Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e(TAG,  "SASocket object is null");
                }
                break;
            case CONNECTION_FAILURE_NETWORK:
                Log.e(TAG,  "connection already exists");
                break;
            default:
                Log.e(TAG,  "default");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        SA myAccessory = new SA();
        try{myAccessory.initialize(this);
            Log.d("SAP Provider", "On Create Try Block");
        }
        catch (SsdkUnsupportedException e){
            Log.d("SAP Provider",  "On create Try Block Error Unsupported Sdk");
        }
        catch(Exception e1){
            Log.e(TAG,  "Cannot initialize Accessory package.");
            e1.printStackTrace();
            stopSelf();
        }
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
        acceptServiceConnectionRequest(peerAgent);
    }

    public class LocalBinder extends Binder {

        public SAPServiceProvider getService(){
            return SAPServiceProvider.this;
        }
    }


    public class SAPServiceProviderConnection extends SASocket{

        private int connectionID;

        protected SAPServiceProviderConnection() {
            super(SAPServiceProviderConnection.class.getName());
        }

        @Override
        public void onError(int channelID, String errorString, int error) {
            Log.e(TAG,  "ERROR:" + errorString + " : " + error);
        }




        @Override
        public void onReceive(int channelID, byte[] data) {

            final String information = TrainingModeActivity.getInformationAsJSON();
            final SAPServiceProviderConnection uHandler = connectionMap.get(Integer.parseInt(String.valueOf(connectionID)));
            if(uHandler == null){
                Log.e(TAG,  "Error, can not get SAPServiceProviderConnection handler");
            }

            new Thread(new Runnable(){
                public void run(){
                    try{
                        uHandler.send(SAP_SERVICE_CHANNEL_ID,  information.getBytes());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        protected void onServiceConnectionLost(int arg0) {
            if(connectionMap !=null){
                connectionMap.remove(connectionID);
            }
        }
    }
}
