package com.camelot.mall.util.emay;

public class SingletonClient {
	private static EmayClient client = null;

	private SingletonClient() {
	}

	public synchronized static EmayClient getClient(String softwareSerialNo, String key) {
		if (client == null) {
			try {
				client = new EmayClient(softwareSerialNo, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

}
