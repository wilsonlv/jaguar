package org.jaguar.test.dubbo;

import org.apache.dubbo.config.annotation.Service;
import org.jaguar.support.dubbo.base.BaseProviderImpl;

/**
 * @author lvws
 * @since 2020/5/21
 */
@Service(interfaceClass = ISystemMgmProvider.class)
public class SystemMgmProvider extends BaseProviderImpl implements ISystemMgmProvider {

}
