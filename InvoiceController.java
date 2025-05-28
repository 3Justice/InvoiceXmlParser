import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    /**
     * 解析上传的XML文件
     */
    @PostMapping("/parse")
    public Invoice parseInvoice(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("上传的文件不能为空");
            }

            // 读取文件内容
            String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 解析XML文件
            return InvoiceXmlParser.parse(xmlContent);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败", e);
        } catch (Exception e) {
            throw new RuntimeException("XML解析失败", e);
        }
    }
}