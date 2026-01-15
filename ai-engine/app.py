import os
import time
import redis
import requests
from flask import Flask, jsonify

# 模擬攤販檢測邏輯的 AI 引擎服務
app = Flask(__name__)

# Redis 配置（用於計數等簡單狀態存儲）
REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

# 後端服務地址（在 Docker 中建議使用 http://backend:8080）
BACKEND_URL = os.getenv("BACKEND_URL", "http://backend:8080")


@app.route('/health')
def health():
    """
    健康檢查：確認 Flask 與 Redis 是否正常
    """
    try:
        redis_ok = r.ping()
    except Exception as e:
        return jsonify({"status": "error", "redis": False, "error": str(e)}), 500

    return {"status": "ok", "redis": redis_ok}


@app.route('/detect')
def simulate_detection():
    """
    模擬一次 AI 檢測流程：
    1. 偵測到固定的 vendor_id（實際場景中應替換為 YOLO 等模型推理結果）
    2. 使用 Redis 累加連續檢測次數
    3. 將檢測結果回調給後端 /api/recognition/detect
    """
    # 假設來自某個攝像頭
    camera_id = "camera_001"
    vendor_id = "vendor_001"

    # 模擬是否檢測到攤販（此處簡化為始終檢測到）
    detected = True

    # 連續檢測計數
    consecutive = r.incr(f"consecutive:{camera_id}:{vendor_id}")
    print(f"Detected {vendor_id} on {camera_id}, consecutive count: {consecutive}")

    # 構造發往後端的檢測結果 payload
    detection_payload = {
        "cameraId": camera_id,
        "detected": detected,
        "objects": {
            vendor_id: {
                "type": "vendor",
                "bbox": [100, 100, 200, 200],  # 模擬邊界框
                "confidence": 0.9
            }
        },
        # 在實際系統中這裡應填寫抓拍圖片的 URL
        "imageUrl": "http://example.com/snapshot.jpg"
    }

    backend_response = None
    try:
        url = f"{BACKEND_URL}/api/recognition/detect"
        resp = requests.post(url, json=detection_payload, timeout=5)
        resp.raise_for_status()
        backend_response = resp.json()
    except Exception as e:
        backend_response = {"status": "error", "message": str(e)}

    return {
        "vendor_id": vendor_id,
        "consecutive_frames": consecutive,
        "backend_result": backend_response
    }


if __name__ == '__main__':
    # 本地開發時可以將 BACKEND_URL 設置為 http://localhost:8080
    # 例如： BACKEND_URL=http://localhost:8080 python app.py
    app.run(host='0.0.0.0', port=5000)
