package com.integrations.orderprocessing.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class SFTPConnectionUtil {

	private ChannelSftp channelSftp;
	private Session session;

	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	public void setChannelSftp(ChannelSftp channelSftp) {
		this.channelSftp = channelSftp;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
