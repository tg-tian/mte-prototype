{
  "scenarioId": "BuildingA",
  "scenarioName": "交叉二号楼",
  "domainId": "SmartBuilding",
  "map": [
    "mapPath": "map.img",
    "mapList":[
        {
            "name": "二层",
            "description": "交叉二号楼二楼",
            "planPath": "plan.img",
        },
    ],
  ],
  "devices": [
    {
      "deviceId": "deviceId",
      "deviceName": "咖啡机器人A",
      "mainObject": {
        "deviceType": "CoffeeMaker",
        "service": {
          "name": "AService",
          "protocol": "HTTP",
          "uri": "http://aservice.coffee",
          "port": "8080",
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
          "protocol": "HTTP",
          "uri": "http://bservice.coffee",
          "port": "8080",
        }
      }
    },
  ]
}
