{
  "domainId": "SmartMine",
  "domainName": "智慧矿山",
  "domainField":[
    {
        "fieldID": "mine",
        "fieldName": "矿井",
        "type": "String"
    },
    {
        "fieldID": "tunnel",
        "fieldName": "巷道",
        "type": "String"
    }
  ],
  "componentTypeList": [
    {
      "componentType": "Device",
      "componentAbout":[
        {
          "componentID": "PUMP",
          "componentName": "水泵"

        }
      ]
    },
    {
      "componentType": "UI",
      "componentAbout":[
        {
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
