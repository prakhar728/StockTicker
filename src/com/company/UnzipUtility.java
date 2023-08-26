package com.company;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by swethakolalapudi on 10/10/15.
 */
public class UnzipUtility {
    // we will use this class to abstract the rest of the code from 2 operations
    // 1. downloading the zip from the NSE website
    // 2. unzipping the downloadd zip file from the website to some location on our location machine


    // note the order in which we went about this - we first decided what member functions we would like
    // our class to have. We defined the 'interface' for these functions , i.e. what inputs and outputs those
    // functions would have

    // note also that we made all 3 member functions here 'static', which was because these member
    // functions have very little (none) object-specific state or behaviour. They almost perfectly resemble
    // functions from a pure procedural or imperative language, but they are still housed in a class because in
    // java all code must reside in a class


    private static final int S_BYTE_SIZE=4096;

    public static List<String> downloadAndUnzip(String urlString, String zipFilePath, String destDirectory)
    throws IOException {


        URL tariff = new URL(urlString);
        String myUserAgentString="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36";

        java.net.URLConnection c= tariff.openConnection();
        c.setRequestProperty("User-Agent", myUserAgentString);

        ReadableByteChannel zipByteChannel = Channels.newChannel(c.getInputStream());
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        fos.getChannel().transferFrom(zipByteChannel,0,Long.MAX_VALUE);

        return unzip(zipFilePath,destDirectory);


    }

    public static List<String> unzip(String zipFilePath, String destDirectory) throws IOException {



        List<String> unzippedFileList = new ArrayList<>();

        File destDir = new File(destDirectory);

        if(!destDir.exists()){
            destDir.mkdir();
        }



        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));



        ZipEntry zipEntry = zipIn.getNextEntry();
        while (zipEntry!=null){
            String filePath = destDirectory+File.separator+zipEntry.getName();


            if(!zipEntry.isDirectory()){

                String oneUnzippedFile=extractFile(zipIn, filePath);
                unzippedFileList.add(oneUnzippedFile);
                            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipEntry = zipIn.getNextEntry();
        }

        return unzippedFileList;
    }

    private static String extractFile(ZipInputStream zipIn, String filePath) throws IOException{

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));

        byte[] bytesIn = new byte[S_BYTE_SIZE];




        int read = 0;
        while ((read=zipIn.read(bytesIn))!= -1){

            bos.write(bytesIn,0,read);

        }
        bos.close();
        return filePath;


    }



}
