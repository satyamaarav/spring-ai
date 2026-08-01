# Comprehensive Guide to ChatModel and ChatClient API in Spring Boot

## Table of Contents
1. [Introduction](#introduction)
2. [ChatModel API](#chatmodel-api)
3. [ChatClient API](#chatclient-api)
4. [Integration with Spring Boot](#integration-with-spring-boot)
5. [Practical Examples](#practical-examples)
6. [Advanced Features](#advanced-features)
7. [Best Practices](#best-practices)

---

## Introduction

Spring AI provides powerful abstractions for working with Large Language Models (LLMs) through the **ChatModel** and **ChatClient** APIs. These APIs enable developers to integrate AI-powered conversational capabilities into their Spring Boot applications with minimal complexity.

### Key Differences:
- **ChatModel**: Low-level API for direct model interaction
- **ChatClient**: High-level, fluent API with additional features like function calling and content filtering

---

## ChatModel API

### 1. Overview

The ChatModel interface is a core abstraction in Spring AI that defines how to call an AI model to generate responses based on messages.

```java
public interface ChatModel extends Model<List<Message>, ChatResponse> {
    ChatResponse call(Prompt prompt);
}
```

### 2. Core Components

#### 2.1 Message Types

Spring AI supports multiple message types for conversation:

```java
// User Message - Input from the user
UserMessage userMessage = new UserMessage("What is Spring Boot?");

// Assistant Message - Response from the AI model
AssistantMessage assistantMessage = new AssistantMessage("Spring Boot is...");

// System Message - Instructions for the AI model
SystemMessage systemMessage = new SystemMessage("You are a helpful AI assistant specializing in Java development");

// Content - Flexible message representation
Content content = new Content("Some text");
```

#### 2.2 Prompt and ChatResponse

```java
// Creating a Prompt with messages
List<Message> messages = List.of(
    new SystemMessage("You are an expert Java developer"),
    new UserMessage("Explain Spring AI in 2 sentences")
);
Prompt prompt = new Prompt(messages);

// ChatResponse contains the response
ChatResponse response = chatModel.call(prompt);

// Accessing the response content
String content = response.getResult().getOutput().getContent();
```

### 3. ChatModel Implementation Example

```java
@Configuration
public class ChatModelConfiguration {
    
    @Bean
    public ChatModel chatModel(OpenAiApi openAiApi) {
        return new OpenAiChatModel(openAiApi);
    }
}
```

### 4. Basic ChatModel Usage

```java
@Service
public class BasicChatService {
    
    private final ChatModel chatModel;
    
    public BasicChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    
    public String getResponse(String userQuery) {
        // Create messages
        List<Message> messages = List.of(
            new SystemMessage("You are a helpful assistant"),
            new UserMessage(userQuery)
        );
        
        // Create prompt
        Prompt prompt = new Prompt(messages);
        
        // Call the model
        ChatResponse response = chatModel.call(prompt);
        
        // Extract content
        return response.getResult().getOutput().getContent();
    }
}
```

### 5. Advanced ChatModel Features

#### 5.1 Temperature Control

Controls the randomness of responses (0.0 = deterministic, 1.0 = very random):

```java
public String getCreativeResponse(String query) {
    List<Message> messages = List.of(
        new UserMessage(query)
    );
    
    ChatResponse response = chatModel.call(
        new Prompt(
            messages,
            ChatOptions.builder()
                .withTemperature(0.8)  // More creative
                .build()
        )
    );
    
    return response.getResult().getOutput().getContent();
}
```

#### 5.2 Max Tokens

Limits the length of the response:

```java
ChatResponse response = chatModel.call(
    new Prompt(
        messages,
        ChatOptions.builder()
            .withMaxTokens(100)  // Limit to 100 tokens
            .build()
    )
);
```

---

## ChatClient API

### 1. Overview

ChatClient is a higher-level, fluent API built on top of ChatModel. It provides a more convenient and flexible way to interact with AI models.

```java
public interface ChatClient {
    PromptUserSpec prompt(String text);
    PromptUserSpec prompt(Content content);
}
```

### 2. Core Features

#### 2.1 Fluent API Design

ChatClient uses a fluent builder pattern for intuitive code:

```java
String response = chatClient
    .prompt("What is Spring Boot?")
    .call()
    .content();
```

#### 2.2 ChatClient Builder

The ChatClient.Builder is typically injected and configured:

```java
@Configuration
public class ChatClientConfiguration {
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
```

### 3. Basic ChatClient Usage

```java
@Service
public class ChatClientService {
    
    private final ChatClient chatClient;
    
    public ChatClientService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    public String askQuestion(String question) {
        return chatClient
            .prompt(question)
            .call()
            .content();
    }
}
```

### 4. Structured Responses with ChatClient

#### 4.1 Entity Extraction

Extract structured data from responses:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {
    String name;
    int age;
    String occupation;
}

public PersonInfo extractPersonInfo(String text) {
    return chatClient
        .prompt(text + "\nExtract person info in JSON format.")
        .call()
        .entity(PersonInfo.class);
}
```

#### 4.2 List Extraction

Extract lists of objects:

```java
public List<String> extractKeyPoints(String article) {
    return chatClient
        .prompt("Extract 5 key points from: " + article)
        .call()
        .list(String.class);
}
```

### 5. System Prompts and Context

#### 5.1 Using System Messages

```java
String response = chatClient
    .prompt()
    .system("You are a Java expert. Answer all questions in detail.")
    .user("What is dependency injection?")
    .call()
    .content();
```

#### 5.2 Message History

```java
ChatResponse response = chatClient
    .prompt()
    .system("You are a helpful assistant")
    .user("What is Spring?")
    .call();

String firstResponse = response.getResult().getOutput().getContent();

// Continue conversation
String secondResponse = chatClient
    .prompt()
    .system("You are a helpful assistant")
    .user("Tell me more about microservices")
    .advisors(List.of(
        new MessageChatMemoryAdvisor(chatMemory)  // Add conversation history
    ))
    .call()
    .content();
```

### 6. Function Calling with ChatClient

Function calling allows AI models to trigger specific functions:

```java
@Configuration
public class FunctionConfiguration {
    
    @Bean
    public FunctionCallbackInfo<WeatherInfo> weatherFunction() {
        return FunctionCallbackInfo.builder()
            .function("getWeather", new Function<WeatherRequest, WeatherInfo>() {
                @Override
                public WeatherInfo apply(WeatherRequest request) {
                    return new WeatherInfo(request.getLocation(), "Sunny", 25);
                }
            })
            .description("Get weather information for a location")
            .build();
    }
}
```

Using function calls:

```java
public String getWeatherResponse(String location) {
    return chatClient
        .prompt()
        .user("What's the weather in " + location + "?")
        .functions("getWeather")
        .call()
        .content();
}
```

### 7. Options and Customization

```java
public String customizedResponse(String prompt) {
    return chatClient
        .prompt(prompt)
        .options(ChatOptions.builder()
            .withTemperature(0.7)
            .withMaxTokens(200)
            .withTopP(0.9)
            .withTopK(40)
            .build())
        .call()
        .content();
}
```

---

## Integration with Spring Boot

### 1. Dependencies

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
```

For other providers:

```xml
<!-- For Azure OpenAI -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-azure-openai</artifactId>
</dependency>

<!-- For Google -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-google-genai</artifactId>
</dependency>

<!-- For Ollama (Local Models) -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-ollama</artifactId>
</dependency>
```

### 2. Configuration

#### 2.1 application.yml

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://api.openai.com/v1
      model: gpt-4
      temperature: 0.7
      max-tokens: 1000
```

#### 2.2 Environment Variables

```bash
export OPENAI_API_KEY=your-api-key-here
export SPRING_AI_OPENAI_MODEL=gpt-4
export SPRING_AI_OPENAI_TEMPERATURE=0.7
```

### 3. Auto-Configuration

Spring Boot automatically configures ChatModel and ChatClient.Builder:

```java
@SpringBootApplication
@EnableSpringAi  // Optional - enables auto-configuration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 4. Multiple AI Provider Configuration

```java
@Configuration
public class MultiProviderConfiguration {
    
    @Bean
    @ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
    public ChatModel openaiChatModel(OpenAiApi openAiApi) {
        return new OpenAiChatModel(openAiApi);
    }
    
    @Bean
    @ConditionalOnProperty(name = "ai.provider", havingValue = "azure")
    public ChatModel azureChatModel(AzureOpenAiServiceClient client) {
        return new AzureOpenAiChatModel(client);
    }
}
```

---

## Practical Examples

### 1. Simple Chat Service

```java
@Service
public class SimpleChatService {
    
    private final ChatClient chatClient;
    
    public SimpleChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    public String chat(String message) {
        return chatClient
            .prompt(message)
            .call()
            .content();
    }
}
```

### 2. REST Controller Integration

```java
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatClientService chatService;
    
    public ChatController(ChatClientService chatService) {
        this.chatService = chatService;
    }
    
    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(
            @RequestBody ChatRequest request) {
        String response = chatService.ask(request.getQuestion());
        return ResponseEntity.ok(new ChatResponse(response));
    }
    
    @GetMapping("/stream")
    public SseEmitter streamChat(@RequestParam String message) 
            throws IOException {
        SseEmitter emitter = new SseEmitter();
        chatService.streamChat(message, emitter);
        return emitter;
    }
}

@Data
class ChatRequest {
    private String question;
}

@Data
class ChatResponse {
    private String answer;
    
    public ChatResponse(String answer) {
        this.answer = answer;
    }
}
```

### 3. Conversation Management

```java
@Service
public class ConversationService {
    
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    
    public ConversationService(ChatClient.Builder builder, 
                              ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }
    
    public String continueConversation(String conversationId, 
                                       String userMessage) {
        String response = chatClient
            .prompt()
            .system("You are a helpful assistant")
            .user(userMessage)
            .advisors(new MessageChatMemoryAdvisor(chatMemory))
            .call()
            .content();
        
        // Store in memory
        chatMemory.add(conversationId, 
                      new UserMessage(userMessage), 
                      new AssistantMessage(response));
        
        return response;
    }
}
```

### 4. Document Analysis

```java
@Service
public class DocumentAnalysisService {
    
    private final ChatClient chatClient;
    
    public DocumentAnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    public SummaryData analyzDocument(String documentContent) {
        return chatClient
            .prompt(String.format(
                "Analyze the following document and provide summary, " +
                "key points, and sentiment:\n%s", 
                documentContent))
            .call()
            .entity(SummaryData.class);
    }
}

@Data
class SummaryData {
    private String summary;
    private List<String> keyPoints;
    private String sentiment;
}
```

### 5. Multi-Turn Conversation

```java
@Service
public class MultiTurnChatService {
    
    private final ChatClient chatClient;
    private final Map<String, List<Message>> conversations 
        = new ConcurrentHashMap<>();
    
    public MultiTurnChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    public String sendMessage(String sessionId, String userMessage) {
        // Get or create conversation history
        List<Message> messages = conversations
            .computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // Add user message
        messages.add(new UserMessage(userMessage));
        
        // Build prompt with history
        List<Message> messagesWithContext = new ArrayList<>();
        messagesWithContext.add(new SystemMessage(
            "You are a helpful assistant"));
        messagesWithContext.addAll(messages);
        
        // Get response
        Prompt prompt = new Prompt(messagesWithContext);
        ChatResponse response = chatClient.prompt(prompt).call();
        
        String content = response.getResult().getOutput().getContent();
        
        // Store assistant response
        messages.add(new AssistantMessage(content));
        
        return content;
    }
    
    public void clearConversation(String sessionId) {
        conversations.remove(sessionId);
    }
}
```

---

## Advanced Features

### 1. Streaming Responses

```java
@Service
public class StreamingChatService {
    
    private final ChatModel chatModel;
    
    public void streamResponse(String prompt, Consumer<String> onChunk) {
        List<Message> messages = List.of(new UserMessage(prompt));
        Prompt p = new Prompt(messages);
        
        chatModel.stream(p).subscribe(
            chunk -> onChunk.accept(chunk.getResult().getOutput().getContent()),
            error -> System.err.println("Error: " + error),
            () -> System.out.println("Streaming complete")
        );
    }
}
```

### 2. Content Filtering

```java
public String getFilteredResponse(String prompt) {
    return chatClient
        .prompt(prompt)
        .call()
        .content();
    // Spring AI automatically handles content filtering
}
```

### 3. Token Counting

```java
public int estimateTokens(String text) {
    // Estimate tokens for cost calculation
    return Math.round(text.length() / 4.0f);
}

public String optimizeForTokenLimit(String text, int maxTokens) {
    int currentTokens = estimateTokens(text);
    if (currentTokens > maxTokens) {
        // Truncate or summarize
        return text.substring(0, (int)(text.length() * maxTokens / currentTokens));
    }
    return text;
}
```

### 4. Error Handling and Retry

```java
@Service
public class ResilientChatService {
    
    private final ChatClient chatClient;
    private final int maxRetries = 3;
    
    @Retryable(
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String getResponse(String prompt) {
        try {
            return chatClient
                .prompt(prompt)
                .call()
                .content();
        } catch (Exception e) {
            throw new ChatException("Failed to get response", e);
        }
    }
    
    @Recover
    public String recover(ChatException e) {
        return "I'm experiencing technical difficulties. Please try again.";
    }
}
```

### 5. Caching Responses

```java
@Service
@EnableCaching
public class CachedChatService {
    
    private final ChatClient chatClient;
    
    @Cacheable(value = "chatResponses", key = "#prompt")
    public String getResponse(String prompt) {
        return chatClient
            .prompt(prompt)
            .call()
            .content();
    }
    
    @CacheEvict(value = "chatResponses", allEntries = true)
    public void clearCache() {}
}
```

### 6. Async Responses

```java
@Service
public class AsyncChatService {
    
    private final ChatClient chatClient;
    
    @Async
    public CompletableFuture<String> getResponseAsync(String prompt) {
        String response = chatClient
            .prompt(prompt)
            .call()
            .content();
        return CompletableFuture.completedFuture(response);
    }
}
```

### 7. Custom Advisors

```java
public class CustomAdvisor implements Advisor {
    
    @Override
    public int getOrder() {
        return 0;
    }
    
    @Override
    public AdvisedRequest advise(AdvisedRequest request) {
        // Modify the request before sending to model
        List<Message> messages = request.getMessages();
        // Add custom logic
        return request;
    }
}

// Usage
chatClient.prompt()
    .advisors(new CustomAdvisor())
    .user("Your question")
    .call()
    .content();
```

---

## Best Practices

### 1. Configuration Management

```java
@Configuration
public class AIConfiguration {
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(
            "You are a helpful Spring Boot expert assistant. " +
            "Provide clear, accurate, and concise responses."
        ).build();
    }
}
```

### 2. Error Handling

```java
@Service
public class SafeChatService {
    
    private final ChatClient chatClient;
    
    public String askSafely(String question) {
        try {
            return chatClient
                .prompt(question)
                .call()
                .content();
        } catch (RateLimitException e) {
            logger.warn("Rate limit exceeded: " + e.getMessage());
            return "Service temporarily unavailable. Please try again later.";
        } catch (Exception e) {
            logger.error("Chat error", e);
            return "An error occurred processing your request.";
        }
    }
}
```

### 3. Input Validation

```java
public String validateAndChat(String input) {
    if (input == null || input.trim().isEmpty()) {
        throw new IllegalArgumentException("Input cannot be empty");
    }
    
    if (input.length() > 10000) {
        throw new IllegalArgumentException("Input too long (max 10000 chars)");
    }
    
    return chatClient
        .prompt(input)
        .call()
        .content();
}
```

### 4. Rate Limiting

```java
@Service
@EnableRateLimiting
public class RateLimitedChatService {
    
    private final ChatClient chatClient;
    private final RateLimiter rateLimiter = RateLimiter.create(10.0);
    
    public String chat(String message) {
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitException("Too many requests");
        }
        
        return chatClient
            .prompt(message)
            .call()
            .content();
    }
}
```

### 5. Logging and Monitoring

```java
@Service
public class MonitoredChatService {
    
    private final ChatClient chatClient;
    private final MeterRegistry meterRegistry;
    
    public String chat(String message) {
        long startTime = System.currentTimeMillis();
        
        try {
            String response = chatClient
                .prompt(message)
                .call()
                .content();
            
            meterRegistry.timer("chat.response.time")
                .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
            
            meterRegistry.counter("chat.requests.success").increment();
            
            return response;
        } catch (Exception e) {
            meterRegistry.counter("chat.requests.error").increment();
            throw e;
        }
    }
}
```

### 6. Context and Memory Management

```java
@Service
public class ContextualChatService {
    
    private final ChatClient chatClient;
    private final ChatMemory memory;
    
    public String chat(String sessionId, String message) {
        return chatClient
            .prompt()
            .system("You are a helpful assistant. Consider the conversation history.")
            .user(message)
            .advisors(new MessageChatMemoryAdvisor(memory))
            .call()
            .content();
    }
}
```

### 7. Cost Optimization

```java
public String optimizeForCost(String prompt) {
    return chatClient
        .prompt(prompt)
        .options(ChatOptions.builder()
            .withTemperature(0.2)  // Lower temperature uses fewer tokens
            .withMaxTokens(500)    // Limit response length
            .build())
        .call()
        .content();
}
```

---

## Comparison: ChatModel vs ChatClient

| Aspect | ChatModel | ChatClient |
|--------|-----------|-----------|
| **Abstraction Level** | Low-level | High-level |
| **Complexity** | More control | Simpler usage |
| **Fluent API** | No | Yes |
| **Message Management** | Manual | Automatic |
| **Function Calling** | Limited | Full support |
| **Content Filtering** | Manual | Built-in |
| **Learning Curve** | Steep | Gentle |
| **Use Case** | Advanced | General use |

---

## Common Patterns

### Pattern 1: Question-Answering

```java
public String answerQuestion(String question) {
    return chatClient
        .prompt(question)
        .call()
        .content();
}
```

### Pattern 2: Text Transformation

```java
public String transformText(String text, String transformation) {
    return chatClient
        .prompt(String.format(
            "%s the following text:\n%s", 
            transformation, text))
        .call()
        .content();
}
```

### Pattern 3: Data Extraction

```java
public <T> T extractData(String text, Class<T> type) {
    return chatClient
        .prompt("Extract structured data from: " + text)
        .call()
        .entity(type);
}
```

### Pattern 4: Classification

```java
public String classify(String text) {
    return chatClient
        .prompt("Classify this text: " + text)
        .call()
        .content();
}
```

---

## Troubleshooting

### Issue: API Key Not Found

**Solution:**
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

Set environment variable: `export OPENAI_API_KEY=sk-...`

### Issue: Rate Limiting

**Solution:** Implement exponential backoff:
```java
@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
public String chat(String message) {
    return chatClient.prompt(message).call().content();
}
```

### Issue: Token Limit Exceeded

**Solution:** Use max tokens option:
```java
.options(ChatOptions.builder().withMaxTokens(500).build())
```

---

## Conclusion

ChatModel and ChatClient APIs in Spring Boot provide powerful abstractions for integrating AI capabilities. While ChatModel offers low-level control, ChatClient provides a more convenient and feature-rich interface suitable for most applications. Choose based on your specific requirements for control versus ease of use.

The key to successful implementation is proper error handling, rate limiting, and understanding the limitations of your AI provider.
