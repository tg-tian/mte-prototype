package demo.lowcode.platform.business;

import demo.lowcode.platform.entity.Binding;
import demo.lowcode.platform.mapper.BindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BindingBusiness {
    private final BindingMapper bindingMapper;

    @Autowired
    public BindingBusiness(BindingMapper bindingMapper) {this.bindingMapper = bindingMapper;}


    public void uploadBindingData(Binding binding) throws IOException{
        int count = bindingMapper.countByDeviceTypeId(binding.getDeviceTypeId());
        if(count == 0){
            bindingMapper.insert(binding);
        }else{
            throw new RuntimeException("deviceTypeId已存在，不能插入重复的记录");
        }
    }
}
