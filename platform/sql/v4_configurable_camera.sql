INSERT INTO device_model (
  modelId,
  modelName,
  provider,
  category,
  model_icon,
  model,
  create_time,
  update_time
) VALUES (
  'configurableCamera',
  '可配置人脸识别摄像头',
  NULL,
  'camera',
  (SELECT model_icon FROM (SELECT model_icon FROM device_model WHERE modelId = 'facecam' LIMIT 1) AS facecam_icon),
  '{
    "modelId": "configurableCamera",
    "modelName": "可配置人脸识别摄像头",
    "provider": null,
    "category": "camera",
    "properties": {
      "configured": { "type": "boolean", "readOnly": true, "description": "是否已配置识别服务" },
      "power_on": { "type": "boolean", "readOnly": true, "description": "是否开启" },
      "stream_status": { "type": "string", "readOnly": true, "description": "视频流状态" },
      "last_seen": { "type": "string", "readOnly": true, "description": "最后上报时间" }
    },
    "actions": {
      "on": { "arguments": {}, "description": "打开" },
      "off": { "arguments": {}, "description": "关闭" },
      "configure": {
        "arguments": {
          "url": { "type": "string", "description": "识别服务 URL" },
          "api_key": { "type": "string", "description": "API Key" },
          "secret_key": { "type": "string", "description": "Secret Key" }
        },
        "description": "配置识别服务"
      },
      "reset": { "arguments": {}, "description": "重置识别服务配置" }
    },
    "events": {
      "detected": {
        "level": "info",
        "description": "识别到人",
        "fields": {
          "user_id": { "max": null, "min": null, "type": "string", "unit": "", "readOnly": false, "enumValues": [], "description": "用户ID" },
          "person_name": { "max": null, "min": null, "type": "string", "unit": "", "readOnly": false, "enumValues": [], "description": "人员姓名" }
        },
        "outputs": {}
      }
    }
  }',
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE
  modelId = VALUES(modelId),
  modelName = VALUES(modelName),
  provider = VALUES(provider),
  category = VALUES(category),
  model_icon = VALUES(model_icon),
  model = VALUES(model),
  update_time = NOW();

INSERT INTO device (
  provider,
  modelId,
  device_id,
  device_name,
  device_mapper_path,
  property_map,
  action_map,
  event_map,
  create_time,
  update_time
) VALUES (
  'mqtt',
  'configurableCamera',
  'CONFIG-CAMERA-01',
  '可配置人脸识别摄像头',
  'MqttConfigurableCameraMapper.ts',
  '{
    "configured": "configured",
    "power_on": "power_on",
    "stream_status": "stream_status",
    "last_seen": "last_seen"
  }',
  '{
    "on": "const payload = { action: ''on'', args: {} }; this.client.publish(''devices/'' + deviceId + ''/command'', JSON.stringify(payload));",
    "off": "const payload = { action: ''off'', args: {} }; this.client.publish(''devices/'' + deviceId + ''/command'', JSON.stringify(payload));",
    "configure": "const payload = { action: ''configureRecognition'', args: { url: args.url, apiKey: args.api_key ?? args.apiKey, secretKey: args.secret_key ?? args.secretKey } }; this.client.publish(''devices/'' + deviceId + ''/command'', JSON.stringify(payload));",
    "reset": "const payload = { action: ''resetRecognition'', args: {} }; this.client.publish(''devices/'' + deviceId + ''/command'', JSON.stringify(payload));"
  }',
  '{
    "face_detected": {
      "_to": "detected",
      "user_id": "user_id",
      "person_name": "person_name"
    }
  }',
  NOW(),
  NOW()
) ON DUPLICATE KEY UPDATE
  device_name = VALUES(device_name),
  device_mapper_path = VALUES(device_mapper_path),
  property_map = VALUES(property_map),
  action_map = VALUES(action_map),
  event_map = VALUES(event_map),
  update_time = NOW();
