-- 本项目未集成数据库迁移框架；部署新版 Mapper 前，请在平台数据库手工执行本脚本。
UPDATE device_model
SET model = JSON_SET(
  model,
  '$.properties.configured', JSON_OBJECT(
    'type', 'boolean',
    'readOnly', true,
    'description', '百度人脸识别服务是否已完成配置'
  ),
  '$.actions.configure', JSON_OBJECT(
    'arguments', JSON_OBJECT(
      'apiKey', JSON_OBJECT(
        'type', 'string',
        'description', '百度智能云 API Key'
      ),
      'secretKey', JSON_OBJECT(
        'type', 'string',
        'description', '百度智能云 Secret Key'
      )
    ),
    'description', '配置百度人脸识别服务凭据'
  )
)
WHERE modelId = 'AICam';
