/**
 * Copyright (C) 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ninja.params;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ninja.Context;
import ninja.session.FlashScope;
import ninja.session.Session;
import ninja.validation.Validation;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

/**
 * Built in argument extractors
 *
 * @author James Roper
 */
public class ArgumentExtractors {

    private static final Map<Class<?>, ArgumentExtractor<?>> STATIC_EXTRACTORS =
        ImmutableMap.<Class<?>, ArgumentExtractor<?>>builder()
                .put(Context.class, new ContextExtractor())
                .put(Validation.class, new ValidationExtractor())
                .put(Session.class, new SessionExtractor())
                .put(FlashScope.class, new FlashExtractor())
                .build();

    public static ArgumentExtractor<?> getExtractorForType(Class<?> type) {
        return STATIC_EXTRACTORS.get(type);
    }

    public static class ContextExtractor implements ArgumentExtractor<Context> {
        @Override
        public Context extract(Context context) {
            return context;
        }

        @Override
        public Class<Context> getExtractedType() {
            return Context.class;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class ValidationExtractor implements ArgumentExtractor<Validation> {
        @Override
        public Validation extract(Context context) {
            return context.getValidation();
        }

        @Override
        public Class<Validation> getExtractedType() {
            return Validation.class;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class SessionExtractor implements ArgumentExtractor<Session> {
        @Override
        public Session extract(Context context) {
            return context.getSession();
        }

        @Override
        public Class<Session> getExtractedType() {
            return Session.class;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class FlashExtractor implements ArgumentExtractor<FlashScope> {
        @Override
        public FlashScope extract(Context context) {
            return context.getFlashScope();
        }

        @Override
        public Class<FlashScope> getExtractedType() {
            return FlashScope.class;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class PathParamExtractor implements ArgumentExtractor<String> {
        private final String key;

        public PathParamExtractor(PathParam pathParam) {
            this.key = pathParam.value();
        }

        @Override
        public String extract(Context context) {
            return context.getPathParameter(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class ParamExtractor implements ArgumentExtractor<String> {
        private final String key;

        public ParamExtractor(Param param) {
            this.key = param.value();
        }

        @Override
        public String extract(Context context) {
            return context.getParameter(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class ParamsExtractor implements ArgumentExtractor<String[]> {
        private final String key;

        public ParamsExtractor(Params param) {
            this.key = param.value();
        }

        @Override
        public String[] extract(Context context) {
            List<String> values = context.getParameterValues(key);
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.toArray(new String[values.size()]);
        }

        @Override
        public Class<String[]> getExtractedType() {
            return String[].class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class FileExtractor implements ArgumentExtractor<InputStream> {

        private final String name;

        public FileExtractor(FileParam file) {
            this.name = file.value();
        }

        @Override
        public InputStream extract(Context context) {
            return context.getUploadedFileStream(name);
        }

        @Override
        public Class<InputStream> getExtractedType() {
            return InputStream.class;
        }

        @Override
        public String getFieldName() {
            return name;
        }
    }

    public static class FilesExtractor implements ArgumentExtractor<InputStream[]> {

        private final String name;

        public FilesExtractor(FileParams fileParams) {
            this.name = fileParams.value();
        }

        @Override
        public InputStream[] extract(Context context) {
            List<InputStream> files = context.getUploadedFileStreams(name);
            if (files == null || files.isEmpty()) {
                return null;
            }
            return files.toArray(new InputStream[files.size()]);
        }

        @Override
        public Class<InputStream[]> getExtractedType() {
            return InputStream[].class;
        }

        @Override
        public String getFieldName() {
            return name;
        }

    }

    public static class HeaderExtractor implements ArgumentExtractor<String> {
        private final String key;

        public HeaderExtractor(Header header) {
            this.key = header.value();
        }

        @Override
        public String extract(Context context) {
            return context.getHeader(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class HeadersExtractor implements ArgumentExtractor<String[]> {
        private final String key;

        public HeadersExtractor(Headers headers) {
            this.key = headers.value();
        }

        @Override
        public String[] extract(Context context) {
            List<String> values = context.getHeaders(key);
            if (values == null || values.isEmpty()) {
                return null;
            }
            return values.toArray(new String[values.size()]);
        }

        @Override
        public Class<String[]> getExtractedType() {
            return String[].class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class SessionParamExtractor implements ArgumentExtractor<String> {
        private final String key;

        public SessionParamExtractor(SessionParam sessionParam) {
            this.key = sessionParam.value();
        }

        @Override
        public String extract(Context context) {
            return context.getSession().get(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class AttributeExtractor implements ArgumentExtractor<Object> {
        private final String key;
        private final Class<?> attributeType;

        @Inject
        public AttributeExtractor(Attribute attribute, ArgumentClassHolder attributeType) {
            this.key = attribute.value();
            this.attributeType = attributeType.getArgumentClass();
        }

        @Override
        public Object extract(Context context) {
            return context.getAttribute(key, attributeType);
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Class getExtractedType() {
            return attributeType;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class BodyAsExtractor<T> implements ArgumentExtractor<T> {
        private final Class<T> bodyType;

        public BodyAsExtractor(Class<T> bodyType) {
            this.bodyType = bodyType;
        }

        @Override
        public T extract(Context context) {
            return context.parseBody(bodyType);
        }

        @Override
        public Class<T> getExtractedType() {
            return bodyType;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }
}
