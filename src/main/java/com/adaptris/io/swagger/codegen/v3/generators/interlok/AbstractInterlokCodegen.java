package com.adaptris.io.swagger.codegen.v3.generators.interlok;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.adaptris.io.swagger.codegen.v3.generators.DefaultCodegenConfig;
import com.adaptris.io.swagger.codegen.v3.generators.handlebars.lambda.OpRegexPathLambda;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.StringHelpers;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.CodegenResponse;
import io.swagger.codegen.v3.CodegenType;
import io.swagger.codegen.v3.SupportingFile;
import io.swagger.codegen.v3.utils.ModelUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;


public abstract class AbstractInterlokCodegen extends DefaultCodegenConfig {

  /**
   * Name of the sub-directory in "src/main/resource" where to find the Mustache template for the Interlok Codegen.
   */
  protected static final String INTERLOK = "interlok";
  protected static final String TEMPLATE_DIRECTORY_NAME = INTERLOK;

  private final Set<String> routerPath = new HashSet<>();

  public AbstractInterlokCodegen(String template, String xml) {
    super();
    outputFolder = "generated-code" + File.separator + INTERLOK;
    embeddedTemplateDir = templateDir = TEMPLATE_DIRECTORY_NAME;

    defaultIncludes = new HashSet<>();

    supportingFiles.add(new SupportingFile(template, "", xml));
    reservedWords = new HashSet<>();

    languageSpecificPrimitives = new HashSet<>();
    importMapping = new HashMap<>();
  }

  @Override
  public CodegenType getTag() {
    return CodegenType.SERVER;
  }

  @Override
  public String getName() {
    return "xml";
  }

  @Override
  public void processOpts() {
    super.processOpts();
    additionalProperties.put("lambdaOpRegexPath", new OpRegexPathLambda());
  }

  @Override
  public void preprocessOpenAPI(OpenAPI openAPI) {
    super.preprocessOpenAPI(openAPI);
    if (openAPI.getComponents() == null) {
      // Prevent a NPE if no components is provided in the openapi specs.
      openAPI.setComponents(new Components());
    }
    if (openAPI.getPaths() != null) {
      Set<Entry<String, PathItem>> paths = openAPI.getPaths().entrySet();
      for (Entry<String, PathItem> pathEntry : paths) {
        String path = pathEntry.getKey();
        PathItem pathItem = pathEntry.getValue();
        if (hasSeveralOperations(pathItem) || containsPathParam(path)) {
          // We evaluate this operation should be in a routing service
          String basePath = extractBasePath(path);
          routerPath.add(basePath);
        }
      }
    }
  }

  protected final boolean hasSeveralOperations(PathItem pathItem) {
    return ModelUtils.createOperationArray(pathItem).length > 1;
  }

  protected final boolean containsPathParam(String path) {
    return path != null && path.matches(".*\\{.*\\}.*");
  }

