package org.jaguar.modules.workflow.cmd;

import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.engine.runtime.Execution;

import java.util.Map;

/**
 * @author lvws
 * @since 2019/11/16
 */
public class AddMultiInstanceExecutionCmd implements Command<Execution> {

    protected final String NUMBER_OF_INSTANCES = "nrOfInstances";

    private final ExecutionEntity miExecution;
    private final Map<String, Object> executionVariables;

    public AddMultiInstanceExecutionCmd(ExecutionEntity miExecution, Map<String, Object> executionVariables) {
        this.miExecution = miExecution;
        this.executionVariables = executionVariables;
    }

    @Override
    public Execution execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();

        ExecutionEntity childExecution = executionEntityManager.createChildExecution(miExecution);
        childExecution.setCurrentFlowElement(miExecution.getCurrentFlowElement());

        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(miExecution.getProcessDefinitionId());
        Activity miActivityElement = (Activity) bpmnModel.getFlowElement(miExecution.getActivityId());
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = miActivityElement.getLoopCharacteristics();

        Integer currentNumberOfInstances = (Integer) miExecution.getVariable(NUMBER_OF_INSTANCES);
        miExecution.setVariableLocal(NUMBER_OF_INSTANCES, currentNumberOfInstances + 1);

        if (executionVariables != null) {
            childExecution.setVariablesLocal(executionVariables);
        }

        if (!multiInstanceLoopCharacteristics.isSequential()) {
            miExecution.setActive(true);
            miExecution.setScope(false);

            childExecution.setCurrentFlowElement(miActivityElement);
            CommandContextUtil.getAgenda().planContinueMultiInstanceOperation(childExecution, miExecution, currentNumberOfInstances);
        }
        return childExecution;
    }
}
