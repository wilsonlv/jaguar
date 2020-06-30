package org.jaguar.modules.workflow.interfaces;

/**
 * @author lvws
 * @since 2019/4/27.
 */
public interface IUserDefinedComponent {

    String getFieldKey();

    void persist(Long processInfoId, Long formDataId, String value);

    String read(Long formDataId);

}
