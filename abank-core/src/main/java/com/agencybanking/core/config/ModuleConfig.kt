/**
 *
 */
package com.agencybanking.core.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page

import java.io.IOException

/**
 * @author dubic
 */
@Configuration
open class ModuleConfig {

    //    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Bean
    open fun jacksonHibernate4Module(): Module {
        val module = Hibernate5Module()
        module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING)
        module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION)
        return module
    }

    @Bean
    open fun springDataPageModule(): Module {
        return SimpleModule().addSerializer<Page<*>>(Page::class.java, object : JsonSerializer<Page<*>>() {
            @Throws(IOException::class)
            override fun serialize(value: Page<*>, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeNumberField("totalElements", value.totalElements)
                gen.writeNumberField("totalPages", value.totalPages)
                gen.writeNumberField("page", value.number)
                gen.writeNumberField("size", value.size)
                gen.writeBooleanField("first", value.isFirst)
                gen.writeBooleanField("last", value.isLast)
                gen.writeFieldName("content")
                serializers.defaultSerializeValue(value.content, gen)
                gen.writeEndObject()
            }

        })
        /*

                .addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                System.out.println("P VALUE: " + p.getText());
                System.out.println(LocalDateTime.parse(p.getValueAsString(), FORMATTER));
                return LocalDateTime.parse(p.getValueAsString());
            }
        }).addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {

            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                System.out.println("P VALUE: " + p.getText());
                System.out.println(LocalDateTime.parse(p.getValueAsString(), FORMATTER));
                return LocalDate.parse(p.getValueAsString());
            }
        })
                .addDeserializer(Money.class, new JsonDeserializer<Money>() {

			@Override
			public Money deserialize(JsonParser p, DeserializationContext ctxt)
					throws IOException, JsonProcessingException {
				System.out.println(p.getValueAsString());
				System.out.println(p.getText());
				return new Money(p.getValueAsString());
			}

		})*/
    }

    //    @Bean
    //    private JavaTimeModule javaTimeModule() {
    //        JavaTimeModule module = new JavaTimeModule();
    //        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    //        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
    //        return module;
    //    }
    //
    //    @Bean
    //    @Primary
    //    public ObjectMapper jsonObjectMapper() {
    //        return Jackson2ObjectMapperBuilder.json()
    //                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
    //                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
    //                .modules(javaTimeModule())
    //                .build();
    //    }
}
