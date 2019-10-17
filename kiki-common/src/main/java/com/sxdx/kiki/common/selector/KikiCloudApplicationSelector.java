package com.sxdx.kiki.common.selector;

import com.sxdx.kiki.common.configure.KikiAuthExceptionConfigure;
import com.sxdx.kiki.common.configure.KikiOAuth2FeignConfigure;
import com.sxdx.kiki.common.configure.KikiServerProtectConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class KikiCloudApplicationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                KikiAuthExceptionConfigure.class.getName(),
                KikiOAuth2FeignConfigure.class.getName(),
                KikiServerProtectConfigure.class.getName()
        };
    }
}