/*
 * To change this license exchangeader, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open texchange template in texchange editor.
 */
package com.trung.g24hcode.lightserver;

import com.trung.g24hcode.utilities.LoggedPrintStream;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.trung.g24hcode.ota.CommandField;
import com.trung.g24hcode.ota.NaviClient;
import com.trung.g24hcode.ota.Taskn;
import static com.trung.g24hcode.ota.Taskn.gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nhatn
 */
public class LightHtttpServer {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8000;
    private static final int BACKLOG = 1;

    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;
    private static NaviClient client;
    private static LoggedPrintStream lps = LoggedPrintStream.create(System.out);
    private static ResponseDataTranfer tranfer = new ResponseDataTranfer();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/test", LightHtttpServer::handleRequest);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        try {
            final Headers exchangeaders = exchange.getResponseHeaders();
            final String requestMethod = exchange.getRequestMethod().toUpperCase();
            switch (requestMethod) {
                case METHOD_GET:
                    final Map<String, List<String>> requestParameters = getRequestParameters(exchange.getRequestURI());
                    // do something with texchange request parameters
                    final String responseBody = "['exchange hello world!']";
                    exchangeaders.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                    exchange.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                    exchange.getResponseBody().write(rawResponseBody);
                    break;
                case METHOD_POST:
                    try {
                        String requestString = "";
                        StringBuilder requestBuffer = new StringBuilder();
                        InputStream is = exchange.getRequestBody();
                        int rByte;
                        while ((rByte = is.read()) != -1) {
                            requestBuffer.append((char) rByte);
                        }
                        is.close();

                        if (requestBuffer.length() > 0) {
                            requestString = URLDecoder.decode(requestBuffer.toString(), "UTF-8");
                        } else {
                            requestString = "";
                        }

                        //change output Stream of responsebody
                        System.setOut(lps);
                        doPost(requestString,tranfer);
                        tranfer.setLog(lps.toString());
                        final String response = gson.toJson(tranfer,ResponseDataTranfer.class);
                        exchangeaders.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                        final byte[] rawResponse = response.getBytes(CHARSET);
                        exchange.sendResponseHeaders(STATUS_OK, rawResponse.length);
                        exchange.getResponseBody().write(rawResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case METHOD_OPTIONS:
                    exchangeaders.set(HEADER_ALLOW, ALLOWED_METHODS);
                    exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                    break;
                default:
                    exchangeaders.set(HEADER_ALLOW, ALLOWED_METHODS);
                    exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                    break;
            }
        } finally {
            exchange.close();
        }
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

    private static void doPost(String requestBody, ResponseDataTranfer rdt) {
        CommandField cmdF = gson.fromJson(requestBody, CommandField.class);
        boolean result = false;
        switch (Taskn.Command.Parse(cmdF.getCommand())) {
            case LOGIN:
                client = new NaviClient(cmdF.getUser().getUsername(), cmdF.getUser().getPassword());
                rdt.setModels(client.getModels());
                rdt.setResult(result);
                result = client.isLogged();
                if (result) {
                    System.out.println("successful!");
                } else {
                    System.out.println("failed!");
                }
                break;
            case ADD_RECORD:
                if (client != null || client.isLogged()) {
                    result = client.addRecord(cmdF.getModel());
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case UPDATE_RECORD:
                if (client != null || client.isLogged()) {
                    result = client.updateRecord(cmdF.getModel());
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case REMOVE_RECORD:
                if (client != null || client.isLogged()) {
                    result = client.removeRecord(cmdF.getModel());
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case REMOVE_ALL:
                if (client != null || client.isLogged()) {
                    result = client.removeAllRecord();
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case CREATE_BY:
                if (client != null || client.isLogged()) {
                    result = client.createRecordsBy(cmdF.getOptions(), cmdF.getData());
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case UPDATE_THIS_IP:
                if (client != null || client.isLogged()) {
                    List<Boolean> results = client.doUpdateIpRecords();
                } else {
                    System.out.println("Client is not logged in! ");
                }
                break;
            case LOGOUT:
                return;
            default:
                System.out.println("unknow command!");
                break;

        }
    }
}
