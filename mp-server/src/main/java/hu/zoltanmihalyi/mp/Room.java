package hu.zoltanmihalyi.mp;

import java.util.HashSet;
import java.util.Set;

public abstract class Room {
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        onJoin(new Membership(user, this));
    }

    public boolean contains(User user) {
        return users.contains(user);
    }

    protected abstract void onJoin(Membership membership);

    public void removeUser(User user) {
        users.remove(user);
    }
}
