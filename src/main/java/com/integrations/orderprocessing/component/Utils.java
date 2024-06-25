package com.integrations.orderprocessing.component;


import static com.integrations.orderprocessing.constants.FileConstants.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integrations.orderprocessing.util.SFTPConnectionUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Component
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public String formattedDate(String stringDate, String format) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat(format);
        try {
            return myFormat.format(fromUser.parse(stringDate));
        } catch (ParseException e) {
            logger.info("**Exception While Formatting Date** {}", e.getMessage());
        }
        return null;
    }

    public ZonedDateTime convertDateToUTC(String deliveryDateTime, String timeZoneId, String pattern) {
        logger.info(deliveryDateTime + " " + timeZoneId);
        ZoneId zoneId = ZoneId.of(timeZoneId);
        ZonedDateTime zonedDate = LocalDateTime.parse(deliveryDateTime, DateTimeFormatter.ofPattern(pattern))
                .atZone(zoneId);
        logger.info("ZonedDateTime in timezone" + zonedDate);
        return zonedDate;
    }

//    public String uploadStatusXMLFiletoServer(String fileName, String hostName, String login, String password,
//                                              String statusPath, String statusXMLFilesUploadedPath) {
//        SFTPConnectionUtil sftpConnectionUtil = getSFTPConnection(hostName, login, password);
//        File file = new File(statusXMLFilesUploadedPath, fileName);
//        if (file.exists()) {
//            if (sftpConnectionUtil.getChannelSftp() != null) {
//                try {
//                    sftpConnectionUtil.getChannelSftp().cd(statusPath);
//                    logger.info("CompletePath:: " + file.getAbsolutePath());
//                    logger.info("FileName:: " + fileName);
//                    sftpConnectionUtil.getChannelSftp().put(file.getAbsolutePath(), fileName);
//                    Boolean isDeleted = file.delete();
//                    logger.info(String.format("File %s is deleted. %s,", file.getAbsolutePath(), isDeleted));
//                    logger.info("Upload Status XML File Done");
//                    return "SUCCESS";
//                } catch (Exception e) {
//                    logger.info("**Exception While Uploading Status XML File Into Server** Username: {}, IP Address: {}"
//                            + e.getMessage(), login, maskInput(hostName));
//                    e.printStackTrace();
//                    commons.sendSlackMessage("**Exception While Uploading Status XML File Into Server**" +
//                            "FileName" + fileName, e, null);
//                } finally {
//                    sftpConnectionUtil.getChannelSftp().exit();
//                    sftpConnectionUtil.getChannelSftp().disconnect();
//                    if (sftpConnectionUtil.getSession().isConnected()) {
//                        sftpConnectionUtil.getSession().disconnect();
//                    }
//                }
//            }
//        }else {
//            // Handle the case where the file doesn't exist
//            logger.error("File not found: " + file.getAbsolutePath() + fileName);
//            return "FAILURE";
//        }
//
//        return FAILURE;
//    }

    public SFTPConnectionUtil getSFTPConnection(String hostName, String login, String password) {
        SFTPConnectionUtil sftpConnectionUtil = null;
        ChannelSftp sftpChannel;
        Session session;
        try {
            logger.info("**getSFTPConnection**");
            sftpConnectionUtil = new SFTPConnectionUtil();
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
//            config.put("PreferredAuthentications", "password");

            JSch ssh = new JSch();
            session = ssh.getSession(login, hostName, 22);
            session.setConfig(config);
            session.setPassword(password);
            session.setTimeout(30000);
            logger.info("**Connecting to** " + maskInput(hostName) + "&UserName:: " + login);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect(20000);
            logger.info("Connected to :: " + maskInput(hostName) + "&UserName:: " + login);
            sftpChannel = (ChannelSftp) channel;
            sftpConnectionUtil.setChannelSftp(sftpChannel);
            sftpConnectionUtil.setSession(session);
            return sftpConnectionUtil;
        } catch (Exception e) {
        	e.printStackTrace();
            logger.debug("**Exception While Connecting to SFTP Server** Username: {}, IP Address: {}",
                    login, maskInput(hostName), e);
//            commons.sendSlackMessage("Exception While Connecting to SFTP Server", e, null);
//            e.printStackTrace();
            // if password or username or hostname invalid. session and channel object will not be instantiated
            // so no need to write disconnect logic
        }
        return sftpConnectionUtil;
    }


    public SFTPConnectionUtil getSFTPConnectionUsingPrivateKey(String hostName, String userName, String privateKeyPath) {
        SFTPConnectionUtil sftpConnectionUtil = null;
        try {
            logger.info("**getSFTPConnection**");
            sftpConnectionUtil = new SFTPConnectionUtil();

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            JSch jSch = new JSch();
            jSch.addIdentity(privateKeyPath);
            Session session = jSch.getSession(userName, hostName, 22);
            session.setConfig(config);
            session.setTimeout(30000);
            logger.info("**Connecting to** {} UserName:: {}", maskInput(hostName), userName);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect(20000);
            logger.info("Connected to :: {} UserName:: {}", maskInput(hostName), userName);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpConnectionUtil.setChannelSftp(sftpChannel);
            sftpConnectionUtil.setSession(session);
            return sftpConnectionUtil;
        } catch (Exception e) {
            logger.error("**Exception While Connecting to SFTP Server** Username: {}, IP Address: {}",
                    userName, maskInput(hostName), e);
//            commons.sendSlackMessage("Exception While Connecting to SFTP Server", e, null);
            // if password or username or hostname invalid. session and channel object will not be instantiated
            // so no need to write disconnect logic
        }
        return sftpConnectionUtil;
    }

    public void deleteStatusXMLFileFromLocal(String fileName, String xmlFilesUploadedPath) {
        File file = new File(xmlFilesUploadedPath, fileName);
        if (file.delete())
            logger.info(xmlFilesUploadedPath + fileName + " File deleted");
        else
            logger.info(xmlFilesUploadedPath + fileName + " doesn't exist");
    }

    public LocalDateTime convertStringDateToLocalDate(String totalDateTime, String pattern) {
        LocalDateTime dateTime = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date date = formatter.parse(totalDateTime);
            dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            System.out.println("dateTime::" + dateTime);
            return dateTime;
        } catch (Exception ignored) {

        }
        return dateTime;
    }

    public ZonedDateTime convertLocalDateTimeToUTC(LocalDateTime scheduledDateTime, String timeZoneId) {
        ZoneId zoneId = ZoneId.of(timeZoneId);
        ZonedDateTime zonedDateTime = scheduledDateTime.atZone(zoneId);
//		ZonedDateTime utcDate = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return zonedDateTime;
    }

    public void moveParsedFileToArchiveNew(String parsedFileName, ChannelSftp sftpChannel, String shipmentPath, boolean isUnprocessedFile) throws Exception{

            logger.info("Inside moveParsedFileToArchive");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();
            
            String subDirName = dtf.format(now);
            logger.info("SubFolderName::" + subDirName);

            String srcFilePath=null;
            String destFilePath=null;

            if (sftpChannel != null && sftpChannel.isConnected()) {
                try {
                	 logger.info(sftpChannel.pwd());
                    sftpChannel.lstat(shipmentPath + "/ARCHIVE");
                } catch (SftpException e) {
                    logger.info("ARCHIVE Folder Not existed and creating Folder");
                    if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        logger.info(sftpChannel.pwd());
                        sftpChannel.mkdir(ARCHIVE);
                    }
                }
                try {
                    sftpChannel.cd(ARCHIVE);
                    sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName);
                } catch (Exception ex) {
                    sftpChannel.mkdir(subDirName);
                }
                logger.info(sftpChannel.pwd());
                logger.info("ParsedFileName:: " + parsedFileName);

                //move files to UNPROCESSED Folder if file not processed due to any unhandled exception
                if (isUnprocessedFile) {
                    sftpChannel.cd(subDirName);
                    try {
                        sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES");
                    } catch (SftpException ex1) {
                        logger.info("UNPROCESSEDFILES Folder Not existed and creating Folder");
                        if (ex1.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                            logger.info(sftpChannel.pwd());
                            sftpChannel.mkdir(UNPROCESSEDFILES);
                        }
                    }

                    srcFilePath= shipmentPath + BACKSLASH_DELIMITER + parsedFileName;
                    destFilePath= shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES/" + parsedFileName;

                    sftpChannel.rename(srcFilePath, destFilePath);
                    logger.info("Inside Unprocessed:: PWD:: " + sftpChannel.pwd());
                    sftpChannel.cd("../..");
                } else {

                    srcFilePath= shipmentPath + BACKSLASH_DELIMITER + parsedFileName;
                    destFilePath= shipmentPath + "/ARCHIVE/" + subDirName + BACKSLASH_DELIMITER + parsedFileName;

                    sftpChannel.rename(srcFilePath, destFilePath);
                    logger.info("Inside Archive:: subdirname:: " + sftpChannel.pwd());
                    sftpChannel.cd("..");
                }

                logger.info("File moved successfully from:: " + srcFilePath + " -> to:: "+destFilePath);
                logger.info("Current Working Directory:: " + sftpChannel.pwd());

            }
    }

    public void moveParsedFileToArchive(String parsedFileName, ChannelSftp sftpChannel, String shipmentPath, boolean isUnprocessedFile){

        try {
        logger.info("Inside moveParsedFileToArchive");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String subDirName = dtf.format(now);
        logger.info("SubFolderName::" + subDirName);

        String srcFilePath=null;
        String destFilePath=null;

        if (sftpChannel != null && sftpChannel.isConnected()) {
            try {
                System.out.println(sftpChannel.pwd());
                sftpChannel.lstat(shipmentPath + "/ARCHIVE");
            } catch (SftpException e) {
                logger.info("ARCHIVE Folder Not existed and creating Folder");
                if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    logger.info(sftpChannel.pwd());
                    sftpChannel.mkdir(ARCHIVE);
                }
            }
            try {
                sftpChannel.cd(ARCHIVE);
                sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName);
            } catch (Exception ex) {
                sftpChannel.mkdir(subDirName);
            }
            logger.info(sftpChannel.pwd());
            logger.info("ParsedFileName:: " + parsedFileName);

            //move files to UNPROCESSED Folder if file not processed due to any unhandled exception
            if (isUnprocessedFile) {
                sftpChannel.cd(subDirName);
                try {
                    sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES");
                } catch (SftpException ex1) {
                    logger.info("UNPROCESSEDFILES Folder Not existed and creating Folder");
                    if (ex1.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        logger.info(sftpChannel.pwd());
                        sftpChannel.mkdir(UNPROCESSEDFILES);
                    }
                }

                srcFilePath= shipmentPath + BACKSLASH_DELIMITER + parsedFileName;
                destFilePath= shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES/" + parsedFileName;

                sftpChannel.rename(srcFilePath, destFilePath);
                logger.info("Inside Unprocessed:: PWD:: " + sftpChannel.pwd());
                sftpChannel.cd("../..");
            } else {

                srcFilePath= shipmentPath + BACKSLASH_DELIMITER + parsedFileName;
                destFilePath= shipmentPath + "/ARCHIVE/" + subDirName + BACKSLASH_DELIMITER + parsedFileName;

                sftpChannel.rename(srcFilePath, destFilePath);
                logger.info("Inside Archive:: subdirname:: " + sftpChannel.pwd());
                sftpChannel.cd("..");
            }

            logger.info("File moved successfully from:: " + srcFilePath + " -> to:: "+destFilePath);
            logger.info("Current Working Directory:: " + sftpChannel.pwd());

        }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Exception While Moving Parsed file " + parsedFileName + " To Archive.." + e.getMessage());
        }
    }

