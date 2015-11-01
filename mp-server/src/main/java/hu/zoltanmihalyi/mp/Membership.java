package hu.zoltanmihalyi.mp;

import java.util.HashMap;
import java.util.Map;

import hu.zoltanmihalyi.mp.privilege.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeNotFoundException;

public class Membership {
    private Map<Class<?>, Object> privileges = new HashMap<>();

    public <T> void grant(Class<? super T> privilegeType, T t) {
        if (hasPrivilege(privilegeType)) {
            throw new PrivilegeAlreadyGrantedException();
        }
        privileges.put(privilegeType, t);
    }

    public void revoke(Class<?> privilegeType){
        if(!hasPrivilege(privilegeType)) {
            throw new PrivilegeNotFoundException();
        }
        privileges.remove(privilegeType);
    }

    public boolean hasPrivilege(Class<?> privilegeType) {
        return privileges.containsKey(privilegeType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPrivilege(Class<T> privilegeType) {
        if (!hasPrivilege(privilegeType)) {
            throw new PrivilegeNotFoundException();
        }
        return (T) privileges.get(privilegeType);
    }
}
