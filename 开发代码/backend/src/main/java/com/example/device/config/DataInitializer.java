package com.example.device.config;

import com.example.device.entity.Device;
import com.example.device.entity.Stock;
import com.example.device.repository.DeviceRepository;
import com.example.device.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DeviceRepository deviceRepository;
    private final StockRepository stockRepository;

    public DataInitializer(DeviceRepository deviceRepository, StockRepository stockRepository) {
        this.deviceRepository = deviceRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (deviceRepository.count() > 0) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // ==================== 一、岩土勘察类 ====================
        createDevice("JT001", "静力触探车", "20吨", "北京地质仪器厂", new BigDecimal("120000.00"), "北京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "LARGE", sdf.parse("2025-01-15"));
        createDevice("JT002", "地质钻机", "XY-100型", "上海探矿机械厂", new BigDecimal("85000.00"), "上海探矿机械厂", "IN_USE", "岩土实验室", "岩土勘察类", "LARGE", sdf.parse("2025-01-20"));
        createDevice("JT003", "标准贯入设备", "63.5kg", "南京地质仪器厂", new BigDecimal("25000.00"), "南京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "LARGE", sdf.parse("2025-02-01"));
        createDevice("JT004", "十字板剪切试验机", "电测式", "广州地质仪器厂", new BigDecimal("18000.00"), "广州地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "LARGE", sdf.parse("2025-02-10"));
        createDevice("JT005", "车载式核子密度仪", "MC-3型", "美国CPN公司", new BigDecimal("350000.00"), "美国CPN公司", "IN_USE", "岩土实验室", "岩土勘察类", "LARGE", sdf.parse("2025-03-01"));
        createDevice("JT006", "环刀", "100cm³", "北京地质仪器厂", new BigDecimal("150.00"), "北京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-03-05"));
        createDevice("JT007", "取土器", "薄壁式", "上海探矿机械厂", new BigDecimal("800.00"), "上海探矿机械厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-03-10"));
        createDevice("JT008", "液塑限测定仪", "光电式", "南京地质仪器厂", new BigDecimal("1200.00"), "南京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-03-15"));
        createDevice("JT009", "比重瓶", "100ml", "广州地质仪器厂", new BigDecimal("200.00"), "广州地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-03-20"));
        createDevice("JT010", "击实仪", "重型", "北京地质仪器厂", new BigDecimal("3500.00"), "北京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-03-25"));
        createDevice("JT011", "灌砂筒", "标准型", "上海探矿机械厂", new BigDecimal("600.00"), "上海探矿机械厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-04-01"));
        createDevice("JT012", "土壤含水率铝盒", "50ml", "南京地质仪器厂", new BigDecimal("80.00"), "南京地质仪器厂", "IN_USE", "岩土实验室", "岩土勘察类", "SMALL", sdf.parse("2025-04-05"));

        // ==================== 二、房建工程专用检测仪器 ====================
        createDevice("FJ001", "万能材料试验机", "600kN", "济南试验机厂", new BigDecimal("150000.00"), "济南试验机厂", "IN_USE", "材料实验室", "房建工程专用", "LARGE", sdf.parse("2025-01-10"));
        createDevice("FJ002", "混凝土压力试验机", "2000kN", "无锡建仪", new BigDecimal("85000.00"), "无锡建仪", "IN_USE", "材料实验室", "房建工程专用", "LARGE", sdf.parse("2025-01-15"));
        createDevice("FJ003", "水泥抗折抗压一体机", "300kN", "上海中久", new BigDecimal("35000.00"), "上海中久", "IN_USE", "材料实验室", "房建工程专用", "LARGE", sdf.parse("2025-02-01"));
        createDevice("FJ004", "门窗三性检测设备", "全自动", "北京建工", new BigDecimal("120000.00"), "北京建工", "IN_USE", "门窗实验室", "房建工程专用", "LARGE", sdf.parse("2025-02-10"));
        createDevice("FJ005", "墙体导热系数检测仪", "JTRG-III", "沈阳天平", new BigDecimal("45000.00"), "沈阳天平", "IN_USE", "节能实验室", "房建工程专用", "LARGE", sdf.parse("2025-02-20"));
        createDevice("FJ006", "混凝土回弹仪", "HT225", "乐清凯达", new BigDecimal("800.00"), "乐清凯达", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-01"));
        createDevice("FJ007", "钢筋扫描仪", "R510", "北京智博联", new BigDecimal("1500.00"), "北京智博联", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-05"));
        createDevice("FJ008", "钢筋锈蚀仪", "GECOR8", "英国易高", new BigDecimal("12000.00"), "英国易高", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-10"));
        createDevice("FJ009", "裂缝测宽仪", "HC-CK101", "北京海创高科", new BigDecimal("2500.00"), "北京海创高科", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-15"));
        createDevice("FJ010", "砂浆回弹仪", "HT20", "乐清凯达", new BigDecimal("700.00"), "乐清凯达", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-20"));
        createDevice("FJ011", "坍落度筒", "标准型", "上虞道墟", new BigDecimal("150.00"), "上虞道墟", "IN_USE", "现场检测", "房建工程专用", "SMALL", sdf.parse("2025-03-25"));
        createDevice("FJ012", "防水卷材检测仪", "ZPY-300", "沧州华瑞", new BigDecimal("4200.00"), "沧州华瑞", "IN_USE", "防水实验室", "房建工程专用", "SMALL", sdf.parse("2025-04-01"));
        createDevice("FJ013", "瓷砖粘结检测仪", "BY-C", "北京北工", new BigDecimal("1800.00"), "北京北工", "IN_USE", "建材实验室", "房建工程专用", "SMALL", sdf.parse("2025-04-05"));

        // ==================== 三、桥梁工程专用检测仪器 ====================
        createDevice("QL001", "桥梁静载试验加载系统", "2000kN", "武汉岩海", new BigDecimal("250000.00"), "武汉岩海", "IN_USE", "桥梁实验室", "桥梁工程专用", "LARGE", sdf.parse("2025-01-05"));
        createDevice("QL002", "桩基声波透射检测仪", "RS-ST01C", "武汉岩海", new BigDecimal("55000.00"), "武汉岩海", "IN_USE", "桩基检测", "桥梁工程专用", "LARGE", sdf.parse("2025-01-10"));
        createDevice("QL003", "桥梁动载测试仪", "INV3060", "北京东方所", new BigDecimal("68000.00"), "北京东方所", "IN_USE", "桥梁实验室", "桥梁工程专用", "LARGE", sdf.parse("2025-01-20"));
        createDevice("QL004", "大型路面弯沉仪", "落锤式", "美国HUMBOLDT", new BigDecimal("280000.00"), "美国HUMBOLDT", "IN_USE", "道路检测", "桥梁工程专用", "LARGE", sdf.parse("2025-02-01"));
        createDevice("QL005", "桥梁取芯钻机", "HZ-200", "无锡钻探工具厂", new BigDecimal("35000.00"), "无锡钻探工具厂", "IN_USE", "现场检测", "桥梁工程专用", "LARGE", sdf.parse("2025-02-10"));
        createDevice("QL006", "桥梁裂缝深度检测仪", "HC-CK101", "北京海创高科", new BigDecimal("3500.00"), "北京海创高科", "IN_USE", "现场检测", "桥梁工程专用", "SMALL", sdf.parse("2025-03-01"));
        createDevice("QL007", "测振仪", "INV9200", "北京东方所", new BigDecimal("4500.00"), "北京东方所", "IN_USE", "桥梁实验室", "桥梁工程专用", "SMALL", sdf.parse("2025-03-05"));
        createDevice("QL008", "碳化深度尺", "0-8mm", "上虞道墟", new BigDecimal("120.00"), "上虞道墟", "IN_USE", "现场检测", "桥梁工程专用", "SMALL", sdf.parse("2025-03-10"));
        createDevice("QL009", "高强回弹仪", "HT1000", "乐清凯达", new BigDecimal("1200.00"), "乐清凯达", "IN_USE", "现场检测", "桥梁工程专用", "SMALL", sdf.parse("2025-03-15"));
        createDevice("QL010", "钢筋保护层探测仪", "ZBL-R660A", "北京智博联", new BigDecimal("1800.00"), "北京智博联", "IN_USE", "现场检测", "桥梁工程专用", "SMALL", sdf.parse("2025-03-20"));
        createDevice("QL011", "桥面平整度仪", "ZBL-P3", "北京智博联", new BigDecimal("3200.00"), "北京智博联", "IN_USE", "道路检测", "桥梁工程专用", "SMALL", sdf.parse("2025-03-25"));
        createDevice("QL012", "支座摩擦检测仪", "ZBL-Z", "北京智博联", new BigDecimal("2800.00"), "北京智博联", "IN_USE", "桥梁实验室", "桥梁工程专用", "SMALL", sdf.parse("2025-04-01"));

        // ==================== 四、公路市政道路检测仪器 ====================
        createDevice("GL001", "连续式路面平整度仪", "ZBL-CG", "北京智博联", new BigDecimal("95000.00"), "北京智博联", "IN_USE", "道路检测", "公路市政道路检测", "LARGE", sdf.parse("2025-01-10"));
        createDevice("GL002", "沥青混合料马歇尔稳定度仪", "LWD-3", "北京智博联", new BigDecimal("18000.00"), "北京智博联", "IN_USE", "沥青实验室", "公路市政道路检测", "LARGE", sdf.parse("2025-01-15"));
        createDevice("GL003", "路面摩擦系数测试车", "BM-III", "美国HUMBOLDT", new BigDecimal("320000.00"), "美国HUMBOLDT", "IN_USE", "道路检测", "公路市政道路检测", "LARGE", sdf.parse("2025-02-01"));
        createDevice("GL004", "大型压实度检测车", "CPR-2000", "美国TROXLER", new BigDecimal("280000.00"), "美国TROXLER", "IN_USE", "道路检测", "公路市政道路检测", "LARGE", sdf.parse("2025-02-10"));
        createDevice("GL005", "摆式摩擦仪", "BM-II", "北京智博联", new BigDecimal("800.00"), "北京智博联", "IN_USE", "道路检测", "公路市政道路检测", "SMALL", sdf.parse("2025-03-01"));
        createDevice("GL006", "路面构造深度仪", "ZBL-C100", "北京智博联", new BigDecimal("1500.00"), "北京智博联", "IN_USE", "道路检测", "公路市政道路检测", "SMALL", sdf.parse("2025-03-05"));
        createDevice("GL007", "灌砂筒", "Φ150mm", "北京智博联", new BigDecimal("400.00"), "北京智博联", "IN_USE", "道路检测", "公路市政道路检测", "SMALL", sdf.parse("2025-03-10"));
        createDevice("GL008", "沥青针入度仪", "SYD-2801G", "上海昌吉", new BigDecimal("1800.00"), "上海昌吉", "IN_USE", "沥青实验室", "公路市政道路检测", "SMALL", sdf.parse("2025-03-15"));
        createDevice("GL009", "低温弯曲试验仪", "SYD-0624", "上海昌吉", new BigDecimal("2800.00"), "上海昌吉", "IN_USE", "沥青实验室", "公路市政道路检测", "SMALL", sdf.parse("2025-03-20"));
        createDevice("GL010", "压实度快速检测仪", "CPR-6", "美国TROXLER", new BigDecimal("15000.00"), "美国TROXLER", "IN_USE", "道路检测", "公路市政道路检测", "SMALL", sdf.parse("2025-03-25"));

        // ==================== 五、隧道、地下工程专用仪器 ====================
        createDevice("SD001", "隧道地质超前预报系统", "TSP203", "瑞士安伯格", new BigDecimal("450000.00"), "瑞士安伯格", "IN_USE", "隧道检测", "隧道地下工程专用", "LARGE", sdf.parse("2025-01-10"));
        createDevice("SD002", "围岩收敛监测仪", "SWJ-IV", "北京航天", new BigDecimal("25000.00"), "北京航天", "IN_USE", "隧道检测", "隧道地下工程专用", "LARGE", sdf.parse("2025-01-15"));
        createDevice("SD003", "锚杆拉力试验机", "MD-100", "北京航天", new BigDecimal("38000.00"), "北京航天", "IN_USE", "隧道检测", "隧道地下工程专用", "LARGE", sdf.parse("2025-02-01"));
        createDevice("SD004", "隧道裂缝检测仪", "HC-CK101", "北京海创高科", new BigDecimal("3200.00"), "北京海创高科", "IN_USE", "隧道检测", "隧道地下工程专用", "SMALL", sdf.parse("2025-03-01"));
        createDevice("SD005", "瓦斯检测仪", "CD4", "重庆煤科", new BigDecimal("1500.00"), "重庆煤科", "IN_USE", "隧道检测", "隧道地下工程专用", "SMALL", sdf.parse("2025-03-05"));
        createDevice("SD006", "围岩强度测定仪", "WZY-1", "北京航天", new BigDecimal("2800.00"), "北京航天", "IN_USE", "隧道检测", "隧道地下工程专用", "SMALL", sdf.parse("2025-03-10"));
        createDevice("SD007", "防水板气密性检测仪", "SF-1", "北京航天", new BigDecimal("1800.00"), "北京航天", "IN_USE", "隧道检测", "隧道地下工程专用", "SMALL", sdf.parse("2025-03-15"));

        // ==================== 六、水利/堤坝工程检测仪器 ====================
        createDevice("SL001", "土工渗透试验机", "TST-55", "南京土壤仪器厂", new BigDecimal("35000.00"), "南京土壤仪器厂", "IN_USE", "水利实验室", "水利堤坝工程检测", "LARGE", sdf.parse("2025-01-15"));
        createDevice("SL002", "大坝沉降观测系统", "DS-1000", "北京航天", new BigDecimal("120000.00"), "北京航天", "IN_USE", "水利检测", "水利堤坝工程检测", "LARGE", sdf.parse("2025-02-01"));
        createDevice("SL003", "大型击实试验台", "DJQ-III", "南京土壤仪器厂", new BigDecimal("28000.00"), "南京土壤仪器厂", "IN_USE", "水利实验室", "水利堤坝工程检测", "LARGE", sdf.parse("2025-02-10"));
        createDevice("SL004", "土壤渗透仪", "TST-70", "南京土壤仪器厂", new BigDecimal("1800.00"), "南京土壤仪器厂", "IN_USE", "水利实验室", "水利堤坝工程检测", "SMALL", sdf.parse("2025-03-01"));
        createDevice("SL005", "含水率快速测定仪", "HKC-30", "南京土壤仪器厂", new BigDecimal("1200.00"), "南京土壤仪器厂", "IN_USE", "水利实验室", "水利堤坝工程检测", "SMALL", sdf.parse("2025-03-05"));
        createDevice("SL006", "测缝计", "VWJ-100", "北京航天", new BigDecimal("800.00"), "北京航天", "IN_USE", "水利检测", "水利堤坝工程检测", "SMALL", sdf.parse("2025-03-10"));
        createDevice("SL007", "水位沉降观测尺", "SW-50", "北京航天", new BigDecimal("500.00"), "北京航天", "IN_USE", "水利检测", "水利堤坝工程检测", "SMALL", sdf.parse("2025-03-15"));

        // ==================== 七、通用原材料检测 ====================
        createDevice("YS001", "负压筛", "Φ200mm", "上虞道墟", new BigDecimal("350.00"), "上虞道墟", "IN_USE", "水泥实验室", "通用原材料检测", "SMALL", sdf.parse("2025-01-10"));
        createDevice("YS002", "胶砂搅拌机", "JJ-5", "无锡建仪", new BigDecimal("4500.00"), "无锡建仪", "IN_USE", "水泥实验室", "通用原材料检测", "SMALL", sdf.parse("2025-01-15"));
        createDevice("YS003", "振动台", "ZT-96", "无锡建仪", new BigDecimal("3200.00"), "无锡建仪", "IN_USE", "水泥实验室", "通用原材料检测", "SMALL", sdf.parse("2025-01-20"));
        createDevice("YS004", "强度试验机", "300kN", "济南试验机厂", new BigDecimal("28000.00"), "济南试验机厂", "IN_USE", "水泥实验室", "通用原材料检测", "LARGE", sdf.parse("2025-02-01"));
        createDevice("YS005", "标准套筛", "Φ200mm", "上虞道墟", new BigDecimal("800.00"), "上虞道墟", "IN_USE", "砂石实验室", "通用原材料检测", "SMALL", sdf.parse("2025-02-05"));
        createDevice("YS006", "压碎值仪", "YEC-200", "上虞道墟", new BigDecimal("1200.00"), "上虞道墟", "IN_USE", "砂石实验室", "通用原材料检测", "SMALL", sdf.parse("2025-02-10"));
        createDevice("YS007", "针片状规准仪", "SFQ-2", "上虞道墟", new BigDecimal("600.00"), "上虞道墟", "IN_USE", "砂石实验室", "通用原材料检测", "SMALL", sdf.parse("2025-02-15"));
        createDevice("YS008", "万能试验机", "1000kN", "济南试验机厂", new BigDecimal("95000.00"), "济南试验机厂", "IN_USE", "钢材实验室", "通用原材料检测", "LARGE", sdf.parse("2025-02-20"));
        createDevice("YS009", "钢筋弯曲夹具", "Φ6-40mm", "济南试验机厂", new BigDecimal("800.00"), "济南试验机厂", "IN_USE", "钢材实验室", "通用原材料检测", "SMALL", sdf.parse("2025-02-25"));
        createDevice("YS010", "金属探伤仪", "USM35X", "德国KRAUTKRAMER", new BigDecimal("35000.00"), "德国KRAUTKRAMER", "IN_USE", "钢材实验室", "通用原材料检测", "LARGE", sdf.parse("2025-03-01"));
        createDevice("YS011", "净浆搅拌机", "NJ-160", "无锡建仪", new BigDecimal("2800.00"), "无锡建仪", "IN_USE", "外加剂实验室", "通用原材料检测", "SMALL", sdf.parse("2025-03-05"));
        createDevice("YS012", "流动度测定仪", "NLD-2", "无锡建仪", new BigDecimal("1500.00"), "无锡建仪", "IN_USE", "外加剂实验室", "通用原材料检测", "SMALL", sdf.parse("2025-03-10"));

        // ==================== 八、钢结构工程专用 ====================
        createDevice("GG001", "金属万能拉力试验机", "1000kN", "济南试验机厂", new BigDecimal("120000.00"), "济南试验机厂", "IN_USE", "钢结构实验室", "钢结构工程专用", "LARGE", sdf.parse("2025-01-10"));
        createDevice("GG002", "超声波探伤仪", "USM35X", "德国KRAUTKRAMER", new BigDecimal("45000.00"), "德国KRAUTKRAMER", "IN_USE", "钢结构实验室", "钢结构工程专用", "LARGE", sdf.parse("2025-01-15"));
        createDevice("GG003", "磁粉探伤设备", "CDX-III", "北京北工", new BigDecimal("25000.00"), "北京北工", "IN_USE", "钢结构实验室", "钢结构工程专用", "LARGE", sdf.parse("2025-02-01"));
        createDevice("GG004", "涂层测厚仪", "QNX-7500", "北京时代", new BigDecimal("1800.00"), "北京时代", "IN_USE", "钢结构实验室", "钢结构工程专用", "SMALL", sdf.parse("2025-03-01"));
        createDevice("GG005", "粗糙度仪", "TR200", "北京时代", new BigDecimal("2500.00"), "北京时代", "IN_USE", "钢结构实验室", "钢结构工程专用", "SMALL", sdf.parse("2025-03-05"));
        createDevice("GG006", "焊缝观测尺", "HC-T200", "北京海创高科", new BigDecimal("350.00"), "北京海创高科", "IN_USE", "钢结构实验室", "钢结构工程专用", "SMALL", sdf.parse("2025-03-10"));
        createDevice("GG007", "防火涂料检测仪", "FT-100", "北京北工", new BigDecimal("1200.00"), "北京北工", "IN_USE", "钢结构实验室", "钢结构工程专用", "SMALL", sdf.parse("2025-03-15"));

        // ==================== 九、节能幕墙专项 ====================
        createDevice("MQ001", "门窗气密性、水密性、抗风压一体机", "MWT-1500", "北京建工", new BigDecimal("180000.00"), "北京建工", "IN_USE", "幕墙实验室", "节能幕墙专项", "LARGE", sdf.parse("2025-01-10"));
        createDevice("MQ002", "保温层厚度检测仪", "HW-1", "北京海创高科", new BigDecimal("1500.00"), "北京海创高科", "IN_USE", "幕墙实验室", "节能幕墙专项", "SMALL", sdf.parse("2025-03-01"));
        createDevice("MQ003", "中空玻璃露点仪", "LD-200", "北京海创高科", new BigDecimal("2200.00"), "北京海创高科", "IN_USE", "幕墙实验室", "节能幕墙专项", "SMALL", sdf.parse("2025-03-05"));
        createDevice("MQ004", "导热系数快速检测仪", "DRY-300", "沈阳天平", new BigDecimal("3500.00"), "沈阳天平", "IN_USE", "幕墙实验室", "节能幕墙专项", "SMALL", sdf.parse("2025-03-10"));

        // ==================== 十、工程计量测量类仪器 ====================
        createDevice("JL001", "GNSS RTK测量系统", "9600", "南方测绘", new BigDecimal("85000.00"), "南方测绘", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-01-10"));
        createDevice("JL002", "全站仪", "徕卡TS16", "瑞士徕卡", new BigDecimal("65000.00"), "瑞士徕卡", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-01-15"));
        createDevice("JL003", "数字水准仪", "DNA03", "瑞士徕卡", new BigDecimal("28000.00"), "瑞士徕卡", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-02-01"));
        createDevice("JL004", "三维激光扫描仪", "FARO X330", "美国FARO", new BigDecimal("950000.00"), "美国FARO", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-02-10"));
        createDevice("JL005", "车载路面平整度检测仪", "ARAN9000", "丹麦RODO", new BigDecimal("350000.00"), "丹麦RODO", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-02-20"));
        createDevice("JL006", "桥梁静载位移采集系统", "ZBL-F800", "北京智博联", new BigDecimal("55000.00"), "北京智博联", "IN_USE", "测量设备", "工程计量测量类", "LARGE", sdf.parse("2025-03-01"));
        createDevice("JL007", "激光测距仪", "DLE50", "博世", new BigDecimal("1200.00"), "博世", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-03-05"));
        createDevice("JL008", "钢卷尺", "5m", "长城", new BigDecimal("80.00"), "长城", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-03-10"));
        createDevice("JL009", "钢卷尺", "50m", "长城", new BigDecimal("150.00"), "长城", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-03-15"));
        createDevice("JL010", "塔尺", "5m", "南方测绘", new BigDecimal("180.00"), "南方测绘", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-03-20"));
        createDevice("JL011", "钢直尺", "1m", "长城", new BigDecimal("50.00"), "长城", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-03-25"));
        createDevice("JL012", "数显游标卡尺", "0-150mm", "桂林量具", new BigDecimal("800.00"), "桂林量具", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-01"));
        createDevice("JL013", "千分尺", "0-25mm", "桂林量具", new BigDecimal("600.00"), "桂林量具", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-05"));
        createDevice("JL014", "百分表", "0-10mm", "桂林量具", new BigDecimal("350.00"), "桂林量具", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-10"));
        createDevice("JL015", "千分表", "0-1mm", "桂林量具", new BigDecimal("450.00"), "桂林量具", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-15"));
        createDevice("JL016", "激光投线水平仪", "GLL3-80", "博世", new BigDecimal("1500.00"), "博世", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-20"));
        createDevice("JL017", "数显倾角仪", "T-100", "北京时代", new BigDecimal("800.00"), "北京时代", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-04-25"));
        createDevice("JL018", "精密水平尺", "300mm", "长城", new BigDecimal("200.00"), "长城", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-01"));
        createDevice("JL019", "焊缝检验尺", "HC-W40", "北京海创高科", new BigDecimal("300.00"), "北京海创高科", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-05"));
        createDevice("JL020", "构件角度测量规", "D-50", "北京时代", new BigDecimal("250.00"), "北京时代", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-10"));
        createDevice("JL021", "碳化深度尺", "0-8mm", "上虞道墟", new BigDecimal("120.00"), "上虞道墟", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-15"));
        createDevice("JL022", "裂缝宽度深度测仪", "HC-CK101", "北京海创高科", new BigDecimal("3500.00"), "北京海创高科", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-20"));
        createDevice("JL023", "涂层测厚仪", "QNX-7500", "北京时代", new BigDecimal("1800.00"), "北京时代", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-25"));
        createDevice("JL024", "钢筋保护层厚度测定仪", "ZBL-R660A", "北京智博联", new BigDecimal("1800.00"), "北京智博联", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-05-30"));
        createDevice("JL025", "围岩收敛计", "SWJ-IV", "北京航天", new BigDecimal("2200.00"), "北京航天", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-06-01"));
        createDevice("JL026", "测缝计", "VWJ-100", "北京航天", new BigDecimal("800.00"), "北京航天", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-06-05"));
        createDevice("JL027", "位移计", "YHD-100", "北京航天", new BigDecimal("600.00"), "北京航天", "IN_USE", "测量设备", "工程计量测量类", "SMALL", sdf.parse("2025-06-10"));

        System.out.println("Data initialization complete. Total devices: " + deviceRepository.count());
    }

    private void createDevice(String deviceCode, String deviceName, String specification, String supplier,
                              BigDecimal purchasePrice, String manufacturer, String status, String location,
                              String category, String deviceType, Date purchaseDate) {
        Device device = new Device();
        device.setDeviceCode(deviceCode);
        device.setDeviceName(deviceName);
        device.setSpecification(specification);
        device.setSupplier(supplier);
        device.setPurchasePrice(purchasePrice);
        device.setManufacturer(manufacturer);
        device.setStatus(status);
        device.setLocation(location);
        device.setCategory(category);
        device.setDeviceType(deviceType);
        device.setPurchaseDate(purchaseDate);

        Device savedDevice = deviceRepository.save(device);

        Stock stock = new Stock();
        stock.setDeviceId(savedDevice.getId());
        stock.setQuantity(0);
        stock.setThreshold(10);
        stockRepository.save(stock);
    }
}