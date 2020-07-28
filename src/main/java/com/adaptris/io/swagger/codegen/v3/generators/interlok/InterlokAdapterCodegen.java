package com.adaptris.io.swagger.codegen.v3.generators.interlok;

public class InterlokAdapterCodegen extends AbstractInterlokCodegen {

  private static final String ADAPTER_TEMPLATE = "adapter.mustache";
  public static final String ADAPTER_XML = "adapter.xml";

  public InterlokAdapterCodegen() {
    super(ADAPTER_TEMPLATE, ADAPTER_XML);
    // To have the channel content indented correctly
    additionalProperties.put("indent", "    ");
  }

  @Override
  public String getHelp() {
    return "Generates an Interlok Adapter XML file.";
  }

}
