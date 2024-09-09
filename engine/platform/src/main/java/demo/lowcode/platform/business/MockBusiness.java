package demo.lowcode.platform.business;

import demo.lowcode.platform.dto.MockDeviceInformation;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class MockBusiness {
    private String status = "空闲";
    private String currentOperation = "";

    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
//        System.out.println("当前操作："+currentOperation);
    }

    public void setStatus(String status) {
        this.status = status;
//        System.out.println("当前状态："+status);
    }

    public MockDeviceInformation getMockData(){
        return new MockDeviceInformation(status, currentOperation);
    }
}
