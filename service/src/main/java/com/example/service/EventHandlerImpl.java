package com.example.service;

import com.example.db.repository.TextFileRepository;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.replication.Repository;
import com.filenet.api.util.Id;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Iterator;

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
        Properties propertyMD5 = documentInstance.getProperties();


        //TODO Logic checks - add if else statement, count MD5 value and update document properties

        if (propertyMD5 == null) {

        }

    }

    private String checksumMD5(MessageDigest messageDigest, String documentContent) {

        MessageDigest md = MessageDigest.getInstance("MD5");

        return null;
    }

    private String getDocumentContent(Document documentInstance) {
        ContentElementList documentContentList = documentInstance.get_ContentElements();
        Iterator iterator = documentContentList.iterator();
        String readStr = "";

        while (iterator.hasNext()) {
            ContentTransfer ct = (ContentTransfer) iterator.next();
            InputStream inputStream = ct.accessContentStream();
            try {
                int docLen = 1024;
                byte[] buf = new byte[docLen];
                int n = 1;

                while (n > 0) {
                    n = inputStream.read(buf, 0, docLen);
                    readStr = readStr + new String((buf));
                    buf = new byte[docLen];
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return readStr;
    }
}
