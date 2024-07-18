{
  "scenarioId": "BuildingA",
  "scenarioName": "交叉二号楼",
  "domainId": "SmartBuilding",
  "map": [
    {
      "name": "房间号",
      "value": "D2009"
    },
    {
      "name": "楼层",
      "value": "2"
    }
  ],
  "devices": [
    {
      "deviceId": "deviceId",
      "deviceName": "咖啡机器人A",
      "mainObject": {
        "deviceType": "CoffeeMaker",
        "service": {
          "name": "AService",
          "uri": "http://xx.xx.xx.xx"
        }
      }
    },
    {
      "deviceId": "deviceId2",
      "deviceName": "咖啡机器人B",
      "mainObject": {
        "deviceType": "CoffeeMaker",
        "service": {
          "name": "BService",
          "uri": "http://10.176.34.85:2000?action=",
        }
      }
    }
  ]
}
