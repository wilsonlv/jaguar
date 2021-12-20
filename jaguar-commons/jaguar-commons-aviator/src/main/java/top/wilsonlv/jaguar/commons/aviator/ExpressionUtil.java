package top.wilsonlv.jaguar.commons.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author lvws
 * @since 2019/3/7.
 */
public class ExpressionUtil {

    public static final String DEFAULT_EXPRESSION_PRE = "${";
    public static final String DEFAULT_EXPRESSION_SUF = "}";

    public static boolean isExpression(String expression) {
        return isExpression(expression, DEFAULT_EXPRESSION_PRE, DEFAULT_EXPRESSION_SUF);
    }

    public static boolean isExpression(String expression, String expressionPre, String expressionSuf) {
        return StringUtils.hasText(expression) && (expression.startsWith(expressionPre) && expression.endsWith(expressionSuf));
    }

    public static Object execute(String expression, Map<String, Object> envVariable) {
        return execute(expression, envVariable, DEFAULT_EXPRESSION_PRE, DEFAULT_EXPRESSION_SUF);
    }

    public static Object execute(String expression, Map<String, Object> envVariable, String expressionPre, String expressionSuf) {
        if (!isExpression(expression, expressionPre, expressionSuf)) {
            throw new IllegalArgumentException("表达式格式错误:" + expression);
        }

        expression = expression.substring(2, expression.length() - 1);
        return AviatorEvaluator.getInstance().execute(expression, envVariable);
    }

    public static Object executeText(String text, Map<String, Object> envVariable) {
        text = text.replaceAll("\\$\\{", "'+");
        text = text.replaceAll("}", "+'");

        return AviatorEvaluator.getInstance().execute('\'' + text + '\'', envVariable);
    }

}
