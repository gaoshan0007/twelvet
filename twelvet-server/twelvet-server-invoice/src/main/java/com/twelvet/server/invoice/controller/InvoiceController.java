package com.twelvet.server.invoice.controller;

import com.twelvet.framework.core.application.controller.TWTController;
import com.twelvet.framework.core.application.domain.AjaxResult;
import com.twelvet.framework.core.application.page.TableDataInfo;
import com.twelvet.api.invoice.dto.InvoiceRequestDTO; // Updated import
import com.twelvet.api.invoice.dto.InvoiceResponseDTO; // Updated import
import com.twelvet.server.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发票管理 Controller
 * Handles REST API requests for Invoices.
 *
 * @author TwelveT
 */
@Tag(name = "Invoice Management", description = "Controller for managing invoices")
@RestController
@RequestMapping("/invoices") // Using a simple, non-versioned path for now
public class InvoiceController extends TWTController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Get Invoice by ID
     * @param invoiceId The ID of the invoice to retrieve
     * @return AjaxResult containing the invoice details
     */
    @Operation(summary = "Get Invoice by ID", description = "Retrieves a specific invoice by its ID.")
    @GetMapping("/{invoiceId}")
    @PreAuthorize("@customSs.hasPermi('invoice:invoice:query')")
    public AjaxResult getInvoice(
            @Parameter(description = "ID of the invoice to be obtained", required = true)
            @PathVariable("invoiceId") Long invoiceId) {
        InvoiceResponseDTO invoice = invoiceService.getInvoiceById(invoiceId);
        return AjaxResult.success(invoice);
    }

    /**
     * List Invoices with optional filtering and pagination
     * @param queryParams DTO containing query parameters
     * @return TableDataInfo containing the paginated list of invoices
     */
    @Operation(summary = "List Invoices", description = "Retrieves a list of invoices, supports pagination and filtering.")
    @GetMapping
    @PreAuthorize("@customSs.hasPermi('invoice:invoice:list')")
    public TableDataInfo listInvoices(
            @Parameter(description = "Query parameters for filtering and pagination")
            InvoiceRequestDTO queryParams) { // InvoiceRequestDTO might need PageDomain fields or use @ModelAttribute
        startPage(); // Initializes pagination from request parameters (pageNum, pageSize)
        List<InvoiceResponseDTO> list = invoiceService.listInvoices(queryParams);
        return getDataTable(list); // getDataTable handles wrapping list in TableDataInfo with total count
    }

    /**
     * Create a new Invoice
     * @param invoiceDto DTO containing the details of the invoice to create
     * @return AjaxResult containing the ID of the created invoice
     */
    @Operation(summary = "Create Invoice", description = "Creates a new invoice.")
    @PostMapping
    @PreAuthorize("@customSs.hasPermi('invoice:invoice:add')")
    public AjaxResult createInvoice(
            @Parameter(description = "Invoice data to create", required = true)
            @Validated @RequestBody InvoiceRequestDTO invoiceDto) {
        Long createdInvoiceId = invoiceService.createInvoice(invoiceDto);
        return AjaxResult.success("Invoice created successfully", createdInvoiceId);
    }

    /**
     * Update an existing Invoice
     * @param invoiceId The ID of the invoice to update
     * @param invoiceDto DTO containing the updated details
     * @return AjaxResult indicating success or failure
     */
    @Operation(summary = "Update Invoice", description = "Updates an existing invoice by its ID.")
    @PutMapping("/{invoiceId}")
    @PreAuthorize("@customSs.hasPermi('invoice:invoice:edit')")
    public AjaxResult updateInvoice(
            @Parameter(description = "ID of the invoice to be updated", required = true)
            @PathVariable("invoiceId") Long invoiceId,
            @Parameter(description = "Updated invoice data", required = true)
            @Validated @RequestBody InvoiceRequestDTO invoiceDto) {
        invoiceService.updateInvoice(invoiceId, invoiceDto);
        return AjaxResult.success("Invoice updated successfully");
    }

    /**
     * Delete an Invoice
     * @param invoiceId The ID of the invoice to delete
     * @return AjaxResult indicating success or failure
     */
    @Operation(summary = "Delete Invoice", description = "Deletes an invoice by its ID.")
    @DeleteMapping("/{invoiceId}")
    @PreAuthorize("@customSs.hasPermi('invoice:invoice:remove')")
    public AjaxResult deleteInvoice(
            @Parameter(description = "ID of the invoice to be deleted", required = true)
            @PathVariable("invoiceId") Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return AjaxResult.success("Invoice deleted successfully");
    }

}
