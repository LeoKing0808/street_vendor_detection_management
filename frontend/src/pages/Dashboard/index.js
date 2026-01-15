import React, { useEffect, useState, useRef } from 'react';
import { Table, Select, Button } from 'antd';
import { LogoutOutlined } from '@ant-design/icons';
import * as echarts from 'echarts';
import {
  violationStatistics,
  locationViolationData,
  locationViolationTable,
  violationImages,
  captureStatistics,
  violationRecords,
  monitoringPoints,
} from '../../utils/mockData';
import './Dashboard.css';

const { Option } = Select;

const Dashboard = () => {
  // 图表引用
  const locationChartRef = useRef(null);
  const captureChartRef = useRef(null);
  const mapChartRef = useRef(null);

  // 状态管理
  const [locationChart, setLocationChart] = useState(null);
  const [captureChart, setCaptureChart] = useState(null);
  const [mapChart, setMapChart] = useState(null);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [violationDegree, setViolationDegree] = useState('严重');

  // 更新时间
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // 格式化时间
  const formatDateTime = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  };

  // 初始化各地点违规数据柱状图
  useEffect(() => {
    if (locationChartRef.current) {
      const chart = echarts.init(locationChartRef.current, 'dark');
      const option = {
        backgroundColor: 'transparent',
        grid: {
          left: '15%',
          right: '10%',
          top: '15%',
          bottom: '10%',
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            color: '#B0C4DE',
            fontSize: 12,
          },
        },
        yAxis: {
          type: 'category',
          data: locationViolationData.map(item => item.name),
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            color: '#B0C4DE',
            fontSize: 12,
          },
        },
        series: [
          {
            type: 'bar',
            data: locationViolationData.map(item => item.value),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#4A90E2' },
                { offset: 1, color: '#87CEEB' },
              ]),
            },
            label: {
              show: true,
              position: 'right',
              color: '#B0C4DE',
              fontSize: 12,
            },
          },
        ],
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#4A90E2',
          textStyle: {
            color: '#B0C4DE',
          },
        },
      };
      chart.setOption(option);
      setLocationChart(chart);
    }
  }, []);

  // 初始化违规抓拍数量统计（横向柱状图）
  useEffect(() => {
    if (captureChartRef.current) {
      const chart = echarts.init(captureChartRef.current, 'dark');
      const option = {
        backgroundColor: 'transparent',
        grid: {
          left: '15%',
          right: '10%',
          top: '15%',
          bottom: '10%',
        },
        xAxis: {
          type: 'category',
          data: captureStatistics.labels,
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            color: '#B0C4DE',
            fontSize: 12,
          },
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            color: '#B0C4DE',
            fontSize: 12,
          },
        },
        series: [
          {
            type: 'bar',
            data: captureStatistics.values,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#FF6B6B' },
                { offset: 1, color: '#FF8E8E' },
              ]),
            },
            label: {
              show: true,
              position: 'top',
              color: '#B0C4DE',
              fontSize: 12,
            },
          },
        ],
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#4A90E2',
          textStyle: {
            color: '#B0C4DE',
          },
        },
      };
      chart.setOption(option);
      setCaptureChart(chart);
    }
  }, []);

  // 初始化地图（使用散点图模拟地图）
  useEffect(() => {
    if (mapChartRef.current) {
      const chart = echarts.init(mapChartRef.current, 'dark');
      
      // 计算坐标范围
      const lngs = monitoringPoints.map(p => p.lng);
      const lats = monitoringPoints.map(p => p.lat);
      const lngMin = Math.min(...lngs) - 0.01;
      const lngMax = Math.max(...lngs) + 0.01;
      const latMin = Math.min(...lats) - 0.01;
      const latMax = Math.max(...lats) + 0.01;
      
      const option = {
        backgroundColor: 'transparent',
        grid: {
          left: '10%',
          right: '10%',
          top: '10%',
          bottom: '10%',
        },
        xAxis: {
          type: 'value',
          min: lngMin,
          max: lngMax,
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            show: false,
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(74, 144, 226, 0.2)',
              type: 'dashed',
            },
          },
        },
        yAxis: {
          type: 'value',
          min: latMin,
          max: latMax,
          axisLine: {
            lineStyle: {
              color: '#4A90E2',
            },
          },
          axisLabel: {
            show: false,
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(74, 144, 226, 0.2)',
              type: 'dashed',
            },
          },
        },
        series: [
          {
            type: 'scatter',
            data: monitoringPoints.map(point => ({
              name: point.name,
              value: [point.lng, point.lat, point.violations],
            })),
            symbolSize: function (val) {
              return Math.sqrt(val[2]) * 3 + 15;
            },
            itemStyle: {
              color: '#87CEEB',
              shadowBlur: 15,
              shadowColor: '#4A90E2',
              borderColor: '#4A90E2',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}',
              position: 'right',
              color: '#B0C4DE',
              fontSize: 11,
              fontWeight: 'bold',
            },
          },
        ],
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            return `${params.data.name}<br/>违规数量: ${params.data.value[2]}`;
          },
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#4A90E2',
          textStyle: {
            color: '#B0C4DE',
          },
        },
      };
      chart.setOption(option);
      setMapChart(chart);
    }
  }, []);

  // 响应式调整
  useEffect(() => {
    const handleResize = () => {
      locationChart?.resize();
      captureChart?.resize();
      mapChart?.resize();
    };
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
      locationChart?.dispose();
      captureChart?.dispose();
      mapChart?.dispose();
    };
  }, [locationChart, captureChart, mapChart]);

  // 表格列定义
  const locationTableColumns = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      width: 60,
    },
    {
      title: '监测点/违规车辆',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: '违规类型',
      dataIndex: 'type',
      key: 'type',
    },
    {
      title: '时间',
      dataIndex: 'time',
      key: 'time',
    },
  ];

  const violationTableColumns = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      width: 60,
    },
    {
      title: '监测点/违规车辆',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: '违规类型',
      dataIndex: 'type',
      key: 'type',
    },
    {
      title: '时间',
      dataIndex: 'time',
      key: 'time',
    },
  ];

  return (
    <div className="dashboard-container">
      {/* 顶部标题栏 */}
      <div className="dashboard-header">
        <div className="header-time">{formatDateTime(currentTime)}</div>
        <div className="header-title">占道经营识别告警系统</div>
        <Button
          type="text"
          icon={<LogoutOutlined />}
          className="logout-btn"
          onClick={() => {
            // 退出登录逻辑
            console.log('退出登录');
          }}
        >
          退出登录
        </Button>
      </div>

      {/* 主要内容区域 */}
      <div className="dashboard-content">
        {/* 左侧面板 */}
        <div className="dashboard-left">
          {/* 违规数据统计 */}
          <div className="statistics-card">
            <div className="card-title">违规数据统计</div>
            <div className="statistics-grid">
              <div className="stat-item">
                <div className="stat-label">总抓拍次数</div>
                <div className="stat-value">{violationStatistics.totalCaptures.toLocaleString()}</div>
              </div>
              <div className="stat-item">
                <div className="stat-label">总违规量</div>
                <div className="stat-value">{violationStatistics.totalViolations}</div>
              </div>
              <div className="stat-item">
                <div className="stat-label">违规占比</div>
                <div className="stat-value">{violationStatistics.violationRatio}%</div>
              </div>
            </div>
          </div>

          {/* 各地点违规数据柱状图 */}
          <div className="chart-card">
            <div className="card-title">各地点违规数据</div>
            <div ref={locationChartRef} className="chart-container" />
          </div>

          {/* 各地点违规数据表格 */}
          <div className="table-card">
            <div className="card-title">各地点违规数据</div>
            <Table
              dataSource={locationViolationTable}
              columns={locationTableColumns}
              pagination={false}
              size="small"
              className="custom-table"
              rowKey="id"
            />
          </div>
        </div>

        {/* 中间面板 */}
        <div className="dashboard-center">
          {/* 违规摊贩图片网格 */}
          <div className="images-card">
            <div className="card-title">违规摊贩</div>
            <div className="images-grid">
              {violationImages.map((image) => (
                <div key={image.id} className="image-item">
                  <div className="image-wrapper">
                    <img src={image.imageUrl} alt={image.location} />
                    <div className="image-overlay">
                      <div className="detection-box">zdjy</div>
                    </div>
                  </div>
                  <div className="image-location">{image.location}</div>
                </div>
              ))}
            </div>
          </div>

          {/* 违规抓拍数量统计 */}
          <div className="chart-card">
            <div className="card-title">违规抓拍数量统计</div>
            <div ref={captureChartRef} className="chart-container" />
          </div>
        </div>

        {/* 右侧面板 */}
        <div className="dashboard-right">
          {/* 在线监测点位分布地图 */}
          <div className="map-card">
            <div className="map-header">
              <div className="card-title">在线监测点位分布</div>
              <Select
                value={violationDegree}
                onChange={setViolationDegree}
                className="violation-select"
                size="small"
              >
                <Option value="严重">严重</Option>
                <Option value="一般">一般</Option>
                <Option value="轻微">轻微</Option>
              </Select>
            </div>
            <div ref={mapChartRef} className="map-container" />
          </div>

          {/* 违规记录表格 */}
          <div className="table-card">
            <div className="card-title">违规记录</div>
            <Table
              dataSource={violationRecords}
              columns={violationTableColumns}
              pagination={false}
              size="small"
              className="custom-table"
              rowKey="id"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
