package main.java.com.chapter4.example1;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentManagementSystemTest {

    @Test
    public void 문서관리시스템_테스트() throws IOException {
        DocumentManagementSystem documentManagementSystem = new DocumentManagementSystem();
        documentManagementSystem.importFile(System.getProperty("user.dir") + "/resources/document.letter");

        Document document = documentManagementSystem.contents().get(0);

        assertAttributeEquals(document, Attributes.PATIENT, "Joe Bloggs");
        assertAttributeEquals(document, Attributes.ADDRESS,
                "123 Fake Street\n" + "Westminster\n" + "London\n" + "United Kingdom");
        assertAttributeEquals(document, Attributes.BODY,
                "We are writing to you to confirm the re-scheduling of your appointment\n" +
                        "with Dr. Avaj from 29th December 2016 to 5th January 2017");

    }
    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
    private void assertAttributeEquals(Document document, String attribute, String expected) {
        assertEquals(document.getAttribute(attribute), expected);
    }
}