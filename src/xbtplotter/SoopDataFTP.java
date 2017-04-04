package xbtplotter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class SoopDataFTP {

    private static final String serverAddress = "ftp.aoml.noaa.gov";
    private static final String userId = "anonymous";
    private static final String password = "AOML.XBT@noaa.gov";
    private static final String soopDataDoneDir =
            "/pub/phod/soopdata/XBT/done/";

    public SoopDataFTP() {}

    /*
    Download callsigns file from ftp server
    ftp.aoml.noaa.gov/phod/pub/soopdata/XBT/done/
    Read through callsigns file and append each line to array list
    */
    public ArrayList<String> callSigns(String dlDir) {
        ArrayList<String> list = new ArrayList<String>(); 
        String callsign_file_name = "callSigns.txt";
        String download_path_file_name = dlDir + "/" + callsign_file_name;
        String line = null;
        try {
            FTPClient ftp = new FTPClient();
            if(!connectionFTPSettings(ftp)) {
                return list;
            }
            ftp.changeWorkingDirectory(soopDataDoneDir);
            if (!downloadOneFTPFile(download_path_file_name, ftp,
                    callsign_file_name)) {
                ftp.logout();
                ftp.disconnect();
                return list;
            }

            try {
                FileReader fileReader =
                    new FileReader(download_path_file_name);
                BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
                bufferedReader.close();   
            }
            catch(FileNotFoundException ex) {
                System.out.println(
                    "Unable to open file '" +
                    download_path_file_name + "'");
            }
            catch(IOException ex) {
                System.out.println(
                    "Error reading file '" +
                    download_path_file_name + "'");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /*
    Check if Download directory exist
    If not, create one
    */
    public boolean checkDLDirectory(String dlDir) {
        try {
            File fdir = new File(dlDir);
            if (!fdir.isDirectory()) {
                boolean successful = fdir.mkdir();
                if (!successful) {
                    return false;
                }
            }
            deleteOldBinFiles(dlDir);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*
    Check if capable of logging into ftp server
    */
    public boolean checkLogIn() {
        try {
            FTPClient ftp = new FTPClient();
            if(!connectionFTPSettings(ftp)) {
                return false;
            }
            ftp.logout();
            ftp.disconnect();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean connectionFTPSettings(FTPClient ftp) {
        try {
            ftp.connect(serverAddress);
            if(!ftp.login(userId, password)) {
                System.out.println("Could not log in!!!!!");
                ftp.logout();
                ftp.disconnect();
                return false;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
        } catch (ConnectException cx) {
            JOptionPane.showMessageDialog(null, "Cannot Connect! Retry Again!");
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /*
    Delete files older than 32 days of current date
    */
    public void deleteOldBinFiles(String dlDir) {
        try {
            File fdir = new File(dlDir);
            if (fdir.isDirectory()) {
                Date referenceDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(referenceDate);
                c.add(Calendar.DAY_OF_YEAR, -32);
                Date dateLimit = c.getTime();
                for (File f : fdir.listFiles()) {
                    Date fileDate = new Date(f.lastModified());
                    if (fileDate.before(dateLimit)) {
                        f.delete();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Loop through array list containing file names and download each one
    */
    public void downloadFTPFiles(ArrayList<String> fileNames, String dlDir) {
        String currentYear = "2011";
        File theDir = new File(dlDir);
        if (!theDir.isDirectory()) {
            try {
                theDir.mkdir();
            } catch ( Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            FTPClient ftp = new FTPClient();
            if(connectionFTPSettings(ftp)) {
                for (String fn : fileNames) {
                    String[] tokens = fn.split("_");
                    String importYear = tokens[1].substring(0,4);
                    if (!currentYear.equals(importYear)) {
                        currentYear = importYear;
                        String remoteDirectory =
                                "/pub/phod/soopdata/XBT/done/" + importYear;
                        ftp.changeWorkingDirectory(remoteDirectory);
                    }
                    if(!downloadOneFTPFile(dlDir + "/" + fn, ftp, fn)) {
                        System.out.println("Could not download file:" + fn);
                    }
                }
                ftp.logout();
                ftp.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Download an individual file from the ftp server
    */
    public boolean downloadOneFTPFile(String file_path,
            FTPClient ftp, String file_name) {
        try {
            File downloadFile1 = new File(file_path);
            OutputStream outputStream1 =
                new BufferedOutputStream(
                    new FileOutputStream(downloadFile1));
            boolean success =
                ftp.retrieveFile(file_name, outputStream1);
            outputStream1.close();
            if(!success) {downloadFile1.delete();}
            return success;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getBinFileNames(
            String strDate, String endDate, String dlDir,
            String selectedCallSign) {
        ArrayList<String> list = new ArrayList<String>();
        String ftp_file_list = "filelist.txt";
        String download_path_file_name = dlDir + "/" + ftp_file_list;
        int startYr = Integer.parseInt(strDate.substring(0, 4));
        int endYr = Integer.parseInt(endDate.substring(0, 4));

        try {
            File mainFile1 = new File(download_path_file_name);
            if(mainFile1.exists() && !mainFile1.isDirectory()) {
                mainFile1.delete();
            }
            FTPClient ftp = new FTPClient();
            if(!connectionFTPSettings(ftp)) {
                return list;
            }
            try {
                /* If multiple years are selected, loop through each year
                directory to download filelist.txt to a temp file and append
                that file to existing filelist.txt file*/
                for (int i = startYr; i <= endYr; i++) {
                    String download_path_file_name_tmp =
                            download_path_file_name + ".tmp" + i;
                    String remoteDirectory =
                            "/pub/phod/soopdata/XBT/done/" + i;
                    ftp.changeWorkingDirectory(remoteDirectory);
                    if (!downloadOneFTPFile(download_path_file_name_tmp,
                            ftp, ftp_file_list)) {
                        ftp.logout();
                        ftp.disconnect();
                        return new ArrayList<>();
                    }
                    File downloadFile1 = new File(download_path_file_name_tmp);
                    File main_file_list = new File(download_path_file_name);
                    FileInputStream ofis =
                        new FileInputStream(downloadFile1);
                    BufferedReader obr =
                        new BufferedReader(new InputStreamReader(ofis));
                    FileWriter overallFile =
                        new FileWriter(main_file_list, true);
                    BufferedWriter out = new BufferedWriter(overallFile);

                    String aLine = null;
                    while ((aLine = obr.readLine()) != null) {
                            out.write(aLine);
                            out.newLine();
                    }
                    downloadFile1.delete();
                    obr.close();
                    out.close();

                }
            } catch (Exception ex) {
                ftp.logout();
                ftp.disconnect();
                return new ArrayList<>();
            }
            ftp.logout();
            ftp.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
        
        String line = null;
        try {
            FileReader fileReader = 
                new FileReader(download_path_file_name);
            BufferedReader bufferedReader =
                new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                int fileExtension = line.lastIndexOf('.');
                if (fileExtension > 0) {
                    String extension = line.substring(fileExtension+1);
                    if (!extension.equals("bin")) {
                        continue;
                    }
                } else {
                    continue;
                }
                /*Find last forward slash from*/
                int removeDirectoriesIndex = line.lastIndexOf('/') + 1;
                String onlyFileName = line.substring(removeDirectoriesIndex);
                String[] tokens = line.split(";");
                String observationDate = tokens[0].substring(0, 8);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String fileCallSign = tokens[1];
                if ((!selectedCallSign.isEmpty()) &&
                        (!selectedCallSign.equals(fileCallSign))) {
                    continue;
                }

                try {
                    int ctStr =
                        sdf.parse(observationDate)
                                .compareTo(sdf.parse(strDate));
                    int ctEnd =
                        sdf.parse(observationDate)
                                .compareTo(sdf.parse(endDate));
                    
                    if (ctStr >= 0 && ctEnd <= 0) {
                        list.add(onlyFileName);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
                download_path_file_name + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '"
                + download_path_file_name + "'");
        }
        return list;
    }
}
