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


        FilterElement filterElement = new FilterElement(null, null, null, "MD5", null);

        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(filterElement);


        Document document = Factory.Document.fetchInstance(textFileRepository, id, null);
        document.getProperties();
    }
}
