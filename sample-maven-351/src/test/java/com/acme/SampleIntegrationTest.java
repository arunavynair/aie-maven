/**
 * Copyright 2012 Attivio Inc., All rights reserved.
 */
package com.acme;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.attivio.TestUtils;
import com.attivio.app.Attivio;
import com.attivio.app.config.Configuration;
import com.attivio.bus.PsbProperties;
import com.attivio.client.ContentFeeder;
import com.attivio.client.SearchClient;
import com.attivio.messages.QueryRequest;
import com.attivio.messages.QueryResponse;
import com.attivio.model.AttivioException;
import com.attivio.model.FieldNames;
import com.attivio.model.document.AttivioDocument;

/**
 * Sample integration test.
 *
 * This integration test can be used for basic integration testing
 * as well as debugging/tracing throughout the entire system.
 *
 */
public class SampleIntegrationTest {

  @BeforeClass
  public static void initializeTestEnvironment() throws AttivioException, IOException {
    PsbProperties.setProperty("log.printStackTraces", true);
    PsbProperties.setProperty("log.level", "INFO");
    PsbProperties.setProperty("attivio.project", System.getProperty("user.dir"));
    PsbProperties.setProperty("log.directory", System.getProperty("user.dir") + "/build/logs");
    PsbProperties.setProperty("data.directory", System.getProperty("user.dir") + "/build/data");
    TestUtils.initializeEnvironment();
  }
    
  @Test
  public void simpleTest() throws Exception {
    ContentFeeder feeder = null;
    try {
      // 'attivio.xml' should be replaced with your project's main configuration file
      Configuration cfg = TestUtils.getConfiguration("attivio.xml");

      Attivio.getInstance().start(cfg, true);

      // feed some content
      feeder = new ContentFeeder();
      feeder.setMessageResultListener(new SampleMessageResultListener());
      feeder.setIngestWorkflowName("ingest");
      AttivioDocument doc = new AttivioDocument("1234");
      doc.setField(FieldNames.TITLE, "test 123");
      feeder.feed(doc);
      feeder.commit();
      feeder.waitForCompletion();

      // now search for documents
      SearchClient searcher = new SearchClient();
      QueryRequest req = new QueryRequest("*:*");
      QueryResponse resp = searcher.search(req);
      Assert.assertEquals(1, resp.getTotalRows());

    } finally {
      if (feeder != null) feeder.disconnect();
      Attivio.getInstance().shutdown();
    }

  }

}