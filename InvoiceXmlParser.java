import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 电子发票XML解析器
 */
public class InvoiceXmlParser {

    public static Invoice parse(String xmlContent) throws Exception {
        if (xmlContent == null) {
            throw new RuntimeException("xmlContent is null");
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

        Invoice invoice = new Invoice();

        // 解析各部分信息
        parseHeader(document, invoice);
        parseEInvoiceData(document, invoice);
        parseTaxSupervisionInfo(document, invoice);

        return invoice;
    }

    private static void parseHeader(Document document, Invoice invoice) {
        NodeList headerList = document.getElementsByTagName("Header");
        if (headerList.getLength() > 0) {
            Element header = (Element) headerList.item(0);

            // 发票代码和号码
            String eiid = getElementText(header, "EIid");
            invoice.setDigitalTicketNumber(eiid);

            // 发票类型
            NodeList inherentLabelList = header.getElementsByTagName("InherentLabel");
            if (inherentLabelList.getLength() > 0) {
                Element inherentLabel = (Element) inherentLabelList.item(0);

                // 是否蓝字发票
                String labelCode = getElementText(inherentLabel, "LabelCode", "InIssuType");
                invoice.setIsPositive("Y".equals(labelCode) ? 1 : 0);

                // 发票种类 (普通发票或专票)
                String vatType = getElementText(inherentLabel, "LabelCode", "GeneralOrSpecialVAT");
                invoice.setInvoiceType("02".equals(vatType) ? 0 : 1);

                // 发票类别
                String eInvoiceType = getElementText(inherentLabel, "LabelName", "EInvoiceType");
                invoice.setInvoiceCategory(eInvoiceType);
            }

            // 开票方式
            NodeList undefinedLabelList = header.getElementsByTagName("UndefinedLabel");
            if (undefinedLabelList.getLength() > 0) {
                Element undefinedLabel = (Element) undefinedLabelList.item(0);
                NodeList labelList = undefinedLabel.getElementsByTagName("Label");

                for (int i = 0; i < labelList.getLength(); i++) {
                    Element label = (Element) labelList.item(i);
                    String labelType = getElementText(label, "LabelType");

                    if ("发票开具方式标签".equals(labelType)) {
                        String labelName = getElementText(label, "LabelName");
                        invoice.setIssueLocation(labelName);
                        break;
                    }
                }
            }
        }
    }

    private static void parseEInvoiceData(Document document, Invoice invoice) {
        NodeList einvoiceDataList = document.getElementsByTagName("EInvoiceData");
        if (einvoiceDataList.getLength() > 0) {
            Element einvoiceData = (Element) einvoiceDataList.item(0);

            // 销售方信息
            NodeList sellerInfoList = einvoiceData.getElementsByTagName("SellerInformation");
            if (sellerInfoList.getLength() > 0) {
                Element sellerInfo = (Element) sellerInfoList.item(0);
                invoice.setSellerName(getElementText(sellerInfo, "SellerName"));
                invoice.setSellerTaxId(getElementText(sellerInfo, "SellerIdNum"));
            }

            // 购买方信息
            NodeList buyerInfoList = einvoiceData.getElementsByTagName("BuyerInformation");
            if (buyerInfoList.getLength() > 0) {
                Element buyerInfo = (Element) buyerInfoList.item(0);
                invoice.setBuyerName(getElementText(buyerInfo, "BuyerName"));
                invoice.setBuyerTaxId(getElementText(buyerInfo, "BuyerIdNum"));
            }

            // 基本信息
            NodeList basicInfoList = einvoiceData.getElementsByTagName("BasicInformation");
            if (basicInfoList.getLength() > 0) {
                Element basicInfo = (Element) basicInfoList.item(0);

                // 金额、税额、价税合计
                invoice.setAmount(new BigDecimal(Objects.requireNonNull(getElementText(basicInfo, "TotalAmWithoutTax"))));
                invoice.setTaxAmount(new BigDecimal(Objects.requireNonNull(getElementText(basicInfo, "TotalTaxAm"))));
                invoice.setTotalAmount(new BigDecimal(Objects.requireNonNull(getElementText(basicInfo, "TotalTax-includedAmount"))));

                // 处理特殊发票类型的开票人信息
                handleSpecialInvoiceIssuer(document, einvoiceData, invoice, basicInfo);

                // 开票时间
                String requestTime = getElementText(basicInfo, "RequestTime");
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(requestTime);
                    invoice.setIssueDate(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void handleSpecialInvoiceIssuer(Document document, Element einvoiceData,
                                                   Invoice invoice, Element basicInfo) {
        // 获取发票类型信息
        NodeList inherentLabelList = document.getElementsByTagName("InherentLabel");
        if (inherentLabelList.getLength() == 0) {
            invoice.setIssuer(getElementText(basicInfo, "Drawer"));
            return;
        }

        Element inherentLabel = (Element) inherentLabelList.item(0);
        String vatType = getElementText(inherentLabel, "LabelCode", "GeneralOrSpecialVAT");
        String eInvoiceType = getElementText(inherentLabel, "LabelCode", "EInvoiceType");

        // 铁路电子客票（类型05）
        if ("05".equals(vatType)) {
            NodeList railwayList = einvoiceData.getElementsByTagName("RailwayE-Ticket");
            if (railwayList.getLength() > 0) {
                Element railway = (Element) railwayList.item(0);
                invoice.setIssuer(getElementText(railway, "NameOfPassenger"));
            } else {
                invoice.setIssuer(getElementText(basicInfo, "Drawer"));
            }
        }
        // 航空运输电子客票（类型04）
        else if ("04".equals(vatType)) {
            NodeList airTicketList = einvoiceData.getElementsByTagName("AirTravelTicket");
            if (airTicketList.getLength() > 0) {
                Element airTicket = (Element) airTicketList.item(0);
                invoice.setIssuer(getElementText(airTicket, "PassengerName"));
            } else {
                invoice.setIssuer(getElementText(basicInfo, "Drawer"));
            }
        }
        // 通行费发票（类型03）
        else if ("03".equals(vatType)) {
            NodeList tollList = einvoiceData.getElementsByTagName("TollInformation");
            if (tollList.getLength() > 0) {
                Element toll = (Element) tollList.item(0);
                String licensePlate = getElementText(toll, "LicensePlateNumber");
                invoice.setIssuer(licensePlate != null ? "车辆: " + licensePlate : "高速公路管理局");
            } else {
                invoice.setIssuer(getElementText(basicInfo, "Drawer"));
            }
        }
        // 其他类型发票
        else {
            invoice.setIssuer(getElementText(basicInfo, "Drawer"));
        }
    }

    private static void parseTaxSupervisionInfo(Document document, Invoice invoice) {
        NodeList taxSupervisionList = document.getElementsByTagName("TaxSupervisionInfo");
        if (taxSupervisionList.getLength() > 0) {
            Element taxSupervision = (Element) taxSupervisionList.item(0);

            // 发票号码
            invoice.setInvoiceNumber(getElementText(taxSupervision, "InvoiceNumber"));

            // 开票日期
            String issueTime = getElementText(taxSupervision, "IssueTime");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(issueTime);
                invoice.setIssueDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private static String getElementText(Element parent, String tagName, String parentTagName) {
        NodeList parentList = parent.getElementsByTagName(parentTagName);
        if (parentList.getLength() > 0) {
            Element parentElement = (Element) parentList.item(0);
            return getElementText(parentElement, tagName);
        }
        return null;
    }
}