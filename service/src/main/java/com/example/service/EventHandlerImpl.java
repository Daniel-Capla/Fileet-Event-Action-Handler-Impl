package com.example.service;

import com.example.db.repository.TextFileRepository;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class EventHandlerImpl implements EventActionHandler {

    TextFileRepository textFileRepository;

    public EventHandlerImpl(TextFileRepository textFileRepository) {
        this.textFileRepository = textFileRepository;
    }

    //To avoid undermining the transactional consistency of the server, handler code must always throw some exception in response to an exception thrown
    // by the Content Engine API. Throwing the exception ensures that the server rolls back the surrounding transaction and cleans up any inconsistent
    // database activities. It is not safe to catch a Content Engine API exception and treat it as a soft or recoverable failure.
    @Override
    public void onEvent(ObjectChangeEvent objectChangeEvent, Id id) throws EngineRuntimeException {
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, null, "MD5", null));

        Document documentInstance = Factory.Document.fetchInstance(textFileRepository, id, pf);
        Properties propertyMD5 = documentInstance.getProperties();

        if (propertyMD5.get("MD5") == null || !propertyMD5.isPropertyPresent("MD5")) {
            propertyMD5.putValue(getDocumentContentAndCountChecksum(documentInstance), id);
            documentInstance.set_DateLastModified(Date.from(Instant.now()));
            //refresh only MD5, that's why we used property filter
            documentInstance.save(RefreshMode.REFRESH, pf);
        }


    }

    private String getDocumentContentAndCountChecksum(Document documentInstance) {
        ContentElementList documentContentList = documentInstance.get_ContentElements();
        Iterator iterator = documentContentList.iterator();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String readStr = "";
        String checksumMD5 = "";

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
                md.update(Byte.parseByte(readStr));
                byte[] digest = md.digest();
                checksumMD5 = DatatypeConverter.printHexBinary(digest).toUpperCase(Locale.ROOT);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return checksumMD5;
    }
}
