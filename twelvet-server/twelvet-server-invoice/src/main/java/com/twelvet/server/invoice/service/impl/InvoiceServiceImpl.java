package com.twelvet.server.invoice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil; // For BigDecimal operations
import cn.hutool.extra.pinyin.PinyinUtil; // Example, might need a specific library for money to words
import com.twelvet.framework.core.exception.TWTException; // Assuming a common exception type
import com.twelvet.framework.utils.Arith; // From the image, seems like a custom util for arithmetic
import com.twelvet.framework.utils.MoneyUtils; // Assuming utility for money to words
import com.twelvet.api.invoice.dto.InvoiceItemRequestDTO; // Updated import
import com.twelvet.api.invoice.dto.InvoiceItemResponseDTO; // Updated import
import com.twelvet.api.invoice.dto.InvoiceRequestDTO; // Updated import
import com.twelvet.api.invoice.dto.InvoiceResponseDTO; // Updated import
import com.twelvet.server.invoice.domain.Invoice;
import com.twelvet.server.invoice.domain.InvoiceItem;
import com.twelvet.server.invoice.mapper.InvoiceItemMapper;
import com.twelvet.server.invoice.mapper.InvoiceMapper;
import com.twelvet.server.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发票服务实现类
 *
 * @author TwelveT
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    @Autowired
    public InvoiceServiceImpl(InvoiceMapper invoiceMapper, InvoiceItemMapper invoiceItemMapper) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    /**
     * 根据ID获取发票详情
     *
     * @param invoiceId 发票ID
     * @return 发票详情DTO
     */
    @Override
    public InvoiceResponseDTO getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceMapper.selectById(invoiceId);
        if (invoice == null) {
            log.warn("Invoice not found with ID: {}", invoiceId);
            // Consider throwing a specific NotFoundException or returning null based on project convention
            return null;
        }

        // The InvoiceMapper.xml selectById might not fetch items if not configured to do so.
        // The selectInvoiceByInvoiceNumber was configured for that.
        // Let's assume selectById only gets the main invoice. We need to fetch items separately.
        // OR, if CommonMapper's selectById is sophisticated enough or if we have a specific method:
        // Invoice invoice = invoiceMapper.selectInvoiceWithItemsById(invoiceId);

        List<InvoiceItem> items = invoiceItemMapper.selectInvoiceItemsByInvoiceId(invoiceId);
        invoice.setItems(items); // Set items to the invoice entity

        return convertToInvoiceResponseDTO(invoice);
    }

    /**
     * 创建发票
     *
     * @param invoiceDto 发票创建请求DTO
     * @return 创建的发票ID
     */
    @Override
    @Transactional
    public Long createInvoice(InvoiceRequestDTO invoiceDto) {
        Invoice invoice = convertToInvoiceEntity(invoiceDto);

        // Calculate totals from items
        calculateInvoiceTotals(invoice, invoiceDto.getItems());

        // Convert total to words
        // Assuming MoneyUtils.toChineseWords(BigDecimal amount) exists from twelvet-framework-utils
        try {
            invoice.setTotalPriceAndTaxInWords(MoneyUtils.toChineseWords(invoice.getTotalPriceAndTaxInFigures()));
        } catch (Exception e) {
            log.error("Error converting money to Chinese words: {}", e.getMessage());
            // Depending on requirements, either throw exception or set a default/error string
            invoice.setTotalPriceAndTaxInWords("转换错误"); // Placeholder for error
        }
        
        // Set audit fields (createBy, createTime) - BaseEntity might handle createTime automatically
        // createBy needs to be set from security context, which is usually handled by framework interceptors
        // or a base service method. For now, assuming it's handled or will be added.

        invoiceMapper.insert(invoice); // Assumes CommonMapper.insert populates the ID

        if (invoice.getId() == null) {
            log.error("Failed to insert invoice, ID is null after insert operation.");
            throw new TWTException("Failed to create invoice, ID not generated.", 500);
        }

        if (CollUtil.isNotEmpty(invoiceDto.getItems())) {
            for (InvoiceItemRequestDTO itemDto : invoiceDto.getItems()) {
                InvoiceItem item = convertToInvoiceItemEntity(itemDto);
                item.setInvoiceId(invoice.getId()); // Link item to the invoice
                
                // Calculate item totals (amountBeforeTax, taxAmount) from quantity, unitPrice, taxRate
                calculateInvoiceItemTotals(item);

                // Set audit fields for item
                invoiceItemMapper.insert(item);
            }
        }
        log.info("Successfully created invoice with ID: {}", invoice.getId());
        return invoice.getId();
    }


    @Override
    public List<InvoiceResponseDTO> listInvoices(InvoiceRequestDTO queryParams) {
        // This is a placeholder. Actual implementation would involve:
        // 1. Building a query wrapper / map for MyBatis based on queryParams.
        //    CommonMapper might have a convention for this (e.g. if queryParams is an Invoice entity itself).
        // 2. Calling invoiceMapper.selectList(preparedQueryParams).
        // 3. For each invoice, potentially fetching its items (N+1 problem if not careful,
        //    or use a specific mapper method that joins invoices and items).
        // 4. Converting to DTOs.
        // 5. PageHelper integration: PageHelper.startPage(page, size); ... List<Invoice> list = invoiceMapper.selectList...
        log.warn("listInvoices is not fully implemented yet.");
        // Example:
        // Invoice queryEntity = BeanUtil.copyProperties(queryParams, Invoice.class);
        // List<Invoice> invoices = invoiceMapper.selectList(queryEntity);
        // return invoices.stream().map(this::convertToInvoiceResponseDTOWithItems).collect(Collectors.toList());
        return new ArrayList<>(); // Placeholder
    }


    @Override
    @Transactional
    public void updateInvoice(Long invoiceId, InvoiceRequestDTO invoiceDto) {
        Invoice existingInvoice = invoiceMapper.selectById(invoiceId);
        if (existingInvoice == null) {
            log.error("Invoice not found for update with ID: {}", invoiceId);
            throw new TWTException("Invoice not found for update", 404);
        }

        // Update fields from DTO
        BeanUtil.copyProperties(invoiceDto, existingInvoice, "id", "createTime", "createBy"); // Keep original ID and audit
        
        // Recalculate totals
        calculateInvoiceTotals(existingInvoice, invoiceDto.getItems());
        try {
            existingInvoice.setTotalPriceAndTaxInWords(MoneyUtils.toChineseWords(existingInvoice.getTotalPriceAndTaxInFigures()));
        } catch (Exception e) {
            log.error("Error converting money to Chinese words during update: {}", e.getMessage());
            existingInvoice.setTotalPriceAndTaxInWords("转换错误");
        }

        // updateBy, updateTime audit fields would be set here or by framework/BaseEntity

        invoiceMapper.updateById(existingInvoice);

        // Handle items: delete old ones and insert new ones (simple approach)
        // Need a method in InvoiceItemMapper: deleteByInvoiceId(Long invoiceId)
        // For now, assuming such a method or using CommonMapper's delete with a query wrapper
        // This part requires InvoiceItemMapper to have a deleteByInvoiceId method or similar
        // For example: invoiceItemMapper.delete(new QueryWrapper<InvoiceItem>().eq("invoice_id", invoiceId));
        // Delete existing items
        invoiceItemMapper.deleteInvoiceItemsByInvoiceId(invoiceId);

        if (CollUtil.isNotEmpty(invoiceDto.getItems())) {
            for (InvoiceItemRequestDTO itemDto : invoiceDto.getItems()) {
                InvoiceItem item = convertToInvoiceItemEntity(itemDto);
                item.setInvoiceId(invoiceId);
                calculateInvoiceItemTotals(item);
                invoiceItemMapper.insert(item);
            }
        }
        log.info("Successfully updated invoice with ID: {}", invoiceId);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long invoiceId) {
        Invoice existingInvoice = invoiceMapper.selectById(invoiceId);
        if (existingInvoice == null) {
            log.warn("Invoice not found for deletion with ID: {}", invoiceId);
            // Optionally throw an exception or just log and return
            return;
        }

        // Delete items first
        // Similar to update, this needs a specific method in InvoiceItemMapper
        // For example: invoiceItemMapper.deleteByInvoiceId(invoiceId);
        // Delete items first
        invoiceItemMapper.deleteInvoiceItemsByInvoiceId(invoiceId);

        invoiceMapper.deleteById(invoiceId);
        log.info("Successfully deleted invoice with ID: {}", invoiceId);
    }

    // --- Helper Methods ---

    private void calculateInvoiceItemTotals(InvoiceItem item) {
        if (item.getQuantity() != null && item.getUnitPrice() != null) {
            // Amount before tax for item = quantity * unitPrice
            item.setAmountBeforeTax(Arith.multiply(item.getQuantity(), item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP));
        }
        if (item.getAmountBeforeTax() != null && item.getTaxRate() != null) {
            // Tax amount for item = amountBeforeTax * taxRate
            item.setTaxAmount(Arith.multiply(item.getAmountBeforeTax(), item.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        }
    }
    
    private void calculateInvoiceTotals(Invoice invoice, List<InvoiceItemRequestDTO> itemDtos) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;

        if (CollUtil.isNotEmpty(itemDtos)) {
            for (InvoiceItemRequestDTO itemDto : itemDtos) {
                // It's better to use values from DTOs for calculation consistency if they are pre-validated
                // or recalculate from basic inputs (quantity, unitPrice, taxRate)
                BigDecimal itemAmountBeforeTax = Arith.multiply(itemDto.getQuantity(), itemDto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal itemTaxAmount = Arith.multiply(itemAmountBeforeTax, itemDto.getTaxRate()).setScale(2, RoundingMode.HALF_UP);
                
                totalAmount = Arith.add(totalAmount, itemAmountBeforeTax);
                totalTax = Arith.add(totalTax, itemTaxAmount);
            }
        }

        invoice.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        invoice.setTotalTax(totalTax.setScale(2, RoundingMode.HALF_UP));
        invoice.setTotalPriceAndTaxInFigures(Arith.add(totalAmount, totalTax).setScale(2, RoundingMode.HALF_UP));
    }

    private Invoice convertToInvoiceEntity(InvoiceRequestDTO dto) {
        Invoice invoice = new Invoice();
        BeanUtil.copyProperties(dto, invoice, "items"); // Exclude items from top-level copy
        // Manual mapping for fields not directly copied if any, or for complex types
        return invoice;
    }

    private InvoiceItem convertToInvoiceItemEntity(InvoiceItemRequestDTO dto) {
        InvoiceItem item = new InvoiceItem();
        BeanUtil.copyProperties(dto, item);
        return item;
    }

    private InvoiceResponseDTO convertToInvoiceResponseDTO(Invoice invoice) {
        if (invoice == null) return null;
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        BeanUtil.copyProperties(invoice, dto, "items");

        if (CollUtil.isNotEmpty(invoice.getItems())) {
            dto.setItems(
                invoice.getItems().stream()
                    .map(this::convertToInvoiceItemResponseDTO)
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }
    
    // Helper to convert and fetch items, useful for list operations
    private InvoiceResponseDTO convertToInvoiceResponseDTOWithItems(Invoice invoice) {
        if (invoice == null) return null;
        List<InvoiceItem> items = invoiceItemMapper.selectInvoiceItemsByInvoiceId(invoice.getId());
        invoice.setItems(items);
        return convertToInvoiceResponseDTO(invoice);
    }


    private InvoiceItemResponseDTO convertToInvoiceItemResponseDTO(InvoiceItem item) {
        if (item == null) return null;
        InvoiceItemResponseDTO itemDto = new InvoiceItemResponseDTO();
        BeanUtil.copyProperties(item, itemDto);
        return itemDto;
    }
}
