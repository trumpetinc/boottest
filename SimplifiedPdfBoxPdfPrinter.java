/**
 * 
 */
package com.trumpetinc.assemblage.printer;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 * @author Kevin
 *
 */
public class SimplifiedPdfBoxPdfPrinter {

    private final String printerName;

    /**
	 * 
	 */
	public SimplifiedPdfBoxPdfPrinter(String printerName) {
		this.printerName = printerName;
	}

    private static void forceFileExist(File f) throws IOException{
        new FileOutputStream(f).close();
    }
	
    private static PrintService getPrintService(String name){
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(name)){
                return service;
            }
        }
        return null;
    }
    
    
	public void print(File pdf) throws Exception {
        if (!pdf.exists())
            throw new IOException(pdf + " does not exist before printing - conversion to prn not possible");

        PrintService printService = getPrintService(printerName);
        
		PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintService(printService);
	    
	    try(PDDocument doc = PDDocument.load(pdf, MemoryUsageSetting.setupMixed(5000000L))){
	    	PDFPageable pageable = new PDFPageable(doc);

	    	job.setPageable(pageable);
	
		    // print with the attributes
		    job.print();
	    }			    
		    
	}


	public static void main(String[] args) throws Exception {
		SimplifiedPdfBoxPdfPrinter svc = new SimplifiedPdfBoxPdfPrinter("\\\\Moby\\Cust Suc Dell C2660dn");
		svc.print(new File("S:\\ClientData\\ClientData\\XPRIA-TPT100892\\JrachvUniverse_H21.pdf"));
	}
}
