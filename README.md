# mte-prototype



+ 前端：demo-fronted
+ 后端：engine
  + 数据定义：engine/definition
    + 领域数据定义：SmartBuilding.do
    + 场景数据定义：BuildingA.sce
    + 应用数据定义：GuestReception.app
    + 流程数据定义：ConferenceService.proc
    + 设备数据定义：CoffeeMaker.json
  + 事件文件：engine/eventsFile
    + 事件定义：MakeCoffeeController.java
  + 设备功能封装：engine/libs
    + coffeeMakerA-0.0.1.jar
    + coffeeMakerB-0.0.1.jar
  + 工程文件：engine/src
    + 主工程：engine/src/main
      + java代码 : engine/src/main/java
        + 低代码demo : engine/src/main/java/demo.lowcode
          + 基本功能定义 : ./common
          + 咖啡机设备事件定义： ./device.coffeemaker
          + 设备功能定义：./engine
            + 业务：./business
            + 前端Api：./controller
            + 数据库：./dao
            + 前后端交互结构: ./dto
            + 数据实体定义: ./entity
            + ./liteflow
            + 模型类声明： ./model
            + ./util
            + EngineApplication.java
      + 资源：engine/src/main/resources
    + 测试工程: engine/src/test
      + java代码：engine/src/test/java
        + 低代码demo : engine/src/main/java/demo.lowcode
          + 咖啡机设备类定义：./device.coffeemaker
            + ./CoffeeMakerTest.java
          + ./engine
            + ./EngineApplicationTests.java
            + ./ProcessTest.java





