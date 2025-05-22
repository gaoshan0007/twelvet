package com.twelvet.server.invoice.mapper;

import com.twelvet.framework.jdbc.mapper.CommonMapper;
import com.twelvet.server.invoice.domain.InvoiceItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发票项目数据层
 *
 * @author TwelveT
 */
@Mapper
public interface InvoiceItemMapper extends CommonMapper<InvoiceItem> {

    /**
     * 根据发票ID查询发票项目列表
     *
     * @param invoiceId 发票ID
     * @return 发票项目列表
     */
    List<InvoiceItem> selectInvoiceItemsByInvoiceId(Long invoiceId);

    /**
     * 根据发票ID删除发票项目
     *
     * @param invoiceId 发票ID
     * @return 影响行数
     */
    int deleteInvoiceItemsByInvoiceId(Long invoiceId);

    // CommonMapper should provide basic CRUD like:
    // insert(InvoiceItem item);
    // updateById(InvoiceItem item);
    // deleteById(Long id);
    // selectById(Long id);
    // selectList(InvoiceItem item); // For querying with conditions
}
