package com.jaguar.process.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.process.mapper.DraftDefinitionMapper;
import com.jaguar.process.model.po.DraftDefinition;
import com.jaguar.process.model.po.FormTemplate;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.enums.DefinitionType;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.process.util.Bpmn20Util;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@Service
@CacheConfig(cacheNames = "DraftDefinition")
public class DraftDefinitionService extends BaseService<DraftDefinition, DraftDefinitionMapper> {

    /**
     * 根据名称和类型查询最新版的草稿
     */
    private DraftDefinition getLatestBy(String name, DefinitionType definitionType) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, 1);
        param.put(ROWS, 1);
        param.put(SORT, "version_");
        param.put(ORDER, OrderType.DESC);
        param.put("name", name);
        param.put("definitionType", definitionType.toString());
        List<DraftDefinition> records = this.query(param).getRecords();
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    /**
     * 查询最新版的草稿列表
     */
    public Page<DraftDefinition> queryLatest(Map<String, Object> param) {
        Page<Long> page = getPage(param);
        page.setRecords(mapper.queryLatest(page, param));
        return getPage(page);
    }


    /**
     * 保存表单草稿
     */
    public void saveForm(FormTemplate formTemplate) {
        DraftDefinition latest = this.getLatestBy(formTemplate.getName(), DefinitionType.FORM);

        DraftDefinition draftDefinition = new DraftDefinition();
        draftDefinition.setElementId(formTemplate.getElementId());
        draftDefinition.setName(formTemplate.getName());
        draftDefinition.setDefinitionType(DefinitionType.FORM);
        draftDefinition.setVersion(latest == null ? 1 : latest.getVersion() + 1);
        draftDefinition.setContext(JSONObject.toJSONString(formTemplate));

        this.update(draftDefinition);
    }

    /**
     * 读取表单草稿
     */
    public FormTemplate getForm(Long id) {
        DraftDefinition draftDefinition = this.getById(id);
        Assert.validateId(draftDefinition, "草稿", id);

        return JSONObject.parseObject(draftDefinition.getContext(), FormTemplate.class);
    }


    /**
     * 保存流程草稿
     */
    public void saveFlow(FlowDefinition flowDefinition) {
        BpmnModel bpmnModel = Bpmn20Util.convertBPMN(flowDefinition);
        String context;
        try {
            context = new String(new BpmnXMLConverter().convertToXML(bpmnModel), CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        DraftDefinition latest = this.getLatestBy(flowDefinition.getName(), DefinitionType.FLOW);

        DraftDefinition draftDefinition = new DraftDefinition();
        draftDefinition.setName(flowDefinition.getName());
        draftDefinition.setDefinitionType(DefinitionType.FLOW);
        draftDefinition.setVersion(latest == null ? 1 : latest.getVersion() + 1);
        draftDefinition.setContext(context);
        draftDefinition.setCreateTime(new Date());

        this.update(draftDefinition);
    }

    /**
     * 读取流程草稿
     */
    public FlowDefinition getFlow(Long id) {
        DraftDefinition draftDefinition = this.getById(id);
        Assert.validateId(draftDefinition, "草稿", id);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader;
        try {
            xmlStreamReader = factory.createXMLStreamReader(new ByteArrayInputStream(draftDefinition.getContext().getBytes(CHARSET)));
        } catch (UnsupportedEncodingException | XMLStreamException e) {
            throw new RuntimeException(e);
        }

        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlStreamReader);
        return Bpmn20Util.parseFlow(bpmnModel);
    }

}
