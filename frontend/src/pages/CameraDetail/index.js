import React, { useState, useEffect } from 'react';
import { Button, Table, Tag, Space, Input } from 'antd';
import { ArrowLeftOutlined, PlayCircleOutlined, PauseOutlined, EyeOutlined, PushpinOutlined } from '@ant-design/icons';
import { Link, useParams } from 'react-router-dom';
import {
  cameraDetailData,
  currentVendors,
  currentBroadcast,
  historicalWorkOrders,
  vendorGuidanceDetail,
  voiceRecords,
} from '../../utils/mockData';
import './CameraDetail.css';

const { TextArea } = Input;

const CameraDetail = () => {
  const { cameraId } = useParams();
  const [selectedVendor, setSelectedVendor] = useState(vendorGuidanceDetail);
  const [isPlaying, setIsPlaying] = useState(currentBroadcast.isPlaying);
  const [currentTime, setCurrentTime] = useState(new Date());

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
    return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
  };

  // 处理查看详情
  const handleViewDetails = (vendor) => {
    setSelectedVendor({
      vendorId: vendor.id,
      category: vendor.category,
      guidanceCount: vendor.evictionCount,
      personnelName: vendor.personnelName || '未指派',
      personnelId: vendor.personnelId || '-',
      workOrderId: vendor.workOrderId || '-',
    });
  };

  // 处理置顶/取消置顶
  const handleTogglePin = (vendorId) => {
    // 这里可以添加置顶逻辑
    console.log('Toggle pin for vendor:', vendorId);
  };

  // 当前识别摊贩表格列
  const vendorColumns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 100,
    },
    {
      title: '类别',
      dataIndex: 'category',
      key: 'category',
      width: 120,
    },
    {
      title: '驱离次数',
      dataIndex: 'evictionCount',
      key: 'evictionCount',
      width: 100,
      render: (count) => `${count}次`,
    },
    {
      title: '城管状态',
      dataIndex: 'personnelStatus',
      key: 'personnelStatus',
      width: 150,
      render: (status) => {
        const color = status === '队员已前往' ? 'green' : 'default';
        return <Tag color={color}>{status}</Tag>;
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            size="small"
            icon={<EyeOutlined />}
            onClick={() => handleViewDetails(record)}
          >
            查看详情
          </Button>
          <Button
            type="link"
            size="small"
            icon={<PushpinOutlined />}
            onClick={() => handleTogglePin(record.id)}
          >
            {record.isPinned ? '取消置顶' : '锁定置顶'}
          </Button>
        </Space>
      ),
    },
  ];

  // 历史工单表格列
  const workOrderColumns = [
    {
      title: '工单ID',
      dataIndex: 'id',
      key: 'id',
      width: 150,
    },
    {
      title: '日期时间',
      dataIndex: 'dateTime',
      key: 'dateTime',
      width: 180,
    },
    {
      title: '工单状态',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: (status) => {
        const colorMap = {
          '已验证': 'green',
          '已驱离': 'blue',
          '已接单': 'orange',
          '待接单': 'default',
        };
        return <Tag color={colorMap[status] || 'default'}>{status}</Tag>;
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 100,
      render: (_, record) => (
        <Button type="link" size="small" onClick={() => console.log('View work order:', record.id)}>
          查看详情
        </Button>
      ),
    },
  ];

  return (
    <div className="camera-detail-container">
      {/* 顶部导航 */}
      <div className="camera-detail-header">
        <Link to="/">
          <Button type="link" icon={<ArrowLeftOutlined />} className="back-btn">
            返回数据大屏
          </Button>
        </Link>
      </div>

      {/* 主要内容区域 */}
      <div className="camera-detail-content">
        {/* 左侧面板 */}
        <div className="camera-detail-left">
          {/* 摄像头信息 */}
          <div className="camera-info-section">
            <div className="info-item">
              <span className="info-label">摄像头ID:</span>
              <span className="info-value">{cameraDetailData.id}</span>
            </div>
            <div className="info-item">
              <span className="info-label">位置:</span>
              <span className="info-value">{cameraDetailData.location}</span>
            </div>
            <div className="info-item">
              <span className="info-label">时间:</span>
              <span className="info-value">{formatDateTime(currentTime)}</span>
            </div>
          </div>

          {/* 实时视频流 */}
          <div className="video-stream-section">
            <div className="video-stream-box">
              <div className="video-placeholder">实时视频流 (模拟)</div>
            </div>
          </div>

          {/* 当前识别摊贩 */}
          <div className="vendors-section">
            <div className="section-title">
              当前识别摊贩 ({currentVendors.length})
            </div>
            <Table
              dataSource={currentVendors}
              columns={vendorColumns}
              pagination={false}
              size="small"
              className="vendor-table"
              rowKey="id"
            />
          </div>

          {/* 当前广播内容 */}
          <div className="broadcast-section">
            <div className="section-title">当前广播内容</div>
            <TextArea
              value={currentBroadcast.content}
              rows={4}
              readOnly
              className="broadcast-textarea"
            />
            <div className="broadcast-actions">
              <Button
                icon={isPlaying ? <PauseOutlined /> : <PlayCircleOutlined />}
                onClick={() => setIsPlaying(!isPlaying)}
              >
                {isPlaying ? '暂停播放' : '再次播放'}
              </Button>
            </div>
          </div>

          {/* 历史驱离工单 */}
          <div className="work-orders-section">
            <div className="section-title">历史驱离工单</div>
            <Table
              dataSource={historicalWorkOrders}
              columns={workOrderColumns}
              pagination={false}
              size="small"
              className="work-order-table"
              rowKey="id"
            />
          </div>
        </div>

        {/* 右侧面板 */}
        <div className="camera-detail-right">
          {/* 摊贩引导详情 */}
          <div className="guidance-detail-section">
            <div className="section-title">摊贩引导详情</div>
            
            {/* 摊贩截图 */}
            <div className="vendor-image-box">
              {selectedVendor.imageUrl ? (
                <img src={selectedVendor.imageUrl} alt="摊贩截图" />
              ) : (
                <div className="no-image-placeholder">■ 暂无摊贩截图</div>
              )}
            </div>

            {/* 摊贩信息 */}
            <div className="vendor-info">
              <div className="info-row">
                <span className="info-label">摊贩ID:</span>
                <span className="info-value">{selectedVendor.vendorId}</span>
              </div>
              <div className="info-row">
                <span className="info-label">类别:</span>
                <span className="info-value">{selectedVendor.category}</span>
              </div>
              <div className="info-row">
                <span className="info-label">引导次数:</span>
                <span className="info-value">{selectedVendor.guidanceCount}</span>
              </div>
              <div className="info-row">
                <span className="info-label">城管人员:</span>
                <span className="info-value">
                  {selectedVendor.personnelName}
                  {selectedVendor.personnelId !== '-' && ` (执法编号: ${selectedVendor.personnelId})`}
                </span>
              </div>
              <div className="info-row">
                <span className="info-label">工单号:</span>
                <span className="info-value">{selectedVendor.workOrderId}</span>
              </div>
            </div>
          </div>

          {/* 语音记录 */}
          <div className="voice-records-section">
            <div className="section-title">语音记录:</div>
            <div className="voice-records-list">
              {voiceRecords.map((record) => (
                <div key={record.id} className="voice-record-item">
                  <Button
                    type="text"
                    icon={<PlayCircleOutlined style={{ color: '#52c41a' }} />}
                    className="play-btn"
                    onClick={() => console.log('Play voice:', record.id)}
                  />
                  <div className="voice-content">{record.content}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CameraDetail;
