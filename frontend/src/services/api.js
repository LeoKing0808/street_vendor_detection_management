import axios from 'axios';

// 创建axios实例
const createApiClient = (baseURL, timeout = 10000) => {
  const client = axios.create({
    baseURL,
    timeout,
  });

  // 请求拦截器
  client.interceptors.request.use(
    config => {
      // 添加请求时间戳，用于调试
      config.metadata = { startTime: Date.now() };
      // 可以在这里添加认证token等
      // const token = localStorage.getItem('token');
      // if (token) {
      //   config.headers.Authorization = `Bearer ${token}`;
      // }
      return config;
    },
    error => {
      return Promise.reject(error);
    }
  );

  // 响应拦截器
  client.interceptors.response.use(
    response => {
      // 计算请求耗时
      const duration = Date.now() - response.config.metadata.startTime;
      console.log(`${response.config.url} 请求耗时: ${duration}ms`);
      
      // 处理统一的响应格式
      if (response.data && typeof response.data === 'object') {
        return response.data;
      }
      return response.data;
    },
    error => {
      // 统一错误处理
      let errorMessage = '网络请求失败，请稍后重试';
      
      if (error.response) {
        // 服务器返回错误状态码
        const { status, data } = error.response;
        
        switch (status) {
          case 400:
            errorMessage = data.message || '请求参数错误';
            break;
          case 401:
            errorMessage = '未授权，请重新登录';
            // 可以在这里处理登录过期逻辑
            // localStorage.removeItem('token');
            // window.location.href = '/login';
            break;
          case 403:
            errorMessage = '权限不足，无法访问';
            break;
          case 404:
            errorMessage = '请求的资源不存在';
            break;
          case 500:
            errorMessage = '服务器内部错误';
            break;
          default:
            errorMessage = `请求失败 (${status})`;
        }
      } else if (error.request) {
        // 请求已发出但没有收到响应
        errorMessage = '服务器无响应，请检查网络连接';
      } else {
        // 请求配置出错
        errorMessage = error.message || '请求配置错误';
      }
      
      console.error('API请求错误:', {
        message: errorMessage,
        originalError: error,
        url: error.config?.url
      });
      
      // 包装错误对象，添加友好的错误信息
      const wrappedError = new Error(errorMessage);
      wrappedError.originalError = error;
      wrappedError.status = error.response?.status;
      
      return Promise.reject(wrappedError);
    }
  );

  return client;
};

// 基础URL配置 - 支持环境变量配置，Docker部署时使用相对路径或配置的地址
const getBaseUrl = () => {
  // 优先使用环境变量
  if (process.env.REACT_APP_BACKEND_URL) {
    return process.env.REACT_APP_BACKEND_URL;
  }
  // 开发环境使用localhost
  if (process.env.NODE_ENV === 'development') {
    return 'http://localhost:8080';
  }
  // 生产环境：使用当前主机地址（假设后端部署在同一主机的8080端口）
  return `http://${window.location.hostname}:8080`;
};

const getAiEngineUrl = () => {
  if (process.env.REACT_APP_AI_ENGINE_URL) {
    return process.env.REACT_APP_AI_ENGINE_URL;
  }
  if (process.env.NODE_ENV === 'development') {
    return 'http://localhost:5000';
  }
  return `http://${window.location.hostname}:5000`;
};

const BASE_URL = {
  BACKEND: getBaseUrl(),
  AI_ENGINE: getAiEngineUrl(),
};

// 创建API客户端
const backendClient = createApiClient(BASE_URL.BACKEND);
const aiClient = createApiClient(BASE_URL.AI_ENGINE);

// 带重试机制的请求封装
const withRetry = (requestFn, maxRetries = 3, delay = 1000) => {
  return async (...args) => {
    let lastError;
    
    for (let i = 0; i < maxRetries; i++) {
      try {
        return await requestFn(...args);
      } catch (error) {
        lastError = error;
        
        // 只对网络错误和500错误进行重试
        if (error.status && error.status < 500) {
          break;
        }
        
        if (i < maxRetries - 1) {
          console.log(`请求失败，${delay}ms后重试 (${i + 1}/${maxRetries})`);
          await new Promise(resolve => setTimeout(resolve, delay));
          // 指数退避策略
          delay *= 1.5;
        }
      }
    }
    
    throw lastError;
  };
};

// API接口定义
const api = {
  // AI引擎接口
  ai: {
    // 健康检查
    healthCheck: withRetry(() => aiClient.get('/health')),
    // 检测小贩
        detectVendor: withRetry((params) => aiClient.get('/detect', { params })),
  },
  
  // 后端接口
  backend: {
    // 视频帧管理
    createFrames: withRetry((frames) => backendClient.post('/api/frames', frames)),
    
    // 工单管理
    createTicket: withRetry((ticketData) => backendClient.post('/api/tickets', ticketData)),
    assignTicket: withRetry((ticketId, personnelId) => backendClient.post(`/api/tickets/${ticketId}/assign`, { personnelId })),
    closeTickets: withRetry((cameraId) => backendClient.post('/api/tickets/close', { cameraId })),
    getTickets: withRetry((params) => backendClient.get('/api/tickets', { params })),
    getTicketsPage: withRetry((params) => backendClient.get('/api/tickets/page', { params })),
    
    // 摄像头管理
    enableCameras: withRetry((cameraIds) => backendClient.post('/api/cameras/enable', { cameraIds })),
    disableCameras: withRetry((cameraIds) => backendClient.post('/api/cameras/disable', { cameraIds })),
    getCameraDetail: withRetry((cameraId) => backendClient.get(`/api/cameras/${cameraId}`)),
    getLatestAlert: withRetry((cameraId, frameId) => backendClient.get(`/api/cameras/${cameraId}/frames/${frameId}/latestAlert`)),
    getObjectDetails: withRetry((cameraId, objectId) => backendClient.get(`/api/cameras/${cameraId}/objects/${objectId}/details`)),
    closeActiveTickets: withRetry((cameraId) => backendClient.post(`/api/cameras/${cameraId}/tickets/close`)),
    
    // 告警管理
    broadcastAlert: withRetry((alertId, cameraId, frameId) => backendClient.post(`/api/alerts/${alertId}/broadcast`, { cameraId, frameId })),
    
    // 用户管理
    createUser: withRetry((userData) => backendClient.post('/api/users', userData)),
    login: withRetry((credentials) => backendClient.post('/api/auth/login', credentials)),
    
    // 识别管理
    updateDetection: withRetry((cameraId, detected) => backendClient.post('/api/recognition/update', { cameraId, detected })),
  },
};

// 导出API工具函数
export const apiUtils = {
  // 处理API响应的通用函数
  handleApiResponse: async (apiCall, onSuccess, onError, onLoading) => {
    try {
      if (onLoading) {
        onLoading(true);
      }
      
      const result = await apiCall();
      
      if (onSuccess) {
        onSuccess(result);
      }
      
      return result;
    } catch (error) {
      if (onError) {
        onError(error.message || '操作失败');
      }
      throw error;
    } finally {
      if (onLoading) {
        onLoading(false);
      }
    }
  },
  
  // 批量请求处理
  batchRequests: async (requests) => {
    try {
      const results = await Promise.all(requests);
      return { success: true, data: results };
    } catch (error) {
      return { success: false, error: error.message };
    }
  },
  
  // 延迟执行
  delay: (ms) => new Promise(resolve => setTimeout(resolve, ms)),
};

export default api;
