package com.jaguar.process.service;

import com.alibaba.fastjson.JSONObject;
import com.jaguar.aviator.ExpressionUtil;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.exception.AuthenticationException;
import com.jaguar.core.exception.BusinessException;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.Constant;
import com.jaguar.process.enums.FormDataPersistenceType;
import com.jaguar.process.enums.TaskFieldPermission;
import com.jaguar.process.interfaces.IUserDefinedComponent;
import com.jaguar.process.mapper.FormDataMapper;
import com.jaguar.process.model.po.*;
import com.jaguar.process.model.vo.UserTask;
import com.jaguar.process.model.vo.component.ComponentConfig;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
@CacheConfig(cacheNames = "FormData")
public class FormDataService extends BaseService<FormData, FormDataMapper> {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private FormDataAttachService formDataAttachService;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 根据工单信息ID和表单字段ID查询表单数据
     */
    public FormData getByProcessInfoIdAndFieldId(Long processInfoId, Long fieldId) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("processInfoId", processInfoId);
        param.put("formTemplateFieldId", fieldId);
        return this.unique(param);
    }

    /**
     * 校验表单数据并保存
     * <p>
     * 保存的数据分两种：
     * 1、权限为可写或必填，
     * 2、有结果值的
     */
    @Transactional
    public void saveFormData(Long processInfoId, String processInstanceId, UserTask userTask,
                             FormTemplateSheet sheet, JSONObject formData) {

        //先保存权限为可写或必填的表单数据（结果值的生成可能对此有依赖关系）
        List<FormTemplateField> resultValueFields = InstanceUtil.newArrayList();
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                TaskFieldPermission taskFormPermission = userTask.getTaskFormPermissionMap().get(field.getKey());
                if (taskFormPermission == null) {
                    throw new BusinessException("没有在【" + userTask.getName() + "】中找到【" + field.getKey() + "】的字段权限！");
                }

                String value = formData.getString(field.getKey());
                switch (taskFormPermission) {
                    case REQUIRED: {
                        if (StringUtils.isBlank(value)) {
                            throw new BusinessException("【" + field.getKey() + "】为必填字段！");
                        }
                    }
                    case HIDDEN:
                    case READ:
                    case WRITE: {
                        createFormData(processInfoId, field, value);
                        runtimeService.setVariable(processInstanceId, Constant.PROCESS_FORM_VARIABLE_PRE + field.getKey(), value);
                        break;
                    }
                }

                //提交的数据的优先级大于结果值
                if (StringUtils.isNotBlank(field.getResultValue()) && StringUtils.isBlank(value)) {
                    resultValueFields.add(field);
                }
            }
        }

        //在保存有结果值的字段
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        for (FormTemplateField field : resultValueFields) {
            String value = ExpressionUtil.execute(field.getResultValue(), variables).toString();
            createFormData(processInfoId, field, value);
            runtimeService.setVariable(processInstanceId, Constant.PROCESS_FORM_VARIABLE_PRE + field.getKey(), value);
        }
    }

    /**
     * 对单一表单字段的保存
     */
    @Transactional
    public void saveFormData(Long processInfoId, String processInstanceId, UserTask userTask, FormTemplateField field, String value) {
        TaskFieldPermission taskFormPermission = userTask.getTaskFormPermissionMap().get(field.getKey());
        if (taskFormPermission == null) {
            throw new BusinessException("没有在【" + userTask.getName() + "】中找到【" + field.getKey() + "】的字段权限！");
        }

        switch (taskFormPermission) {
            case REQUIRED: {
                if (StringUtils.isBlank(value)) {
                    throw new BusinessException("【" + field.getKey() + "】为必填字段！");
                }
            }
            case HIDDEN:
            case READ:
            case WRITE: {
                createFormData(processInfoId, field, value);
                runtimeService.setVariable(processInstanceId, Constant.PROCESS_FORM_VARIABLE_PRE + field.getKey(), value);
                break;
            }
            default:
                throw new AuthenticationException();
        }
    }

    /**
     * 保存表单数据
     */
    private void createFormData(Long processInfoId, FormTemplateField field, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }

        boolean exist = true;
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("processInfoId", processInfoId);
        param.put("formTemplateFieldId", field.getId());
        FormData formData = this.unique(param);
        if (formData == null) {
            formData = new FormData();
            formData.setProcessInfoId(processInfoId);
            formData.setFormTemplateId(field.getFormTemplateId());
            formData.setFormTemplateSheetId(field.getFormTemplateSheetId());
            formData.setFormTemplateFieldId(field.getId());
            exist = false;
        }

        switch (field.getFormTemplateFieldType()) {
            case fileUpload: {
                //附件上传存储在FormDataAttach表中

                formData.setFormDataPersistenceType(FormDataPersistenceType.FORM_DATA_ATTACH);
                formData = this.update(formData);

                FormDataAttach formDataAttach;
                if (exist) {
                    formDataAttach = formDataAttachService.getByFormDataId(formData.getId());
                } else {
                    formDataAttach = new FormDataAttach();
                    formDataAttach.setFormDataId(formData.getId());
                }
                formDataAttach.setValue(value);
                formDataAttachService.update(formDataAttach);
                break;
            }
            case userDefined: {
                //自定义组件由配置类来实现持久化

                formData.setFormDataPersistenceType(FormDataPersistenceType.USER_DEFINED);
                formData = this.update(formData);

                IUserDefinedComponent component = ComponentConfig.resovleUserDefinedComponent(field.getComponentConfig());
                component.persist(formData.getId(), value);
                break;
            }
            default: {
                formData.setFormDataPersistenceType(FormDataPersistenceType.VALUE);
                formData.setValue(value);
                this.update(formData);
            }
        }
    }

    /**
     * 填充表单数据
     */
    public void fillFormData(Long processInfoId, FormTemplateSheet sheet) {
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                FormData formData = this.getByProcessInfoIdAndFieldId(processInfoId, field.getId());
                if (formData == null) {
                    continue;
                }

                switch (formData.getFormDataPersistenceType()) {
                    case VALUE: {
                        field.setValue(formData.getValue());
                        continue;
                    }
                    case FORM_DATA_ATTACH: {
                        FormDataAttach formDataAttach = formDataAttachService.getByFormDataId(formData.getId());
                        field.setValue(formDataAttach.getValue());
                        continue;
                    }
                    case USER_DEFINED: {
                        IUserDefinedComponent component = ComponentConfig.resovleUserDefinedComponent(field.getComponentConfig());
                        String value = component.read(formData.getId());
                        field.setValue(value);
                        continue;
                    }
                    default: {
                        throw new BusinessException("无效的数据存储类型：" + formData.getFormDataPersistenceType());
                    }
                }

            }
        }
    }

}
