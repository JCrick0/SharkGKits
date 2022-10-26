package dev.ukry.gkits.manager.events;

import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GkitCreationEvent extends BaseEvent {
    private Gkit gkit;
}