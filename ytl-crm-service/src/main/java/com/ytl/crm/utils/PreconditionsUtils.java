package com.ytl.crm.utils;


import com.ytl.crm.domain.common.WechatBusinessException;
import org.springframework.lang.Nullable;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/3 10:32
 */
public class PreconditionsUtils {

    private PreconditionsUtils() {
    }

    public static void checkBusiness(boolean expression) {
        if (!expression) {
            throw new WechatBusinessException();
        }
    }

    public static void checkBusiness(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new WechatBusinessException(String.valueOf(errorMessage));
        }
    }

    public static void checkBusiness(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new WechatBusinessException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, char p1) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, int p1) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, long p1) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2, p3));
        }
    }

    public static void checkBusiness(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
        if (!b) {
            throw new WechatBusinessException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }

    static String format(String template, @Nullable Object... args) {
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;

        int i;
        int placeholderStart;
        for (i = 0; i < args.length; templateStart = placeholderStart + 2) {
            placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }

            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
        }

        builder.append(template, templateStart, template.length());
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);

            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }

            builder.append(']');
        }

        return builder.toString();
    }
}
