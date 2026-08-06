package com.spring.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

/**
 * # Prompt Template in Spring AI – Detailed Explanation
 * <p>
 * When we interact with an LLM, we always send a **prompt**.
 * <p>
 * Example:
 * <p>
 * ```text
 * Explain Java Streams.
 * ```
 * <p>
 * This works fine if the prompt never changes. But in real applications, the prompt usually depends on user input.
 * <p>
 * For example:
 * <p>
 * * Explain Java Streams.
 * * Explain Kafka.
 * * Explain Spring Boot.
 * * Explain Microservices.
 * <p>
 * Instead of creating separate prompts every time, we can create a **template**.
 * <p>
 * ```text
 * Explain {topic}
 * ```
 * <p>
 * At runtime:
 * <p>
 * ```text
 * topic = Kafka
 * ```
 * <p>
 * The final prompt becomes:
 * <p>
 * ```text
 * Explain Kafka
 * ```
 * <p>
 * This concept is called a **Prompt Template**.
 * <p>
 * ---
 * <p>
 * # Why Prompt Templates Are Needed
 * <p>
 * Imagine an Interview Application.
 * <p>
 * Without Prompt Templates:
 * <p>
 * ```java
 * String prompt =
 * "Ask 5 Java questions for a 6 years experienced developer.";
 * ```
 * <p>
 * For Spring Boot:
 * <p>
 * ```java
 * String prompt =
 * "Ask 5 Spring Boot questions for a 6 years experienced developer.";
 * ```
 * <p>
 * For Kafka:
 * <p>
 * ```java
 * String prompt =
 * "Ask 5 Kafka questions for a 6 years experienced developer.";
 * ```
 * <p>
 * For Microservices:
 * <p>
 * ```java
 * String prompt =
 * "Ask 5 Microservices questions for a 6 years experienced developer.";
 * ```
 * <p>
 * This approach is repetitive.
 * <p>
 * Using PromptTemplate:
 * <p>
 * ```text
 * Ask 5 questions on {topic}
 * for a {experience} years experienced developer.
 * ```
 * <p>
 * Now you only replace the variables.
 * <p>
 * ```java
 * topic = Spring Boot
 * experience = 6
 * ```
 * <p>
 * Generated prompt:
 * <p>
 * ```text
 * Ask 5 questions on Spring Boot
 * for a 6 years experienced developer.
 * ```
 * <p>
 * This makes prompts **dynamic, reusable, and maintainable**.
 * <p>
 * ---
 * <p>
 * # Real Life Analogy
 * <p>
 * Think of PromptTemplate like an email template.
 * <p>
 * Template:
 * <p>
 * ```text
 * Hello {name},
 *
 * Your order {orderId} has been shipped.
 *
 * Thanks,
 * Amazon
 * ```
 * <p>
 * If:
 * <p>
 * ```text
 * name = Sachin
 * orderId = 12345
 * ```
 * <p>
 * Final email:
 * <p>
 * ```text
 * Hello Sachin,
 *
 * Your order 12345 has been shipped.
 *
 * Thanks,
 * Amazon
 * ```
 * <p>
 * Spring AI PromptTemplate works exactly the same way.
 * <p>
 * ---
 * <p>
 * # Prompt vs PromptTemplate
 * <p>
 * ### Prompt
 * <p>
 * A Prompt is the final message sent to the LLM.
 * <p>
 * Example:
 * <p>
 * ```java
 * Prompt prompt =
 * new Prompt("Explain Java Streams");
 * ```
 * <p>
 * This is the actual request.
 * <p>
 * ---
 * <p>
 * ### PromptTemplate
 * <p>
 * A PromptTemplate generates the prompt dynamically.
 * <p>
 * ```java
 * PromptTemplate template =
 * new PromptTemplate("Explain {topic}");
 * ```
 * <p>
 * After replacement:
 * <p>
 * ```java
 * Prompt prompt =
 * template.create(
 * Map.of("topic", "Kafka")
 * );
 * ```
 * <p>
 * Generated Prompt:
 * <p>
 * ```text
 * Explain Kafka
 * ```
 * <p>
 * So:
 * <p>
 * ```
 * PromptTemplate → Prompt → Model
 * ```
 * <p>
 * ---
 * <p>
 * # Internal Flow
 * <p>
 * ```text
 * User Input
 * ↓
 * PromptTemplate
 * ↓
 * Replace Variables
 * ↓
 * Prompt
 * ↓
 * ChatModel
 * ↓
 * LLM Response
 * ```
 * <p>
 * Example:
 * <p>
 * ```text
 * User enters: Spring AI
 * ```
 * <p>
 * Template:
 * <p>
 * ```text
 * Explain {topic}
 * ```
 * <p>
 * Generated prompt:
 * <p>
 * ```text
 * Explain Spring AI
 * ```
 * <p>
 * This generated prompt is then sent to GPT, Gemini, Claude, Ollama, etc.
 * <p>
 * ---
 * <p>
 * # Basic Example
 * <p>
 * ```java
 * PromptTemplate template =
 * new PromptTemplate(
 * "Explain {topic} in simple terms."
 * );
 *
 * Prompt prompt =
 * template.create(
 * Map.of(
 * "topic",
 * "Spring Boot"
 * )
 * );
 * ```
 * <p>
 * The generated prompt:
 * <p>
 * ```text
 * Explain Spring Boot in simple terms.
 * ```
 * <p>
 * ---
 * <p>
 * # Multiple Variables
 * <p>
 * Template:
 * <p>
 * ```java
 * PromptTemplate template =
 * new PromptTemplate(
 * """
 * Explain {topic}
 * for a {level} developer.
 * """
 * );
 * ```
 * <p>
 * Pass values:
 * <p>
 * ```java
 * Prompt prompt =
 * template.create(
 * Map.of(
 * "topic", "Kafka",
 * "level", "beginner"
 * ));
 * ```
 * <p>
 * Generated prompt:
 * <p>
 * ```text
 * Explain Kafka
 * for a beginner developer.
 * ```
 * <p>
 * You can have any number of placeholders.
 * <p>
 * ```text
 * {topic}
 * {experience}
 * {language}
 * {framework}
 * {difficulty}
 * ```
 * <p>
 * ---
 * <p>
 * # What Does create() Do?
 * <p>
 * The `create()` method performs **variable substitution**.
 * <p>
 * Template:
 * <p>
 * ```text
 * Explain {topic}
 * ```
 * <p>
 * Code:
 * <p>
 * ```java
 * template.create(
 * Map.of(
 * "topic",
 * "Java Streams"
 * ));
 * ```
 * <p>
 * Result:
 * <p>
 * ```text
 * Explain Java Streams
 * ```
 * <p>
 * It returns a `Prompt` object.
 * <p>
 * ```java
 * Prompt prompt =
 * template.create(
 * Map.of("topic","Java Streams")
 * );
 * ```
 * <p>
 * ---
 * <p>
 * # Prompt Object
 * <p>
 * A Prompt contains the final messages sent to the model.
 * <p>
 * ```java
 * Prompt prompt =
 * new Prompt(
 * "Explain Kafka"
 * );
 * ```
 * <p>
 * The model never sees:
 * <p>
 * ```text
 * Explain {topic}
 * ```
 * <p>
 * The model only receives:
 * <p>
 * ```text
 * Explain Kafka
 * ```
 * <p>
 * because the PromptTemplate resolves everything before sending it.
 * <p>
 * ---
 * <p>
 * # Using ChatClient
 * <p>
 * Instead of explicitly creating PromptTemplate:
 * <p>
 * ```java
 * chatClient.prompt()
 * .user("Explain {topic}")
 * .param("topic","Kafka")
 * .call()
 * .content();
 * ```
 * <p>
 * Spring AI internally performs:
 * <p>
 * ```text
 * Explain {topic}
 * ↓
 * Replace topic
 * ↓
 * Explain Kafka
 * ↓
 * Send to model
 * ```
 * <p>
 * The `.param()` method is essentially a shortcut for PromptTemplate variable substitution.
 * <p>
 * ---
 * <p>
 * # External Prompt Files
 * <p>
 * Instead of writing prompts inside Java code:
 * <p>
 * ```java
 * new PromptTemplate(
 * "Explain {topic}"
 * );
 * ```
 * <p>
 * You can store prompts in files.
 * <p>
 * ### interview.st
 * <p>
 * ```text
 * You are a Java interviewer.
 *
 * Ask {count} questions
 * on {topic}
 * for a {experience}
 * years experienced developer.
 * ```
 * <p>
 * Load:
 * <p>
 * ```java
 * PromptTemplate template =
 * new PromptTemplate(
 * new ClassPathResource(
 * "prompts/interview.st"
 * ));
 * ```
 * <p>
 * Use:
 * <p>
 * ```java
 * Prompt prompt =
 * template.create(
 * Map.of(
 * "topic","Spring Boot",
 * "count","5",
 * "experience","6"
 * ));
 * ```
 * <p>
 * Generated prompt:
 * <p>
 * ```text
 * You are a Java interviewer.
 *
 * Ask 5 questions
 * on Spring Boot
 * for a 6 years experienced developer.
 * ```
 * <p>
 * This is commonly used in **RAG applications and enterprise AI systems**.
 * <p>
 * ---
 * <p>
 * # Advantages of PromptTemplate
 * <p>
 * | Advantage            | Explanation                   |
 * | -------------------- | ----------------------------- |
 * | Reusable             | Write once and use many times |
 * | Dynamic              | Accepts runtime values        |
 * | Cleaner Code         | No string concatenation       |
 * | Maintainable         | Easy to update prompts        |
 * | Externalized Prompts | Store prompts in files        |
 * | Better Readability   | Clear placeholders            |
 * <p>
 * ---
 * <p>
 * # Interview Definition
 * <p>
 * > A PromptTemplate in Spring AI is a reusable prompt containing placeholders that are replaced with dynamic values at runtime to generate a final Prompt before sending it to the LLM. It helps build maintainable, reusable, and dynamic AI applications.
 * <p>
 * ---
 * <p>
 * # One-Line Flow to Remember
 * <p>
 * ```text
 * PromptTemplate + Variables
 * ↓
 * Prompt
 * ↓
 * ChatModel
 * ↓
 * LLM Response
 * ```
 * <p>
 * This flow is one of the most important concepts in Spring AI interviews.
 */
