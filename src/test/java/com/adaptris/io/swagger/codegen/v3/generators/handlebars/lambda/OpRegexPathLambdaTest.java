package com.adaptris.io.swagger.codegen.v3.generators.handlebars.lambda;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.adaptris.io.swagger.codegen.v3.generators.handlebars.lambda.OpRegexPathLambda;

import io.swagger.codegen.v3.CodegenConstants;
import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.CodegenParameter;

public class OpRegexPathLambdaTest extends HandlebarsTestBase {

  private OpRegexPathLambda lambdaOpRegexPath = new OpRegexPathLambda();

  @Test
  public void testExecute() throws Exception {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);

    String template = "{{#operation}}{{#opRegexPath}}{{/opRegexPath}}{{/operation}}";
    Object opRegexPathCtx = context(
        "opRegexPath", lambdaOpRegexPath,
        "value", null,
        "operation", co
        );

    String regexPath = compile(template, opRegexPathCtx);

    Assert.assertEquals("/baseName/([^\\/]+)", regexPath);
  }

  @Test
  public void testExecuteInvalid() throws Exception {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);

    String template = "{{#opRegexPath}}{{/opRegexPath}}";
    Object opRegexPathCtx = context(
        "opRegexPath", lambdaOpRegexPath,
        "value", null,
        "operation", co
        );

    String regexPath = compile(template, opRegexPathCtx);

    Assert.assertEquals("Invalid context. OpRegexPathLambda expects a CodegenOperation context.", regexPath);
  }

  @Test
  public void testGetRegexPath() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/([^\\/]+)", regexPath);
  }

  @Test
  public void testGetRegexPathWithSlash() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}/{param2}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);
    CodegenParameter param2 = new CodegenParameter();
    param2.baseName = "param2";
    pathParams.add(param2);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/([^\\/]+)/(.+)", regexPath);
  }

  @Test
  public void testGetRegexPathWithHyphon() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}-{param2}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);
    CodegenParameter param2 = new CodegenParameter();
    param2.baseName = "param2";
    pathParams.add(param2);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/([^\\/]+)-(.+)", regexPath);
  }

  @Test
  public void testGetRegexPathWithPath() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}/subPath/{param2}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);
    CodegenParameter param2 = new CodegenParameter();
    param2.baseName = "param2";
    pathParams.add(param2);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/([^\\/]+)/subPath/(.+)", regexPath);
  }

  @Test
  public void testGetRegexPathWithNoSeparator() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/{param1}{param2}";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);
    CodegenParameter param2 = new CodegenParameter();
    param2.baseName = "param2";
    pathParams.add(param2);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/([^\\/]+)(.+)", regexPath);
  }

  @Test
  public void testGetRegexPathWithNoParam() {
    CodegenOperation co = new CodegenOperation();
    co.path = "/baseName/subPath";

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("/baseName/subPath", regexPath);
  }

  @Test
  public void testGetRegexPathWithEmptyPath() {
    CodegenOperation co = new CodegenOperation();
    co.path = "";
    List<CodegenParameter> pathParams = co.pathParams;
    CodegenParameter param1 = new CodegenParameter();
    param1.baseName = "param1";
    param1.setVendorExtensions(Collections.singletonMap(CodegenConstants.HAS_MORE_EXT_NAME, true));
    pathParams.add(param1);
    CodegenParameter param2 = new CodegenParameter();
    param2.baseName = "param2";
    pathParams.add(param2);

    String regexPath = lambdaOpRegexPath.execute(co);

    Assert.assertNotNull(regexPath);
    Assert.assertEquals("", regexPath);
  }

}
