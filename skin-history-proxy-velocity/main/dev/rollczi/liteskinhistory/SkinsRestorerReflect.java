package dev.rollczi.liteskinhistory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.property.SkinIdentifier;

class SkinsRestorerReflect {

    private static final Class<?> componentClass;
    private static final Class<?> helperClass;

    public static final SkinsRestorer SKINS_RESTORER = SkinsRestorerProvider.get();

    private static final Method declaredMethod;
    private static final Method method;

    static {
        try {
            componentClass = Class.forName("net.skinsrestorer.shared.subjects.messages.ComponentString");
            helperClass = Class.forName("net.skinsrestorer.shared.subjects.messages.ComponentHelper");

            declaredMethod = SKINS_RESTORER.getSkinStorage().getClass().getDeclaredMethod("resolveSkinName", SkinIdentifier.class);
            declaredMethod.setAccessible(true);

            method = helperClass.getDeclaredMethod("convertJsonToPlain", componentClass);
            method.setAccessible(true);

        } catch (ClassNotFoundException | NoSuchMethodException notFoundException) {
            throw new RuntimeException(notFoundException);
        }
    }

    static String getSkinName(SkinIdentifier skinIdentifier) throws InvocationTargetException, IllegalAccessException {
        Object componentString = declaredMethod.invoke(SKINS_RESTORER.getSkinStorage(), skinIdentifier);

        return (String) method.invoke(null, componentString);
    }

}
