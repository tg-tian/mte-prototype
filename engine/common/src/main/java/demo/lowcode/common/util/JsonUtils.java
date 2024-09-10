package demo.lowcode.common.util;

public class JsonUtils {
    public static boolean evaluateCondition(String condition) {
        condition = condition.trim();

        if (condition.contains("==")) {
            String[] parts = condition.split("==");
            return evaluate(parts[0].trim(), parts[1].trim(), "==");
        } else if (condition.contains("!=")) {
            String[] parts = condition.split("!=");
            return evaluate(parts[0].trim(), parts[1].trim(), "!=");
        } else if (condition.contains(">=")) {
            String[] parts = condition.split(">=");
            return evaluate(parts[0].trim(), parts[1].trim(), ">=");
        } else if (condition.contains(">")) {
            String[] parts = condition.split(">");
            return evaluate(parts[0].trim(), parts[1].trim(), ">");
        } else if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            return evaluate(parts[0].trim(), parts[1].trim(), "<=");
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            return evaluate(parts[0].trim(), parts[1].trim(), "<");
        }

        throw new IllegalArgumentException("Unsupported condition: " + condition);
    }

    private static boolean evaluate(String left, String right, String operator) {
        Object leftValue = getValueFromContext(left);
        Object rightValue = getValueFromContext(right);

        if (leftValue instanceof Comparable leftComparable && rightValue instanceof Comparable rightComparable) {

            switch (operator) {
                case "==":
                    return leftComparable.compareTo(rightComparable) == 0;
                case "!=":
                    return leftComparable.compareTo(rightComparable) != 0;
                case ">":
                    return leftComparable.compareTo(rightComparable) > 0;
                case ">=":
                    return leftComparable.compareTo(rightComparable) >= 0;
                case "<":
                    return leftComparable.compareTo(rightComparable) < 0;
                case "<=":
                    return leftComparable.compareTo(rightComparable) <= 0;
            }
        }

        throw new IllegalArgumentException("Cannot compare values: " + leftValue + " and " + rightValue);
    }

    private static Object getValueFromContext(String key) {
        try {
            // 尝试将key解析为数值
            if (key.contains(".")) {
                return Double.parseDouble(key);
            } else {
                return Integer.parseInt(key);
            }
        } catch (NumberFormatException e) {
            // 不是数值，返回原始字符串
            return key;
        }
    }
}
