package com.example.service;

import com.example.db.repository.TextFileRepository;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.replication.Repository;
import com.filenet.api.util.Id;

public class EventHandlerImpl implements EventActionHandler {

    TextFileRepository textFileRepository;

    public EventHandlerImpl(TextFileRepository textFileRepository) {
        this.textFileRepository = textFileRepository;
    }

    @Override
    public void onEvent(ObjectChangeEvent objectChangeEvent, Id id) throws EngineRuntimeException {
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, null, "MD5", null));

        Document documentInstance = Factory.Document.fetchInstance(textFileRepository, id, pf);
        documentInstance.getProperties();

        //TODO Logic checks - add if else statement, count MD5 value and update document properties
    }
}
