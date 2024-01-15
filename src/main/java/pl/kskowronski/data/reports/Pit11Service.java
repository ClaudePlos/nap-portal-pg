package pl.kskowronski.data.reports;

import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.xml.sax.InputSource;
import pl.kskowronski.data.service.UserRepository;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class Pit11Service {

    @Autowired
    private UserRepository userRepo;

    public String exportPit11Report(String reportFormat, String workerID, String yearPit, String xmlPit11) throws FileNotFoundException, JRException {
        String path = "C:\\tmp\\";

        //load file and compile it
        //ClassLoader cl = this.getClass().getClassLoader();
        //URL url =  cl.getResource(getPatternVersion(yearPit));


        String nameOS = System.getProperty("os.name");
        //String absolutePath = url.getPath(); //+ "\\"

        //if (!nameOS.toUpperCase().contains("WINDOWS")){
            //absolutePath = "/home/szeryf/kskowronski_projects/nap_emp_portal/pit11_pdf/" + getPatternVersionDocker(yearPit);
            //path = "/home/szeryf/kskowronski_projects/nap_emp_portal/pit11_pdf/";
        //}
        //System.out.println(absolutePath);
        //File file = ResourceUtils.getFile("classpath:pit11_25.jrxml"); // only for windows
        //File filePattern = ResourceUtils.getFile(absolutePath);
        InputStream employeeReportStream = getClass().getResourceAsStream(getPatternVersion(yearPit)); //update ks 2021-12-23 much better
        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listUsers);
        //Map<String, Object> parameters = new HashMap<>();
        //parameters.put("createdBy", "Java Techie");

        Map<String, Object> params = new HashMap<String, Object>();
        Document document = convertStringToXMLDocument(xmlPit11); //JRXmlUtils.parse(new InputSource(new StringReader(xmlPit11)), true); //JRLoader.getLocationInputStream("classpath:xmlFromDB.xml"));
        params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
        params.put("createdBy", "anfix poland");

        JRDesignStyle jrDesignStyle = new JRDesignStyle();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params);
        jrDesignStyle.setPdfEncoding("UTF-8");
        jasperPrint.addStyle(jrDesignStyle);
        jasperPrint.setLocaleCode("UTF-8");

        String pathPdfFile = path + "pit11_"+ yearPit + "_" + workerID + ".pdf";
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\pit11_"+ yearPit + "_" + workerID + ".html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathPdfFile);
        }

        return pathPdfFile;
    }


    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            InputSource is = new InputSource(new StringReader(xmlString));
            //is.setEncoding("ISO-8859-2");
            Document doc = builder.parse(is);
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String getPatternVersion( String year ) {

        if (year.equals("2019"))
            return "/pit11/v25/pit11_25.jrxml";
        if (year.equals("2020"))
            return "/pit11/v26/pit11_26.jrxml";
        if (year.equals("2021"))
            return "/pit11/v27/pit11_27.jrxml";
        if (year.equals("2022"))
            return "/pit11/v29/PIT11_29.jrxml";
        if (year.equals("2023"))
            return "/pit11/v29/PIT11_29.jrxml";
        return "-";

    }

    // If you want from file outside - klaudiusz
    /*

    import java.io.File;
    import javax.xml.parsers.DocumentBuilder;
    import javax.xml.parsers.DocumentBuilderFactory;
    import org.w3c.dom.Document;

    public class StringtoXMLExample
    {
        public static void main(String[] args)
        {
            final String xmlFilePath = "employees.xml";

            //Use method to convert XML string content to XML Document object
            Document doc = convertXMLFileToXMLDocument( xmlFilePath );

            //Verify XML document is build correctly
            System.out.println(doc.getFirstChild().getNodeName());
        }

        private static Document convertXMLFileToXMLDocument(String filePath)
        {
            //Parser that produces DOM object trees from XML content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //API to obtain DOM Document instance
            DocumentBuilder builder = null;
            try
            {
                //Create DocumentBuilder with default configuration
                builder = factory.newDocumentBuilder();

                //Parse the content to Document object
                Document doc = builder.parse(new File(filePath));
                return doc;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

     */

}
