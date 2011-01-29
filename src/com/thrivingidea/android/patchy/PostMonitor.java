package com.thrivingidea.android.patchy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import winterwell.jtwitter.Twitter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class PostMonitor extends Service {

	private AtomicBoolean active = new AtomicBoolean(true);
	private static final int POLL_PERIOD = 60000;
	private Set<Long> seenStatus = new HashSet<Long>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		new Thread(threadBody).start();
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		active.set(false);
	}
	
	private Runnable threadBody = new Runnable() {
		public void run() {
			while (active.get()) {
				poll();
				SystemClock.sleep(POLL_PERIOD);
			}
		}
	};
	
	private void poll() {
		Twitter client = new Twitter();
		List<Twitter.Status> timeline = client.getFriendsTimeline();
		
		for (Twitter.Status s : timeline) {
			if (!seenStatus.contains(s.id)) {
				// found a new one!
				seenStatus.add(s.id);
			}
		}
	}
	
}
