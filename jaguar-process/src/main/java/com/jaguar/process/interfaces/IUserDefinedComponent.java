package com.jaguar.process.interfaces;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IUserDefinedComponent {

    String getFieldKey();

    void persist(Long formDataId, String value);

    String read(Long formDataId);

}
