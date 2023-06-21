package com.adaptris.io.swagger.codegen.v3.generators.interlok;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.adaptris.io.swagger.codegen.v3.generators.AbstractCodegenTest;

import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.ClientOpts;
import io.swagger.codegen.v3.CodegenConfig;
import io.swagger.codegen.v3.CodegenType;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

/**
 * Tests for DefaultGenerator logic
 */
public abstract class InterlokGeneratorDefaultGeneratorTestAbstract extends AbstractCodegenTest {

  private static final String TEST_SKIP_OVERWRITE = "testSkipOverwrite";

  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    folder.create();
  }

  @After
  public void tearDown() throws Exception {
    folder.delete();
  }

  @Test
  public void testGenerateFromYaml() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel.yaml", getExpectedXmlFilePathPrefix() + ".xml");
  }

  @Test
  public void testGenerateFromJson() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel.json", getExpectedXmlFilePathPrefix() + ".xml");
  }

  @Test
  public void testGenerateFromYamlRoutingService() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-with-routing-service.yaml",
        getExpectedXmlFilePathPrefix() + "-with-routing-service.xml");
  }

  @Test
  public void testGenerateFromJsonRoutingService() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-with-routing-service.json",
        getExpectedXmlFilePathPrefix() + "-with-routing-service.xml");
  }

  @Test
  public void testGenerateFromYamlMix() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-mix.yaml", getExpectedXmlFilePathPrefix() + "-mix.xml");
  }

  @Test
  public void testGenerateFromJsonMix() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-mix.json", getExpectedXmlFilePathPrefix() + "-mix.xml");
  }

  @Test
  public void testGenerateFromYamlNoPath() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-no-path.yaml", getExpectedXmlFilePathPrefix() + "-no-path.xml");
  }

  @Test
  public void testGenerateFromJsonNoPath() throws Exception {
    testGenerateFrom("src/test/resources/interlok/channel-no-path.json", getExpectedXmlFilePathPrefix() + "-no-path.xml");
  }

  @Test
  public void testGenerateFromNull() throws Exception {
    OpenAPI openApi = parse(null);
    assertNull(openApi);
  }

  @Test
  public void testGenerateFromInvalid() throws Exception {
    try {
      final File output = folder.getRoot();

      final OpenAPI openApi = parse("{}");
      CodegenConfig codegenConfig = getCodegenConfig();
      codegenConfig.setOutputDir(output.getAbsolutePath());

      ClientOptInput clientOptInput = new ClientOptInput().opts(new ClientOpts()).openAPI(openApi).config(codegenConfig);

      // generate content first time without skipOverwrite flag, so all generated files should be
      // recorded
      new DefaultGenerator().opts(clientOptInput).generate();
      fail("Should have thrown a RuntimeException");
    } catch (RuntimeException rte) {
      // Should come here
    }
  }

  @Test
  public void testGetName() throws Exception {
    assertEquals("xml", getCodegenConfig().getName());
  }

  @Test
  public void testGetTag() throws Exception {
    assertEquals(CodegenType.SERVER, getCodegenConfig().getTag());
  }

  private void testGenerateFrom(String location, String expectedXmlFilePath) throws Exception {
    final File output = folder.getRoot();

    Path path = Paths.get(expectedXmlFilePath);
    File expectedXml = path.toFile();

    OpenAPI openApi = getOpenAPI(location);

    CodegenConfig codegenConfig = getCodegenConfig();
    codegenConfig.setOutputDir(output.getAbsolutePath());

    ClientOptInput clientOptInput = new ClientOptInput().opts(new ClientOpts()).openAPI(openApi).config(codegenConfig);

    // generate content first time without skipOverwrite flag, so all generated files should be
    // recorded
    new DefaultGenerator().opts(clientOptInput).generate();
    final File generatedXml = new File(output, getGeneratedXmlFileName());
    assertTrue(generatedXml.exists());

    assertEquals(toString(expectedXml), toString(generatedXml));

    // change content of one file
    changeContent(generatedXml);

    // generate content second time without skipOverwrite flag, so changed file should be rewritten
    new DefaultGenerator().opts(clientOptInput).generate();
    assertTrue(!TEST_SKIP_OVERWRITE.equals(toString(generatedXml)));

    // change content again
    changeContent(generatedXml);

    // generate content third time with skipOverwrite flag, so changed file should not be rewritten
    // and deleted file should be recorded
    codegenConfig.setSkipOverwrite(true);
    new DefaultGenerator().opts(clientOptInput).generate();
    assertEquals(toString(generatedXml), TEST_SKIP_OVERWRITE);

    // and again to have the xml file at last
    codegenConfig.setSkipOverwrite(false);
    new DefaultGenerator().opts(clientOptInput).generate();
    assertTrue(!TEST_SKIP_OVERWRITE.equals(toString(generatedXml)));

    // System.out.println(FileUtils.readFileToString(generatedXml, StandardCharsets.UTF_8));
  }

  protected abstract CodegenConfig getCodegenConfig();

  protected abstract String getGeneratedXmlFileName();

  protected abstract String getExpectedXmlFilePathPrefix();

  private String toString(File expectedXml) throws IOException {
    String xml = FileUtils.readFileToString(expectedXml, StandardCharsets.UTF_8);
    return xml.trim().replace("\r\n", "\n").replace("\r", "\n");
  }

  private static void changeContent(File file) throws IOException {
    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), UTF_8));
    out.write(TEST_SKIP_OVERWRITE);
    out.close();
  }

  private OpenAPI parse(String content) throws IOException {
    SwaggerParseResult parsed = new OpenAPIV3Parser().readContents(content);
    return parsed.getOpenAPI();
  }

}
