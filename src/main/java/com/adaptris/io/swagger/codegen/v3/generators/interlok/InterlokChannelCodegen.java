package com.adaptris.io.swagger.codegen.v3.generators.interlok;

public class InterlokChannelCodegen extends AbstractInterlokCodegen {

  private static final String CHANNEL_TEMPLATE = "channel.mustache";
  public static final String CHANNEL_XML = "channel.xml";

  public InterlokChannelCodegen() {
    super(CHANNEL_TEMPLATE, CHANNEL_XML);
    additionalProperties.put("indent", "");
  }

  @Override
  public String getHelp() {
    return "Generates an Interlok Channel XML file.";
  }

}
