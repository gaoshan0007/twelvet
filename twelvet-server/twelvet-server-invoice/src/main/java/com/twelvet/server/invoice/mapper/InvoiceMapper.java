package com.twelvet.server.invoice.mapper;

import com.twelvet.framework.jdbc.mapper.CommonMapper;
import com.twelvet.server.invoice.domain.Invoice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票数据层
 *
 * @author TwelveT
 */
@Mapper
public interface InvoiceMapper extends CommonMapper<Invoice> {

    /**
     * 根据发票号码查询发票信息（包含发票项目）
     *
     * @param invoiceNumber 发票号码
     * @return 发票信息
     */
    Invoice selectInvoiceByInvoiceNumber(String invoiceNumber);

    // CommonMapper should provide basic CRUD like:
    // insert(Invoice invoice);
    // updateById(Invoice invoice);
    // deleteById(Long id);
    // selectById(Long id);
    // selectList(Invoice invoice); // For querying with conditions
}
