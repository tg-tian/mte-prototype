{
  "applicationId": "GuestReception",
  "applicationName": "来访接待",
  "scenarioId": "BuildingA",
  "processes": [
    {
      "processId": "EventRegistration",
      "processName": "活动申报",
      "parentProcessId": "",
      "actionList": []
    },
    {
      "processId": "ConferenceService",
      "processName": "会议服务",
      "parentProcessId": "EventRegistration",
      "actionList": [
        {
          "actionId": "start",
          "actionName": "开始",
          "type": "Default",
          "parentActionId": "",
          "objectId": "",
          "execParam": "",
          "inputParam": "",
          "condition": ""
        },
        {
          "actionId": "makeCoffee",
          "actionName": "制作咖啡",
          "type": "Device",
          "parentActionId": "start",
          "objectId": "CoffeeMaker",
          "execParam": "makeCoffee",
          "inputParam": "",
          "outputParam": "",
          "condition": ""
        },
        {
          "actionId": "check",
          "actionName": "检查",
          "type": "Device",
          "parentActionId": "makeCoffee",
          "objectId": "CoffeeMaker",
          "execParam": "check",
          "inputParam": "${parent.outputParam}",
          "condition": "${parent.outputParam} == 1",
          "outputParam": ""
        }
      ]
    }
  ]
}
