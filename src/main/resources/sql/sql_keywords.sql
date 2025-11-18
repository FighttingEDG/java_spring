USE interview_study;

-- ============================================
-- SQL 关键字（必会）
-- ============================================

-- ============================================
-- 1. LIMIT：分页
-- ============================================

-- 【说明】LIMIT 用于限制查询结果的数量，实现分页功能
-- 语法：LIMIT offset, count
-- offset：从第几条开始（从0开始计数）
-- count：显示多少条

-- 【示例】查询员工表，从第3条开始显示，显示6条
SELECT '=== LIMIT示例：分页查询员工 ===' AS description;
SELECT * FROM employee LIMIT 2, 6;

-- ============================================
-- 2. GROUP BY：分组
-- ============================================

-- 【说明】GROUP BY 用于将数据按照某个字段进行分组
-- 通常与聚合函数一起使用

-- 【示例】按部门分组，统计每个部门的员工数量
SELECT '=== GROUP BY示例：按部门统计员工数量 ===' AS description;
SELECT d.dept_name AS 部门名称, COUNT(e.emp_id) AS 员工数量
FROM department d
LEFT JOIN employee e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;

-- ============================================
-- 3. DISTINCT：去重
-- ============================================

-- 【说明】DISTINCT 用于去除查询结果中的重复记录

-- 【示例】查询所有不重复的员工姓名
SELECT '=== DISTINCT示例：查询不重复的员工姓名 ===' AS description;
SELECT DISTINCT emp_name FROM employee;

