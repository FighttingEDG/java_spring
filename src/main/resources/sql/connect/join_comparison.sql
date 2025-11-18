USE interview_study;

-- ============================================
-- 左连接 vs 右连接 对比说明
-- ============================================

-- 【核心区别】
-- LEFT JOIN（左连接）：以左表为基准，左表数据全部显示，右表匹配不上显示 NULL
-- RIGHT JOIN（右连接）：以右表为基准，右表数据全部显示，左表匹配不上显示 NULL

-- ============================================
-- 示例1：员工-部门连接
-- ============================================

-- 【场景1】左连接：查询所有员工及其部门（即使员工没有部门也要显示）
-- 业务需求：统计所有员工，看看哪些员工还没有分配部门
SELECT '=== 左连接示例：所有员工及其部门 ===' AS description;
SELECT e.emp_id,
       e.emp_name,
       d.dept_name,
       CASE WHEN d.dept_name IS NULL THEN '未分配部门' ELSE '已分配' END AS status
FROM employee AS e
LEFT JOIN department AS d ON e.dept_id = d.dept_id
ORDER BY e.emp_id;
