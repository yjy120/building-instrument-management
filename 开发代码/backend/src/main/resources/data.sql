INSERT INTO sys_user (username, password, real_name, role, department, status, create_time, update_time) VALUES 
('admin', '$2a$10$0zJ8dmGCqFwfykJtCQJ24uOk.HkZf/Y3rFmWdJYqzpAKa4EByT2Ne', '管理员', 'ADMIN', '管理部', 1, NOW(), NOW()),
('manager', '$2a$10$0zJ8dmGCqFwfykJtCQJ24uOk.HkZf/Y3rFmWdJYqzpAKa4EByT2Ne', '张经理', 'MANAGER', '检测部', 1, NOW(), NOW()),
('tester1', '$2a$10$0zJ8dmGCqFwfykJtCQJ24uOk.HkZf/Y3rFmWdJYqzpAKa4EByT2Ne', '李检测员', 'TESTER', '检测部', 1, NOW(), NOW()),
('tester2', '$2a$10$0zJ8dmGCqFwfykJtCQJ24uOk.HkZf/Y3rFmWdJYqzpAKa4EByT2Ne', '王检测员', 'TESTER', '检测部', 1, NOW(), NOW());

INSERT INTO device (device_code, device_name, specification, supplier, purchase_price, manufacturer, status, location, category, create_time, update_time) VALUES 
('DEV001', '万能试验机', 'WDW-100', '上海材料试验机厂', 120000.00, '上海材料试验机厂', 'IN_USE', 'A区-101', '力学检测', NOW(), NOW()),
('DEV002', '压力试验机', 'YES-2000', '济南试验机厂', 85000.00, '济南试验机厂', 'IN_USE', 'A区-102', '力学检测', NOW(), NOW()),
('DEV003', '电子天平', 'FA2004', '上海精密科学仪器', 5000.00, '上海精密科学仪器', 'IN_USE', 'B区-201', '计量检测', NOW(), NOW()),
('DEV004', '回弹仪', 'HT-225', '山东乐陵', 800.00, '山东乐陵', 'IN_USE', 'B区-202', '无损检测', NOW(), NOW()),
('DEV005', '超声波探伤仪', 'CTS-22', '汕头超声', 35000.00, '汕头超声', 'REPAIRING', '维修区', '无损检测', NOW(), NOW()),
('DEV006', '混凝土搅拌机', 'HJW-60', '郑州建工', 12000.00, '郑州建工', 'IN_USE', 'C区-301', '建材检测', NOW(), NOW()),
('DEV007', '坍落度仪', '标准', '河北北方', 300.00, '河北北方', 'IN_USE', 'C区-302', '建材检测', NOW(), NOW()),
('DEV008', '钢筋扫描仪', 'ZBL-R620', '北京智博联', 28000.00, '北京智博联', 'IN_USE', 'B区-203', '无损检测', NOW(), NOW());

INSERT INTO stock (device_id, quantity, threshold, warehouse_location) VALUES 
(1, 2, 1, 'A区-101'),
(2, 1, 1, 'A区-102'),
(3, 5, 2, 'B区-201'),
(4, 8, 3, 'B区-202'),
(5, 1, 1, '维修区'),
(6, 2, 1, 'C区-301'),
(7, 10, 5, 'C区-302'),
(8, 1, 1, 'B区-203');

INSERT INTO use_record (device_id, user_id, project_name, borrow_time, status, create_time) VALUES 
(1, 3, '某小区住宅楼项目', DATE_SUB(NOW(), INTERVAL 5 DAY), 'BORROWED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, 4, '某桥梁检测项目', DATE_SUB(NOW(), INTERVAL 2 DAY), 'BORROWED', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 3, '某实验室校准项目', DATE_SUB(NOW(), INTERVAL 7 DAY), 'RETURNED', DATE_SUB(NOW(), INTERVAL 7 DAY));

INSERT INTO calibration_record (device_id, calibration_date, next_calibration_date, calibration_organization, result, create_time) VALUES 
(1, DATE_SUB(NOW(), INTERVAL 180 DAY), DATE_ADD(NOW(), INTERVAL 180 DAY), '山东省计量科学研究院', 'PASS', DATE_SUB(NOW(), INTERVAL 180 DAY)),
(2, DATE_SUB(NOW(), INTERVAL 90 DAY), DATE_ADD(NOW(), INTERVAL 270 DAY), '山东省计量科学研究院', 'PASS', DATE_SUB(NOW(), INTERVAL 90 DAY)),
(3, DATE_SUB(NOW(), INTERVAL 330 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), '济南市计量检定所', 'PASS', DATE_SUB(NOW(), INTERVAL 330 DAY));

INSERT INTO repair_order (device_id, applicant_id, fault_description, status, create_time, update_time) VALUES 
(5, 3, '超声波探头故障，无法正常检测', 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

INSERT INTO purchase_apply (device_name, quantity, project_usage, applicant_id, status, create_time, update_time) VALUES 
('激光测距仪', 2, '某大型建筑项目测量需求', 3, 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('便携式硬度计', 1, '现场金属硬度检测', 4, 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));