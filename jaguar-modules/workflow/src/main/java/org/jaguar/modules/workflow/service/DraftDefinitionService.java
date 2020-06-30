package org.jaguar.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.Charsets;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.modules.workflow.enums.DefinitionType;
import org.jaguar.modules.workflow.mapper.DraftDefinitionMapper;
import org.jaguar.modules.workflow.model.po.DraftDefinition;
import org.jaguar.modules.workflow.model.po.FormTemplate;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.util.Bpmn20Util;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@Service
public class DraftDefinitionService extends BaseService<DraftDefinition, DraftDefinitionMapper> {

    /**
     * 根据名称和类型查询最新版的草稿
     */
    private DraftDefinition getLatestBy(String name, DefinitionType definitionType) {
        LambdaQueryWrapper<DraftDefinition> wrapper = JaguarLambdaQueryWrapper.<DraftDefinition>newInstance()
                .eq(DraftDefinition::getName, name)
                .eq(DraftDefinition::getDefinitionType, definitionType)
                .orderByDesc(DraftDefinition::getVersion);
        List<DraftDefinition> records = this.list(wrapper);
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    /**
     * 查询最新版的草稿列表
     */
    public IPage<DraftDefinition> queryLatest(IPage<DraftDefinition> page, Map<String, Object> params) {
        List<DraftDefinition> draftDefinitions = mapper.queryLatest(page, params);
        return page.setRecords(draftDefinitions);
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

        this.insert(draftDefinition);
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
            context = new String(new BpmnXMLConverter().convertToXML(bpmnModel), Charsets.UTF_8_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        DraftDefinition latest = this.getLatestBy(flowDefinition.getName(), DefinitionType.FLOW);

        DraftDefinition draftDefinition = new DraftDefinition();
        draftDefinition.setName(flowDefinition.getName());
        draftDefinition.setDefinitionType(DefinitionType.FLOW);
        draftDefinition.setVersion(latest == null ? 1 : latest.getVersion() + 1);
        draftDefinition.setContext(context);

        this.insert(draftDefinition);
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
            xmlStreamReader = factory.createXMLStreamReader(
                    new ByteArrayInputStream(draftDefinition.getContext().getBytes(Charsets.UTF_8_NAME)));
        } catch (UnsupportedEncodingException | XMLStreamException e) {
            throw new RuntimeException(e);
        }

        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlStreamReader);
        return Bpmn20Util.parseFlow(bpmnModel);
    }

}
