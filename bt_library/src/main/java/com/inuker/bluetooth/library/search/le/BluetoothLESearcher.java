package com.inuker.bluetooth.library.search.le;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import com.inuker.bluetooth.library.search.BluetoothSearcher;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.BluetoothSearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.P;

import java.util.UUID;

/**
 * @author dingjikerbo
 */
public class BluetoothLESearcher extends BluetoothSearcher {

	private BluetoothLESearcher() {
		mBluetoothAdapter = BluetoothUtils.getBluetoothAdapter();
	}

	public static BluetoothLESearcher getInstance() {
		return BluetoothLESearcherHolder.instance;
	}

	private static class BluetoothLESearcherHolder {
		private static BluetoothLESearcher instance = new BluetoothLESearcher();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	public void startScanBluetooth(BluetoothSearchResponse response) {
		// TODO Auto-generated method stub
		super.startScanBluetooth(response);
		mBluetoothAdapter.startLeScan(mLeScanCallback);
//		mBluetoothAdapter.startLeScan(new UUID[]{uuid},mLeScanCallback);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	public void stopScanBluetooth() {
		// TODO Auto-generated method stub
		try {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		} catch (Exception e) {
			BluetoothLog.e(e);
		}

		super.stopScanBluetooth();
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	protected void cancelScanBluetooth() {
		// TODO Auto-generated method stub
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		super.cancelScanBluetooth();
	}

	private final LeScanCallback mLeScanCallback = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// TODO Auto-generated method stub
			P.c("发现设备");
			if(device.getName()!=null){
				notifyDeviceFounded(new SearchResult(device, rssi, scanRecord));
			}
		}
		
	};
}
