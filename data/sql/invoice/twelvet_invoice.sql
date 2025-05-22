-- Ensure the database exists, though in a real deployment, this might be handled separately.
-- CREATE DATABASE IF NOT EXISTS `twelvet` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
-- USE `twelvet`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invoice
-- ----------------------------
DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Invoice ID (发票ID)',
  `invoice_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Invoice Code (发票代码)',
  `invoice_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Invoice Number (发票号码)',
  `invoice_date` date NOT NULL COMMENT 'Invoice Date (开票日期)',
  `check_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Check Code (校验码)',
  `machine_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Machine Number (机器编号)',
  `purchaser_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Purchaser Name (购买方名称)',
  `purchaser_tin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Purchaser TIN (购买方纳税人识别号)',
  `purchaser_address_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Purchaser Address and Phone (购买方地址、电话)',
  `purchaser_bank_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Purchaser Bank and Account (购买方开户行及账号)',
  `seller_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Seller Name (销售方名称)',
  `seller_tin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Seller TIN (销售方纳税人识别号)',
  `seller_address_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Seller Address and Phone (销售方地址、电话)',
  `seller_bank_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Seller Bank and Account (销售方开户行及账号)',
  `total_amount` decimal(20,2) NOT NULL COMMENT 'Total Amount before tax (合计金额)',
  `total_tax` decimal(20,2) NOT NULL COMMENT 'Total Tax (合计税额)',
  `total_price_and_tax_in_words` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Total Price and Tax in Words (价税合计大写)',
  `total_price_and_tax_in_figures` decimal(20,2) NOT NULL COMMENT 'Total Price and Tax in Figures (价税合计小写)',
  `payee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Payee (收款人)',
  `reviewer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Reviewer (复核人)',
  `issuer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Issuer (开票人)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_invoice_code_number` (`invoice_code`, `invoice_number`) COMMENT 'Unique combination of Invoice Code and Number'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Invoice Table (发票表)' ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for invoice_item
-- ----------------------------
DROP TABLE IF EXISTS `invoice_item`;
CREATE TABLE `invoice_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Invoice Item ID (发票项目ID)',
  `invoice_id` bigint(20) NOT NULL COMMENT 'Associated Invoice ID (关联发票ID)',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Goods or Taxable Services Name (货物或应税劳务、服务名称)',
  `specification_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Specification/Model (规格型号)',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Unit (单位)',
  `quantity` decimal(24,6) NOT NULL COMMENT 'Quantity (数量)',
  `unit_price` decimal(24,6) NOT NULL COMMENT 'Unit Price (单价)',
  `amount_before_tax` decimal(20,2) NOT NULL COMMENT 'Amount before tax (金额)',
  `tax_rate` decimal(7,4) NOT NULL COMMENT 'Tax Rate (税率)',
  `tax_amount` decimal(20,2) NOT NULL COMMENT 'Tax Amount (税额)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_invoice_id` (`invoice_id`) USING BTREE,
  CONSTRAINT `fk_invoice_item_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Invoice Item Table (发票项目表)' ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
