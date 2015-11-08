package hu.zoltanmihalyi.mp.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ServerEvent {
    @Getter
    private int roomId;
}
