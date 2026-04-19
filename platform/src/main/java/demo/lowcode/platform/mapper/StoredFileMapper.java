package demo.lowcode.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.lowcode.platform.entity.StoredFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoredFileMapper extends BaseMapper<StoredFile> {
}
