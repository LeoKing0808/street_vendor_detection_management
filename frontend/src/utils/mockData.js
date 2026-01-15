// Mock数据，用于在后端服务尚未实现时展示

// 违规数据统计
export const violationStatistics = {
  totalCaptures: 3567,      // 总抓拍次数
  totalViolations: 35,       // 总违规量
  violationRatio: 0.32,     // 违规占比（百分比）
};

// 各地点违规数据（用于柱状图）
export const locationViolationData = [
  { name: '南京路A_2', value: 789 },
  { name: '南门街', value: 621 },
  { name: 'xxx小区公交站', value: 589 },
  { name: 'xx路口人行横道', value: 488 },
  { name: 'xxx小区楼下', value: 321 },
  { name: 'xx路农贸市场', value: 10 },
];

// 各地点违规数据表格
export const locationViolationTable = [
  { id: 1, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 2, location: '南门街', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 3, location: 'xxx小区公交站', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 4, location: 'xxx小区楼下', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 5, location: '南京路A_2自由市场', type: '严重违规', time: '2024.10.15 21:39:28' },
];

// 违规摊贩图片数据（9张）
export const violationImages = [
  { id: 1, location: '南京路A_2', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩1' },
  { id: 2, location: '南门街', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩2' },
  { id: 3, location: 'xxx小区公交站', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩3' },
  { id: 4, location: '南京路A_2人行横道', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩4' },
  { id: 5, location: '南京路A_2自由市场', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩5' },
  { id: 6, location: 'xx路口人行横道', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩6' },
  { id: 7, location: 'xxx小区楼下', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩7' },
  { id: 8, location: '南门街', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩8' },
  { id: 9, location: '南京路A_2', imageUrl: 'https://via.placeholder.com/200x150/1a1a2e/ffffff?text=违规摊贩9' },
];

// 违规抓拍数量统计（横向柱状图数据）
export const captureStatistics = {
  labels: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'],
  values: [12, 8, 25, 35, 28, 18],
};

// 违规记录表格数据
export const violationRecords = [
  { id: 1, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 2, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 3, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 4, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 5, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 6, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 7, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 8, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 9, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
  { id: 10, location: '南京路A_2', type: '严重违规', time: '2024.10.15 21:39:28' },
];

// 在线监测点位分布（地图数据）
export const monitoringPoints = [
  { id: 1, name: '南京路A_2', lng: 116.416, lat: 39.914, violations: 789 },
  { id: 2, name: '南门街', lng: 116.366, lat: 39.908, violations: 621 },
  { id: 3, name: 'xxx小区公交站', lng: 116.456, lat: 39.912, violations: 589 },
  { id: 4, name: 'xx路口人行横道', lng: 116.305, lat: 39.984, violations: 488 },
  { id: 5, name: 'xxx小区楼下', lng: 116.423, lat: 39.867, violations: 321 },
];

// 统计数据（保留原有数据以兼容）
export const statisticsData = {
  totalVendors: 156,
  activeCameras: 45,
  detectionRate: 89.5,
  processedAlerts: 234,
};

// 检测趋势数据（最近7天）
export const detectionTrendData = {
  dates: ['1月9日', '1月10日', '1月11日', '1月12日', '1月13日', '1月14日', '1月15日'],
  counts: [45, 52, 49, 63, 71, 58, 65],
};

// 区域分布数据
export const areaDistributionData = [
  { name: '东城区', value: 35 },
  { name: '西城区', value: 28 },
  { name: '朝阳区', value: 42 },
  { name: '海淀区', value: 38 },
  { name: '丰台区', value: 22 },
  { name: '石景山区', value: 15 },
];

// 摄像头列表数据
export const camerasData = [
  {
    id: 'cam001',
    name: '王府井路口',
    location: '东城区王府井大街',
    status: 'online',
    lastDetection: '2026-01-15 15:00:00',
    vendorCount: 3,
  },
  {
    id: 'cam002',
    name: '西单商场',
    location: '西城区西单北大街',
    status: 'online',
    lastDetection: '2026-01-15 14:55:00',
    vendorCount: 2,
  },
  {
    id: 'cam003',
    name: '国贸中心',
    location: '朝阳区建国门外大街',
    status: 'online',
    lastDetection: '2026-01-15 14:50:00',
    vendorCount: 5,
  },
  {
    id: 'cam004',
    name: '中关村广场',
    location: '海淀区中关村大街',
    status: 'offline',
    lastDetection: '2026-01-15 14:30:00',
    vendorCount: 0,
  },
  {
    id: 'cam005',
    name: '方庄小区',
    location: '丰台区方庄路',
    status: 'online',
    lastDetection: '2026-01-15 14:45:00',
    vendorCount: 1,
  },
];

// 单个摄像头详情数据
export const cameraDetailData = {
  id: 'CAM-ZHONGLU-023',
  name: '中山路与解放街交叉口',
  location: '中山路与解放街交叉口',
  status: 'online',
  currentTime: '2026/1/4 19:50:35',
  vendorCount: 6,
};

// 当前识别摊贩列表
export const currentVendors = [
  {
    id: 'V1001',
    category: '水果摊',
    evictionCount: 4,
    personnelStatus: '队员已前往',
    personnelName: '张伟',
    personnelId: 'ZF2021056',
    isPinned: true,
    workOrderId: 'WO20250405023',
  },
  {
    id: 'V1002',
    category: '小吃车',
    evictionCount: 2,
    personnelStatus: '未指派',
    isPinned: false,
  },
  {
    id: 'V1003',
    category: '水果摊',
    evictionCount: 1,
    personnelStatus: '未指派',
    isPinned: false,
  },
  {
    id: 'V1004',
    category: '小吃车',
    evictionCount: 4,
    personnelStatus: '队员已前往',
    isPinned: false,
  },
  {
    id: 'V1005',
    category: '水果摊',
    evictionCount: 3,
    personnelStatus: '未指派',
    isPinned: false,
  },
  {
    id: 'V1006',
    category: '小吃车',
    evictionCount: 1,
    personnelStatus: '未指派',
    isPinned: false,
  },
];

// 当前广播内容
export const currentBroadcast = {
  content: '请注意! 中山路与解放街交叉口禁止占道经营。请各位摊贩立即前往解放街临时疏导点 (向东300米), 该区域目前空余摊位充足, 感谢您的配合!',
  isPlaying: true,
};

// 历史驱离工单
export const historicalWorkOrders = [
  {
    id: 'WO20250401001',
    dateTime: '2025-04-01 14:23',
    status: '已验证',
  },
  {
    id: 'WO20250402005',
    dateTime: '2025-04-02 09:15',
    status: '已驱离',
  },
  {
    id: 'WO20250403012',
    dateTime: '2025-04-03 16:40',
    status: '已接单',
  },
  {
    id: 'WO20250404008',
    dateTime: '2025-04-04 11:05',
    status: '待接单',
  },
];

// 摊贩引导详情（选中摊贩的详情）
export const vendorGuidanceDetail = {
  vendorId: 'V1001',
  category: '水果摊',
  guidanceCount: 4,
  personnelName: '张伟',
  personnelId: 'ZF2021056',
  workOrderId: 'WO20250405023',
  imageUrl: null, // 暂无摊贩截图
};

// 语音记录
export const voiceRecords = [
  {
    id: 1,
    content: '穿红色外套的大爷,此处不允许摆摊,请移步至中山路便民市场,从路口直行200米右转即到,那里的水果摊位较少。',
  },
  {
    id: 2,
    content: '穿红色外套的大爷,再次提醒您此处禁止占道经营,请中山路便民市场,从路口直行200米右转即到,水果类摊位空余较多。',
  },
  {
    id: 3,
    content: '穿红色外套的大爷,再次提醒您此处禁止占道经营,请中山路便民市场,从路口直行200米右转即到,水果类摊位空余较多。',
  },
  {
    id: 4,
    content: '穿红色外套的大爷,您已被三次劝导仍未离开,城管执法队员已出发前往现场,请配合执法,停止占道行为!',
  },
];

// 地图标记数据
export const mapMarkersData = [
  { id: 'cam001', name: '王府井路口', position: [116.416, 39.914], vendorCount: 3 },
  { id: 'cam002', name: '西单商场', position: [116.366, 39.908], vendorCount: 2 },
  { id: 'cam003', name: '国贸中心', position: [116.456, 39.912], vendorCount: 5 },
  { id: 'cam004', name: '中关村广场', position: [116.305, 39.984], vendorCount: 0 },
  { id: 'cam005', name: '方庄小区', position: [116.423, 39.867], vendorCount: 1 },
];
