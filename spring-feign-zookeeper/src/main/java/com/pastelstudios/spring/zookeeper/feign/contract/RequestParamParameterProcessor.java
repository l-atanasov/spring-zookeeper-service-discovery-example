package com.pastelstudios.spring.zookeeper.feign.contract;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.springframework.web.bind.annotation.RequestParam;

import feign.MethodMetadata;

/**
 * {@link RequestParam} parameter processor.
 *
 * @author Jakub Narloch
 * @see AnnotatedParameterProcessor
 */
public class RequestParamParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<RequestParam> ANNOTATION = RequestParam.class;

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ANNOTATION;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation) {
        String name = ANNOTATION.cast(annotation).value();
        checkState(emptyToNull(name) != null,
                "RequestParam.value() was empty on parameter %s", context.getParameterIndex());
        context.setParameterName(name);

        MethodMetadata data = context.getMethodMetadata();
        Collection<String> query = context.setTemplateParameter(name, data.template().queries().get(name));
        data.template().query(name, query);
        return true;
    }
}
