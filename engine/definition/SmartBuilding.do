{
  "domainId": "SmartBuilding",
  "domainName": "智慧楼宇",
  "domainField":[
    {
        "fieldID": "001",
        "fieldName": "楼层",
        "type": "String"
    },
    {
        "fieldID": "002",
        "fieldName": "描述",
        "type": "String"
    },
    {
        "fieldID": "003",
        "fieldName": "平面图",
        "type": "Img"
    }
  ],
  "componentTypeList": [
    {
      "componentType": "Device",
      "componentAbout":[
        {
          "componentID": "CoffeeMaker",
          "componentName": "咖啡机"

        },{
          "componentID":"AirConditioner",
          "componentName": "空调"
        }
      ]
    },
    {
      "componentType": "UI",
      "componentAbout":[
        {
          "componentID": "001",
          "componentName": "场景化导航",
          "imgPath":"guide.png"
        },{
          "componentID":"002",
          "componentName": "柱状图",
          "imgPath":"bar.png"
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
