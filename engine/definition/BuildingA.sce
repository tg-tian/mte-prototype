{
  "scenarioId": "BuildingA",
  "scenarioName": "交叉二号楼",
  "domainId": "smartBuilding",
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
        "serviceType": ["AService"],
        "propertyMap": [],
        "eventPath": "MakeCoffeeController.java",
        "eventMap": [
          {
            "name": "onMakeCoffeeStart",
            "value": "prepare"
          },
          {
            "name": "onMakeCoffeeComplete",
            "value": "sendMessage"
          },
          {
            "name": "onCheckError",
            "value": "errorAlert"
          }
        ]
      }
    },
    {
      "deviceId": "deviceId2",
      "deviceName": "咖啡机器人B",
      "mainObject": {
        "deviceType": "CoffeeMaker",
        "serviceType": ["BService"],
        "propertyMap": [],
        "eventPath": "MakeCoffeeController.java",
        "eventMap": [
          {
            "name": "onMakeCoffeeStart",
            "value": "prepare"
          },
          {
            "name": "onMakeCoffeeComplete",
            "value": "sendMessage"
          },
          {
            "name": "onCheckError",
            "value": "errorAlert"
          }
        ]
      }
    }
  ]
}
