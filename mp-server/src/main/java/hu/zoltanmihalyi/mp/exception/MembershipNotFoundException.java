package hu.zoltanmihalyi.mp.exception;

public class MembershipNotFoundException  extends RuntimeException{
    public MembershipNotFoundException(String message) {
        super(message);
    }
}
