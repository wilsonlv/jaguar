package org.jaguar.commons.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * @author lvws
 * @since 2019/3/15.
 */
public class XmlUtil {

    /**
     * 将javaBean转换为xml对象
     */
    public static String parseBeanToXml(Class<?> clazz, Object bean) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter sw = new StringWriter();
            //该值默认为false,true则不会创建即头信息,即<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            jaxbMarshaller.marshal(bean, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
