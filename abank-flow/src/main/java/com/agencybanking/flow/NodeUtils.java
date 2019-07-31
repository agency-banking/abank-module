package com.agencybanking.flow;

import com.agencybanking.flow.bpmn.models.BPMNNode;
import com.agencybanking.flow.bpmn.models.ConditionExpression;
import com.agencybanking.flow.bpmn.models.Flow;
import com.agencybanking.flow.bpmn.models.ProcessDefinition;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

public class NodeUtils {
    public static BPMNNode nextNode(String currentNode, ProcessDefinition def, Map<String, Object> variables) {
        Flow flow = getNextFlow(def, currentNode, variables);
        notNull(flow, "No flow from " + currentNode + "," + def.getProcess().getId());
        BPMNNode anotherNode = def.nextNode(flow);
        notNull(anotherNode, "Null node reached in process execution : " + def.getProcess().getId());
        return anotherNode;
    }

    /**
     * if one flow is found, this is returned. Otherwise flows must contain a <code>ConditionExpression</code>,
     * which must return true on evaluation for the next flow.
     *
     * @param processDefinition
     * @param nodeId
     * @param variables
     * @return the next sequence flow for this process
     * @throws IllegalStateException if no flow could be evaluated
     */
    public static Flow getNextFlow(ProcessDefinition processDefinition, String nodeId, Map<String, Object> variables) {
        List<Flow> flows = processDefinition.getNextFlows(nodeId);
        notEmpty(flows, "Invalid process: no target sequence flow from node " + nodeId);
        if (flows.size() == 1) {
            return flows.get(0);
        }
        for (Flow flow : flows) {
            ConditionExpression conditionExpression = flow.getCondition();
            notNull(conditionExpression, "Condition expression not found in flow " + flow.getId());
            if (evaluateExpression(conditionExpression.getExpression(), variables, Boolean.class)) {
                return flow;
            }
        }
        throw new IllegalStateException("Invalid process: no target sequence flow from node " + nodeId);
    }

    public static <T> T evaluateExpression(String expression, Map<String, Object> variables, Class<T> cls) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (String key : variables.keySet()) {
            context.setVariable(key, variables.get(key));
        }
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context, cls);
    }
}
