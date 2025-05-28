import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子发票实体类
 */
public class Invoice {
    /** 发票代码 */
    private String invoiceCode;

    /** 发票号码(纸质发票) */
    private String invoiceNumber;

    /** 数电票号 */
    private String digitalTicketNumber;

    /** 发票种类(0：普通发票，1：专票) */
    private Integer invoiceType;

    /** 购买方名称 */
    private String buyerName;

    /** 购买方税号 */
    private String buyerTaxId;

    /** 销售方名称 */
    private String sellerName;

    /** 销售方税号 */
    private String sellerTaxId;

    /** 发票类别 */
    private String invoiceCategory;

    /** 开票日期 */
    private Date issueDate;

    /** 金额 */
    private BigDecimal amount;

    /** 税额 */
    private BigDecimal taxAmount;

    /** 价税合计 */
    private BigDecimal totalAmount;

    /** 开票地点 */
    private String issueLocation;

    /** 是否正数发票(0:负数,1:正数) */
    private Integer isPositive;

    /** 开票人 */
    private String issuer;

    // Getters and Setters
    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDigitalTicketNumber() {
        return digitalTicketNumber;
    }

    public void setDigitalTicketNumber(String digitalTicketNumber) {
        this.digitalTicketNumber = digitalTicketNumber;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxId() {
        return buyerTaxId;
    }

    public void setBuyerTaxId(String buyerTaxId) {
        this.buyerTaxId = buyerTaxId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxId() {
        return sellerTaxId;
    }

    public void setSellerTaxId(String sellerTaxId) {
        this.sellerTaxId = sellerTaxId;
    }

    public String getInvoiceCategory() {
        return invoiceCategory;
    }

    public void setInvoiceCategory(String invoiceCategory) {
        this.invoiceCategory = invoiceCategory;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIssueLocation() {
        return issueLocation;
    }

    public void setIssueLocation(String issueLocation) {
        this.issueLocation = issueLocation;
    }

    public Integer getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(Integer isPositive) {
        this.isPositive = isPositive;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", digitalTicketNumber='" + digitalTicketNumber + '\'' +
                ", invoiceType=" + invoiceType +
                ", buyerName='" + buyerName + '\'' +
                ", buyerTaxId='" + buyerTaxId + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", sellerTaxId='" + sellerTaxId + '\'' +
                ", invoiceCategory='" + invoiceCategory + '\'' +
                ", issueDate=" + issueDate +
                ", amount=" + amount +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", issueLocation='" + issueLocation + '\'' +
                ", isPositive=" + isPositive +
                ", issuer='" + issuer + '\'' +
                '}';
    }
}