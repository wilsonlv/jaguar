package org.jaguar.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.jaguar.commons.aviator.ExpressionUtil;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.document.model.Document;
import org.jaguar.modules.workflow.Constant;
import org.jaguar.modules.workflow.enums.FormDataPersistenceType;
import org.jaguar.modules.workflow.enums.FormTemplateFieldType;
import org.jaguar.modules.workflow.enums.TaskFieldPermission;
import org.jaguar.modules.workflow.interfaces.IUserDefinedComponent;
import org.jaguar.modules.workflow.mapper.FormDataMapper;
import org.jaguar.modules.workflow.model.po.*;
import org.jaguar.modules.workflow.model.vo.UserTask;
import org.jaguar.modules.workflow.model.vo.component.AbstractComponentConfig;
import org.jaguar.modules.workflow.model.vo.component.UserDefinedConfigAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单数据表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Slf4j
@Service
public class FormDataService extends BaseService<FormData, FormDataMapper> {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessInfoService processInfoService;
    @Autowired
    private FormDataAttachService formDataAttachService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    /**
     * 根据工单信息ID和表单字段key查询表单数据
     */
    public FormData getByProcessInfoIdAndFieldKey(Long processInfoId, String fieldKey) {
        return this.getByProcessInfoIdAndFieldKey(processInfoId, fieldKey, 0);
    }

