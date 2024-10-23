{
  "domainId": "SmartBuilding",
  "domainName": "智慧楼宇",
  "domainField":[
    {
        "fieldID": "floor",
        "fieldName": "楼层",
        "type": "String"
    },
    {
        "fieldID": "description",
        "fieldName": "描述",
        "type": "String"
    },
    {
        "fieldID": "planPath",
        "fieldName": "平面图",
        "type": "Image"
    }
  ],
  "componentTypeList": [
    {
      "componentType": "Device",
      "componentAbout":[
        {
          "componentID": "CoffeeMaker",
          "componentName": "咖啡机器人"
        }
      ]
    },
    {
      "componentType": "UI",
      "componentAbout":[
        {
          "componentID": "001",
          "componentName": "场景化导航",
          "imgPath":"guide.svg"
        },{
          "componentID":"002",
          "componentName": "柱状图",
          "imgPath":"bar.svg"
        }
      ]
    },
    {
      "componentType": "Process",
      "componentAbout":[
        {
          "componentID": "001",
          "componentName": "预约流程",
          "componentBrief": "此流程用于各类预约系统，可以实现预约时间选择、预约队列管理等"
        }
      ]
    }
  ]
}