@Service
public class PromptTemplateEx {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/user-message.st")
    private Resource promptResource;

    public PromptTemplateEx(@Qualifier("ollamaAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chatWithTemplate(String adjective, String topic) {

        PromptTemplate promptTemplate = new PromptTemplate("Tell me a {adjective} joke about {topic}");

        Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

        return chatClient.prompt(prompt).call().content();
    }

    public String chatWithTemplateExample2(String name, String voice) {
        String userText = """
                 Tell me about three famous pirates from the Golden Age of Piracy and why they did.
                 Write at least a sentence for each pirate.
                """;

        Message userMessage = new UserMessage(userText);

        String systemText = """
                   You are a helpful AI assistant that helps people find information.
                   Your name is {name}
                   You should reply to the user's request with your name and also in the style of a {voice}.
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        String response = chatClient.prompt(prompt).call().content();
        return response;
    }

    public String chatWithTemplateExample3(String name, String voice) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
            Tell me the names of 5 movies whose soundtrack was composed by <composer>.
            """)
                .build();

        String prompt = promptTemplate.render(Map.of("composer", "John Williams"));
        return chatClient.prompt(prompt).call().content();
    }

    public String chatWithTemplateExample4(String name, String voice) {
        return this.chatClient.prompt()
                .system(system -> system.text("You are a helpful assistant."))
                .user(user -> user.text(promptResource).param("concept", "Spring AI"))
                .call()
                .content();
    }

}
