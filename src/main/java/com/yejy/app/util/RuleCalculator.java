package com.yejy.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则计算器
 */
public class RuleCalculator {
    private Map<String, String> params;

    public RuleCalculator(Map<String, String> params) {
        this.params = params;
    }

    public RuleCalculator(String key, String value) {
        this.params = new HashMap<>();
        this.params.put(key, value);
    }

    public String execute(String rule) {
        // 先提取
        List<String> orgin = extract(rule);
        // 去括号
        List<String> orginSimple = takeParentheses(orgin);

        String result = takeResult(orginSimple);

        return result;
    }

    // 转换成变量
    private String parseToVar(String item) {
        if ("true".equals(item) || "false".equals(item)) {
            return item;
        }
        if (params != null && params.containsKey(item)) {
            return params.get(item);
        }
        return item;
    }

    // 返回结果 [qty, >=, 3, ?, (, price, *, (, qty, -, 1, ), +, 20, ), :, amount]
    private String takeResult(List<String> origin) {
        List<String> mulDiv = takeMulDivAddSub(origin, "*", "/");
        if (mulDiv.size() == 1) return mulDiv.get(0);
        List<String> addSub = takeMulDivAddSub(mulDiv, "+", "-");
        if (addSub.size() == 1) return addSub.get(0);
        // 关系运算和三目在一起
        List<String> relation = takeRelation(addSub);
        return takeThree(relation).get(0);
    }

    // 4、去三元运算符，右结合
    private List<String> takeThree(List<String> origin) {
        int index = origin.lastIndexOf(":");
        if (index == -1) return origin;
        String condition = origin.get(index - 3);
        String x = origin.get(index - 1);
        String y = origin.get(index + 1);
        String res = threeExp(Boolean.valueOf(condition), x, y).toString();
        List<String> set = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            if (i >= index - 3 && i <= index + 1) {
                if (i == index - 3) {
                    set.add(res);
                }
                continue;
            }
            set.add(origin.get(i));
        }
        return takeThree(set);
    }

    // 3、去关系
    private List<String> takeRelation(List<String> origin) {
        int start = -1;
        for (int i = 0; i < origin.size(); i++) {
            String str = origin.get(i);
            if (str.matches("[<,>]=?|[=,!]=")) {
                start = i;
                break;
            }
        }
        if (start == -1) return origin;
        String x = origin.get(start - 1);
        String y = origin.get(start + 1);
        String exp = origin.get(start);
        String result = relationExp(exp, x, y);
        List<String> newSet = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            if (i >= start - 1 && i <= start + 1) {
                if (i == start - 1) {
                    newSet.add(result);
                }
                continue;
            }
            newSet.add(origin.get(i));
        }
        return takeRelation(newSet);
    }


    // 2、去乘除,加减
    private List<String> takeMulDivAddSub(List<String> origin, String a, String b) {
        int startA = origin.indexOf(a); // *
        int startB = origin.indexOf(b); // /
        if (startA == -1 && startB == -1) {
            return origin;
        }
        int start;
        if (startA == -1 || startB == -1) {
            start = startA == -1 ? startB : startA;
        } else {
            start = startA < startB ? startA : startB;
        }
        String x = origin.get(start - 1);
        String y = origin.get(start + 1);
        String exp = origin.get(start);
        String ele = mathExp(exp, x, y).toString();
        List<String> newSet = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            if (i >= start - 1 && i <= start + 1) {
                if (i == start - 1) {
                    newSet.add(ele);
                }
                continue;
            }
            newSet.add(origin.get(i));
        }

        return takeMulDivAddSub(newSet, a, b);
    }


    // 1、去括号
    private List<String> takeParentheses(List<String> origin) {
        // 先去最内的")"
        int end = origin.indexOf(")");
        if (end == -1) {
            return origin;
        }
        // 取最靠近的"("
        int start = origin.subList(0, end).lastIndexOf("(");
        // 提取数组
        List<String> subList = origin.subList(start + 1, end);
        String ele = takeResult(subList);
        List<String> newSet = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            if (i >= start && i <= end) {
                if (i == start) {
                    newSet.add(ele);
                }
                continue;
            }
            newSet.add(origin.get(i));
        }
        return takeParentheses(newSet);
    }


    private ArrayList<String> extract(String exp) {
        ArrayList<String> resultSet = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (c == '<') {
                // 开头
                char c1 = nextNotEmpChar(exp, i + 1);
                if (c1 >= 'a' && c1 <= 'z') {
                    continue;
                }
            }
            if (c == '>') {
                // 第一，变量结束
                if (sb.length() > 0) {
                    resultSet.add(sb.toString());
                    sb = new StringBuilder();
                    // 第二，大于号
                    char c1 = nextNotEmpChar(exp, i + 1);
                    if (c1 == '<' || (c1 >= '0' && c1 <= '9')) {
                        resultSet.add(">");
                        sb = new StringBuilder();
                        continue;
                    }
                    continue;
                }
            }
            sb.append(c);

            // 判断结束
            if (i == exp.length() - 1) {
                resultSet.add(sb.toString());
                break;
            }
            char c1 = nextNotEmpChar(exp, i + 1);
            if (c1 == '?' || c1 == ':' || c1 == '+' || c1 == '-' || c1 == '*' || c1 == '/' || c1 == '(' || c1 == ')') {
                resultSet.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            if (c == '?' || c == ':' || c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
                resultSet.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            if (c == '>' || c == '<' || c == '=') {
                if (c1 != '=') {
                    resultSet.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
        }
        return resultSet;
    }

    private char nextNotEmpChar(String exp, int start) {
        for (int i = start; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c != ' ') {
                return c;
            }
        }
        return ' ';
    }

    // 运算表达式
    private BigDecimal mathExp(String exp, String xStr, String yStr) {
        BigDecimal x = BigDecimal.valueOf(Double.valueOf(parseToVar(xStr)));
        BigDecimal y = BigDecimal.valueOf(Double.valueOf(parseToVar(yStr)));
        BigDecimal result = null;
        if ("+".equals(exp)) {
            result = x.add(y);
        } else if ("-".equals(exp)) {
            result = x.subtract(y);
        } else if ("*".equals(exp)) {
            result = x.multiply(y);
        } else if ("/".equals(exp)) {
            result = x.divide(y, 2, RoundingMode.HALF_UP);
        }
        return result;
    }

    // 关系表达式
    private String relationExp(String exp, String xStr, String yStr) {
        BigDecimal x = BigDecimal.valueOf(Double.valueOf(parseToVar(xStr)));
        BigDecimal y = BigDecimal.valueOf(Double.valueOf(parseToVar(yStr)));
        boolean result = false;
        if (">".equals(exp)) {
            result = x.doubleValue() > y.doubleValue();
        } else if (">=".equals(exp)) {
            result = x.doubleValue() >= y.doubleValue();
        } else if ("==".equals(exp)) {
            result = x.doubleValue() == y.doubleValue();
        } else if ("<".equals(exp)) {
            result = x.doubleValue() < y.doubleValue();
        } else if ("<=".equals(exp)) {
            // <=
            result = x.doubleValue() <= y.doubleValue();
        } else {
            // !=
            result = x.doubleValue() != y.doubleValue();
        }
        return String.valueOf(result);
    }

    private BigDecimal threeExp(boolean condition, String xStr, String yStr) {
        BigDecimal x = BigDecimal.valueOf(Double.valueOf(parseToVar(xStr)));
        BigDecimal y = BigDecimal.valueOf(Double.valueOf(parseToVar(yStr)));
        return condition ? x : y;
    }
}
