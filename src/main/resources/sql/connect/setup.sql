-- 面试题数据库：涵盖连接查询、聚合、索引、事务、锁等练习
DROP DATABASE IF EXISTS interview_study;
CREATE DATABASE interview_study CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE interview_study;

-- 一、部门基础信息表
DROP TABLE IF EXISTS department;
CREATE TABLE department (
    dept_id      INT PRIMARY KEY AUTO_INCREMENT,
    dept_name    VARCHAR(50) NOT NULL UNIQUE COMMENT '部门名称',
    location     VARCHAR(50) NOT NULL COMMENT '办公地点',
    cost_center  CHAR(4)     NOT NULL COMMENT '成本中心编码',
    grade        ENUM('A','B','C') DEFAULT 'B' COMMENT '级别'
) COMMENT='部门主数据';

INSERT INTO department (dept_name, location, cost_center, grade) VALUES
('技术部', '深圳', 'R001', 'A'),
('产品部', '上海', 'R002', 'A'),
('销售部', '北京', 'S101', 'B'),
('支持部', '武汉', 'S102', 'C');

-- 二、员工表：包含自连接字段 manager_id
DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
    emp_id       INT PRIMARY KEY AUTO_INCREMENT,
    emp_name     VARCHAR(50) NOT NULL COMMENT '员工姓名',
    gender       ENUM('M','F') NOT NULL,
    hire_date    DATE NOT NULL,
    dept_id      INT NOT NULL,
    manager_id   INT NULL,
    salary       DECIMAL(10,2) NOT NULL,
    status       ENUM('active','probation','left') DEFAULT 'active',
    INDEX idx_employee_dept_salary (dept_id, salary DESC),
    CONSTRAINT fk_employee_dept FOREIGN KEY (dept_id) REFERENCES department(dept_id),
    CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES employee(emp_id)
) COMMENT='员工信息';

INSERT INTO employee (emp_name, gender, hire_date, dept_id, manager_id, salary, status) VALUES
('张三', 'F', '2018-03-12', 1, NULL, 28000, 'active'),
('李四', 'M', '2019-05-08', 1, 1,    21000, 'active'),
('王五', 'F', '2020-11-01', 2, 1,    19000, 'active'),
('赵六', 'M', '2017-09-20', 3, NULL, 25000, 'active'),
('孙七', 'F', '2022-01-15', 3, 4,    15000, 'probation'),
('周八', 'M', '2021-07-03', 4, NULL, 16000, 'active'),
('吴九', 'F', '2016-02-26', 2, 1,    23000, 'active'),
('郑十', 'M', '2015-12-12', 4, 6,    14000, 'left');

-- 三、项目及员工项目关联表（多对多）
DROP TABLE IF EXISTS project;
CREATE TABLE project (
    project_id   INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(50) NOT NULL,
    dept_id      INT NOT NULL,
    start_date   DATE NOT NULL,
    end_date     DATE NULL,
    budget       DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_project_dept FOREIGN KEY (dept_id) REFERENCES department(dept_id)
) COMMENT='项目主表';

INSERT INTO project (project_name, dept_id, start_date, end_date, budget) VALUES
('阿波罗项目', 1, '2022-02-01', NULL, 1200000),
('赫尔墨斯项目', 2, '2021-04-15', '2023-12-31', 800000),
('水星项目', 3, '2023-01-10', NULL, 500000),
('阿特拉斯项目', 4, '2020-07-01', '2022-06-30', 300000);

DROP TABLE IF EXISTS employee_project;
CREATE TABLE employee_project (
    emp_id       INT NOT NULL,
    project_id   INT NOT NULL,
    weekly_hours TINYINT NOT NULL,
    PRIMARY KEY (emp_id, project_id),
    CONSTRAINT fk_emp_proj_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id),
    CONSTRAINT fk_emp_proj_project FOREIGN KEY (project_id) REFERENCES project(project_id)
) COMMENT='员工项目桥接表';

INSERT INTO employee_project (emp_id, project_id, weekly_hours) VALUES
(1,1,30), (2,1,35), (3,2,25), (7,2,30), (4,3,20), (5,3,15), (6,4,10), (8,4,20);

-- 四、客户-订单-商品-支付体系，用于多表连接与聚合
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    customer_id   INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(50) NOT NULL,
    tier          ENUM('银牌','金牌','白金') DEFAULT '银牌',
    region        VARCHAR(30) NOT NULL,
    credit_limit  DECIMAL(10,2) NOT NULL
) COMMENT='客户信息';

INSERT INTO customer (customer_name, tier, region, credit_limit) VALUES
('卓越公司',     '白金', '华北', 500000),
('贝塔零售',   '金牌',     '华东',  200000),
('宇宙健康',  '金牌',     '华南', 150000),
('德尔塔数字', '银牌',   '华西',  80000),
('常青公司',     '银牌',   '华北', 60000);

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    product_id   INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(50) NOT NULL,
    category     VARCHAR(30) NOT NULL,
    unit_price   DECIMAL(10,2) NOT NULL,
    inventory_qty INT NOT NULL,
    INDEX idx_product_category (category)
) COMMENT='商品信息';

