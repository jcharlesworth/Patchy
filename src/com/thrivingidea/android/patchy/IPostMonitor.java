package com.thrivingidea.android.patchy;

public interface IPostMonitor {
	void registerAccount(String user, String password, IPostListener callback);
	void removeAccount(IPostListener callback);
}
