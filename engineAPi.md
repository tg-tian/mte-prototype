# Engine_Domain_API

+ 领域业务（./engine/src/main/java/demo/lowcode/engine/business/**DomainBusiness.java**）

  + **领域信息传输**

    + 函数声明定义

      + ```java
        public DomainJson loadJson() throws IOException {}
        ```

      + 输入:无

      + 输出：DomainJson

    + 领域字段json定义

      + ```java
        public class DomainJson {
            private String domainID;
            private  String domainName;
            private Map<String, List<String>> domainField;
            }
        ```

      + 字段介绍：
        + domainID:存储领域ID
        + domainName:存储领域名称
        + domainField:存储领域字段
      + 

  + **领域组件传输**

    + 领域组件字段定义

      + ```java
        public class Domain_ComponentJson {
            private String componentType;
            private Map<String,List<String>> componentAbout;
        }

      + 字段介绍：

        + componentType: 组件类型
        + componentAbout: 组件信息

      + 1

      + 

