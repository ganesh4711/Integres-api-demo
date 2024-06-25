package com.integrations.orderprocessing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.integrations.orderprocessing.component.Utils;
import com.integrations.orderprocessing.model.payload.FETokenPayload;
import com.integrations.orderprocessing.model.payload.RRTokenBotristaPayload;
import com.integrations.orderprocessing.model.payload.RRTokenSharpPayload;
import com.integrations.orderprocessing.model.payload.UserCredentials;
import com.integrations.orderprocessing.util.SFTPConnectionUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AppConfiguration {
	
//	@Value("${sftp.SHARP.hostName}")
//	private String hostIP;
//
//	@Value("${sftp.SHARP.login}")
//	private String loginUserName;
//
//	@Value("${sftp.SHARP.password}")
//	private String password;
	
	@Value("${app.rr.api.grant_type}")
    private String rr_grant_type;

    @Value("${app.rr.api.sharp.client_id}")
    private String sharp_rr_client_id;

    @Value("${app.rr.api.sharp.client_secret}")
    private String sharp_rr_client_secret;

    @Value("${app.rr.api.sharp.username}")
    private String sharp_rr_username;

    @Value("${app.rr.api.sharp.password}")
    private String sharp_rr_password;
    
    //--------------------------------
    
    @Value("${app.rr.api.botrista.client_id}")
    private String botrista_rr_client_id;

    @Value("${app.rr.api.botrista.client_secret}")
    private String botrista_rr_client_secret;

    @Value("${app.rr.api.botrista.username}")
    private String botrista_rr_username;

    @Value("${app.rr.api.botrista.password}")
    private String botrista_rr_password;
    
    //----------------------------------
    
    @Value("${app.fe.api.login.username}")
    private String fe_login_username;

    @Value("${app.fe.api.login.password}")
    private String fe_login_password;
    
    //----------------------------------
    
    @Value("${app.fs.api.login.username}")
    private String fs_login_username;

    @Value("${app.fs.api.login.password}")
    private String fs_login_password;
	
//	@Bean
//	@Scope("prototype")
//	SFTPConnectionUtil getSFTPConnection() {
//		SFTPConnectionUtil sftpConnectionUtil = null;
//        ChannelSftp sftpChannel;
//        Session session;
//        try {
//            log.info("**getSFTPConnection**");
//            sftpConnectionUtil = new SFTPConnectionUtil();
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//
//            JSch ssh = new JSch();
//            session = ssh.getSession(loginUserName, hostIP, 22);
//            session.setConfig(config);
//            session.setPassword(password);
//            session.setTimeout(30000);
//            log.info("**Connecting to** " + Utils.maskInput(hostIP) + "&UserName:: " + loginUserName);
//            session.connect();
//
//            Channel channel = session.openChannel("sftp");
//            channel.connect(20000);
//            log.info("Connected to :: " + Utils.maskInput(hostIP) + "&UserName:: " + loginUserName);
//            sftpChannel = (ChannelSftp) channel;
//            sftpConnectionUtil.setChannelSftp(sftpChannel);
//            sftpConnectionUtil.setSession(session);
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	log.debug("**Exception While Connecting to SFTP Server** Username: {}, IP Address: {}",
//            		loginUserName, Utils.maskInput(hostIP), e);
//        }
//
//        return sftpConnectionUtil;
//	}
//
	@Bean
	RRTokenSharpPayload getRRTokenSharpPayload() {
		return new RRTokenSharpPayload(rr_grant_type, sharp_rr_client_id, sharp_rr_client_secret, sharp_rr_username, sharp_rr_password);
	}
	
	@Bean
	RRTokenBotristaPayload getRRTokenBotristaPayload() {
		return new RRTokenBotristaPayload(rr_grant_type, botrista_rr_client_id, botrista_rr_client_secret, botrista_rr_username, botrista_rr_password);
	}
	
	@Bean
	FETokenPayload getFETokenPayload() {
		return new FETokenPayload(fe_login_username, fe_login_password);
	}
	
	@Bean
	UserCredentials getFreightSnapUserCredentials() {
		return new UserCredentials(fs_login_username, fs_login_password);
	}
}