//    public void moveParsedFileToArchiveNew(String parsedFileName, ChannelSftp sftpChannel, String shipmentPath, boolean isUnprocessedFile, Marshaller marshaller, SharpGRInboundRootEleDTO obj) {
//
//        try {
//            logger.info("Inside moveParsedFileToArchive");
//            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
//            LocalDateTime now = LocalDateTime.now();
//            String subDirName = dtf.format(now);
//            logger.info("SubFolderName::" + subDirName);
//            if (sftpChannel != null && sftpChannel.isConnected()) {
//                try {
//                    System.out.println(sftpChannel.pwd());
//                    sftpChannel.lstat(shipmentPath + "/ARCHIVE");
//                } catch (SftpException e) {
//                    logger.info("ARCHIVE Folder Not existed and creating Folder");
//                    if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
//                        logger.info(sftpChannel.pwd());
//                        sftpChannel.mkdir(ARCHIVE);
//                    }
//                }
//                try {
//                    sftpChannel.cd(ARCHIVE);
//                    sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName);
//                } catch (Exception ex) {
//                    sftpChannel.mkdir(subDirName);
//                }
//                logger.info(sftpChannel.pwd());
//                logger.info("ParsedFileName:: " + parsedFileName);
//                //move files to UNPROCESSED Folder if file not processed due to any unhandled exception
//                if (isUnprocessedFile) {
//                    sftpChannel.cd(subDirName);
//                    try {
//                        sftpChannel.lstat(shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES");
//                    } catch (SftpException ex1) {
//                        logger.info("UNPROCESSEDFILES Folder Not existed and creating Folder");
//                        if (ex1.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
//                            logger.info(sftpChannel.pwd());
//                            sftpChannel.mkdir(UNPROCESSEDFILES);
//                        }
//                    }
//                    sftpChannel.rename(shipmentPath + BACKSLASH_DELIMITER + parsedFileName, shipmentPath + "/ARCHIVE/" + subDirName + "/UNPROCESSEDFILES/" + parsedFileName);
//                    logger.info("Inside Unprocessed:: PWD:: " + sftpChannel.pwd());
//                    sftpChannel.cd("../..");
//                } else {
//                    sftpChannel.rename(shipmentPath + BACKSLASH_DELIMITER + parsedFileName, shipmentPath + "/ARCHIVE/" + subDirName + BACKSLASH_DELIMITER + parsedFileName);
//                    logger.info("Inside Archive:: subdirname:: " + sftpChannel.pwd());
//                    sftpChannel.cd("..");
//                }
//                logger.info("Current Working Directory:: " + sftpChannel.pwd());
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.debug("Exception While Moving Parsed file " + parsedFileName + " To Archive.." + e.getMessage());
//        }
//    }

    /*
    This method creates the folder in SFTP in the following way:-
    Source Folder
    |___ ARCHIVE (folder)
         |___ 07062023 (Today's Date folder)
              |___ PROCESSED (folder)
                   |___ file1, file2 ... (processed files)
              |___ UNPROCESSED (folder)
                   |___ file1, file2 ... (unprocessed files)
     */
    public void moveParsedFileToArchiveTree(String parsedFileName, ChannelSftp sftpChannel, String sourcePath,
                                            boolean isProcessed, File file) {
        try {
            logger.info("Inside moveParsedFileToProcessed");
            logger.info("PWD::{}", sftpChannel.pwd());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMddyyyy");
            LocalDateTime now = LocalDateTime.now();
            String subFolderName = dtf.format(now);
            logger.info("SubFolderName::" + subFolderName);
            if (sftpChannel.isConnected()) {
                try {
                    System.out.println("Current Working Directory :: " + sftpChannel.pwd());
                    sftpChannel.lstat(sourcePath + "/ARCHIVE");
                } catch (SftpException e) {
                    logger.info("ARCHIVE Folder Doesn't exist... Creating ARCHIVE Folder");
                    if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        logger.info(sftpChannel.pwd());
                        sftpChannel.mkdir("ARCHIVE");
                    }
                }
                try {
                    sftpChannel.cd("ARCHIVE");
                    sftpChannel.lstat(sourcePath + "/ARCHIVE/" + subFolderName);
                } catch (SftpException ex) {
                    logger.info("Date Folder Doesn't exist... Creating " + subFolderName + " Folder");
                    if(ex.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        sftpChannel.mkdir(subFolderName);
                    }
                }
                logger.info(sftpChannel.pwd());
                logger.info("ParsedFileName:: " + parsedFileName);
                String childFolderName = isProcessed ? "PROCESSED" : "UNPROCESSED";
                sftpChannel.cd(subFolderName);
                try {
                    sftpChannel.lstat(sourcePath + "/ARCHIVE/" + subFolderName + "/" + childFolderName);
                } catch (SftpException ex1) {
                    logger.info(childFolderName + " Folder Doesn't exist... Creating " + childFolderName + " Folder...");
                    if (ex1.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                        logger.info(sftpChannel.pwd());
                        sftpChannel.mkdir(childFolderName);
                    }
                }
                sftpChannel.cd(childFolderName);
                logger.info("Inside " + childFolderName + " :: PWD :: " + sftpChannel.pwd());
                try {
                    sftpChannel.rename(sourcePath + "/" + parsedFileName,
                            sourcePath + "/ARCHIVE/" + subFolderName + "/" + childFolderName + "/" + parsedFileName);
                } catch (SftpException e) {
                    try {
                        if(file != null)
                            sftpChannel.put(file.getAbsolutePath(),
                                sourcePath + "/ARCHIVE/" + subFolderName + "/" + childFolderName + "/" + file.getName());
                    }
                    catch (SftpException ignored) {}
                }
                logger.info("Saved File Path -> " + sourcePath + "/ARCHIVE/" + subFolderName + "/" + childFolderName + "/" + parsedFileName);
                sftpChannel.cd("../../..");
                logger.info("Current Working Directory:: " + sftpChannel.pwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Exception While Moving Parsed file To Processed.." + e.getMessage());
        }
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }


    public static Double getDistance(Float[] c1, Float[] c2) {

        if (c1 == null || c2 == null) {
            return null;
        }
        double lat1 = c1[1];
        double lon1 = c1[0];
        double lat2 = c2[1];
        double lon2 = c2[0];
        int MAXITERS = 20;
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha;
        double cos2SM;
        double cosSigma;
        double sinSigma;
        double cosLambda;
        double sinLambda;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * // (6)
                    (cos2SM + (B / 4.0) * (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0) * cos2SM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SMSq)));

            lambda = L + (1.0 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }
        return (b * A * (sigma - deltaSigma)) / 1000; // returns distance in KM
    }

    public static String maskInput(String input) {
        int inputLength = input.length();
        int maskLength = Math.min(inputLength / 2, 4); // Minimum between half of the length and 4
        return "x".repeat(inputLength - maskLength) + input.substring(inputLength - maskLength);
    }

}
