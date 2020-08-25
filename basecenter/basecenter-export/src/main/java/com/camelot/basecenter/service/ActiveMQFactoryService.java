package com.camelot.basecenter.service;

public interface ActiveMQFactoryService {
	public PublisherMessageService getMessagePublisher(String name,Class cla);
}
