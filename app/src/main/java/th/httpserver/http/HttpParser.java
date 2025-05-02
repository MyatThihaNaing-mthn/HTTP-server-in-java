package th.httpserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParser {
    public static HttpRequest parse(InputStream inputStream) {
        HttpRequest.Builder httpRequestBuilder = new HttpRequest.Builder();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean firstLine = true;
        int contentLength = 0;
        Map<String, String> headers = new HashMap<>();

        try {
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                System.out.println("line: " + line);
                if (firstLine) {
                    String path = getPath(line);
                    System.out.println("path: " + path);
                    httpRequestBuilder.method(getMethod(line));
                    httpRequestBuilder.path(path);
                    httpRequestBuilder.version(getVersion(line));
                    httpRequestBuilder.queryParams(getQueryParams(line));
                    httpRequestBuilder.multiParams(getMultiQueryParams(line));
                    firstLine = false;
                } else if (line.startsWith("User-Agent: ")) {
                    headers.put("User-Agent", line.substring(12));
                } else if (line.startsWith("Content-Type: ")) {
                    headers.put("Content-Type", line.substring(15));
                } else if (line.startsWith("Content-Length: ")) {
                    contentLength = Integer.parseInt(line.substring(16));
                } else if (line.startsWith("Host: ")) {
                    headers.put("Host", line.substring(5));
                } else if (line.startsWith("Accept-Encoding: ")) {
                    headers.put("Accept-Encoding", line.substring(17));
                } else if (line.startsWith("Accept: ")) {
                    headers.put("Accept", line.substring(7));
                } else if (line.startsWith("Connection: ")) {
                    headers.put("Connection", line.substring(12));
                }
            }

            String body = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int read = br.read(buffer);
                if (read > 0) {
                    body = new String(buffer, 0, read);
                }
            }
            httpRequestBuilder.body(body);
            httpRequestBuilder.headers(headers);
            return httpRequestBuilder.build();
        } catch (IOException e) {
            System.out.println("Error parsing request: " + e.getMessage());
            e.printStackTrace();
        }

        // check request is valid

        return null;
    }

    private static HttpMethod getMethod(String firstLine) {
        String[] requestParts = firstLine.split(" ");
        if (requestParts.length >= 2) {
            System.out.println("Method Name: " + requestParts[0]);
            return HttpMethod.valueOf(requestParts[0]);
        }
        return null;
    }

    private static String getPath(String firstLine) {
        String[] requestParts = firstLine.split(" ");
        if (requestParts.length >= 2) {
            // remove query params
            return requestParts[1].split("\\?")[0];
        }
        return null;
    }

    private static String getVersion(String firstLine) {
        String[] requestParts = firstLine.split(" ");
        if (requestParts.length >= 2) {
            return requestParts[2];
        }
        return null;
    }

    private static Map<String, String> getQueryParams(String line) {
        String[] lineParts = line.split(" ");
        if (lineParts.length >= 2) {
            String path = lineParts[1];
            String[] requestParts = path.split("\\?");
            if (requestParts.length >= 2) {
                String[] queryParams = requestParts[1].split("&");
                Map<String, String> queryParamsMap = new HashMap<>();
                for (String queryParam : queryParams) {
                    String[] keyValue = queryParam.split("=");
                    if (keyValue.length >= 2 && !isMultiParam(keyValue[1])) {
                        queryParamsMap.put(keyValue[0], keyValue[1]);
                    }
                }
                System.out.println("queryParamsMap: " + queryParamsMap);
                return queryParamsMap;
            }
            
        }
        return new HashMap<String, String>();
    }

    private static Map<String, List<String>> getMultiQueryParams(String line) {
        String[] lineParts = line.split(" ");
        System.out.println("LLine for query params: " + line);
        if (lineParts.length >= 2) {
            String path = lineParts[1];
            String[] requestParts = path.split("\\?");
            if (requestParts.length >= 2) {
                String[] queryParams = requestParts[1].split("&");
                Map<String, List<String>> multiQueryParams = new HashMap<>();

                for (String queryParam : queryParams) {
                    String[] keyValue = queryParam.split("=");
                    if (keyValue.length >= 2 && isMultiParam(keyValue[1])) {
                        String[] multiParams = keyValue[1].substring(1, keyValue[1].length() - 1).split(",");
                        multiQueryParams.put(keyValue[0], Arrays.asList(multiParams));
                    }
                }
                System.out.println("multiQueryParams: " + multiQueryParams);
                return multiQueryParams;
            }
        }
        return new HashMap<>();
    }

    private static boolean isMultiParam(String queryParam) {
        return queryParam != null && queryParam.startsWith("[") && queryParam.endsWith("]");
    }

}
