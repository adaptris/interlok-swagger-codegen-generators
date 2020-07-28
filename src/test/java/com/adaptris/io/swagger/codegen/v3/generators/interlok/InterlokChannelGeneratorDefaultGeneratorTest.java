package com.adaptris.io.swagger.codegen.v3.generators.interlok;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adaptris.io.swagger.codegen.v3.generators.interlok.InterlokChannelCodegen;

import io.swagger.codegen.v3.CodegenConfig;

/**
 * Tests for DefaultGenerator logic
 */
public class InterlokChannelGeneratorDefaultGeneratorTest extends InterlokGeneratorDefaultGeneratorTestAbstract {

  @Test
  public void testGetHelp() throws Exception {
    assertEquals("Generates an Interlok Channel XML file.", getCodegenConfig().getHelp());
  }

  @Override
  protected CodegenConfig getCodegenConfig() {
    return new InterlokChannelCodegen();
  }

  @Override
  protected String getGeneratedXmlFileName() {
    return InterlokChannelCodegen.CHANNEL_XML;
  }

  @Override
  protected String getExpectedXmlFilePathPrefix() {
    return "src/test/resources/interlok/channel";
  }

}
