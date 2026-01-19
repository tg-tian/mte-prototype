{
  "scenarioId": "PhysicalBuilding",
  "scenarioName": "物理楼",
  "domainId": "SmartBuilding",
  "map": {
    "mapPath": "map.png",
    "mapList": [
      {
        "floor": "二层",
        "description": "物理楼二楼",
        "planPath": "floor.jpeg"
      }
    ]
  },
  "devices": [
    {
      "deviceId": "deviceId",
      "deviceName": "咖啡机器人A",
      "deviceType": "CoffeeMaker",
      "service": {
        "code": "AService",
        "name": "A品牌",
        "protocol": "HTTP",
        "uri": "http://aservice.coffee",
        "port": "8080"
        }
    },
    {
      "deviceId": "deviceId2",
      "deviceName": "咖啡机器人B",
      "deviceType": "CoffeeMaker",
        "service": {
          "code": "BService",
          "name": "B品牌",
          "protocol": "HTTP",
          "uri": "http://bservice.coffee",
          "port": "8080"
      }
    }
  ]
}
