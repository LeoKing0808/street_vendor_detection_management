import os
import time
import redis
from flask import Flask

# Simulate vendor detection logic
app = Flask(__name__)

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

@app.route('/health')
def health():
    return {"status": "ok", "redis": r.ping()}

@app.route('/detect')
def simulate_detection():
    # In real use: run YOLO inference here
    vendor_id = "vendor_001"
    consecutive = r.incr(f"consecutive:{vendor_id}")
    print(f"Detected {vendor_id}, consecutive count: {consecutive}")
    return {"vendor_id": vendor_id, "consecutive_frames": consecutive}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
