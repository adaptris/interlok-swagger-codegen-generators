package com.adaptris.io.swagger.codegen.v3.generators.handlebars.lambda;

import java.io.IOException;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Lambda;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.CodegenParameter;

/**
 * Custom Interlok lambda class that take the codegen operation in the context and replace its path params by some regex
 * /basePath/{param1}/{param2} will be replaced by /basePath/([^\/]+)/(.+)
 *
 */
public class OpRegexPathLambda implements Lambda<Context, String> {

  public OpRegexPathLambda() {
  }

  @Override
  public String apply(Context context, com.github.jknack.handlebars.Template template) throws IOException {
    if (context.model() instanceof CodegenOperation) {
      return execute((CodegenOperation) context.model());
    }
    return "Invalid context. OpRegexPathLambda expects a CodegenOperation context.";
  }

  public String execute(CodegenOperation co) {
    String regexPath = co.path;

    if (!regexPath.isEmpty() && co.getHasPathParams()) {
      for (CodegenParameter pathParam : co.pathParams) {
        if (pathParam.getHasMore()) {
          regexPath = regexPath.replace("{" + pathParam.getBaseName() + "}", "([^\\/]+)");
        } else {
          regexPath = regexPath.replace("{" + pathParam.getBaseName() + "}", "(.+)");
        }
      }
    }
    return regexPath;
  }

}
