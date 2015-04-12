package com.pwcgarage.ibeacontest;

import java.util.Collection;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements BeaconConsumer
{

	private BeaconManager m_beaconManager;
	private static String TAG = "com.pwcgarage.ibeacontest.MainActivity";

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_beaconManager = BeaconManager.getInstanceForApplication(this);
		
		// beaconinside specific parser
		 m_beaconManager.getBeaconParsers().add(new BeaconParser()
		   .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
		Log.i(TAG, "Binding BeaconManager");
		m_beaconManager.bind(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Resources res = getResources();
		// String sdkToken = res.getString(R.string.ibeacon_sdk_id);
		// BeaconService.init(this, "YOUR_TOKEN");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		m_beaconManager.unbind(this);
	}

	@Override
	public void onBeaconServiceConnect()
	{
		Log.i(TAG, "onBeaconServiceConnect called");
		
		m_beaconManager.setRangeNotifier(
				new RangeNotifier()
				{
					@Override
					public void didRangeBeaconsInRegion(Collection<Beacon> beacons,
							Region region)
					{
						if (beacons.size() > 0)
						{
							Log.i(TAG, "The first beacon I see is about "
									+ beacons.iterator().next().getDistance()
									+ " meters away.");
						}
					}
				});
	
		try
		{
			m_beaconManager.startRangingBeaconsInRegion(new Region(
					"myRangingUniqueId", null, null, null));
		}
		catch (RemoteException e)
		{
		}
	}
}
