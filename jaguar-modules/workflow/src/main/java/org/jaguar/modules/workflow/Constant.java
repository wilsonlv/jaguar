package org.jaguar.modules.workflow;

/**
 * Created by lvws on 2019/3/5.
 */
public interface Constant {

    String RESOURCE_NAME_PATTERN = ".bpmn20.xml";

    String ELEMENT_ID_PRE = "id-";

    String PROCESS_PROPERTIES = "process";

    String PROCESS_PROPERTIES_FORM_NAME = "formName";
    String PROCESS_PROPERTIES_FORM_ELEMENT_ID = "formElementId";
    String PROCESS_PROPERTIES_INITIATE_TASK = "initiateTask";

    String NODE_PROPERTIES_PRE = "node-";

    String LINE_PROPERTIES_PRE = "line-";

    String PROCESS_FORM_VARIABLE_PRE = "form_";

    /*------------------  工单常量  ------------------*/
    String PROCESS_NUM = "processNum";
    String PROCESS_TITLE = "processTitle";
    String PROCESS_PRIORITY = "processPriority";
    String PROCESS_ORDER_TIME = "processOrderTime";
    String PROCESS_NOW = "now";

    String PROCESS_INITIATOR = "initiator";
    String PROCESS_HANDLER = "handler";

    String PROCESS_TAGS = "PROCESS_TAGS";
    String PROCESS_TAG_GO_BACK_NAME = "回退";
    String PROCESS_TAG_REJECT_NAME = "驳回";

    String LAST_TASK_INSTANCE_ID = "LAST_TASK_INSTANCE_ID";

    String ELEMENT_VARIABLE = "multi_assignee_item";
    String ELEMENT_VARIABLE_EXPRESSION = "${multi_assignee_item}";

    String DEFAULT_COMPLETION_CONDITION = "${nrOfCompletedInstances==nrOfInstances}";
}
