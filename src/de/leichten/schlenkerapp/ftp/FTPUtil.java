package de.leichten.schlenkerapp.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.util.Log;

public class FTPUtil {
    private static final String TAG = null;
    public static FTPClient mFTPClient = null;
    
    
    // Method to connect to FTP server:
    public static boolean ftpConnect(String host, String username, String password, int port) {
    	try {
            mFTPClient = new FTPClient();
            mFTPClient.setConnectTimeout(4000);
            mFTPClient.connect(host, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                boolean status = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                return status;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host);
            return false;
        
        }
        return true;
    }

    // Method to disconnect from FTP server:
    public static boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }

        return false;
    }

    // Method to get current working directory:

    public static String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not get current working directory.");
        }

        return null;
    }

    // Method to change working directory:
    public static boolean ftpChangeDirectory(String directory_path) {
    	boolean changeWorkingDirectory = false;
    	
    	try {
    		changeWorkingDirectory = mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {
            Log.d(TAG, "Error: could not change directory to " + directory_path);
        }
    	return changeWorkingDirectory;

    }

  
    
    
    // Method to check if file exist on FTP:
    public static boolean ftpCheckFileExists(String dir_path, String filename) {
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;

            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                	if (filename.equals(name)){
                		return true;
                	}
                } else {
                	//Do nothing
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    // Method to create new directory:
    public static boolean ftpMakeDirectory(String new_dir_path) {
        try {
        	String status2 = mFTPClient.getStatus();
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not create new directory named " + new_dir_path);
        }

        return false;
    }

    public static void ftpCreateDirectoryTree(String dirTree ) throws IOException {
        mFTPClient.changeWorkingDirectory("/");
 
    	boolean dirExists = true;

    	  //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
    	  String[] directories = dirTree.split("/");
    	  for (String dir : directories ) {
    	    if (!dir.isEmpty() ) {
    	      if (dirExists) {
    	        dirExists = mFTPClient.changeWorkingDirectory(dir);
    	      }
    	      if (!dirExists) {
    	        if (!mFTPClient.makeDirectory(dir)) {
    	          throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + mFTPClient.getReplyString()+"'");
    	        }
    	        if (!mFTPClient.changeWorkingDirectory(dir)) {
    	          throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + mFTPClient.getReplyString()+"'");
    	        }
    	      }
    	    }
    	  }     
    	}
    
    // Method to delete/remove a directory:
    public static boolean ftpRemoveDirectory(String dir_path) {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not remove directory named " + dir_path);
        }

        return false;
    }

    // Method to delete a file:
    public static boolean ftpRemoveFile(String filePath) {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    // Method to rename a file:
    public static boolean ftpRenameFile(String from, String to) {
        try {
            boolean status = mFTPClient.rename(from, to);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Could not rename file: " + from + " to: " + to);
        }

        return false;
    }

    // Method to download a file from FTP server:
    public static boolean ftpDownload(String srcFilePath, String desFilePath) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
            ;
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();

            return status;
        } catch (Exception e) {
            Log.d(TAG, "download failed");
        }

        return status;
    }

    // Method to upload a file to FTP server:
    public static boolean ftpUpload(String srcFilePath, String desFileName,
            String desDirectory) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(srcFileStream);
            // change working directory to the destination directory
            if (ftpChangeDirectory(desDirectory)) {
                status = mFTPClient.storeFile(desFileName, bufferedInputStream);
            }

            srcFileStream.close();
            return status;
        } catch (Exception e) {
            Log.d(TAG, "upload failed");
        }

        return status;
    }

	public static String[] ftpGetFileList(String dir_path) {
		String[] files = null;
		
		try {
			FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
			if(ftpFiles.length > 0){
				files = new String[ftpFiles.length];
				for (int i = 0; i < ftpFiles.length; i++) {
					files[i] = ftpFiles[i].getName();
				}
			}
		
			for (FTPFile ftpFile : ftpFiles) {
				Log.d(TAG, ftpFile.getName());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return files;
	}

	

}