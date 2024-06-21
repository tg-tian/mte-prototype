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
        "serviceType": "AService",
        "propertyMap": [],
        "eventPath": "C:\\Users\\22435\\Desktop\\设备元建模\\demo\\engine\\eventsFile\\MakeCoffeeController.java",
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
