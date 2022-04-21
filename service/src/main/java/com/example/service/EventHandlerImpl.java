package com.example.service;

import com.filenet.api.core.Factory;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;

public class EventHandlerImpl  implements EventActionHandler {


    @Override
    public void onEvent(ObjectChangeEvent objectChangeEvent, Id id) throws EngineRuntimeException {

    }
}
