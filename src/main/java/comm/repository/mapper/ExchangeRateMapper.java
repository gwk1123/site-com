package comm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import comm.repository.entity.ExchangeRate;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 实时外汇数据 服务类
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Repository
public interface ExchangeRateMapper extends BaseMapper<ExchangeRate> {

}