INSERT INTO product (product_name, category, unit_price, inventory_qty) VALUES
('边缘路由器',  '网络设备', 8000,  50),
('核心交换机',  '网络设备', 12000, 30),
('AI模块',    '计算设备', 15000, 20),
('存储节点', '存储设备', 10000, 40),
('支持服务包', '服务', 2000,  200);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id     INT PRIMARY KEY AUTO_INCREMENT,
    customer_id  INT NOT NULL,
    order_date   DATE NOT NULL,
    status       ENUM('草稿','已确认','已发货','已付款','已取消') DEFAULT '草稿',
    total_amount DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
) COMMENT='订单主表';
CREATE INDEX idx_orders_order_date ON orders(order_date);

INSERT INTO orders (customer_id, order_date, status, total_amount) VALUES
(1, '2023-11-10', '已付款',       160000),
(2, '2023-12-05', '已发货',    90000),
(3, '2024-01-18', '已确认',  75000),
(4, '2024-02-02', '已取消',  30000),
(5, '2024-02-15', '已付款',       40000),
(1, '2024-03-01', '已发货',   110000);

DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id      INT NOT NULL,
    product_id    INT NOT NULL,
    quantity      INT NOT NULL,
    unit_price    DECIMAL(10,2) NOT NULL,
    discount_rate DECIMAL(4,2) DEFAULT 0.00,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(product_id)
) COMMENT='订单明细';
CREATE INDEX idx_order_item_order_product ON order_item(order_id, product_id);

INSERT INTO order_item (order_id, product_id, quantity, unit_price, discount_rate) VALUES
(1,1,5,8000,0.05),
(1,5,10,2000,0.10),
(2,2,3,12000,0.00),
(2,5,5,2000,0.00),
(3,3,2,15000,0.00),
(3,5,8,2000,0.05),
(4,4,2,10000,0.00),
(5,1,2,8000,0.00),
(5,5,4,2000,0.00),
(6,2,4,12000,0.05);

DROP TABLE IF EXISTS payment;
CREATE TABLE payment (
    payment_id    INT PRIMARY KEY AUTO_INCREMENT,
    order_id      INT NOT NULL,
    payment_method ENUM('银行转账','银行卡','现金','信用额度') NOT NULL,
    paid_amount   DECIMAL(12,2) NOT NULL,
    paid_date     DATE NOT NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(order_id)
) COMMENT='收款记录';

INSERT INTO payment (order_id, payment_method, paid_amount, paid_date) VALUES
(1, '银行转账',   150000, '2023-11-15'),
(1, '信用额度', 10000,  '2023-11-28'),
(2, '银行卡',   50000,  '2023-12-20'),
(5, '现金',   40000,  '2024-02-20'),
(6, '银行转账',   90000,  '2024-03-10'),
(6, '信用额度', 20000,  '2024-03-18');

-- 五、库存流水表，可用于事务/隔离级别演示
DROP TABLE IF EXISTS inventory_movement;
CREATE TABLE inventory_movement (
    movement_id   INT PRIMARY KEY AUTO_INCREMENT,
    product_id    INT NOT NULL,
    movement_type ENUM('入库','出库','调整') NOT NULL,
    qty           INT NOT NULL,
    reference     VARCHAR(50) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movement_product FOREIGN KEY (product_id) REFERENCES product(product_id)
) COMMENT='库存出入库流水';

INSERT INTO inventory_movement (product_id, movement_type, qty, reference) VALUES
(1,'入库',   20,'采购单-20231101'),
(1,'出库',   5,'订单-1'),
(2,'入库',   10,'采购单-20231102'),
(2,'出库',   3,'订单-2'),
(3,'出库',   2,'订单-3'),
(5,'调整',-2,'盘点-001');

-- 六、登录审计表：索引示例
DROP TABLE IF EXISTS login_audit;
CREATE TABLE login_audit (
    audit_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    emp_id     INT NOT NULL,
    login_time DATETIME NOT NULL,
    device     VARCHAR(40) NOT NULL,
    success    TINYINT(1) NOT NULL DEFAULT 1,
    INDEX idx_login_emp_time (emp_id, login_time DESC),
    CONSTRAINT fk_login_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
) COMMENT='登录审计';

INSERT INTO login_audit (emp_id, login_time, device, success) VALUES
(1,'2024-03-20 09:00:11','MacBook','1'),
(2,'2024-03-20 09:05:04','MacBook','1'),
(3,'2024-03-20 09:06:20','iPad','1'),
(4,'2024-03-20 08:55:44','Windows','0'),
(5,'2024-03-21 10:10:00','MacBook','1'),
(6,'2024-03-21 10:15:33','Windows','1');

-- 七、用于快速演示的视图
DROP VIEW IF EXISTS v_employee_comp;
CREATE VIEW v_employee_comp AS
SELECT e.emp_id,
       e.emp_name,
       d.dept_name,
       e.salary,
       IFNULL(m.emp_name, '无上级') AS manager_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.dept_id
LEFT JOIN employee m   ON e.manager_id = m.emp_id;

DROP VIEW IF EXISTS v_order_summary;
CREATE VIEW v_order_summary AS
SELECT o.order_id,
       c.customer_name,
       o.order_date,
       o.status,
       SUM(oi.quantity * oi.unit_price * (1 - oi.discount_rate)) AS calculated_amount,
       o.total_amount
FROM orders o
JOIN customer c ON o.customer_id = c.customer_id
LEFT JOIN order_item oi ON o.order_id = oi.order_id
GROUP BY o.order_id, c.customer_name, o.order_date, o.status, o.total_amount;
