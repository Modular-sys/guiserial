package xyz.zenheart.guiserial.utils;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: 字段工具类 </p>
 * <p>创建时间: 2021/9/9 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
public interface FieldUtils<T, R> extends Function<T, R>, Serializable {

    int CONVERT = 0;
    int UPPER_TO_LOWER = 1;
    int LOWER_TO_UPPER = 2;

    @SneakyThrows
    default String getName() {
        int first = 0;
        final Method method = this.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
        String methodName = serializedLambda.getImplMethodName();
        if (methodName.startsWith("get")) {
            return convertChar(methodName.substring(3), first, UPPER_TO_LOWER);
        } else if (methodName.startsWith("is")) {
            return convertChar(methodName.substring(2), first, UPPER_TO_LOWER);
        }
        return "";
    }

    static <T, R> String getName(FieldUtils<T, R> getter) {
        return getter.getName();
    }

    static String convertChar(String name, int index, int convert) {
        if (Objects.isNull(name) || index >= name.length()) {
            return name;
        }
        char[] cs = name.toCharArray();
        int ci = cs[index];
        /*大写转小写*/
        if ((CONVERT == convert || convert == UPPER_TO_LOWER) && ci <= 90 && ci >= 65) {
            cs[index] += 32;
        }
        /*小写转大写 Character.isLowerCase(cs[index])*/
        if ((CONVERT == convert || convert == LOWER_TO_UPPER) && ci <= 122 && ci >= 97) {
            cs[index] -= 32;
        }
        return String.valueOf(cs);
    }
}
