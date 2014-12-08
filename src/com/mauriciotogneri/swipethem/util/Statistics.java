package com.mauriciotogneri.swipethem.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

public class Statistics
{
	private static Object trackerLock = new Object();
	private static Tracker tracker;
	
	private static final String CATEGORY_GAME = "GAME";
	private static final String EVENT_APP_LAUNCHED = "APP_LAUNCHED";
	private static final String EVENT_NEW_GAME = "NEW_GAME";
	
	public static void initialize(Tracker tracker)
	{
		synchronized (Statistics.trackerLock)
		{
			Statistics.tracker = tracker;
		}
	}
	
	public static void sendHitAppLaunched()
	{
		if (Statistics.tracker != null)
		{
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					synchronized (Statistics.trackerLock)
					{
						Statistics.tracker.setScreenName(Statistics.EVENT_APP_LAUNCHED);
						Statistics.tracker.send(new HitBuilders.ScreenViewBuilder().build());
					}
				}
			});
			thread.start();
		}
	}
	
	public static void sendHitNewGame()
	{
		if (Statistics.tracker != null)
		{
			Thread threadGameMode = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					EventBuilder builder = new HitBuilders.EventBuilder();
					builder.setCategory(Statistics.CATEGORY_GAME);
					builder.setAction(Statistics.EVENT_NEW_GAME);
					
					synchronized (Statistics.trackerLock)
					{
						Statistics.tracker.send(builder.build());
					}
				}
			});
			threadGameMode.start();
		}
	}
}