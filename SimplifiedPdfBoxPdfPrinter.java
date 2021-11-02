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
    
    
	public void convertToPrn(File pdf, File prn) throws IOException {
		try {
	        if (!pdf.exists())
	            throw new IOException(pdf + " does not exist before printing - conversion to prn not possible");
	
	        if (prn.exists() && !prn.delete())
	            throw new IOException("Unable to delete " + prn);
	        
	        File tempPrn = new File(prn.getParentFile(), "tempPrn"); 
	        forceFileExist(tempPrn);
	        
	        // implementation note:  we have to convert the printer name to something besides it's actual name before passing it in.  I have no idea why this matters, but if we don't, it prints to the printer instead of to the named port.  Very, very odd.
	        String altPrinterName = printerName.toUpperCase();
	        if (altPrinterName.equals(printerName))
	            altPrinterName = printerName.toLowerCase();

	        PrintService printService = getPrintService(altPrinterName);
	        
            PdfPrintHandler handler = new PdfPrintHandler(altPrinterName);
	        
			PrinterJob job = PrinterJob.getPrinterJob();
		    job.setPrintService(printService);
		    
		    // create a new HashPrintRequestAttributeSet and initialize it with the printer's default attributes
		    HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

		    // set the output file as a destination 
		    attributes.add(new javax.print.attribute.standard.Destination(tempPrn.toURI()));
		    
		    try(PDDocument doc = PDDocument.load(pdf, MemoryUsageSetting.setupMixed(5000000L))){
		    	PDFPageable pageable = new PDFPageable(doc);

		    	job.setPageable(pageable);
		
			    // print with the attributes
			    job.print(attributes);
		    }			    
		    
            if (!tempPrn.exists() || tempPrn.length() == 0){
                throw new IOException(tempPrn + " does not exist after printing");
            }
            
            if (!tempPrn.renameTo(prn))
                throw new IOException("Unable to rename " + tempPrn + " to " + prn);
			    
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }	    
	}


	public static void main(String[] args) throws IOException {
		SimplifiedPdfBoxPdfPrinter svc = new SimplifiedPdfBoxPdfPrinter("\\\\Moby\\Cust Suc Dell C2660dn");
		svc.convertToPrn(new File("S:\\ClientData\\ClientData\\XPRIA-TPT100892\\JrachvUniverse_H21.pdf"), new File("S:\\ClientData\\ClientData\\XPRIA-TPT100892\\JrachvUniverse_H21.prn"));
	}
}
