package org.jaguar.support.dubbo.base;

import org.apache.dubbo.config.annotation.Reference;
import org.jaguar.core.base.BaseController;

/**
 * @author lvws
 * @since 2020/6/4
 */
public class AbstractDubboSupportController<T extends IBaseProvider> extends BaseController {

    @Reference(check = false, version = IBaseProvider.VERSION)
    protected T provider;

}
