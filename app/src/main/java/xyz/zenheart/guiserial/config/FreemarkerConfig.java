package xyz.zenheart.guiserial.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import xyz.zenheart.guiserial.utils.FtlUtils;
import xyz.zenheart.guiserial.utils.PathUtils;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: freemarker配置类 </p>
 * <p>创建时间: 2021/9/16 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@org.springframework.context.annotation.Configuration
public class FreemarkerConfig {

    @Bean
    public Configuration configuration() {
        Configuration cfg = new Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

        cfg.setClassForTemplateLoading(FtlUtils.class, PathUtils.FTL);

        cfg.setDefaultEncoding("UTF-8");

        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}
