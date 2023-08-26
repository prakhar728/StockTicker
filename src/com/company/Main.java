package com.company;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	//let's try and give this code  a whirl
        try{
            String urlString = "http://www.nseindia.com/content/historical/EQUITIES/2015/JUL/cm17JUL2015bhav.csv.zip";
            String zipFilePath = "/Users/swethakolalapudi/JavaDrills/boo.zip";
            String destinationDirectory="/Users/swethakolalapudi/JavaDrills/";



            List<String> unzippedFileList = UnzipUtility.downloadAndUnzip(urlString, zipFilePath, destinationDirectory);


            if (unzippedFileList!=null){
                String csvFile = unzippedFileList.get(0);
                OneDayMarketAction odma = new OneDayMarketAction(csvFile);
                // use the constructor of the class we just defined

                List<OneDayMarketAction.OneTickerOneDay> listOfMovers = odma.getSortedMovers();
                // btw, notice how the inner class 'OneTickerOneDay' is referred to as
                // OneDayMarketAction.OneTickerOneDay by code outside the class
                // static inner classes are always referred to as OuterClass.InnerClass

                for (OneDayMarketAction.OneTickerOneDay otod:listOfMovers){
                    System.out.println("Ticker="+otod.getM_ticker()+", Moved by" +otod.getPctChange()*100+"%");
                }

                // Eyeballing the results convinces us that the results are really sorted in the order of
                // how much they moved.
                // The smallest (negative) movers are up top and the largest (gainers) are last

                // Ok. now let's actually create the excel
                ExcelWriter xlWriter = new ExcelWriter(odma);
                xlWriter.createFile("/Users/swethakolalapudi/JavaDrills/Test.xls");

            }

            System.out.println("All done");

        }
        catch (Exception ex){
            ex.printStackTrace();
            // since we are dealing with files and downloading from the web - both of which are operations prone
            // to throw errors, we are surrounding the code with a try/catch block.
        }
    }
}
