package org.jaguar.commons.mybatisplus.extension;

import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author lvws
 * @since 2019/5/14.
 */
@Slf4j
public class MybatisMapperRefresh implements Runnable {
    /**
     * 记录jar包存在的mapper
     */
    private static final Map<String, List<Resource>> JAR_MAPPER = new HashMap<>();
    private final SqlSessionFactory sqlSessionFactory;
    private final Resource[] mapperLocations;
    private Long beforeTime = 0L;
    private Configuration configuration;
    /**
     * 是否开启刷新mapper
     */
    private final boolean enabled;
    /**
     * xml文件目录
     */
    private Set<String> fileSet;
    /**
     * 延迟加载时间
     */
    private int delaySeconds = 10;
    /**
     * 刷新间隔时间
     */
    private int sleepSeconds = 20;

    public MybatisMapperRefresh(Resource[] mapperLocations, SqlSessionFactory sqlSessionFactory, int delaySeconds,
                                int sleepSeconds, boolean enabled) {
        this.mapperLocations = mapperLocations.clone();
        this.sqlSessionFactory = sqlSessionFactory;
        this.delaySeconds = delaySeconds;
        this.enabled = enabled;
        this.sleepSeconds = sleepSeconds;
        this.configuration = sqlSessionFactory.getConfiguration();
        this.run();
    }

    public MybatisMapperRefresh(Resource[] mapperLocations, SqlSessionFactory sqlSessionFactory, boolean enabled) {
        this.mapperLocations = mapperLocations.clone();
        this.sqlSessionFactory = sqlSessionFactory;
        this.enabled = enabled;
        this.configuration = sqlSessionFactory.getConfiguration();
        this.run();
    }