  /* Modified copy from io.swagger.codegen.languages.JavaJerseyServerCodegen#addOperationToGroup */
  @Override
  public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co,
      Map<String, List<CodegenOperation>> operations) {
    String basePath = resourcePath;
    basePath = extractBasePath(basePath);

    // Not a routing service? We set the base path as default
    if (basePath == "" || !routerPath.contains(basePath)) {
      basePath = "default";
    } else {
      if (co.path.startsWith("/" + basePath)) {
        co.path = co.path.substring(("/" + basePath).length());
      }
      co.subresourceOperation = !co.path.isEmpty();
    }
    List<CodegenOperation> opList = operations.get(basePath);
    if (opList == null) {
      opList = new ArrayList<>();
      operations.put(basePath, opList);
    }
    opList.add(co);
    co.baseName = basePath;
  }

  private String extractBasePath(String basePath) {
    if (basePath.startsWith("/")) {
      basePath = basePath.substring(1);
    }
    int pos = basePath.indexOf("/");
    if (pos > 0) {
      basePath = basePath.substring(0, pos);
    }
    return basePath;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
    Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
    if (operations != null) {
      // Utility variable to use in the mustache template
      operations.put("useRoutingService", routerPath.contains(operations.get("pathPrefix")));
      List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
      for (CodegenOperation operation : operationList) {
        // op.httpMethod = op.httpMethod.toLowerCase();
        for (CodegenResponse codegenResponse : operation.responses) {
          try {
            HttpStatus valueForCode = HttpStatus.valueForCode(Integer.parseInt(codegenResponse.code));
            codegenResponse.code = valueForCode.toString();
          } catch (NumberFormatException nbe) {
            throw new IllegalArgumentException("Satus code " + codegenResponse.code + " is not a valid number.");
          }
        }
      }
    }
    return objs;
  }

  @Override
  public void addHandlebarHelpers(Handlebars handlebars) {
    super.addHandlebarHelpers(handlebars);
    handlebars.registerHelper(StringHelpers.defaultIfEmpty.name(), StringHelpers.defaultIfEmpty);
  }

  @Override
  public String escapeQuotationMark(String input) {
    // Just return the original string
    return input;
  }

  @Override
  public String escapeUnsafeCharacters(String input) {
    // Just return the original string
    return input;
  }

  public enum HttpStatus {
    ACCEPTED_202(HttpURLConnection.HTTP_ACCEPTED),
    BAD_GATEWAY_502(HttpURLConnection.HTTP_BAD_GATEWAY),
    BAD_METHOD_405(HttpURLConnection.HTTP_BAD_METHOD),
    BAD_REQUEST_400(HttpURLConnection.HTTP_BAD_REQUEST),
    REQUEST_TIMEOUT_408(HttpURLConnection.HTTP_CLIENT_TIMEOUT),
    CONFLICT_409(HttpURLConnection.HTTP_CONFLICT),
    CREATED_201(HttpURLConnection.HTTP_CREATED),
    REQUEST_TOO_LARGE_413(HttpURLConnection.HTTP_ENTITY_TOO_LARGE),
    FORBIDDEN_403(HttpURLConnection.HTTP_FORBIDDEN),
    GATEWAY_TIMEOUT_504(HttpURLConnection.HTTP_GATEWAY_TIMEOUT),
    GONE_410(HttpURLConnection.HTTP_GONE),
    INTERNAL_ERROR_500(HttpURLConnection.HTTP_INTERNAL_ERROR),
    LENGTH_REQUIRED_411(HttpURLConnection.HTTP_LENGTH_REQUIRED),
    MOVED_PERM_301(HttpURLConnection.HTTP_MOVED_PERM),
    MOVED_TEMP_302(HttpURLConnection.HTTP_MOVED_TEMP),
    MULT_CHOICE_300(HttpURLConnection.HTTP_MULT_CHOICE),
    NO_CONTENT_204(HttpURLConnection.HTTP_NO_CONTENT),
    NOT_ACCEPTABLE_406(HttpURLConnection.HTTP_NOT_ACCEPTABLE),
    NOT_AUTHORITATIVE_203(HttpURLConnection.HTTP_NOT_AUTHORITATIVE),
    NOT_FOUND_404(HttpURLConnection.HTTP_NOT_FOUND),
    NOT_IMPLEMENTED_501(HttpURLConnection.HTTP_NOT_IMPLEMENTED),
    NOT_MODIFIED_304(HttpURLConnection.HTTP_NOT_MODIFIED),
    OK_200(HttpURLConnection.HTTP_OK),
    PARTIAL_206(HttpURLConnection.HTTP_PARTIAL),
    PAYMENT_REQUIRED_402(HttpURLConnection.HTTP_PAYMENT_REQUIRED),
    PRECON_FAILED_412(HttpURLConnection.HTTP_PRECON_FAILED),
    PROXY_AUTH_407(HttpURLConnection.HTTP_PROXY_AUTH),
    REQUEST_URI_TOO_LONG_414(HttpURLConnection.HTTP_REQ_TOO_LONG),
    RESET_205(HttpURLConnection.HTTP_RESET),
    SEE_OTHER_303(HttpURLConnection.HTTP_SEE_OTHER),
    UNAUTHORIZED_401(HttpURLConnection.HTTP_UNAUTHORIZED),
    UNAVAILABLE_503(HttpURLConnection.HTTP_UNAVAILABLE),
    UNSUPPORTED_TYPE_415(HttpURLConnection.HTTP_UNSUPPORTED_TYPE),
    USE_PROXY_305(HttpURLConnection.HTTP_USE_PROXY),
    HTTP_VERSION_NOT_SUPPORTED_505(HttpURLConnection.HTTP_VERSION),
    CONTINUE_100(100),
    SWITCH_PROTOCOL_101(101);
    private int statusCode;

    private HttpStatus(int i) {
      statusCode = i;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public static HttpStatus valueForCode(int code) {
      for (HttpStatus httpStatus : values()) {
        if (httpStatus.getStatusCode() == code) {
          return httpStatus;
        }
      }
      throw new IllegalArgumentException("Satus code " + code + " is not a valid http code.");
    }
  }

  @Override
  public String getDefaultTemplateDir() {
    return TEMPLATE_DIRECTORY_NAME;
  }

}
