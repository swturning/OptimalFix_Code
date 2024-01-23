package start.Util;

import org.dozer.DozerBeanMapper;

public class CloneUtil {
    public static <T> T clone(T obj,Class<T> targetClass) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        T target = mapper.map(obj, targetClass);
        return target;
    }



}
