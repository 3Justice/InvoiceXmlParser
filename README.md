中国电子发票XML解析器

简介

这是一个简单的Java工具，用于解析中国电子发票XML文件并提取关键信息。

功能

• 解析电子发票XML文件

• 提取发票基本信息（代码、号码、金额等）

• 识别买卖双方信息

• 支持普通发票和专用发票


使用示例

```java
// 从XML字符串解析
String xmlContent = "<Invoice>...</Invoice>";
Invoice invoice = InvoiceXmlParser.parse(xmlContent);

// 从文件解析
Path path = Paths.get("invoice.xml");
String xmlContent = Files.readString(path, StandardCharsets.UTF_8);
Invoice invoice = InvoiceXmlParser.parse(xmlContent);

// 获取解析结果
System.out.println("发票号码: " + invoice.getInvoiceNumber());
System.out.println("金额: " + invoice.getAmount());
```

依赖

• Java 8+

• 无额外第三方依赖


开源协议

MIT License