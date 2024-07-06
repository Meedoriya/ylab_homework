package org.alibi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Аспект для логирования действий пользователей и времени выполнения методов.
 */
@Aspect
public class UserActionAuditAspect {

    /**
     * Поинткат, который соответствует всем методам в UserService.
     */
    @Pointcut("execution(* org.alibi.application.UserService.*(..))")
    public void applicationMethods() {}

    /**
     * Аспект вокруг, который логирует действия пользователей и время выполнения методов.
     *
     * @param joinPoint точка соединения
     * @return результат выполнения метода
     * @throws Throwable если возникает ошибка во время выполнения метода
     */
    @Around("applicationMethods()")
    public Object logUserActions(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logAction(methodName, methodArgs, System.currentTimeMillis() - start, throwable);
            throw throwable;
        }
        logAction(methodName, methodArgs, System.currentTimeMillis() - start, null);
        return result;
    }

    private void logAction(String methodName, Object[] methodArgs, long duration, Throwable throwable) {
        try (FileWriter fw = new FileWriter("audit.log", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.printf("%s - Метод: %s, Аргументы: %s, Длительность: %dмс, Исключение: %s%n",
                    LocalDateTime.now(),
                    methodName,
                    methodArgs != null ? arrayToString(methodArgs) : "[]",
                    duration,
                    throwable != null ? throwable.getMessage() : "Нет"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String arrayToString(Object[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (Object obj : array) {
            sb.append(obj).append(",");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
