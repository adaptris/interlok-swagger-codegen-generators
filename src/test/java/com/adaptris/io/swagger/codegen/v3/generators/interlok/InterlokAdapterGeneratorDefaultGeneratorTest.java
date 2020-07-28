package com.adaptris.io.swagger.codegen.v3.generators.interlok;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adaptris.io.swagger.codegen.v3.generators.interlok.InterlokAdapterCodegen;

import io.swagger.codegen.v3.CodegenConfig;

/**
 * Tests for DefaultGenerator logic
 */
public class InterlokAdapterGeneratorDefaultGeneratorTest extends InterlokGeneratorDefaultGeneratorTestAbstract {

  @Test
  public void testGetHelp() throws Exception {
    assertEquals("Generates an Interlok Adapter XML file.", getCodegenConfig().getHelp());
  }

  @Override
  protected CodegenConfig getCodegenConfig() {
    return new InterlokAdapterCodegen();
  }

  @Override
  protected String getGeneratedXmlFileName() {
    return InterlokAdapterCodegen.ADAPTER_XML;
  }

  @Override
  protected String getExpectedXmlFilePathPrefix() {
    return "src/test/resources/interlok/adapter";
  }

}
