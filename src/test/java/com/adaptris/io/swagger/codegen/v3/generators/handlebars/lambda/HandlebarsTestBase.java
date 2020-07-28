package com.adaptris.io.swagger.codegen.v3.generators.handlebars.lambda;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;

public abstract class HandlebarsTestBase {

  protected Object context(Object... data) {
    Map<String, Object> ctx = new HashMap<>();
    if (data.length % 2 != 0) {
      throw new IllegalArgumentException("context helper accepts pairs of key/value varargs");
    }
    for (int i = 0; i < data.length; i += 2) {
      ctx.put(data[i].toString(), data[i + 1]);
    }
    return ctx;
  }

  protected String compile(String template, Object context) throws IOException {
      return new Handlebars().compileInline(template).apply(context);
    // return Mustache.compiler().compile(template).execute(context);
  }

}