    @Override
    public void run() {
        GlobalConfigUtils.getGlobalConfig(configuration);

        /*
         * 启动 XML 热加载
         */
        if (enabled) {
            beforeTime = SystemClock.now();
            final MybatisMapperRefresh runnable = this;
            new Thread(() -> {
                if (fileSet == null) {
                    fileSet = new HashSet<>();
                    if (mapperLocations != null) {
                        for (Resource mapperLocation : mapperLocations) {
                            try {
                                if (ResourceUtils.isJarURL(mapperLocation.getURL())) {
                                    String key = new UrlResource(ResourceUtils.extractJarFileURL(mapperLocation.getURL()))
                                            .getFile().getPath();
                                    fileSet.add(key);
                                    if (JAR_MAPPER.get(key) != null) {
                                        JAR_MAPPER.get(key).add(mapperLocation);
                                    } else {
                                        List<Resource> resourcesList = new ArrayList<>();
                                        resourcesList.add(mapperLocation);
                                        JAR_MAPPER.put(key, resourcesList);
                                    }
                                } else {
                                    fileSet.add(mapperLocation.getFile().getPath());
                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(delaySeconds * 1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                do {
                    try {
                        long tempBeforeTime = beforeTime;
                        for (String filePath : fileSet) {
                            File file = new File(filePath);
                            if (file.isFile() && file.lastModified() > tempBeforeTime) {
                                List<Resource> removeList = JAR_MAPPER.get(filePath);
                                if (removeList != null && !removeList.isEmpty()) {
                                    for (Resource resource : removeList) {
                                        runnable.refresh(resource);
                                    }
                                } else {
                                    runnable.refresh(new FileSystemResource(file));
                                }
                            }
                            if (beforeTime < file.lastModified()) {
                                beforeTime = file.lastModified();
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    try {
                        Thread.sleep(sleepSeconds * 1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }

                } while (true);
            }, "mybatis-plus MapperRefresh").start();
        }
    }

    /**
     * 刷新mapper
     */
    @SuppressWarnings("rawtypes")
    private void refresh(Resource resource) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        this.configuration = sqlSessionFactory.getConfiguration();
        boolean isSupper = configuration.getClass().getSuperclass() == Configuration.class;
        try {
            Field loadedResourcesField = isSupper ? configuration.getClass().getSuperclass().getDeclaredField("loadedResources")
                    : configuration.getClass().getDeclaredField("loadedResources");
            loadedResourcesField.setAccessible(true);
            Set loadedResourcesSet = ((Set) loadedResourcesField.get(configuration));
            XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(),
                    new XMLMapperEntityResolver());
            XNode context = xPathParser.evalNode("/mapper");
            String namespace = context.getStringAttribute("namespace");
            Field field = MapperRegistry.class.getDeclaredField("knownMappers");
            field.setAccessible(true);
            Map mapConfig = (Map) field.get(configuration.getMapperRegistry());

            mapConfig.remove(Resources.classForName(namespace));
            loadedResourcesSet.remove(resource.toString());
            configuration.getCacheNames().remove(namespace);

            cleanParameterMap(context.evalNodes("/mapper/parameterMap"), namespace);
            cleanResultMap(context.evalNodes("/mapper/resultMap"), namespace);
            cleanKeyGenerators(context.evalNodes("insert|update|select"), namespace);
            cleanSqlElement(context.evalNodes("/mapper/sql"), namespace);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(),
                    sqlSessionFactory.getConfiguration(),
                    resource.toString(), sqlSessionFactory.getConfiguration().getSqlFragments());
            xmlMapperBuilder.parse();
            log.debug("refresh: '" + resource + "', success!");
        } catch (IOException e) {
            log.error("Refresh IOException :" + e.getMessage());
        } finally {
            ErrorContext.instance().reset();
        }
    }

    /**
     * 清理parameterMap
     */
    private void cleanParameterMap(List<XNode> list, String namespace) {
        for (XNode parameterMapNode : list) {
            String id = parameterMapNode.getStringAttribute("id");
            configuration.getParameterMaps().remove(namespace + "." + id);
        }
    }

    /**
     * 清理resultMap
     */
    private void cleanResultMap(List<XNode> list, String namespace) {
        for (XNode resultMapNode : list) {
            String id = resultMapNode.getStringAttribute("id", resultMapNode.getValueBasedIdentifier());
            configuration.getResultMapNames().remove(id);
            configuration.getResultMapNames().remove(namespace + "." + id);
            clearResultMap(resultMapNode, namespace);
        }
    }

    private void clearResultMap(XNode xNode, String namespace) {
        for (XNode resultChild : xNode.getChildren()) {
            if ("association".equals(resultChild.getName()) || "collection".equals(resultChild.getName())
                    || "case".equals(resultChild.getName())) {
                if (resultChild.getStringAttribute("select") == null) {
                    configuration.getResultMapNames().remove(
                            resultChild.getStringAttribute("id", resultChild.getValueBasedIdentifier()));
                    configuration.getResultMapNames().remove(
                            namespace + "." + resultChild.getStringAttribute("id", resultChild.getValueBasedIdentifier()));
                    if (resultChild.getChildren() != null && !resultChild.getChildren().isEmpty()) {
                        clearResultMap(resultChild, namespace);
                    }
                }
            }
        }
    }

    /**
     * 清理selectKey
     */
    private void cleanKeyGenerators(List<XNode> list, String namespace) {
        for (XNode context : list) {
            String id = context.getStringAttribute("id");
            configuration.getKeyGeneratorNames().remove(id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
            configuration.getKeyGeneratorNames().remove(namespace + "." + id + SelectKeyGenerator.SELECT_KEY_SUFFIX);

            Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();
            List<MappedStatement> objects = new ArrayList<>();
            for (Object mappedStatement : mappedStatements) {
                if (mappedStatement instanceof MappedStatement) {
                    MappedStatement statement = (MappedStatement) mappedStatement;
                    if (statement.getId().equals(namespace + "." + id)) {
                        objects.add(statement);
                    }
                }
            }
            mappedStatements.removeAll(objects);
        }
    }

    /**
     * 清理sql节点缓存
     */
    private void cleanSqlElement(List<XNode> list, String namespace) {
        for (XNode context : list) {
            String id = context.getStringAttribute("id");
            configuration.getSqlFragments().remove(id);
            configuration.getSqlFragments().remove(namespace + "." + id);
        }
    }
}