    /**
     * 根据工单信息ID和表单字段key查询最新得表单数据
     */
    public FormData getLatestByProcessInfoIdAndFieldKey(Long processInfoId, String fieldKey) {
        @SuppressWarnings("unchecked") int count = this.list(JaguarLambdaQueryWrapper.<FormData>newInstance()
                .eq(FormData::getProcessInfoId, processInfoId)
                .eq(FormData::getFieldKey, fieldKey)
                .groupBy(FormData::getBatchNum)).size();

        JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormData::getProcessInfoId, processInfoId)
                .eq(FormData::getFieldKey, fieldKey)
                .eq(FormData::getBatchNum, count - 1);
        return this.unique(wrapper);
    }

    /**
     * 根据工单信息ID、表单字段key和批次号查询表单数据
     */
    public FormData getByProcessInfoIdAndFieldKey(Long processInfoId, String fieldKey, Integer batachNum) {
        JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormData::getProcessInfoId, processInfoId)
                .eq(FormData::getFieldKey, fieldKey)
                .eq(FormData::getBatchNum, batachNum);
        return this.unique(wrapper);
    }

    /**
     * 根据工单信息ID和表单字段ID查询表单数据
     */
    public FormData getByProcessInfoIdAndFieldId(Long processInfoId, Long fieldId) {
        return this.getByProcessInfoIdAndFieldId(processInfoId, fieldId, 0);
    }

    /**
     * 根据工单信息ID、表单字段ID和批次号查询表单数据
     */
    public FormData getByProcessInfoIdAndFieldId(Long processInfoId, Long fieldId, Integer batchNum) {
        JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormData::getProcessInfoId, processInfoId)
                .eq(FormData::getFormTemplateFieldId, fieldId)
                .eq(FormData::getBatchNum, batchNum);
        return this.unique(wrapper);
    }

    /**
     * 根据工单信息ID查询表单数据
     */
    public List<FormData> listByProcessInfoId(Long processInfoId) {
        return this.list(JaguarLambdaQueryWrapper.<FormData>newInstance()
                .eq(FormData::getProcessInfoId, processInfoId));
    }

    /**
     * 根据工单信息ID查询表单数据
     */
    public Map<String, String> listMapByProcessInfoId(Long processInfoId) {
        List<FormData> formDataList = this.listByProcessInfoId(processInfoId);

        Map<String, String> formDataMap = new HashMap<>(formDataList.size());
        for (FormData formData : formDataList) {
            formDataMap.put(formData.getFieldKey(), formData.getValue());
        }
        return formDataMap;
    }

    /**
     * 校验表单数据并保存
     * <p>
     * 保存的数据分两种：
     * 1、权限为可写或必填，
     * 2、有结果值的
     */
    @Transactional
    public void saveFormData(ProcessInfo processInfo, UserTask userTask, FormTemplateSheet sheet,
                             JSONObject formData, boolean submit) {

        //先保存权限为可写或必填的表单数据（结果值的生成可能对此有依赖关系）
        List<FormTemplateField> resultValueFields = new ArrayList<>();
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                TaskFieldPermission taskFormPermission = userTask.getTaskFormPermissionMap().get(field.getKey());
                if (taskFormPermission == null) {
                    continue;
                }

                String value = formData.getString(field.getKey());
                if (taskFormPermission == TaskFieldPermission.REQUIRED) {
                    if (submit && !field.getFormTemplateFieldType().equals(FormTemplateFieldType.userDefined) && StringUtils.isBlank(value)) {
                        throw new CheckedException("【" + field.getKey() + "】为必填字段！");
                    }
                }

                createFormData(processInfo, field, value, sheet.getOverride());

                //提交的数据的优先级大于结果值
                if (StringUtils.isNotBlank(field.getResultValue()) && StringUtils.isBlank(value)) {
                    resultValueFields.add(field);
                }
            }
        }

        //在保存有结果值的字段
        Map<String, Object> variables = runtimeService.getVariables(processInfo.getProcessInstanceId());
        for (FormTemplateField field : resultValueFields) {
            if (StringUtils.isEmpty((String) field.getValue())) {
                String value = ExpressionUtil.execute(field.getResultValue(), variables).toString();
                createFormData(processInfo, field, value, sheet.getOverride());
            }
        }
    }

    /**
     * 对单一表单字段的保存
     */
    @Transactional
    public void saveFormData(ProcessInfo processInfo, UserTask userTask, FormTemplateField field, String value) {
        TaskFieldPermission taskFormPermission = userTask.getTaskFormPermissionMap().get(field.getKey());
        if (taskFormPermission == null) {
            throw new CheckedException("没有在【" + userTask.getName() + "】中找到【" + field.getKey() + "】的字段权限！");
        }

        if (taskFormPermission == TaskFieldPermission.REQUIRED) {
            if (StringUtils.isBlank(value)) {
                throw new CheckedException("【" + field.getKey() + "】为必填字段！");
            }
        }
        createFormData(processInfo, field, value, true);
    }


    /**
     * 附件上传
     */
    @Transactional
    public List<FormDataAttach> upload(ProcessInfo processInfo, FormTemplateField field, Integer batchNum, List<Document> documentList) {
        LambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.<FormData>newInstance()
                .eq(FormData::getProcessInfoId, processInfo.getId())
                .eq(FormData::getFormTemplateFieldId, field.getId())
                .eq(FormData::getBatchNum, batchNum);
        FormData formData = this.unique(wrapper);
        if (formData == null) {
            formData = new FormData();
            formData.setProcessInfoId(processInfo.getId());
            formData.setFormTemplateId(field.getFormTemplateId());
            formData.setFormTemplateSheetId(field.getFormTemplateSheetId());
            formData.setFormTemplateFieldId(field.getId());
            formData.setFieldKey(field.getKey());
            formData.setFormDataPersistenceType(FormDataPersistenceType.FORM_DATA_ATTACH);
            formData.setBatchNum(batchNum);
            formData = this.saveOrUpdate(formData);
        }

        List<FormDataAttach> formDataAttachList = new ArrayList<>();

        //附件上传存储在FormDataAttach表中
        for (Document document : documentList) {
            FormDataAttach formDataAttach = new FormDataAttach();
            formDataAttach.setProcessInfoId(processInfo.getId());
            formDataAttach.setFormDataId(formData.getId());
            formDataAttach.setDocumentId(document.getId());
            formDataAttach = formDataAttachService.insert(formDataAttach);

            formDataAttachList.add(formDataAttach);
        }
        return formDataAttachList;
    }

    /**
     * 保存表单数据
     */
    private FormData createFormData(ProcessInfo processInfo, FormTemplateField field, String value, boolean override) {
        if (field.getFormTemplateFieldType() == FormTemplateFieldType.fileUpload) {
            return null;
        }

        field.setValue(value);

        if (field.getUnique()) {
            String processDefinitionName = processInfo.getProcessDefinitionId().split(":")[0];
            if (processInfoService.checkUnique(processDefinitionName, field.getKey(), value, processInfo.getId())) {
                throw new CheckedException("【" + field.getLabel() + "】重复");
            }
        }

        JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormData::getProcessInfoId, processInfo.getId())
                .eq(FormData::getFormTemplateFieldId, field.getId())
                .orderByDesc(FormData::getBatchNum);
        List<FormData> formDatas = this.query(new Page<>(1, 1), wrapper).getRecords();

        FormData formData;
        if (formDatas.size() == 0) {
            //如果还没有提交过字段结果，则创建新的
            formData = new FormData();
            formData.setProcessInfoId(processInfo.getId());
            formData.setFormTemplateId(field.getFormTemplateId());
            formData.setFormTemplateSheetId(field.getFormTemplateSheetId());
            formData.setFormTemplateFieldId(field.getId());
            formData.setFieldKey(field.getKey());
            formData.setBatchNum(0);
        } else if (override) {
            //如果已经提交过字段结果：数据覆盖，则使用原来的对象
            formData = formDatas.get(0);
        } else {
            //如果已经提交过字段结果，并且数据不覆盖，则创建新的，并递增批次号
            formData = new FormData();
            formData.setProcessInfoId(processInfo.getId());
            formData.setFormTemplateId(field.getFormTemplateId());
            formData.setFormTemplateSheetId(field.getFormTemplateSheetId());
            formData.setFormTemplateFieldId(field.getId());
            formData.setFieldKey(field.getKey());
            formData.setBatchNum(formDatas.get(0).getBatchNum() + 1);
        }

        if (field.getFormTemplateFieldType() == FormTemplateFieldType.userDefined) {
            //自定义组件由配置来实现持久化

            UserDefinedConfigAbstract userDefinedConfig = AbstractComponentConfig.resovleComponent(field.getComponentConfig(), UserDefinedConfigAbstract.class);
            //如果没有组件配置，则不需要管理
            if (userDefinedConfig == null) {
                return null;
            } else if (userDefinedConfig.getFormDataPersistenceType() == null && StringUtils.isBlank(userDefinedConfig.getComponentClassName())) {
                return null;
            }

            if (FormDataPersistenceType.USER_DEFINED.equals(userDefinedConfig.getFormDataPersistenceType())
                    || StringUtils.isNotBlank(userDefinedConfig.getComponentClassName())) {
                //如果持久化方式为USER_DEFINED或配置了组件类，组使用自定义存储

                formData.setFormDataPersistenceType(FormDataPersistenceType.USER_DEFINED);
                formData = this.saveOrUpdate(formData);
                userDefinedConfig.getBean().persist(processInfo.getId(), formData.getId(), value);
                return formData;
            } else if (FormDataPersistenceType.FORM_DATA_ATTACH.equals(userDefinedConfig.getFormDataPersistenceType())) {
                formData.setFormDataPersistenceType(FormDataPersistenceType.FORM_DATA_ATTACH);
                formData = this.saveOrUpdate(formData);

                List<FormDataAttach> formDataAttachList = formDataAttachService.getByFormDataId(formData.getId());
                FormDataAttach formDataAttach;
                if (formDataAttachList.size() == 0) {
                    formDataAttach = new FormDataAttach();
                    formDataAttach.setFormDataId(formData.getId());
                } else {
                    formDataAttach = formDataAttachList.get(0);
                }

                formDataAttach.setValue(value);
                formDataAttachService.saveOrUpdate(formDataAttach);
                return formData;
            }
            //如果持久化方式为VALUE，则使用值存储
        }

        switch (field.getKey()) {
            case Constant.PROCESS_NUM: {
                processInfo.setProcessNum(value);
                runtimeService.setVariable(processInfo.getProcessInstanceId(), Constant.PROCESS_NUM, value);
                break;
            }
            case Constant.PROCESS_TITLE: {
                processInfo.setTitle(value);
                runtimeService.setVariable(processInfo.getProcessInstanceId(), Constant.PROCESS_TITLE, value);
                break;
            }
            case Constant.PROCESS_PRIORITY: {
                processInfo.setPriority(Integer.parseInt(value));
                runtimeService.setVariable(processInfo.getProcessInstanceId(), Constant.PROCESS_PRIORITY, value);
                break;
            }
            default:
        }

        formData.setFormDataPersistenceType(FormDataPersistenceType.VALUE);
        formData.setValue(value);
        formData = this.saveOrUpdate(formData);

        runtimeService.setVariable(processInfo.getProcessInstanceId(), Constant.PROCESS_FORM_VARIABLE_PRE + field.getKey(), value);
        return formData;
    }

    /**
     * 填充表单数据
     */
    public void fillFormData(Long processInfoId, FormTemplateSheet sheet) {
        this.fillFormData(processInfoId, sheet, 0);
    }

    /**
     * 为扩展块填充表单数据
     */
    public List<FormTemplateSheet> fillFormDataForExtendSheet(Long processInfoId, FormTemplateSheet sheet) {

        @SuppressWarnings("unchecked") int count = this.list(JaguarLambdaQueryWrapper.<FormData>newInstance()
                .eq(FormData::getProcessInfoId, processInfoId)
                .eq(FormData::getFormTemplateSheetId, sheet.getId())
                .groupBy(FormData::getBatchNum)).size();

        List<FormTemplateSheet> sheets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FormTemplateSheet clone;
            try {
                clone = sheet.clone();
            } catch (CloneNotSupportedException e) {
                throw new CheckedException(e);
            }
            this.fillFormData(processInfoId, clone, i);
            sheets.add(clone);
        }
        return sheets;
    }

    /**
     * 填充表单数据
     */
    private void fillFormData(Long processInfoId, FormTemplateSheet sheet, int batchNum) {
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                if (!sheet.getOverride()) {
                    field.setKey(field.getKey() + batchNum);
                }

                FormData formData = this.getByProcessInfoIdAndFieldId(processInfoId, field.getId(), batchNum);
                if (formData == null) {
                    continue;
                }

                switch (formData.getFormDataPersistenceType()) {
                    case VALUE: {
                        field.setValue(formData.getValue());
                        continue;
                    }
                    case FORM_DATA_ATTACH: {
                        List<FormDataAttach> formDataAttaches = formDataAttachService.getByFormDataId(formData.getId());
                        field.setValue(formDataAttaches);
                        continue;
                    }
                    case USER_DEFINED: {
                        UserDefinedConfigAbstract userDefinedConfig = AbstractComponentConfig.resovleComponent(field.getComponentConfig(), UserDefinedConfigAbstract.class);
                        IUserDefinedComponent component = userDefinedConfig.getBean();
                        if (component != null) {
                            String value = component.read(formData.getId());
                            field.setValue(value);
                        } else {
                            field.setValue(formData.getValue());
                        }
                        continue;
                    }
                    default: {
                        throw new CheckedException("无效的数据存储类型：" + formData.getFormDataPersistenceType());
                    }
                }

            }
        }
    }

    public FormData getByKeyAndValueInProcessDefinition(@NotBlank String processDefinitionName, @NotBlank String key, @NotBlank String value) {
        return this.mapper.getByKeyAndValueInProcessDefinition(processDefinitionName, key, value);
    }

    public Page<FormData> queryOverride(Page<FormData> page, Long processInfoId, String fuzzyFieldLabel, String fuzzyFieldKey) {
        return this.mapper.queryOverride(page, processInfoId, fuzzyFieldLabel, fuzzyFieldKey);
    }

    @Transactional
    public FormData modifyValue(Long id, String value) {
        FormData formData = this.getById(id);
        Assert.validateId(formData, "表单数据", id);

        ProcessInfo processInfo = processInfoService.getById(formData.getProcessInfoId());
        FormTemplateField field = formTemplateFieldService.getById(formData.getFormTemplateFieldId());
        formData = this.createFormData(processInfo, field, value, true);

        processInfoService.updateById(processInfo);
        return formData;
    }
}
