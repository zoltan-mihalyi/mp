package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.exception.UserAlreadyAddedException;
import hu.zoltanmihalyi.mp.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Room {
    private Map<User, Membership> users = new HashMap<>();

    public void addUser(User user) {
        if (contains(user)) {
            throw new UserAlreadyAddedException();
        }
        Membership membership = new Membership(user, this);
        users.put(user, membership);
        user.join(this);
        onJoin(membership);
    }

    public boolean contains(User user) {
        return users.containsKey(user);
    }

    protected abstract void onJoin(Membership membership);

    public void removeUser(User user) {
        users.remove(user);
        user.leave(this);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.keySet());
    }

    public int getUsersCount() {
        return users.size();
    }

    public Membership getMembershipOf(User user) {
        if (!users.containsKey(user)) {
            throw new UserNotFoundException();
        }
        return users.get(user);
    }
}
