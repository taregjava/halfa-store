package com.halfacode.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfacode.dto.ApiResponse;
import com.halfacode.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LoggingServiceImpl implements LoggingService,LogCleanupService  {

    private final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class.getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final S3Service s3Service;

    public LoggingServiceImpl(S3Service s3Service) {
        this.s3Service = s3Service;
    }
    @Override
    // Schedule to run the log cleanup every 7 days (604800000 milliseconds)
    @Scheduled(fixedRate = 604800000)
    public void performLogCleanup() {
        // Delete log files older than 7 days in the "test" folder
        cleanLogs("D:\\test");
    }

    private void cleanLogs(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    long lastModified = file.lastModified();
                    long currentTimestamp = System.currentTimeMillis();
                    long diffInMilliseconds = currentTimestamp - lastModified;
                    long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);

                    // Delete the file if it's older than 7 days
                    if (diffInDays >= 7) {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void displayReq(HttpServletRequest request, Object body) throws JsonProcessingException {
        StringBuilder reqMessage = new StringBuilder();
        Map<String, String> parameters = getParameters(request);
        Map<String, String> headers = getHeadersInfo(request);

        LoggingContent build = LoggingContent.builder()
                .httpMethod(request.getMethod())
                .header(headers)
                .path(request.getRequestURI())
                .parameter(parameters)
                .body(body)
                .build();

        reqMessage.append("REQUEST ");
        reqMessage.append("method = [").append(request.getMethod()).append("]");
        reqMessage.append(" path = [").append(request.getRequestURI()).append("] ");

        if (!parameters.isEmpty()) {
            reqMessage.append(" parameters = [").append(parameters).append("] ");
        }

        if (!Objects.isNull(body)) {
            reqMessage.append(" body = [").append(body).append("]");
        }

        logger.info("start logger at {}", dateFormat.format(new Date()));
        logger.info("log Request: {}", new ObjectMapper().writeValueAsString(build));
        writefile(build);
    }

    @Async
    public void uploadToS3(String json) {
        // TODO: Add the code for uploading to S3 here (currently commented out)
    }

    public void writefile(LoggingContent logincontent) {
        ObjectMapper mapper = new ObjectMapper();
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);

        // Create the folder based on the current date
        File folder = new File("D:\\test\\" + formattedDate);
        folder.mkdirs();

        try {
            // Save the log file inside the folder with a unique name
            String fileName = "logs.json";
            File file = new File(folder, fileName);
            try (FileWriter writer = new FileWriter(file, true)) {
                String separator = "========================================";

                // Check if the log entry is for a request or response
                if (logincontent.getBody() == null) {
                    // Write the request log
                    writer.write(separator + " REQUEST " + separator + "\n");
                    writer.write(new ObjectMapper().writeValueAsString(logincontent) + "\n");
                } else {
                    // Check if response is not null before writing the response log
                    if (logincontent.getBody() instanceof ApiResponse) {
                        ApiResponse<?> response = (ApiResponse<?>) logincontent.getBody();
                        if (response != null) {
                            // Write the response log
                            writer.write(separator + " RESPONSE " + separator + "\n");
                            writer.write("status: " + response.getStatus() + "\n");
                            writer.write("timestamp: " + response.getTimestamp() + "\n");
                            writer.write("payload: " + "\n");
                            // Write the response payload in a formatted JSON style
                            if (response.getPayload() != null) {
                                writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getPayload()) + "\n");
                            } else {
                                writer.write("null\n");
                            }
                            writer.write("error: " + response.getError() + "\n");
                            writer.write("successful: " + response.isSuccessful() + "\n");
                            writer.write("\n"); // Add a newline after each response log
                        }
                    }
                }

                // No need to close the writer here since it's in the try-with-resources block

                System.out.println("success");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Upload the log file to S3
            InputStream inputStream = new FileInputStream(file);
            s3Service.uploadFile("logs/" + formattedDate, fileName, inputStream);

            // Delete the local log file after successful upload
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Deleted local log file: " + file.getAbsolutePath());
            } else {
                System.err.println("Failed to delete local log file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void displayResp(HttpServletRequest request, HttpServletResponse response, Object body) throws JsonProcessingException {
        StringBuilder respMessage = new StringBuilder();
        Map<String, String> headers = getHeadersInfo(request);
        Map<String, String> parameters = getParameters(request);
        LoggingContent build = LoggingContent.builder()
                .httpMethod(request.getMethod())
                .header(headers)
                .path(request.getRequestURI())
                .parameter(parameters)
                .body(body)
                .build();

        respMessage.append("RESPONSE ");
        respMessage.append(" method = [").append(request.getMethod()).append("]");
        if (!headers.isEmpty()) {
            respMessage.append(" ResponseHeaders = [").append(headers).append("]");
        }
        respMessage.append(" responseBody = [").append(body).append("]");

        logger.info("log logResponse: {}", new ObjectMapper().writeValueAsString(build));
        writefile(build);
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName, paramValue);
        }
        return parameters;
    }
}
